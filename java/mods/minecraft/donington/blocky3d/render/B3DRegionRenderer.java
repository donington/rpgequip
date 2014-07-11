package mods.minecraft.donington.blocky3d.render;

import java.util.HashSet;
import java.util.Set;

import mods.minecraft.donington.blocky3d.helper.B3DRegionCache;
import mods.minecraft.donington.blocky3d.world.B3DRegion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class B3DRegionRenderer {
  private B3DRegionCache regionCache;

  private float[] rgb = { 0.5F, 0.0F, 0.0F };
  private double playerX;
  private double playerY;
  private double playerZ;


  public B3DRegionRenderer() {
    regionCache = new B3DRegionCache();
  }


  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void renderWorldLastEvent(RenderWorldLastEvent event) {
    if ( regionCache.isEmpty() ) return;

    EntityPlayer entityPlayer = Minecraft.getMinecraft().thePlayer;
    playerX = entityPlayer.lastTickPosX + (entityPlayer.posX - entityPlayer.lastTickPosX) * (double) event.partialTicks;
    playerY = entityPlayer.lastTickPosY + (entityPlayer.posY - entityPlayer.lastTickPosY) * (double) event.partialTicks;
    playerZ = entityPlayer.lastTickPosZ + (entityPlayer.posZ - entityPlayer.lastTickPosZ) * (double) event.partialTicks;

    renderRegisteredRegions();
  }


  public void addRegionToRenderer(B3DRegion map) {
	regionCache.addRegion(map);
  }


  public void removeRegionFromRenderer(B3DRegion map) {
    regionCache.invalidate(map);
  }


  public void renderRegisteredRegions() {

    GL11.glEnable(GL11.GL_BLEND);
    GL11.glLineWidth(3.0f);
    GL11.glDisable(GL11.GL_TEXTURE_2D);
    GL11.glDisable(GL11.GL_CULL_FACE);
    GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

    for ( B3DRegion map : regionCache.iterate() ) {
      AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(map.minX, map.minY, map.minZ, map.maxX, map.maxY, map.maxZ);
      double expand = 0.005F;
      aabb = aabb.expand(expand, expand, expand).getOffsetBoundingBox(-playerX, -playerY, -playerZ);

      GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
      renderRegionOutline(aabb, 0.2F);
      GL11.glEnable(GL11.GL_POLYGON_OFFSET_LINE);
      GL11.glPolygonOffset(-1.f,-1.f);
      GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
      renderRegionOutline(aabb, 1.0F);
    }

    GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
    GL11.glEnable(GL11.GL_CULL_FACE);
    GL11.glEnable(GL11.GL_TEXTURE_2D);
    GL11.glDisable(GL11.GL_BLEND);
  }


  public void renderRegionOutline(AxisAlignedBB aabb, float alpha) {
    Tessellator tessellator = Tessellator.instance;

    tessellator.startDrawing(GL11.GL_QUADS);
    tessellator.setColorRGBA_F(rgb[0], rgb[1], rgb[2], alpha);
    tessellator.addVertex(aabb.minX, aabb.minY, aabb.minZ);
    tessellator.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
    tessellator.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
    tessellator.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
    tessellator.draw();
      
    tessellator.startDrawing(GL11.GL_QUADS);
    tessellator.setColorRGBA_F(rgb[0], rgb[1], rgb[2], alpha);
    tessellator.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
    tessellator.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
    tessellator.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
    tessellator.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
    tessellator.draw();
      
    tessellator.startDrawing(GL11.GL_QUADS);
    tessellator.setColorRGBA_F(rgb[0], rgb[1], rgb[2], alpha);
    tessellator.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
    tessellator.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
    tessellator.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
    tessellator.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
    tessellator.draw();
      
    tessellator.startDrawing(GL11.GL_QUADS);
    tessellator.setColorRGBA_F(rgb[0], rgb[1], rgb[2], alpha);
    tessellator.addVertex(aabb.minX, aabb.minY, aabb.minZ);
    tessellator.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
    tessellator.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
    tessellator.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
    tessellator.draw();
      
    tessellator.startDrawing(GL11.GL_QUADS);
    tessellator.setColorRGBA_F(rgb[0], rgb[1], rgb[2], alpha);
    tessellator.addVertex(aabb.minX,aabb.minY, aabb.minZ);
    tessellator.addVertex(aabb.minX,aabb.minY, aabb.maxZ);
    tessellator.addVertex(aabb.minX,aabb.maxY, aabb.maxZ);
    tessellator.addVertex(aabb.minX,aabb.maxY, aabb.minZ);
    tessellator.draw();
      
    tessellator.startDrawing(GL11.GL_QUADS);
    tessellator.setColorRGBA_F(rgb[0], rgb[1], rgb[2], alpha);
    tessellator.addVertex(aabb.maxX,aabb.minY, aabb.minZ);
    tessellator.addVertex(aabb.maxX,aabb.minY, aabb.maxZ);
    tessellator.addVertex(aabb.maxX,aabb.maxY, aabb.maxZ);
    tessellator.addVertex(aabb.maxX,aabb.maxY, aabb.minZ);
    tessellator.draw();
  }

}
