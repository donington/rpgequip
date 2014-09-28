package mods.minecraft.donington.blocky3d.item;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.minecraft.donington.blocky3d.Blocky3DMod;
import mods.minecraft.donington.blocky3d.proxy.B3DClientProxy;
import mods.minecraft.donington.blocky3d.region.B3DRegion;
import mods.minecraft.donington.blocky3d.region.B3DRegionCache;
import mods.minecraft.donington.blocky3d.region.B3DRegionSelectorRenderer;
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

  public ItemWorldWand() {
    super();
    this.setCreativeTab(CreativeTabs.tabMisc);
    this.setMaxStackSize(1);
    this.setFull3D();
    this.setUnlocalizedName("worldWand");
    this.setTextureName("blocky3d:WorldWand");
  }


  @Override
  public int getMaxItemUseDuration(ItemStack stack) {
    return 65535;
  }


  public EnumAction getItemUseAction(ItemStack par1ItemStack) {
    return EnumAction.block;
  }


  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
    player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
    return stack;
  }


  /** do not allow the world wand to effect entities **/
  @Override
  public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
    return true;  // cancel entity click
  }

}
