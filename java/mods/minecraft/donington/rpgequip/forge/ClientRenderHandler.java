package mods.minecraft.donington.rpgequip.forge;

import java.util.HashMap;

import mods.minecraft.donington.rpgequip.RPGEquipMod;
import mods.minecraft.donington.rpgequip.client.render.AuraRenderer;
import mods.minecraft.donington.rpgequip.client.render.EntityEliteRenderer;
import mods.minecraft.donington.rpgequip.client.render.MagicFireRenderer;
import mods.minecraft.donington.rpgequip.client.render.MagicShieldRenderer;
import mods.minecraft.donington.rpgequip.client.render.SpecialRenderer;
import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import mods.minecraft.donington.rpgequip.common.RPGEAttributes;
import mods.minecraft.donington.rpgequip.common.RPGESpecial;
import mods.minecraft.donington.rpgequip.common.entity.elite.EliteCreatureAura;
import mods.minecraft.donington.rpgequip.common.entity.elite.EntityEliteHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientRenderHandler extends Gui {
	public static ClientRenderHandler instance;

	private static Minecraft mc;

	/* magic renderer instantiation is handled by this render handler */
	public static MagicFireRenderer magicFireRenderer;
	public static MagicShieldRenderer magicShieldRenderer;

	// aura renderer
	private AuraRenderer auraRenderer = new AuraRenderer(new ResourceLocation(RPGEquipMod.MOD_ID, "textures/aura.png"));
	private static double auraOffset;

	// special renderer
	private SpecialRenderer specialRenderer = new SpecialRenderer();


	public ClientRenderHandler() {
		super();
		this.instance = this;
		this.mc = Minecraft.getMinecraft();

		this.auraOffset = 0.0;

		magicFireRenderer = new MagicFireRenderer();
		magicShieldRenderer = new MagicShieldRenderer();
	}


	/** update render events every tick **/
	@SubscribeEvent
	public void onTickEvent(RenderHandEvent event) {
		auraOffset += 0.1;

		if ( auraOffset >= 360 )
			auraOffset -= 360;

		magicFireRenderer.update();
		magicShieldRenderer.update();
	}


	/** override the default armor bar and render one that factors RPGEAttributes **/
	@SubscribeEvent
	public void onRenderArmorHUD(RenderGameOverlayEvent.Pre event) {
		if ( event.type != ElementType.ARMOR ) return;
		event.setCanceled(true);

		int width = event.resolution.getScaledWidth();
		int height = event.resolution.getScaledHeight();

		GL11.glEnable(GL11.GL_BLEND);

		int left = width / 2 - 91;
		int top = height - GuiIngameForge.left_height;

		EntityPlayer player = mc.thePlayer;
		RPGEAttributes attrs = RPGEAttributes.get(player);
		float impact = attrs.getArmorReduction(player);
		int level = Math.round(( (1.0F - impact) / 0.8F ) * 20F);
		//System.out.printf("impact := %2.3f  level := %d\n", impact, level);
		for (int i = 1; level > 0 && i < 20; i += 2) {
			if (i < level) {
				drawTexturedModalRect(left, top, 34, 9, 9, 9);
			}
			else if (i == level) {
				drawTexturedModalRect(left, top, 25, 9, 9, 9);
			}
			else if (i > level) {
				drawTexturedModalRect(left, top, 16, 9, 9, 9);
			}
			left += 8;
		}
		GuiIngameForge.left_height += 10;

		GL11.glDisable(GL11.GL_BLEND);
	}


	/* EntityLivingBase pre render event */
	@SubscribeEvent
	public void onCreatureRender(RenderLivingEvent.Pre event) {
		if ( !(event.entity instanceof EntityLivingBase) ) return;
		//if ( !(event.entity instanceof EntityPlayer) ) return;

		// check for special
		int special = RPGESpecial.getInt(event.entity);

		// early out if invisible
		if ( ( special & (1 << RPGESpecial.invisible) ) != 0 ) {
			event.setCanceled(true);
			return;
		}

		/*
		GL11.glPushMatrix();
		GL11.glTranslated(event.x, event.y, event.z);
		// inject special prerender here
		GL11.glPopMatrix();
		 */
	}


	/** EntityLivingBase post render event **/
	@SubscribeEvent
	public void onCreatureRender(RenderLivingEvent.Post event) {

		if ( event.entity instanceof EntityPlayer ) {
			/*
			GL11.glPushMatrix();
			GL11.glTranslated(event.x, event.y, event.z);
			// inject special postrender here
			GL11.glPopMatrix();
			 */

			return;
		}


		/** Handle EntityLiving below **/
		EliteCreatureAura creatureAura;

		// only render if we get a valid aura
		creatureAura = EliteCreatureAura.getCreatureAura(EntityEliteHelper.getEliteAura((EntityLiving) event.entity));
		if ( creatureAura == null ) return;

		float[] rgb = creatureAura.getRGB();
		GL11.glPushMatrix();
		GL11.glTranslated(event.x, event.y, event.z);
		auraRenderer.render(event.entity, rgb[0], rgb[1], rgb[2], auraOffset);
		GL11.glPopMatrix();
	}

}
