package mods.minecraft.donington.superioranvil.common.item.crafting;

import java.util.HashMap;

import mods.minecraft.donington.rpgequip.common.item.ItemEquipSocket;
import mods.minecraft.donington.rpgequip.common.item.ItemGear;
import mods.minecraft.donington.rpgequip.common.item.ItemRing;
import mods.minecraft.donington.rpgequip.common.item.gear.EquipSocket;
import mods.minecraft.donington.rpgequip.common.item.gear.ItemAmulet;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemGearSocketRecipe implements ISuperiorAnvilRecipe {
	private HashMap<IInventory,ItemStack> results;


	public ItemGearSocketRecipe() {
		results = new HashMap<IInventory,ItemStack>();
	}


	@Override
	public boolean matches(IInventory inv) {
		ItemStack gear = null;
		EquipSocket gem = null;

		ItemStack stack;
		Item item;
		int i;

		for ( i = 0; i < inv.getSizeInventory(); i++ ) {
			stack = inv.getStackInSlot(i);
			if ( stack == null ) continue;
			item = stack.getItem();

			/* filter out items that cannot be socketed here */
			//if ( item instanceof ItemTalisman ) return;
			if ( item instanceof ItemGear ) {
				if ( gear != null ) return false;
				gear = stack;
			}
			else if ( item instanceof ItemEquipSocket ) {
				if ( gem != null ) return false;
				gem = EquipSocket.getGemById(stack.getItemDamage());
			}
			else
				return false;
		}

		if ( gear == null || gem == null ) return false;

		stack = gear.copy();
		if ( gem.insertSocket(stack) ) {
		  results.put(inv, stack);
		  return true;
		}
		return false;
	}


	@Override
	public ItemStack getCraftingResult(IInventory inv) {
		return results.remove(inv);
	}


	@Override
	public int getRecipeSize() {
		return 2;
	}

}
