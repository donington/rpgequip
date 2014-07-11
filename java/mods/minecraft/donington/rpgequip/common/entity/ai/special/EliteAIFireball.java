package mods.minecraft.donington.rpgequip.common.entity.ai.special;

import mods.minecraft.donington.rpgequip.common.entity.magic.EntityMagicFire;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

public class EliteAIFireball extends EntityAIBase {
	private final EntityLiving entity;
	private EntityLivingBase target;
	private int cooldown;
	private int activeTime;
	private int fireballPulse;

	private static final int cooldownMax = 60;
	private static final int durationMax = 15;
	private static final int fireballRate = 1;
	private static final float minimumDistance = 2.0F;
	private static final float maximumDistance = 50.0F;


	public EliteAIFireball(EntityLiving entity) {
		this.entity = entity;
	}


	@Override
	public boolean shouldExecute() {
        if ( cooldown > cooldownMax ) {
        	if ( entity.isAirBorne ) return false;
    		target = entity.getAttackTarget();
    		if ( target == null ) return false;
    		if ( !entity.canEntityBeSeen(target) ) return false;
    		if ( target instanceof EntityPlayer && ((EntityPlayer)target).capabilities.isCreativeMode ) return false;
    		float distance = entity.getDistanceToEntity(target);
    		if ( distance < minimumDistance || distance > maximumDistance ) return false;

        	cooldown = 0;
        	activeTime = 0;
        	return true;
        }

    	cooldown++;
    	return false;
	}


	@Override
	public void startExecuting() {}


	@Override
    public boolean continueExecuting() {
		if ( activeTime > durationMax ) return false;

		//Vec3 src = entity.getPosition(1.0F);
    	//Vec3 dest = target.getPosition(1.0F);
		Vec3 src =  Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ);
    	Vec3 dest = Vec3.createVectorHelper(target.posX, target.posY, target.posZ);
    	Vec3 ptr = src.subtract(dest).normalize();

    	// breath attack that vector
    	ptr.rotateAroundY(entity.getRNG().nextFloat()*0.7F-0.35F);
    	ptr.rotateAroundZ(entity.getRNG().nextFloat()*0.7F-0.35F);

		fireballPulse++;
		if ( fireballPulse > fireballRate ) {
			entity.worldObj.spawnEntityInWorld(new EntityMagicFire(entity.worldObj, (EntityLivingBase)entity, ptr));
			fireballPulse = 0;
		}

		activeTime++;
		return true;
	}

}
