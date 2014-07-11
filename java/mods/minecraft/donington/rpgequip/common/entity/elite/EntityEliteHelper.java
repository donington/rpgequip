package mods.minecraft.donington.rpgequip.common.entity.elite;

import net.minecraft.entity.EntityLiving;
import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import mods.minecraft.donington.rpgequip.common.entity.living.EliteAura;

public abstract class EntityEliteHelper {

	/** get the aura of the elite **/
	public static int getEliteAura(EntityLiving e) {
		return e.getDataWatcher().getWatchableObjectInt(RPGECommonProxy.eliteAuraDataWatcherId);
	}


	/** get the creature type of the elite **/
	public static int getEliteCreatureType(EntityLiving e) {
		return e.getDataWatcher().getWatchableObjectInt(RPGECommonProxy.eliteCreatureTypeDataWatcherId);
	}


	/** set the aura of the elite **/
	public static void setEliteAura(EntityLiving e, int aura) {
		e.getDataWatcher().updateObject(RPGECommonProxy.eliteAuraDataWatcherId, aura);
	}


	/** set the creature type of the elite **/
	public static void setEliteCreatureType(EntityLiving e, int creature) {
		e.getDataWatcher().updateObject(RPGECommonProxy.eliteCreatureTypeDataWatcherId, creature);
	}


	/*
	public static boolean checkEliteIsReady(EntityLiving e) {
		if ( RPGECommonProxy.isServer() && EntityEliteHelper.getEliteCreatureType(e) <= 0 ) {
			EliteHelper.createElite(e);
			//index.setRandomCreatureType(this);

			// choose a random elite creature from the index cache (todo: EliteCreatureIndex.setRandomCreatureType(Entity e)
			//e.getDataWatcher().updateObject(RPGECommonProxy.eliteCreatureTypeDataWatcherId, e.getRNG().nextInt(EliteCreatureIndex.getMaxCreatureTypes())+1);
		}

		int creatureType = EntityEliteHelper.getEliteCreatureType(e);
		if ( creatureType <= 0 ) return false;
		return true;
	}
      */
}
