package mods.minecraft.donington.superioranvil.common.block;

import mods.minecraft.donington.blocky3d.helper.Blocky3DMetaRotation;
import mods.minecraft.donington.blocky3d.world.Blocky3DBlock;
import mods.minecraft.donington.rpgequip.RPGEquipMod;
import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import mods.minecraft.donington.superioranvil.common.tileentity.SuperiorAnvilTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class SuperiorAnvil extends Blocky3DBlock {
  public static final int gridSize = 3;
  public static final int invSize = gridSize * gridSize;


public SuperiorAnvil() {
    super(Material.anvil, Blocky3DMetaRotation.zAxis, true);
    this.setBlockName("superiorAnvil");
    this.setBlockTextureName("anvil_base");
    this.setHardness(10.0F);
	this.setCreativeTab(RPGECommonProxy.tabGear);
  }


  public TileEntity createNewTileEntity(World var1, int var2) {
    return new SuperiorAnvilTileEntity();
  }


  public void setBlockBoundsBasedOnState(IBlockAccess world, int posX, int posY, int posZ) {
    SuperiorAnvilTileEntity tile = (SuperiorAnvilTileEntity) world.getTileEntity(posX, posY, posZ);
    if ( tile == null ) return;
    int facing = tile.getDirection();

    if (facing != 3 && facing != 1) {
        this.setBlockBounds(0.014F, 0.0F, 0.123F,
                            0.986F, 0.650F, 0.877F);
    } else {
        this.setBlockBounds(0.123F, 0.0F, 0.014F,
                            0.877F, 0.650F, 0.986F);
      }
  }


  /** onBlockActivated(): open the anvil gui **/
  public boolean onBlockActivated(World world, int posX, int posY, int posZ, EntityPlayer player, int _guess_side, float _guess_hitX, float _guess_hitY, float _guess_hitZ) {
    SuperiorAnvilTileEntity tileEntity = (SuperiorAnvilTileEntity) world.getTileEntity(posX, posY, posZ);
    if ( tileEntity == null ) return false;
    player.openGui(RPGEquipMod.instance, RPGECommonProxy.GuiSuperiorAnvilId, world, posX, posY, posZ);
    return true;
  }


  /** breakBlock(): handle removing the anvil tile entity from the world properly **/
  public void breakBlock(World world, int posX, int posY, int posZ, Block block, int p_149749_6_) {
    SuperiorAnvilTileEntity tile = (SuperiorAnvilTileEntity) world.getTileEntity(posX, posY, posZ);
    if ( tile == null ) return;

    tile.releaseInventoryIntoWorld(world, posX, posY, posZ);
    world.removeTileEntity(posX, posY, posZ);
  }

}
