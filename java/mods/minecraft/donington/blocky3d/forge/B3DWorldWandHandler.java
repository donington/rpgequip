package mods.minecraft.donington.blocky3d.forge;

import java.util.HashSet;
import java.util.Set;

import mods.minecraft.donington.blocky3d.proxy.B3DCommonProxy;
import mods.minecraft.donington.blocky3d.region.B3DRegion;
import mods.minecraft.donington.blocky3d.region.B3DRegionCache;
import mods.minecraft.donington.blocky3d.region.B3DRegionSelector;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class B3DWorldWandHandler {


  /** do not allow the world wand to break blocks **/
  @SubscribeEvent
  public void onBreakBlock(BlockEvent.BreakEvent event) {
    EntityPlayer player = event.getPlayer();
    if ( !isUsingWorldWand(player) ) return;
    event.setCanceled(true);
  }


  /** do not allow block interaction when using the world wand **/
  @SubscribeEvent
  public void onInteract(PlayerInteractEvent event) {
    EntityPlayer player = event.entityPlayer;
    if ( !isUsingWorldWand(player) ) return;
    event.useBlock = Result.DENY;
  }


  /** handle world wand usage (client side event) **/
  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public void onMouseInput(MouseEvent event) {
	// ignore if not triggered by mouse button up
    if ( event.button == -1 || event.buttonstate == true ) return;

    EntityPlayer player = Minecraft.getMinecraft().thePlayer;
    if ( !isUsingWorldWand(player) ) return;
    if ( !hasPermissions(player) ) return;

    MovingObjectPosition mop = player.rayTrace(5.0, 0.0F);
    if ( mop != null && mop.typeOfHit == MovingObjectType.BLOCK ) {
      switch ( event.button ) {
        case 0:
          handleSelect(player, player.worldObj, true, mop.blockX, mop.blockY, mop.blockZ);
        case 1:
          handleSelect(player, player.worldObj, false, mop.blockX, mop.blockY, mop.blockZ);
        default:
    	  return;
      }
    }

    switch ( event.button ) {
      case 0:
   		handleSelect(player, player.worldObj, true);
      case 1:
   		handleSelect(player, player.worldObj, false);
    	return;
      default:
    	return;
    }
  }


  /** trigger world wand use ability when duration == ItemWorldWand.USE_TRIGGER **/
/*
  @SubscribeEvent
  public void onUseWorldWand(PlayerUseItemEvent.Tick event) {
	if ( event.duration != ItemWorldWand.USE_TRIGGER ) return;
	EntityPlayer player = event.entityPlayer;
    if ( player.isSwingInProgress ) return;
    ItemStack stack = player.getHeldItem();
    if ( stack == null || stack.getItem() != B3DCommonProxy.itemWorldWand ) return;

    event.duration = 0;

    B3DRegionSelector select = B3DRegionCache.getSelector(player);
	if ( select.isEmpty() ) return;

    B3DRegionCache.clearSelector(player);
    player.swingItem();
  }
  */


  /*
  @SubscribeEvent
  public void onStopUsingWorldWand(PlayerUseItemEvent.Stop event) {
	if ( event.duration < ItemWorldWand.USE_TRIGGER ) return;
	EntityPlayer player = event.entityPlayer;
    if ( player.isSwingInProgress ) return;
    ItemStack stack = player.getHeldItem();
    if ( stack == null || stack.getItem() != B3DCommonProxy.itemWorldWand ) return;

    B3DRegionSelector select = B3DRegionCache.getSelector(player);
	if ( select.isEmpty() ) select.put(new B3DRegion());

	handleSelect(player, player.worldObj, select);
    player.swingItem();
  }
 */


  private boolean isUsingWorldWand(EntityPlayer player) {
    ItemStack stack = player.getHeldItem();
    if ( stack == null || stack.getItem() != B3DCommonProxy.itemWorldWand ) return false;
    return true;
  }


  private boolean hasPermissions(EntityPlayer player) {
//    if ( MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(player.getCommandSenderName()) )
    if ( player.capabilities.isCreativeMode ) return true;

    fizzle(player, player.worldObj);
	return false;
  }


  private void fizzle(EntityPlayer player, World world) {
	if ( world.isRemote ) {
	  world.spawnParticle("smoke", player.posX, player.posY, player.posZ, 0D, 0D, 0D);
	}
  }

  private void validate(EntityPlayer player, World world, B3DRegionSelector select, B3DRegion map) {
	// maybe sync packet crap here?

	// DEBUG: announce selected cache region
    System.out.printf("MAP Effected Cache Region: ");
    Set<Integer> vvv = new HashSet();
    map.getEffectedCacheXZ(vvv);
    for ( Integer cacheXZ : vvv )
      System.out.printf("%d ", cacheXZ);
    System.out.println();
  }


  private void handleSelect(EntityPlayer player, World world, boolean remove) {
	B3DRegionSelector select = B3DRegionCache.getSelector(player);
	if ( select.isEmpty() ) select.put(new B3DRegion());
	B3DRegion map = select.getSelectedRegion();

    int playerX, playerY, playerZ;
    playerX = (int) Math.floor(player.posX);
    playerY = (int) Math.floor(player.posY) -1;
    playerZ = (int) Math.floor(player.posZ);
    if ( remove )
      map.removePointFromRegion(playerX, playerY, playerZ);
    else
      map.addPointToRegion(playerX, playerY, playerZ);

    validate(player, world, select, map);
  }


  private void handleSelect(EntityPlayer player, World world, boolean remove, int blockX, int blockY, int blockZ) {
	B3DRegionSelector select = B3DRegionCache.getSelector(player);
	if ( select.isEmpty() ) select.put(new B3DRegion());
	B3DRegion map = select.getSelectedRegion();

    if ( remove )
      map.removePointFromRegion(blockX, blockY, blockZ);
    else
      map.addPointToRegion(blockX, blockY, blockZ);

    validate(player, world, select, map);
    player.swingItem();
  }

}
