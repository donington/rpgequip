package mods.minecraft.donington.rpgequip.common.entity.ai;

import java.util.Random;

import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.Vec3;

public class EliteAIFlyingHarassTarget extends EntityAIBase {
	private EntityLiving entity;
	private EntityLivingBase target;
	private PathPoint dest;
	private int chargeTime;


	public EliteAIFlyingHarassTarget(EntityLiving entity) {
		this.entity = entity;
		this.target = null;
		this.dest = null;
	}


	private void chargeTarget() {
		Random rng = entity.getRNG();
		int worldX = (int)Math.floor(target.posX + (rng.nextDouble() - 0.5) * 8.0);
		int worldY = (int)Math.floor(target.posY + 3.0 + rng.nextDouble() * 8.0);
		int worldZ = (int)Math.floor(target.posZ + (rng.nextDouble() - 0.5) * 8.0);
		dest = new PathPoint(worldX, worldY, worldZ);
		entity.setAttackTarget(target);
		chargeTime = 0;
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
		System.out.println("EliteAIChargeTarget :: in pursuit");
	}


	public boolean continueExecuting() {
		double distance = entity.getDistanceToEntity(target);

		if ( distance < 1.3 ) {
			chargeTarget();
			System.out.println("EliteAIChargeTarget :: maintaining pursuit");
			return true;
		}

		if ( dest != null ) {
			Vec3 point = Vec3.createVectorHelper(dest.xCoord - entity.posX, dest.yCoord - entity.posY, dest.zCoord - entity.posZ).normalize();
			entity.motionX += point.xCoord * 0.03;
			entity.motionY += point.yCoord * 0.03;
			entity.motionZ += point.zCoord * 0.03;
			entity.faceEntity(target, 5.0F, 5.0F);
		}

		if ( target.isEntityAlive() && distance < RPGECommonProxy.eliteAggroRangeMax )
			return true;

		System.out.println("EliteAIChargeTarget :: target lost");
		entity.setAttackTarget(null);
		entity.setLastAttacker(null);
		return false;
	}

}
