package mods.minecraft.donington.rpgequip.common.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import mods.minecraft.donington.rpgequip.common.item.ItemGear;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ArmorSlot extends GearSlot {


  public ArmorSlot(EntityPlayer player, int equipType, int invSlot, int uiPosX, int uiPosY) {
    super(player, player.inventory, equipType, invSlot, uiPosX, uiPosY);
  }


  public boolean isItemValid(ItemStack stack) {
    if (stack == null) return false;

    Item item = stack.getItem();
    if (item instanceof ItemGear) {
      return ((ItemGear)item).getEquipId() == equipType;
    }

    return stack.getItem().isValidArmor(stack, this.equipType, this.player);
  }

}
