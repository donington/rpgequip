package mods.minecraft.donington.rpgequip.forge;

import java.util.HashMap;

import mods.minecraft.donington.rpgequip.RPGEquipMod;
import mods.minecraft.donington.rpgequip.client.render.AuraRenderer;
import mods.minecraft.donington.rpgequip.client.render.EntityEliteRenderer;
import mods.minecraft.donington.rpgequip.client.render.MagicFireRenderer;
import mods.minecraft.donington.rpgequip.client.render.MagicShieldRenderer;
import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import mods.minecraft.donington.rpgequip.common.RPGEAttributes;
import mods.minecraft.donington.rpgequip.common.entity.elite.EliteCreatureAura;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.EntityCreature;
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

	/* TODO: aura class to better define auras */
	private AuraRenderer eliteAura = new AuraRenderer(new ResourceLocation(RPGEquipMod.MOD_ID, "textures/aura.png"));
	private static double auraOffset;


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
		if ( RPGECommonProxy.isServer() ) return;

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


	/* quick hack to test auras */
	/*
	@SubscribeEvent
	public void onCreatureRender(RenderLivingEvent.Pre event) {
		if ( event.entity instanceof EntityPlayer ) return;
		//if ( !(event.entity instanceof EntityLiving) ) return;
		int aura = event.entity.getDataWatcher().getWatchableObjectInt(RPGECommonProxy.auraDataWatcherId);
		if ( aura == 0 ) return;

		// quick n dirty aura no creature hack for testing
		event.setCanceled(true);
		GL11.glPushMatrix();
		GL11.glTranslated(event.x, event.y, event.z);
		eliteAura.render(event.entity, 0.6F, 0.1F, 0.2F, auraOffset);
		GL11.glPopMatrix();
	}
	*/


	/** render an aura on creatures with RPGEAttributes **/
	@SubscribeEvent
	public void onCreatureRender(RenderLivingEvent.Post event) {

		if ( event.entity instanceof EntityPlayer ) return;
		//if ( !(event.entity instanceof EntityLiving) ) return;
		int aura = event.entity.getDataWatcher().getWatchableObjectInt(RPGECommonProxy.eliteAuraDataWatcherId);
		if ( aura < 1 ) return;
		if ( aura > EliteCreatureAura.getNumAuras() ) return;
		//if ( aura > RPGECommonProxy.auraCount ) return;
		//System.out.println("aura := " + aura);

		GL11.glPushMatrix();
		GL11.glTranslated(event.x, event.y, event.z);

		// FIXME: this needs to be faster (lookup enum hashmap)
		for ( EliteCreatureAura creatureAura : EliteCreatureAura.values() ) {
			if ( creatureAura.isAura(aura) ) {
				float[] rgb = creatureAura.getRGB();
				eliteAura.render(event.entity, rgb[0], rgb[1], rgb[2], auraOffset);
				break;
			}
		}
/*
		switch ( aura ) {
		case 1:
			eliteAura.render(event.entity, 0.6F, 0.1F, 0.2F, auraOffset);  // red heavy blue
			break;
		case 2:
			eliteAura.render(event.entity, 0.1F, 0.6F, 0.2F, auraOffset);  // green heavy blue
			break;
		case 3:
			eliteAura.render(event.entity, 0.2F, 0.1F, 0.6F, auraOffset);  // blue heavy red
			break;
		case 4:
			eliteAura.render(event.entity, 0.88F, 0.63F, 0.18F, auraOffset);  // orange
			break;
		case 5:
			eliteAura.render(event.entity, 0.63F, 0.88F, 0.18F, auraOffset);  // yellow
			break;
		case 6:
			eliteAura.render(event.entity, 0.63F, 0.18F, 0.88F, auraOffset);  // magenta
			break;
		case 7:
			eliteAura.render(event.entity, 0.18F, 0.63F, 0.88F, auraOffset);  // cyan
			break;
		case 8:
			eliteAura.render(event.entity, 1.0F, 1.0F, 1.0F, auraOffset);  // white
			break;
		default:
			// renderAura(event.entity, 0.2F, 0.6F, 0.1F);  // green heavy red
			// renderAura(event.entity, 0.6F, 0.1F, 0.2F);  // red heavy green
			// renderAura(event.entity, 0.1F, 0.2F, 0.6F);  // blue heavy green
			break;
		}
		*/
		GL11.glPopMatrix();
	}

}
