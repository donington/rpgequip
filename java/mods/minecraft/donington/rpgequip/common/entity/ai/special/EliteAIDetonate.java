package mods.minecraft.donington.rpgequip.common.entity.ai.special;

import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import mods.minecraft.donington.rpgequip.common.entity.magic.EntityMagicExplosion;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class EliteAIDetonate extends EntityAIBase {
	private final EntityLiving entity;

	private static final float range = 2.0F;
	private static final float magnitude = 4.0F;

	private int cooldown;
	private static final int cooldownMax = 10;
	private int delay;
	private static final int delayTime = 28;


	public EliteAIDetonate(EntityLiving entity) {
		this.entity = entity;
	}


	@Override
	public boolean shouldExecute() {
		if ( cooldown > cooldownMax ) {
			EntityLivingBase target = entity.getAttackTarget();
			if ( target == null ) return false;
    		if ( !entity.canEntityBeSeen(target) ) return false;
    		if ( target instanceof EntityPlayer && ((EntityPlayer)target).capabilities.isCreativeMode ) return false;
			if ( entity.getDistanceToEntity(target) > range ) return false;
			cooldown = 0;
			return true;
		}

		cooldown++;
		return false;
	}


	@Override
	public void startExecuting() {
		entity.playSound("creeper.primed", 1.0F, 0.5F);
		delay = 0;
	}


	@Override
	public boolean continueExecuting() {
		if ( delay > delayTime ) {
			entity.worldObj.spawnEntityInWorld(new EntityMagicExplosion(entity, magnitude));
			entity.getDataWatcher().updateObject(RPGECommonProxy.eliteFuseDataWatcherId, 0.0F);
			return false;
		}
		delay++;
		entity.getDataWatcher().updateObject(RPGECommonProxy.eliteFuseDataWatcherId, (float) delay / delayTime);
		return true;		
	}
}
