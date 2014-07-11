package mods.minecraft.donington.blocky3d.item;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.minecraft.donington.blocky3d.Blocky3DMod;
import mods.minecraft.donington.blocky3d.world.B3DRegion;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;


public class ItemWorldWand extends Item {
  private B3DRegion renderMap;
  private boolean spamlock;


  public ItemWorldWand() {
    super();
    this.setCreativeTab(CreativeTabs.tabMisc);
    this.setMaxStackSize(1);
    this.setFull3D();
    this.setUnlocalizedName("worldWand");
    this.setTextureName("blocky3d:WorldWand");
    renderMap = new B3DRegion();
    spamlock = false;
  }


  private void fizzle(EntityPlayer player) {
    World world = player.worldObj;
    if ( world.isRemote ) {
      world.spawnParticle("smoke", player.posX, player.posY, player.posZ, 0D, 0D, 0D);
    }
  }


  private void handleSelect(EntityPlayer player) {
    if ( player.isSwingInProgress ) return;

    if ( player.capabilities.isCreativeMode == false ) {
      this.fizzle(player);
      return;
    }

    if ( player.worldObj.isRemote ) {
      Minecraft client = Minecraft.getMinecraft();
      MovingObjectPosition mop = client.objectMouseOver;
      if ( mop == null ) return;
        
      if ( mop.typeOfHit == MovingObjectType.BLOCK ) {
        System.out.println("CLICK (" + mop.blockX + "," + mop.blockY + "," + mop.blockZ + ")");
        renderMap.addPointToRegion(mop.blockX, mop.blockY, mop.blockZ);
      }
    }

    //if ( renderMap.isValid() ) Blocky3DMod.regionRenderer.addRegionToRenderer(renderMap);
    return;
  }


  @Override
  public int getMaxItemUseDuration(ItemStack stack) {
    return 20;  // 5 sec to clear
  }


  public EnumAction getItemUseAction(ItemStack par1ItemStack) {
    return EnumAction.block;
  }


/*
  @Override
  public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, EntityPlayer player) {
    return true;  // cancel block breaking
  }
 */

  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
    player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
    spamlock = false;
    return stack;
  }


  @Override
  public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int useCount) {
    if ( spamlock ) return;
    handleSelect(player);
	player.swingItem();
  }


  @Override
  public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
	if ( spamlock ) return stack;
    spamlock = true;

    if ( !world.isRemote )
      renderMap.invalidate();
    return stack;
  }

  @Override
  public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
    return true;  // cancel entity click
  }


/* lol derp mod could live again */
/*
  @Override
  public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
    if ( !(par3Entity instanceof EntityPlayer) ) return;
    EntityPlayer player = (EntityPlayer) par3Entity;
    player.rotationYawHead = player.getRNG().nextFloat() * 360F;
    player.prevRotationYawHead = player.rotationYawHead;
  }
 */
}
