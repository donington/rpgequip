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
	private static Minecraft mc;
	private static Tessellator tes;

	// TODO: simplify, expand
	private static double rise;
	private static double flicker;

	public static final int ID_ELITE_FIRE = 0;
	public static final int ID_DEADLY_FORCE = 1;
	public static final int ID_DOOM_ORB = 2;

	private static final int fireIndexMax = 2;
	private static float[][] rgb;
	private static float[][] rgbShift;
	private static float[] colorMultiplier;
	private static float[] riseMultiplier;
	private static float[] flickerMultiplier;

	private static ResourceLocation texture[];
	private static ResourceLocation textureMask[];

	private static int[] blendFunc;
	private static final int BLEND_ZERO_ONE = 0;
	private static final int BLEND_ONE_ONE = 1;
	private static final int BLEND_DST_COLOR_ONE = 2;
	private static final int BLEND_SRC_ALPHA_ONE = 3;
	private static final int BLEND_SRC_ALPHA_ONE_MINUS_SRC_ALPHA = 4;



	static {
		rgb = new float[][] {
				{1.0F, 0.2F, 0.1F},
				{0.8F, 0.8F, 0.8F},
				{1.0F, 0.2F, 0.1F}
		};
		rgbShift = new float[][] {
				{0.5F, 2.0F, 0.01F},
				{0.5F, 0.5F, 0.5F},
				{0.9F, 0.2F, 0.1F}
		};
		colorMultiplier = new float[] {
				0.8F,
				0.25F,
				0.8F,
		};
		riseMultiplier = new float[] {
				1.0F,
				-0.5F,
				1.3F,
		};
		flickerMultiplier = new float[] {
				1.0F,
				0.7F,
				1.3F,
		};
		texture = new ResourceLocation[] {
				new ResourceLocation(RPGEquipMod.MOD_ID, "textures/magic/elite_fire.png"),
				new ResourceLocation(RPGEquipMod.MOD_ID, "textures/magic/doom.png"),
				new ResourceLocation(RPGEquipMod.MOD_ID, "textures/magic/flame.png")
		};

		ResourceLocation flicker1 = new ResourceLocation(RPGEquipMod.MOD_ID, "textures/magic/flicker1.png");
		textureMask = new ResourceLocation[] {
				flicker1,
				new ResourceLocation(RPGEquipMod.MOD_ID, "textures/magic/deadly_force.png"),
				flicker1
		};
		blendFunc = new int[] {
				BLEND_ZERO_ONE,
				BLEND_DST_COLOR_ONE,
				BLEND_SRC_ALPHA_ONE_MINUS_SRC_ALPHA
		};
		// BLEND_ZERO_ONE
		// BLEND_ONE_ONE
		// BLEND_DST_COLOR_ONE
		// BLEND_SRC_ALPHA_ONE
		// BLEND_SRC_ALPHA_ONE_MINUS_SRC_ALPHA

	}


	public MagicFireRenderer() {
		this.mc = Minecraft.getMinecraft();
		this.tes = Tessellator.instance;
		this.rise = 0;
		this.flicker = 0;
	}


	public void update() {
		rise += 0.005;
		//if ( rise > 1.0 ) rise -= 1.0;
		flicker += Math.random() * 0.002 - 0.001;
		if ( flicker > 1.0 ) flicker = 1.0;
		if ( flicker < -1.0 ) flicker = -1.0;
	}


	public void doRender(EntityMagicFire entity, double posX, double posY, double posZ, float par8, float par9) {
		int id = entity.getColorIndex();
		if ( id < 0 || id > fireIndexMax ) return;

		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);

		GL11.glPushMatrix();
		GL11.glTranslated(posX, posY, posZ);
		GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);

		renderFire(entity, id);
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}


	protected ResourceLocation getEntityTexture(EntityMagicFire entity) {
		int id = entity.getColorIndex();
		if ( id < 0 || id > fireIndexMax ) return null;
		return texture[id];
	}


	protected ResourceLocation getEntityTexture(Entity entity) {
		return this.getEntityTexture((EntityMagicFire)entity);
	}


	public void doRender(Entity entity, double posX, double posY, double posZ, float par8, float par9) {
		this.doRender((EntityMagicFire)entity, posX, posY, posZ, par8, par9);
	}


	private void renderFire(EntityMagicFire entity, int id) {
		double sin = 0;
		double cos = 0;
		float r = this.rgb[id][0];
		float g = this.rgb[id][1];
		float b = this.rgb[id][2];
		float shR = this.rgbShift[id][0];
		float shG = this.rgbShift[id][1];
		float shB = this.rgbShift[id][2];
		float decay = this.colorMultiplier[id];
		double rise = this.rise * riseMultiplier[id];
		double flicker = this.flicker * flickerMultiplier[id];
		double size = entity.width * 1.0 + flicker * 0.1;
		
		mc.renderEngine.bindTexture(textureMask[id]);
		GL11.glColor4f(r, g, b, 1.0F);
		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glTranslated(flicker*4, -rise*4, 0);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_ONE_MINUS_CONSTANT_ALPHA);
		RPGERenderHelper.renderCircleZ(tes, size, 0);

		mc.renderEngine.bindTexture(texture[id]);

		// TODO: blend func selection here
		switch ( this.blendFunc[id] ) {
		case BLEND_ZERO_ONE:
			GL11.glBlendFunc(GL11.GL_ZERO, GL11.GL_ONE);
			break;
		case BLEND_ONE_ONE:
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
			break;
		case BLEND_DST_COLOR_ONE:
			GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_ONE);
			break;
		case BLEND_SRC_ALPHA_ONE:
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			break;
		case BLEND_SRC_ALPHA_ONE_MINUS_SRC_ALPHA:
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			break;
		}


		// GL11.glDisable(GL11.GL_DEPTH_TEST);

		r = r * shR;
		g = g * shG;
		b = b * shB;
		// 0.5 0.4 0.01
		for ( int j = 1; j < 5; j++ ) {
			r = r * decay;
			g = g * decay;
			b = b * decay;
			GL11.glColor4f(r, g, b, 0.6F + j*0.9F);

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
