package mods.minecraft.donington.rpgequip.client.render;

import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class RPGERenderHelper {

	/** render a circle on the Y plane **/
	public static void renderCircleY(Tessellator tes, double size, int layer) {
		double lv = layer * 0.01F;
		tes.startDrawing(GL11.GL_TRIANGLE_STRIP);
		tes.addVertexWithUV(0, lv, 0, 0.5, 0.5);
		for ( int i = 0; i < 16; i++ ) {
			tes.addVertexWithUV(size * Math.sin(Math.PI*i/8), lv, size * Math.cos(Math.PI*i/8), 0.5 + Math.sin(Math.PI*i/8), 0.5 + Math.cos(Math.PI*i/8));
			tes.addVertexWithUV(0, lv, 0, 0.5, 0.5);
		}
		tes.addVertexWithUV(size * Math.sin(0), lv, size * Math.cos(0), 0.5 + Math.sin(Math.PI*2), 0.5 + Math.cos(Math.PI*2));
		tes.draw();
	}


	/** render a circle on the Z plane **/
	public static void renderCircleZ(Tessellator tes, double size, int layer) {
		double lv = layer * -0.01F;
		tes.startDrawing(GL11.GL_TRIANGLE_STRIP);
		tes.addVertexWithUV(0, 0, lv, 0.5, 0.5);
		for ( int i = 0; i < 16; i++ ) {
			tes.addVertexWithUV(size * Math.sin(Math.PI*i/8), size * Math.cos(Math.PI*i/8), lv, 0.5 + Math.sin(Math.PI*i/8), 0.5 + Math.cos(Math.PI*i/8));
			tes.addVertexWithUV(0, 0, lv, 0.5, 0.5);
		}
		tes.addVertexWithUV(size * Math.sin(0), size * Math.cos(0), lv, 0.5 + Math.sin(Math.PI*2), 0.5 + Math.cos(Math.PI*2));
		tes.draw();
	}


	/** render an elipses on the Z plane **/
	public static void renderElipseZ(Tessellator tes, double width, double height, int layer) {
		double lv = layer * -0.01F;
		tes.startDrawing(GL11.GL_TRIANGLE_STRIP);
		tes.addVertexWithUV(0, 0, lv, 0.5, 0.5);
		for ( int i = 0; i < 16; i++ ) {
			tes.addVertexWithUV(width * Math.sin(Math.PI*i/8), height * Math.cos(Math.PI*i/8), lv, 0.5 + Math.sin(Math.PI*i/8), 0.5 + Math.cos(Math.PI*i/8));
			tes.addVertexWithUV(0, 0, lv, 0.5, 0.5);
		}
		tes.addVertexWithUV(width * Math.sin(0), height * Math.cos(0), lv, 0.5 + Math.sin(Math.PI*2), 0.5 + Math.cos(Math.PI*2));
		tes.draw();
	}

}
