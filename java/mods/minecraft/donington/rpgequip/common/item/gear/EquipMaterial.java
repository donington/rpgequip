package mods.minecraft.donington.rpgequip.common.item.gear;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;


/** EquipMaterial: define stats based on different crafting materials */
public enum EquipMaterial {
  NONE,
  WOOD    ( 4,  18, Blocks.planks),
  LEATHER ( 5,  15, Items.leather),
  IRON    (15,   9, Items.iron_ingot),
  GOLD    ( 7,  22, Items.gold_ingot),
  DIAMOND (33,  10, Items.diamond);

  // wood and dye for talismans;  wood be used for shields (if I decide to implement them)

  private int durability;
  private int enchantability;
  private Item item;
  //private int[] armorValue;


  private EquipMaterial() {
    this.durability = 0;
    this.enchantability = 0;
    this.item = null;
  }

  private EquipMaterial(int durability, int enchantability, Item item) {
	this.durability      = durability;
	this.enchantability  = enchantability;
	this.item = item;
  }


  private EquipMaterial(int durability, int enchantability, Block block) {
    this(durability, enchantability, Item.getItemFromBlock(block));
  }


  public int getMaxDurability(EquipType type) {
    int durabilityBase = type.getDurabilityBase();
    if ( durabilityBase < 0 ) return -1;
    return durabilityBase * this.durability;
  }


  public int getEnchantability() {
    return this.enchantability;
	}


  public int getArmorValue(EquipType equipType) {
    //return armorValue[equipType.getEquipId()];
    return 0;
  }


public Item getItem() {
	return this.item;
}

}
