package mods.minecraft.donington.rpgequip.common.inventory;

import mods.minecraft.donington.blocky3d.network.B3DPacketDispatcher;
import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import mods.minecraft.donington.rpgequip.common.RPGEAttributes;
import mods.minecraft.donington.rpgequip.common.item.ItemGear;
import mods.minecraft.donington.rpgequip.common.player.GearInventory;
import mods.minecraft.donington.rpgequip.network.PlayerAttributePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerRPGEInventory extends Container {
  private EntityPlayer  player;
  private IInventory    inventory;
  private IInventory    gearInventory;

  private InventoryCrafting  craftMatrix = new InventoryCrafting(this, 2, 2);
  private IInventory         craftResult = new InventoryCraftResult();

  /** ui planning saves time */
  private static final int ARMOR_OFFSET_X = 12;
  private static final int ARMOR_OFFSET_Y = 8;
  private static final int GEAR1_OFFSET_X = 30;
  private static final int GEAR1_OFFSET_Y = 71;
  private static final int GEAR2_OFFSET_X = 84;
  private static final int GEAR2_OFFSET_Y = 8;

  private static final int CRAFTINPUT_OFFSET_X = 107;
  private static final int CRAFTINPUT_OFFSET_Y = 53;
  private static final int CRAFTOUTPUT_OFFSET_X = 147;
  private static final int CRAFTOUTPUT_OFFSET_Y = 62;
  
  private static final int INVENTORY_OFFSET_X = 8;
  private static final int INVENTORY_OFFSET_Y = 94;
  private static final int HOTBAR_OFFSET_X = 8;
  private static final int HOTBAR_OFFSET_Y = 152;


  public ContainerRPGEInventory(InventoryPlayer inv) {
    this.inventory = inv;
    this.player = inv.player;
    this.gearInventory = GearInventory.get(player).getGearInventory();

	int i;
	int j;

    /** armor slots [0-3] */
    for (i = 0; i < 4; ++i) {
      final int armorType = i;
      this.addSlotToContainer(new ArmorSlot(player, ItemGear.ArmorSlotList[i].getEquipId(), this.inventory.getSizeInventory() - 1 - i, ARMOR_OFFSET_X, ARMOR_OFFSET_Y + i * 18));
    }

    /** bottom gear slots [4-6] */
    for ( i = 0; i < 3; i++ )
      this.addSlotToContainer(new GearSlot(player, gearInventory, ItemGear.GearSlotList[i].getEquipId(), i, GEAR1_OFFSET_X + i * 18,  GEAR1_OFFSET_Y));

    /** side gear slots [7-10] */
    for ( i = 0; i < 4; i++ )
      this.addSlotToContainer(new GearSlot(player, gearInventory, ItemGear.GearSlotList[i+3].getEquipId(), i+3, GEAR2_OFFSET_X,  GEAR2_OFFSET_Y + i * 18));

    /** crafting [11-15] */
    this.addSlotToContainer(new SlotCrafting(player, craftMatrix, craftResult, 0, CRAFTOUTPUT_OFFSET_X, CRAFTOUTPUT_OFFSET_Y));
    for (i = 0; i < 2; ++i) {
      for (j = 0; j < 2; ++j) {
        this.addSlotToContainer(new Slot(craftMatrix, j + i * 2, CRAFTINPUT_OFFSET_X + j * 18, CRAFTINPUT_OFFSET_Y + i * 18));
      }
    }

	/** inventory [16-43] */
    for (i = 0; i < 3; i++) {
      for (j = 0; j < 9; j++) {
        addSlotToContainer(new Slot(inventory, j + i * 9 + 9, INVENTORY_OFFSET_X + j * 18, INVENTORY_OFFSET_Y + i * 18));
      }
    }

    /** hotbar [44-53] */
    for (i = 0; i < 9; i++) {
      addSlotToContainer(new Slot(inventory, i, HOTBAR_OFFSET_X + i * 18, HOTBAR_OFFSET_Y));
    }

    this.onCraftMatrixChanged(this.craftMatrix);
  }


  public boolean canInteractWith(EntityPlayer player) {
    return true;
  }


  @Override
  public ItemStack slotClick(int slotnum, int mouseClick, int par3, EntityPlayer player) {
    ItemStack stack = super.slotClick(slotnum, mouseClick, par3, player);
//    System.out.printf("%d  %d  %d  %s\n", slotnum, mouseClick, par3, player.getCommandSenderName());
	return stack;
  }


  /* TODO: rewrite */
  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int slotnum) {
    ItemStack itemstack = null;
    Slot slot = (Slot)this.inventorySlots.get(slotnum);

    //System.out.println("slotnum := " + slotnum);

    if ( slot == null || !slot.getHasStack() ) return null;

    ItemStack itemstack1 = slot.getStack();
    itemstack = itemstack1.copy();

    return null;
  }


  @Override
  public void onCraftMatrixChanged(IInventory inv) {
    this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(craftMatrix, player.worldObj));
  }


/*
  @Override
  public void detectAndSendChanges() {
    super.detectAndSendChanges();

    if ( RPGECommonProxy.isServer() ) {
      RPGEAttributes attrs = RPGEAttributes.get(player);
      if ( attrs.areDirty() ) {
    	System.out.println("cleaning dirty attributes");
    	GearInventory.get(player).recalculate(attrs);
        B3DPacketDispatcher.sendTo(new RPGEAttributePacket(attrs), (EntityPlayerMP) player);
        attrs.clearDirty();
      }
    }
  }
 */


  /** canItemStackGoIntoSlotInventory: seems to be a generic slot filter */
  @Override
  public boolean func_94530_a(ItemStack par1ItemStack, Slot par2Slot) {
    return par2Slot.inventory != this.craftResult && super.func_94530_a(par1ItemStack, par2Slot);
  }

}