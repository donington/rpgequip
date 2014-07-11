package mods.minecraft.donington.rpgequip.client.render;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.minecraft.donington.rpgequip.RPGEquipMod;
import mods.minecraft.donington.rpgequip.common.entity.magic.EntityMagicFire;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;


@SideOnly(Side.CLIENT)
public class MagicFireRenderer extends Render {
	private static ResourceLocation texture;
	private static ResourceLocation textureMask;
	private static Minecraft mc;
	private static Tessellator tes;
	private static double rise;
	private static double flicker;


	public MagicFireRenderer() {
		this.texture = new ResourceLocation(RPGEquipMod.MOD_ID, "textures/fire.png");
		this.textureMask = new ResourceLocation(RPGEquipMod.MOD_ID, "textures/flicker.png");
		this.mc = Minecraft.getMinecraft();
		this.tes = Tessellator.instance;
		this.rise = 0;
		this.flicker = 0;
	}


	public void update() {
		rise += 0.005;
		if ( rise > 1.0 ) rise -= 1.0;
		flicker += Math.random() * 0.002 - 0.001;
		if ( flicker > 1.0 ) flicker = 1.0;
		if ( flicker < -1.0 ) flicker = -1.0;
	}


	public void doRender(EntityMagicFire entity, double posX, double posY, double posZ, float par8, float par9) {

		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);

		GL11.glPushMatrix();
		GL11.glTranslated(posX, posY, posZ);
		GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);

		renderFire(entity, 1.0F, 0.0F, 0.02F, this.rise, this.flicker);
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}


	protected ResourceLocation getEntityTexture(EntityMagicFire entity) {
		return texture;
	}


	protected ResourceLocation getEntityTexture(Entity entity) {
		return this.getEntityTexture((EntityMagicFire)entity);
	}


	public void doRender(Entity entity, double posX, double posY, double posZ, float par8, float par9) {
		this.doRender((EntityMagicFire)entity, posX, posY, posZ, par8, par9);
	}


	private void renderFire(EntityMagicFire entity, float r, float g, float b, double rise, double flicker) {
		double sin = 0;
		double cos = 0;
		double size = entity.width * 1.0 + flicker * 0.1;

		mc.renderEngine.bindTexture(textureMask);
		GL11.glColor4d(1.0, 0.2, 0.1, 1.0);
		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glTranslated(flicker*4, -rise*4, 0);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_ONE_MINUS_CONSTANT_ALPHA);
		RPGERenderHelper.renderCircleZ(tes, size, 0);

		mc.renderEngine.bindTexture(texture);
//		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
		GL11.glBlendFunc(GL11.GL_ZERO, GL11.GL_ONE);
//		GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_ONE);
//		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
//		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//		GL11.glDisable(GL11.GL_DEPTH_TEST);

		for ( int j = 1; j < 5; j++ ) {
			GL11.glColor4d(0.5+j*0.2, 0.4+j*0.2, 0.01, 0.6 + j*0.9);

			size -= 0.048;   // dither the effect
			rise += 0.21;  // crazy up the layering

			GL11.glMatrixMode(GL11.GL_TEXTURE);
			GL11.glTranslated(flicker, -rise, 0);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);

			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			RPGERenderHelper.renderCircleZ(tes, size, j);
		}

		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

}
