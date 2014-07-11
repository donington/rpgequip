package mods.minecraft.donington.blocky3d.render;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.minecraft.donington.blocky3d.helper.Blocky3DMetaRotation;
import mods.minecraft.donington.blocky3d.world.Blocky3DBlock;
import mods.minecraft.donington.blocky3d.world.Blocky3DTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;


public class Blocky3DTileEntityRenderer extends TileEntitySpecialRenderer {
  private Blocky3DModel model;

  public Blocky3DTileEntityRenderer(Blocky3DModel model) {
    this.model = model;
  }


  @Override
  public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {

	if ( ( tileEntity instanceof Blocky3DTileEntity ) &&
         ( tileEntity.blockType instanceof Blocky3DBlock ) &&
         ( model != null ) ) {
      renderBlockModel(x, y, z, 1.0F, (Blocky3DTileEntity)tileEntity, (Blocky3DBlock)tileEntity.blockType);
    }
	// TODO: maybe notice of invalid Blocky3D state
  }


  /* world object version */
  public void renderBlockModel(double x, double y, double z, float scale, Blocky3DTileEntity tile, Blocky3DBlock block) {
    //renderBlockModel(x, y, z, scale, tile.getDirection(), block.getMetaRotation());
    renderBlockModel(x + 0.5D, y, z + 0.5D, scale, tile.getDirection(), block.getMetaRotation());
  }


  /* generic version */
  public void renderBlockModel(double x, double y, double z, float scale, int direction, Blocky3DMetaRotation rotation) {
	GL11.glPushMatrix();
    GL11.glScalef(scale, scale, scale);
    //GL11.glTranslated(x + 0.5D, y, z + 0.5D);
    //GL11.glTranslated(x, y - 0.5D, z);
    GL11.glTranslated(x, y, z);
    Blocky3DMetaRotation.glTransform(direction, rotation);
    this.bindTexture(model.getTexture());
    model.doRender();
    GL11.glPopMatrix();
  }


  /* useful for renderBlockModel overrides */
  public Blocky3DModel getModel() {
    return model;
  }

}
