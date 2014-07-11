package mods.minecraft.donington.superioranvil.common.item.crafting;

import java.util.HashMap;

import mods.minecraft.donington.rpgequip.common.item.ItemGear;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;

public class RepairEquipmentRecipe implements ISuperiorAnvilRecipe {
	private HashMap<IInventory,ItemStack> results;

	public RepairEquipmentRecipe() {
		results = new HashMap<IInventory,ItemStack>();
	}

	@Override
	public boolean matches(IInventory inv) {
		ItemStack equip = null;
		ItemStack material = null;

		ItemStack stack;
		Item item;
		int i;

		for ( i = 0; i < inv.getSizeInventory(); i++ ) {
			stack = inv.getStackInSlot(i);
			if ( stack == null ) continue;
			item = stack.getItem();

			if ( item instanceof ItemGear || item instanceof ItemArmor || item instanceof ItemSword || item instanceof ItemTool ) {
				if ( equip != null ) return false;
				if ( !stack.isItemStackDamageable() ) return false;
				equip = stack;
				continue;
			}

			if ( material != null ) return false;
			material = stack;
		}

		if ( equip == null || material == null ) return false;

		if ( equip.getItem().getIsRepairable(equip, material) ) {
			int cost = equip.getRepairCost() + 1;
			if ( cost > 16 ) return false;
			double repairFactor = 0.39 - (double)(cost*2) / 100;
			int damage = equip.getItemDamage() - (1+(int)(equip.getMaxDamage()*repairFactor));
			if ( damage < 0 ) damage = 0;
			stack = equip.copy();
			stack.setItemDamage(damage);
			stack.setRepairCost(cost);
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
