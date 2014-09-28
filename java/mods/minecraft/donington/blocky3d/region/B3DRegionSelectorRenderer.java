package mods.minecraft.donington.blocky3d.region;

import java.util.HashSet;
import java.util.Set;

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


@SideOnly(Side.CLIENT)
public class B3DRegionSelectorRenderer {
  private static B3DRegionSelectorRenderer instance;
  private static Minecraft mc;

  private final float[] rgbValid = { 0.3F, 0.3F, 0.0F };
  private final float[] rgbInvalid = { 0.5F, 0.0F, 0.0F };
  private final float[] rgbSelected = { 0.2F, 0.5F, 0.2F };
  private final float[] rgbHighlight = { 0.4F, 0.75F, 0.4F };
  

  public B3DRegionSelectorRenderer() {
	instance = this;
	mc = Minecraft.getMinecraft();
  }


  public static B3DRegionSelectorRenderer instance() {
    return instance;
  }


  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void renderWorldLastEvent(RenderWorldLastEvent event) {
    EntityPlayer player = mc.thePlayer;
    B3DRegionSelector select = B3DRegionCache.getSelector(player);
    if ( select.isEmpty() ) return;

    double playerX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) event.partialTicks;
    double playerY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) event.partialTicks;
    double playerZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) event.partialTicks;

    renderRegisteredRegions(select, player.worldObj.provider.dimensionId, playerX, playerY, playerZ);
  }


  public void renderRegisteredRegions(B3DRegionSelector select, int dimension, double playerX, double playerY, double playerZ) {
    select.validateDimension(dimension);
    B3DRegionSet set = select.getSet();
    if ( set == null ) return;

    GL11.glEnable(GL11.GL_BLEND);
    GL11.glLineWidth(3.0f);
    GL11.glDisable(GL11.GL_TEXTURE_2D);
    GL11.glDisable(GL11.GL_CULL_FACE);
    GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

    int pos = 0;
    int selected = select.getSelectedRegionIndex();
    float rgb[];
    AxisAlignedBB aabb;
    double expand;

    for ( B3DRegion map : set.iterator() ) {
      if ( pos == selected ) {
    	rgb = rgbSelected;

        int[] box = map.getClosestCorner(playerX, playerY, playerZ);
        if ( box != null ) {
      	  aabb = AxisAlignedBB.getBoundingBox(box[0], box[1], box[2], box[0]+1, box[1]+1, box[2]+1);
          expand = 0.008F;
      	  aabb = aabb.expand(expand, expand, expand).getOffsetBoundingBox(-playerX, -playerY, -playerZ);

      	  GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
          renderRegionOutline(aabb, rgbHighlight, 0.2F);
          GL11.glEnable(GL11.GL_POLYGON_OFFSET_LINE);
          GL11.glPolygonOffset(-1.f,-1.f);
          GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
          renderRegionOutline(aabb, rgbHighlight, 1.0F);
          GL11.glDisable(GL11.GL_POLYGON_OFFSET_LINE);
        }

      }
      else if ( map.isValid() )
    	rgb = rgbValid;
      else
    	rgb = rgbInvalid;

      aabb = AxisAlignedBB.getBoundingBox(map.minX, map.minY, map.minZ, map.maxX, map.maxY, map.maxZ);
      expand = 0.005F;
      aabb = aabb.expand(expand, expand, expand).getOffsetBoundingBox(-playerX, -playerY, -playerZ);

      GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
      renderRegionOutline(aabb, rgb, 0.2F);
      GL11.glEnable(GL11.GL_POLYGON_OFFSET_LINE);
      GL11.glPolygonOffset(-1.f,-1.f);
      GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
      renderRegionOutline(aabb, rgb, 1.0F);
      GL11.glDisable(GL11.GL_POLYGON_OFFSET_LINE);

      pos++;
    }
    GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
    GL11.glEnable(GL11.GL_CULL_FACE);
    GL11.glEnable(GL11.GL_TEXTURE_2D);
    GL11.glDisable(GL11.GL_BLEND);
  }


  public void renderRegionOutline(AxisAlignedBB aabb, float[] rgb, float alpha) {
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
