package mods.minecraft.donington.rpgequip.common.entity.elite;

import java.util.ArrayList;

public enum EliteCreatureType {

	/* EntityEliteMob */
	Creeper(1),
	Enderman(2),
	Pigman(3),

	/* EntityEliteFlying */
	Blaze(4),
	Ghast(5);

	public static final int mobCount = 3;
    public static final int mobOffset = 1;
    
    public static final int flyingCount = 2;
    public static final int flyingOffset = 4;

    private int cindex;


	EliteCreatureType(int creature) {
		cindex = creature;
	}


	public int getIndex() {
		return cindex;
	}


	boolean isCreature(int creature) {
		return cindex == creature;
	}

}
