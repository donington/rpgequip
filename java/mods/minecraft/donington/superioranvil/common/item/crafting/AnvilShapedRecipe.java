package mods.minecraft.donington.superioranvil.common.item.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mods.minecraft.donington.superioranvil.common.block.SuperiorAnvil;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AnvilShapedRecipe implements ISuperiorAnvilRecipe {
	private final ItemStack result;
	private final int width;
	private final int height;
	private final List items;


	public AnvilShapedRecipe(ItemStack stack, int width, int height, Object... data) {
		this.result = stack;
		this.width = width;
		this.height = height;
		this.items = new ArrayList();
		for ( int i = 0; i < data.length; i++ )
			this.items.add(data[i]);
	}

	private int getInvPos(int i, int j) {
//		if ( i < 0 || i > SuperiorAnvil.gridSize ) return -1;
//		if ( j < 0 || j > SuperiorAnvil.gridSize ) return -1;
		int pos = i + j*SuperiorAnvil.gridSize;
		return pos;
	}

	private int getRecipePos(int i, int j) {
//		if ( i < 0 || i > width )  return -1;
//		if ( j < 0 || j > height ) return -1;
		int pos = i + j*width;
		return pos;
	}

	private boolean checkMatch(IInventory inv, int offsetW, int offsetH) {
		int i, j;
		int rangeW = offsetW + width -1;
		int rangeH = offsetH + height -1;
//		if ( rangeW >= SuperiorAnvil.gridSize || rangeH >= SuperiorAnvil.gridSize ) return false;
		ItemStack invStack;
		Object match = null;

		for ( j = 0; j < SuperiorAnvil.gridSize; j++ ) {
			for ( i = 0; i < SuperiorAnvil.gridSize; i++ ) {
				invStack = inv.getStackInSlot(getInvPos(i,j));
				if ( i < offsetW || i > rangeW || j < offsetH || j > rangeH ) {
					if ( invStack != null ) {
						return false;
					}
				}
				else {
	                int recipeI = i - offsetW;
	                int recipeJ = j - offsetH;
	                match = items.get(getRecipePos(recipeI, recipeJ));
	                // TODO: fix whatever is wrong here
					if ( invStack == null ) {
						if ( match == null ) continue;
						return false;
					}
					if ( match instanceof ItemStack ) {
						if ( invStack.isItemEqual((ItemStack)match) ) continue;
						return false;
					}
					if ( !invStack.getItem().equals(match) )
						return false;
				}
			}
		}
		return true;
	}


	@Override
	public boolean matches(IInventory inv) {
		Item[] invItems = new Item[width*height];
		int i, j, invPos;
		ItemStack stack;
		boolean isMatch;

		//if ( result.getUnlocalizedName().equals("item.equipsocket_obsidian") )
		//	System.out.printf("%s:matches()\n", result.getUnlocalizedName());

		Object match = items.get(0);
		isMatch = false;
		for ( j = 0; j < SuperiorAnvil.gridSize +1 - height; j++ ) {
			for ( i = 0; i < SuperiorAnvil.gridSize +1 - width; i++ ) {
				int pos = getInvPos(i, j);
				stack = inv.getStackInSlot(pos);
				//System.out.printf("inv[%d] := %s\n", pos, stack);
				isMatch = checkMatch(inv, i, j);
				if ( isMatch ) return true;
			}
		}
		return false;
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

