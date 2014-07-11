package mods.minecraft.donington.rpgequip.common.item.gear;


/** EquipType: clearly defines the different types of equipment */
public enum EquipType {
	Helmet    (0,  11,  "empty_slot_helmet"),
	Chest     (1,  16,  "empty_slot_chest"),
	Pants     (2,  15,  "empty_slot_pants"),
	Boots     (3,  13,  "empty_slot_boots"),
	Belt      (4,  12,  "empty_slot_belt"),
	Ring      (5,  -1,  "empty_slot_ring"),
	RESERVED  (6,  -1,  "empty_slot_generic"),
	Amulet    (7,  -1,  "empty_slot_amulet"),
	Bracer    (8,  14,  "empty_slot_bracer"),
	Talisman  (9,  -1,  "empty_slot_generic");

	public static final int size = EquipType.values().length;

  private final int equipID;
  private final int durabilityBase;
  private final String emptyIconName;

  private EquipType(int equipID, int durabilityBase, String emptySlotIcon) {
    this.equipID = equipID;
    this.durabilityBase = durabilityBase;
	  this.emptyIconName = emptySlotIcon;
	}

  public int getEquipId() {
    return this.equipID;
  }

  public int getDurabilityBase() {
    return this.durabilityBase;
  }

  public String getEmptyIconName() {
    return this.emptyIconName;
  }

}
