package mods.minecraft.donington.rpgequip.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class AuraRenderer {
	private static Minecraft mc;
	private static Tessellator tes;
	private static ResourceLocation auraTexture;
	private static double pulse;


	public AuraRenderer(ResourceLocation texture) {
		this.mc = Minecraft.getMinecraft();
		this.tes = Tessellator.instance;
		auraTexture = texture;
	}


	/** render a badass aura at the feet of the entity **/
	public void render(EntityLivingBase entity, float r, float g, float b, double offset) {
		double size;
		int i;

		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		//GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		//GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
		//GL11.glBlendFunc(GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		mc.renderEngine.bindTexture(auraTexture);

		// set pulse state for this render based off of offset
		// rotation of 360 degrees converted to 64 pulses per full rotation
		// pulse range is 15/16ths to 1
		pulse = 1.0 - (Math.abs(Math.sin(offset/5.625*Math.PI)) * 0.0625);

		render3DAura(entity, r, g, b, offset);
		render2DAura(entity, r, g, b, offset);

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
	}


	private void render2DAura(EntityLivingBase entity, float r, float g, float b, double offset) {
		double sin = 0;
		double cos = 0;
		double size = entity.width * 1.5 * pulse;

		for ( float j = 0.01F; j < 0.05F; j+=0.01F ) {
			GL11.glColor4d(r, g, b, 0.5 + j*8);

			size -= 0.064;   // dither the effect
			offset += 22.5;  // crazy up the layering

			GL11.glMatrixMode(GL11.GL_TEXTURE);
			GL11.glTranslated(0.5, 0.5, 0);
			GL11.glRotated(offset, 0, 0, 1);
			GL11.glTranslated(-0.5, -0.5, 0);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);

			tes.startDrawing(GL11.GL_TRIANGLE_STRIP);
			tes.addVertexWithUV(0, j, 0, 0.5, 0.5);
			for ( int i = 0; i < 16; i++ ) {
				tes.addVertexWithUV(size * Math.sin(Math.PI*i/8), j, size * Math.cos(Math.PI*i/8), 0.5 + Math.sin(Math.PI*i/8), 0.5 + Math.cos(Math.PI*i/8));
				tes.addVertexWithUV(0, j, 0, 0.5, 0.5);
			}
			tes.addVertexWithUV(size * Math.sin(0), j, size * Math.cos(0), 0.5 + Math.sin(Math.PI*2), 0.5 + Math.cos(Math.PI*2));
			tes.draw();
		}

		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}


	/** render 3D portion of aura **/
	private void render3DAura(EntityLivingBase entity, float r, float g, float b, double offset) {
		double heartbeat = pulse * pulse;
		double uvOffset = Math.PI*(offset/360);
		double height = entity.height * heartbeat * 0.1;
		double size = entity.width * heartbeat * 0.35;
		double min = entity.width * heartbeat * 0.21;

		GL11.glDisable(GL11.GL_CULL_FACE);

		for ( int facing = 0; facing < 360; facing+=15 ) {
			GL11.glPushMatrix();
			GL11.glRotated(-facing, 0.0, 1.0, 0.0);
			GL11.glTranslated(-0.3 * heartbeat, 0.0, 0.3 * heartbeat);
			GL11.glColor4d(r, g, b, 1.0);
			tes.startDrawingQuads();

			tes.addVertexWithUV(-size, height, -size, Math.sin(uvOffset), Math.cos(uvOffset));
			tes.addVertexWithUV(-min, 0.0, -min, Math.cos(uvOffset), Math.sin(uvOffset));
			tes.addVertexWithUV(+min, 0.0, +min, Math.cos(uvOffset), Math.sin(uvOffset));
			tes.addVertexWithUV(+size, height, +size, Math.sin(uvOffset), Math.cos(uvOffset));

			tes.draw();
			GL11.glPopMatrix();
		}

		GL11.glEnable(GL11.GL_CULL_FACE);

	}

}
