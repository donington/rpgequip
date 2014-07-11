package mods.minecraft.donington.rpgequip.common.entity.elite;

import static java.util.Arrays.asList;

import java.util.List;

import net.minecraft.entity.EntityLiving;


public class EliteNameGenerator {


	public static String nameEliteLike(int creature, int aura) {
		String creatureName = null;
		String auraName = null;

		for ( EliteCreatureType type : EliteCreatureType.values() ) {
			if ( !type.isCreature(creature) ) continue;
			creatureName = type.name();
			break;
		}

		for ( EliteCreatureAura type : EliteCreatureAura.values() ) {
			if ( !type.isAura(aura) ) continue;
			auraName = type.name();
			break;
		}

		if ( creatureName == null ) return "";
		if ( auraName == null ) return "Elite " + creatureName;
		return auraName + " " + creatureName;
	}


	public static String nameEliteLike(EntityLiving entity, int aura) {
		String creatureName = null;
		String auraName = null;

		creatureName = entity.getClass().getSimpleName().substring(6);

		for ( EliteCreatureAura type : EliteCreatureAura.values() ) {
			if ( !type.isAura(aura) ) continue;
			auraName = type.name();
			break;
		}

		if ( creatureName == null ) return "";
		if ( auraName == null ) return "Elite " + creatureName;
		return auraName + " " + creatureName;
	}

}
