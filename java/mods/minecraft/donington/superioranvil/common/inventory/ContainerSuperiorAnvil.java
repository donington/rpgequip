package mods.minecraft.donington.superioranvil.common.inventory;

import mods.minecraft.donington.superioranvil.common.block.SuperiorAnvil;
import mods.minecraft.donington.superioranvil.common.item.crafting.SuperiorAnvilCraftingManager;
import mods.minecraft.donington.superioranvil.common.tileentity.SuperiorAnvilTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSuperiorAnvil extends Container {
	private IInventory               playerInv;
	private SuperiorAnvilTileEntity  tileEntity;
	private InventoryBasic           anvilInv;
	private InventoryCraftResult     craftResult;

	private static final int CRAFTING_OFFSET_X = 62;
	private static final int CRAFTING_OFFSET_Y = 28;
	private static final int RESULT_OFFSET_X = 125;
	private static final int RESULT_OFFSET_Y = 46;
	private static final int INVENTORY_OFFSET_X = 8;
	private static final int INVENTORY_OFFSET_Y = 94;
	private static final int HOTBAR_OFFSET_X = 8;
	private static final int HOTBAR_OFFSET_Y = 152;

	public ContainerSuperiorAnvil(InventoryPlayer inv, SuperiorAnvilTileEntity tileEntity) {
		this.playerInv = inv;
		this.tileEntity = tileEntity;
		this.anvilInv = tileEntity.getInventory();
		this.craftResult = new InventoryCraftResult();
		int i, j;

		/** anvil [0-8] **/
		for ( i = 0; i < SuperiorAnvil.gridSize; i++ ) {
			for ( j = 0; j < SuperiorAnvil.gridSize; j++ ) {
				addSlotToContainer(new Slot(anvilInv, j + i * 3, CRAFTING_OFFSET_X + j * 18, CRAFTING_OFFSET_Y + i * 18));
			}
		}

		/** anvil result [9] **/
		addSlotToContainer(new Slot(craftResult, 0, RESULT_OFFSET_X, RESULT_OFFSET_Y));

		/** inventory [10-] */
		for (i = 0; i < 3; i++) {
			for (j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, INVENTORY_OFFSET_X + j * 18, INVENTORY_OFFSET_Y + i * 18));
			}
		}

		/** hotbar [] */
		for (i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(playerInv, i, HOTBAR_OFFSET_X + i * 18, HOTBAR_OFFSET_Y));
		}

		updateCraftResult();
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		//return tileEntity.getDistanceFrom(player.posX, player.posY, player.posZ) <= SuperiorAnvil.reachDistance;
		return true;
	}

	@Override
	public ItemStack slotClick(int slotnum, int mouseClick, int clickType, EntityPlayer player) {
		if ( slotnum == 9 ) return null;  // forbid clicks on craft result
//		System.out.printf("slotnum := %d  mouseClick := %d  clickType := %d\n", slotnum, mouseClick, clickType);
		ItemStack stack = super.slotClick(slotnum, mouseClick, clickType, player);

		// update craft result if crafting area was altered or stack was double clicked
		if ( clickType == 6 || slotnum < 9 ) updateCraftResult();

		return stack;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotnum) {
		return null;
	}

	/** update the craft result **/
	private void updateCraftResult() {
		ItemStack craftStack = SuperiorAnvilCraftingManager.getInstance().findMatchingRecipe(anvilInv);
		craftResult.setInventorySlotContents(0, craftStack);
		tileEntity.setCraftResult(craftStack);
	}

}
