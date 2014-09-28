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
	private int moveTimer;

	private static final int moveTimerMax = 60;


	public EliteAIFlyingHarassTarget(EntityLiving entity) {
		this.entity = entity;
		this.target = null;
		this.dest = null;
	}


	private void wander() {
		Random rng = entity.getRNG();

		int worldX = (int)Math.floor(target.posX + (rng.nextDouble() - 0.5) * 16);
		int worldY = (int)Math.floor(target.posY + 3 + rng.nextDouble() * 8);
		int worldZ = (int)Math.floor(target.posZ + (rng.nextDouble() - 0.5) * 16);
		dest = new PathPoint(worldX, worldY, worldZ);
		moveTimer = 0;
	}


	private void harassTarget() {
		Random rng = entity.getRNG();

		int worldX = (int)Math.floor(target.posX + (rng.nextDouble() - 0.5) * 12);
		int worldY = (int)Math.floor(target.posY + 3 + rng.nextDouble() * 8);
		int worldZ = (int)Math.floor(target.posZ + (rng.nextDouble() - 0.5) * 12);
		dest = new PathPoint(worldX, worldY, worldZ);
		moveTimer = 0;
	}


	public boolean shouldExecute() {
		target = entity.getAttackTarget();
		return target != null;
	}


	public void startExecuting() {
		harassTarget();
		//System.out.println("EliteAIFlyingHarassTarget :: harassing target");
	}


	public boolean continueExecuting() {
		target = entity.getAttackTarget();
		if ( target == null ) {
			//System.out.println("EliteAIFlyingHarassTarget :: target lost");
			return false;
		}

		moveTimer++;
		float distance = entity.getDistanceToEntity(target);

		if ( moveTimer > moveTimerMax ) {
			if ( entity.canEntityBeSeen(target) && distance > 1.3F ) {
				harassTarget();
				//System.out.println("EliteAIFlyingHarassTarget :: harassing target");
				return true;
			}
			else {
				wander();
				//System.out.println("EliteAIFlyingHarassTarget :: wandering a little");
				return true;
			}
		}


		if ( dest != null ) {
			Vec3 point = Vec3.createVectorHelper(dest.xCoord - entity.posX, dest.yCoord - entity.posY, dest.zCoord - entity.posZ).normalize();
			entity.motionX += point.xCoord * 0.02;
			entity.motionY += point.yCoord * 0.02;
			entity.motionZ += point.zCoord * 0.02;
			entity.faceEntity(target, 5.0F, 5.0F);
		}

		if ( target.isEntityAlive() )
			return true;

		System.out.println("EliteAIFlyingHarassTarget :: target is dead");
		return false;
	}

}
