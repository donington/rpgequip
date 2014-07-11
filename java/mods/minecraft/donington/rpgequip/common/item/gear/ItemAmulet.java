package mods.minecraft.donington.rpgequip.common.item.gear;

import mods.minecraft.donington.rpgequip.common.item.ItemGear;

public class ItemAmulet extends ItemGear {
  private final EquipSocket gem;

  public ItemAmulet(EquipSocket gem, String unlocalizedName) {
	super(EquipType.Amulet, EquipMaterial.NONE, unlocalizedName);
    this.gem = gem;
  }

  public EquipSocket getGem() {
    return this.gem;
  }

}
