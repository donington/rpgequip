package mods.minecraft.donington.rpgequip.client.render;

import mods.minecraft.donington.rpgequip.RPGEquipMod;
import mods.minecraft.donington.rpgequip.common.entity.magic.EntityMagicShield;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class MagicShieldRenderer extends Render {

	private static ResourceLocation texture;
	private static ResourceLocation textureMask;
	private static Minecraft mc;
	private static Tessellator tes;
	private static double flare;
	private static double pulse;
	private static final double pulseTick = Math.PI * 0.02;

	private static double red = 1.0;
	private static double green = 1.0;
	private static double blue = 1.0;


	public MagicShieldRenderer() {
		this.texture = new ResourceLocation(RPGEquipMod.MOD_ID, "textures/magic/elite_shield.png");
		this.textureMask = new ResourceLocation(RPGEquipMod.MOD_ID, "textures/magic/scale.png");
		this.mc = Minecraft.getMinecraft();
		this.tes = Tessellator.instance;
		this.pulse = 0;
	}


	public void update() {
		flare += 0.005;
		if ( flare > 1.0 ) flare -= 1.0;
		pulse += pulseTick;
		if ( pulse > Math.PI ) pulse = 0;
	}


	public void doRender(EntityMagicShield entity, double posX, double posY, double posZ, float par8, float par9) {

		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);

		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glTranslated(flare*1.5, -flare*0.5, 0);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		// only render ground portion of shield when caster is on the ground
		EntityLivingBase caster = entity.getCaster();
		if ( caster != null && caster.onGround ) {
			GL11.glPushMatrix();
			GL11.glTranslated(posX, posY + 0.01, posZ);
			renderShieldGround(entity);
			GL11.glPopMatrix();
		}

		// always render sheild facing player
		GL11.glPushMatrix();
		GL11.glTranslated(posX, posY + entity.height * 0.5, posZ);
		GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		//GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		renderShield(entity);
		GL11.glPopMatrix();

		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}


	protected ResourceLocation getEntityTexture(EntityMagicShield entity) {
		return texture;
	}


	protected ResourceLocation getEntityTexture(Entity entity) {
		return this.getEntityTexture((EntityMagicShield)entity);
	}


	public void doRender(Entity entity, double posX, double posY, double posZ, float par8, float par9) {
		this.doRender((EntityMagicShield)entity, posX, posY, posZ, par8, par9);
	}


	private void renderShieldGround(EntityMagicShield entity) {
		double size = entity.width * 0.9 + Math.sin(pulse) * 0.5;

		mc.renderEngine.bindTexture(textureMask);
		GL11.glColor4d(red, green, blue, 1.0);
		GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_ONE_MINUS_CONSTANT_ALPHA);
		RPGERenderHelper.renderCircleY(tes, size, 0);

		mc.renderEngine.bindTexture(texture);
		GL11.glBlendFunc(GL11.GL_ZERO, GL11.GL_ONE);

		for ( int j = 1; j < 5; j++ ) {
			GL11.glColor4d(red - 0.5, green - j*0.2, blue + 0.5, 0.6 + j*0.9);

			size -= 0.048;   // dither the effect

			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			RPGERenderHelper.renderCircleY(tes, size, j);
		}

	}

	private void renderShield(EntityMagicShield entity) {
		double width = entity.width + Math.sin(pulse) * 0.5;
		double height = entity.height + Math.sin(pulse) * 0.5;

		mc.renderEngine.bindTexture(textureMask);
		GL11.glColor4d(red, green, blue, 1.0);
		GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_ONE_MINUS_CONSTANT_ALPHA);
		RPGERenderHelper.renderElipseZ(tes, width, height, 0);

		mc.renderEngine.bindTexture(texture);
		GL11.glBlendFunc(GL11.GL_ZERO, GL11.GL_ONE);

		for ( int j = 1; j < 3; j++ ) {
			GL11.glColor4d(red - 0.5, green - j*0.2, blue + 0.5, 0.6 + j*0.9);

			width -= 0.048;   // dither the effect
			height -= 0.048;   // dither the effect

			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			RPGERenderHelper.renderElipseZ(tes, width, height, (int)(j*50));
		}

	}



}
