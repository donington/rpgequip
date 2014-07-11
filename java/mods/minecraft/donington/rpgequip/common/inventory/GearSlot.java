package mods.minecraft.donington.rpgequip.common.inventory;

import mods.minecraft.donington.blocky3d.network.B3DPacketDispatcher;
import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import mods.minecraft.donington.rpgequip.common.RPGEAttributes;
import mods.minecraft.donington.rpgequip.common.item.ItemGear;
import mods.minecraft.donington.rpgequip.common.player.GearInventory;
import mods.minecraft.donington.rpgequip.network.PlayerAttributePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GearSlot extends Slot {
  protected final EntityPlayer player;
  protected final int equipType;


  public GearSlot(EntityPlayer player, IInventory inv, int equipType, int invSlot, int uiPosX, int uiPosY) {
    super(inv, invSlot, uiPosX, uiPosY);
    this.player = player;
    this.equipType = equipType;
  }


  /** handle server attribute update */
  private void updateAttributes() {

    if ( RPGECommonProxy.isServer() ) {
      //System.out.println("updateAttributes()");
      RPGEAttributes attrs = RPGEAttributes.get(player);
      GearInventory.get(player).recalculate(attrs);
      attrs.applyModifiers(player);
      B3DPacketDispatcher.sendTo(new PlayerAttributePacket(attrs), (EntityPlayerMP) player);
    }
  }


  @Override
  public void putStack(ItemStack stack) {
	super.putStack(stack);
	updateAttributes();
  }


  @Override
  public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
	super.onPickupFromSlot(player, stack);
	updateAttributes();
  }


  @Override
  public int getSlotStackLimit() {
    return 1;
  }


  @Override
  public boolean isItemValid(ItemStack stack) {
    if (stack == null) return false;
    Item item = stack.getItem();
    if (item instanceof ItemGear) {
      return ((ItemGear)item).getEquipId() == equipType;
    }
    return false;
  }


  @Override
  @SideOnly(Side.CLIENT)
  public IIcon getBackgroundIconIndex() {
    return ItemGear.emptySlotIcons[this.equipType];
  }
	
}
