package mods.minecraft.donington.rpgequip.common;

import java.util.HashSet;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.EntityLivingBase;

/** this design is not very good for countdowns.  maybe timestamps? */
public class RPGESpecial {
	public static final int invisible = 1;

	// update cache per tick
	private static final HashSet<RPGESpecial> specialTracker = new HashSet();

	private int special;
	private EntityLivingBase creature;


	private RPGESpecial(int mask, EntityLivingBase entity) {
		special = mask;
		creature = entity;
		System.out.printf("init(): entity %s: special := %x\n", creature.getClass().getSimpleName(), mask);
	}


	public static RPGESpecial get(EntityLivingBase entity) {
		int mask = entity.getDataWatcher().getWatchableObjectInt(RPGECommonProxy.specialDataWatcherId);
		RPGESpecial special = new RPGESpecial(mask, entity);
		System.out.printf("get(): entity %s: special := %x\n", entity.getClass().getSimpleName(), mask);
		return special;
	}


	public static int getInt(EntityLivingBase entity) {
		return entity.getDataWatcher().getWatchableObjectInt(RPGECommonProxy.specialDataWatcherId);
	}


	public static void tick() {
		for ( RPGESpecial special : specialTracker ) {
			if ( special.creature.isDead ) {
				special.clear();
				continue;
			}
		}
	}


	public void clear() {
		special = 0;
		update();
		specialTracker.remove(special);
	}


	public void update() {
		if ( creature.isDead ) return;
		creature.getDataWatcher().updateObject(RPGECommonProxy.specialDataWatcherId, special);
		System.out.printf("update(): entity %s: special := %x\n", creature.getClass().getSimpleName(), special);
	}


	/* handle specials */

	public boolean hasSpecial() {
		return special != 0;
	}

	public boolean getSpecial(int bit) {
		return ( special & (1 << bit) ) != 0;
	}

	public void setSpecial(int bit, boolean value) {
		if ( value == true )
			special |= 1 << bit;
		else
			special &= ~(1 << bit);

		System.out.printf("setSpecial(): entity %s: special := %x\n", creature.getClass().getSimpleName(), special);
	}

}
