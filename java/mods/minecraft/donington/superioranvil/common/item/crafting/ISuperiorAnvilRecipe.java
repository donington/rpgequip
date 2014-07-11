package mods.minecraft.donington.superioranvil.common.item.crafting;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface ISuperiorAnvilRecipe {

	boolean matches(IInventory inventory);
	ItemStack getCraftingResult(IInventory inventory);
	int getRecipeSize();
}
