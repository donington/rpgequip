package mods.minecraft.donington.rpgequip.common;

import net.minecraft.item.Item;

public class LootChance {
	public final Item item;
	public final int min;
	public final int max;


	public LootChance(Item item, int min, int max) {
		this.item = item;
		this.min = min;
		this.max = max;
	}

}
