package mods.minecraft.donington.blocky3d.render;

import mods.minecraft.donington.blocky3d.world.Blocky3DBlock;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;

@Deprecated
public class Blocky3DItemBlockRenderer implements IItemRenderer {
  private Blocky3DTileEntityRenderer renderer;
  private float iconScale;


  /** renderer:   used to render the model
   *  iconScale:  scale of model when rendered as icon
   */
  public Blocky3DItemBlockRenderer(Blocky3DTileEntityRenderer renderer, float iconScale) {
    this.renderer = renderer;
    this.iconScale = iconScale;
  }


  @Override
  public boolean handleRenderType(ItemStack item, ItemRenderType type) {
    return true;
  }


  @Override
  public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
    return true;
  }


  /** never change this again.  if it seems wrong, you exported wrong.
   * export tips: center origin on 0. place the origin at the base of the model. 
   **/
  @Override
  public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
    if ( type == ItemRenderType.INVENTORY )
        renderer.renderBlockModel(0D, -0.5D, 0D, iconScale, 0, null);
    else if ( type == ItemRenderType.EQUIPPED )
        renderer.renderBlockModel(0.5D, 0D, 0.5D, iconScale, 0, null);
    else if ( type == ItemRenderType.EQUIPPED_FIRST_PERSON )
        renderer.renderBlockModel(0.5D, 0D, 0.5D, iconScale, 0, null);
    else
      renderer.renderBlockModel(0D, 0D, 0D, 1.0F, 0, null);
  }

}
