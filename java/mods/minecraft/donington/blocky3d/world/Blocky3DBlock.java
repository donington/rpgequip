package mods.minecraft.donington.blocky3d.world;

import java.util.HashMap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.minecraft.donington.blocky3d.helper.Blocky3DMetaRotation;
import mods.minecraft.donington.blocky3d.render.Blocky3DModel;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class Blocky3DBlock extends Block implements ITileEntityProvider {
  private final boolean isOpaque;
  private Blocky3DMetaRotation rotation;
  private final boolean renderModel;


  private class BlockPlaceCacheKey {
    public World world;
	public int posX;
	public int posY;
	public int posZ;

	public BlockPlaceCacheKey(World world, int posX, int posY, int posZ) {
		this.world = world;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}
  }

  private class BlockPlaceCacheValue {
    public int side;
    public float hitX, hitY, hitZ;

    public BlockPlaceCacheValue(int side, float hitX, float hitY, float hitZ) {
      this.side = side;
      this.hitX = hitX;
      this.hitY = hitY;
      this.hitZ = hitZ;
    }
  }


  private HashMap<BlockPlaceCacheKey,BlockPlaceCacheValue> blockPlaceCache;


  public Blocky3DBlock(Material material) {
    this(material, null, false);
  }


  public Blocky3DBlock(Material material, Blocky3DMetaRotation rotation, boolean customModel) {	  
    super(material);
    this.renderModel = customModel;
    this.isOpaque = !customModel;
    this.rotation = rotation;
    this.blockPlaceCache = new HashMap<BlockPlaceCacheKey,BlockPlaceCacheValue>();
  }


  @Override
  @SideOnly(Side.CLIENT)
  // fallback on mc rendering if model is null
  public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
    if ( this.renderModel ) return false;
    return super.shouldSideBeRendered(iblockaccess, i, j, k, l);
  }


  @Override
  public boolean isOpaqueCube() {
    return this.isOpaque;
  }


  @Override
  public boolean renderAsNormalBlock() {
    if ( this.renderModel ) return false;
    return true;
  }


  /** onBlockPlaced(): called before the block is actually placed in the world.
   * cache block placement data (side, hitX, hitY, hitZ) for use in onBlockPlacedBy()
   */
  @Override
  public int onBlockPlaced(World world, int posX, int posY, int posZ, int side, float hitX, float hitY, float hitZ, int metadata) {
    blockPlaceCache.put(new BlockPlaceCacheKey(world, posX, posY, posZ), new BlockPlaceCacheValue(side, hitX, hitY, hitZ));
    return metadata;
  }


  /** onBlockPlacedBy(): called after the block has been placed in the world.
   * use cached placement data to determine metaRotation
   */
  @Override
  public void onBlockPlacedBy(World world, int posX, int posY, int posZ, EntityLivingBase entity, ItemStack stack) {
    int direction = Blocky3DMetaRotation.getDirectionFacingEntity(entity);

    // use cache data to set appropriate rotation data
    if ( this.rotation == Blocky3DMetaRotation.zAll ) {
      BlockPlaceCacheValue cache = blockPlaceCache.get(new BlockPlaceCacheKey(world, posX, posY, posZ));
      //System.out.println("BlockPlaceRegistry(" + cache.side + ", " + cache.hitX + ", " + cache.hitY + ", " + cache.hitZ + ")");
      if ( ( cache.side == 0 ) || ( cache.side > 1 ) && ( cache.hitY > 0.5F ) )
        direction += Blocky3DMetaRotation.getNumFacing();
    }

	Blocky3DTileEntity tile = (Blocky3DTileEntity) world.getTileEntity(posX,posY,posZ);
    tile.setDirection(direction);
    blockPlaceCache.remove(this);
  }


  public Blocky3DMetaRotation getMetaRotation() {
    return rotation;
  }

}
