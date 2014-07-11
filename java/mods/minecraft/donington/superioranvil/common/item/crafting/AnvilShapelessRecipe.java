package mods.minecraft.donington.superioranvil.common.item.crafting;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AnvilShapelessRecipe implements ISuperiorAnvilRecipe {
	ItemStack result;
	List items;


	public AnvilShapelessRecipe(ItemStack stack, Object[] data) {
		this.result = stack;
		this.items = new ArrayList();
		for ( int i = 0; i < data.length; i++ )
			this.items.add(data[i]);
	}

	@Override
	public boolean matches(IInventory inv) {
		ArrayList unmatched = new ArrayList(items);
		int i;
		ItemStack stack;
		Item item;
		Object match = null;

		for ( i = 0; i < inv.getSizeInventory(); i++ ) {
			stack = inv.getStackInSlot(i);
			if ( stack == null ) continue;
			if ( unmatched.isEmpty() ) return false;

			for ( Iterator it = unmatched.iterator(); it.hasNext(); ) {
				match = it.next();
				if ( ( match instanceof ItemStack && stack.isItemEqual((ItemStack) match) ) ||
					 ( stack.getItem().equals(match) ) ) {
					it.remove();
					match = null;
					break;
				}
			}
			if ( match != null ) return false;
		}
		return unmatched.isEmpty();
	}

	@Override
	public ItemStack getCraftingResult(IInventory inventory) {
		return result.copy();
	}

	@Override
	public int getRecipeSize() {
		return items.size();
	}

}
