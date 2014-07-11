package mods.minecraft.donington.rpgequip.common.entity.ai;

import java.util.Random;

import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class EliteAIGroundChargeTarget extends EntityAIBase {
	private EntityLiving entity;
	private EntityLivingBase target;


	public EliteAIGroundChargeTarget(EntityLiving entity) {
		this.entity = entity;
	}


	private void chargeTarget() {
		Random rng = entity.getRNG();
		double posX = target.posX + rng.nextDouble() - 0.5;
		double posZ = target.posZ + rng.nextDouble() - 0.5;
		this.entity.getNavigator().tryMoveToXYZ(posX, target.posY, posZ, 1.0);
		entity.setAttackTarget(target);
	}


	public boolean shouldExecute() {
		target = entity.getAttackTarget();
		if ( target != null ) {
    		if ( !entity.canEntityBeSeen(target) ) return false;
			if ( entity.getDistanceToEntity(target) > RPGECommonProxy.eliteAggroRange )
				return false;
			if ( target instanceof EntityPlayer && ((EntityPlayer)target).capabilities.isCreativeMode )
				return false;

			return true;
		}

		target = entity.getLastAttacker();
		if ( target == null || entity.getDistanceToEntity(target) > RPGECommonProxy.eliteAggroRangeMax )
			return false;

		return true;
	}


	public void startExecuting() {
		chargeTarget();
		//System.out.println("EliteAIChargeTarget :: in pursuit");
	}


	public boolean continueExecuting() {
		double distance = entity.getDistanceToEntity(target);

		if ( entity.getNavigator().noPath() && distance > 1.3 ) {
			chargeTarget();
			if ( entity.getNavigator().noPath() ) {
				//System.out.println("EliteAIChargeTarget :: no path available");
				return false;
			}
			//System.out.println("EliteAIChargeTarget :: maintaining pursuit");
			return true;
		}

		if ( target.isEntityAlive() && distance < RPGECommonProxy.eliteAggroRangeMax )
			return true;

		//System.out.println("EliteAIChargeTarget :: target lost");
		entity.setAttackTarget(null);
		entity.setLastAttacker(null);
		return false;
	}

}
