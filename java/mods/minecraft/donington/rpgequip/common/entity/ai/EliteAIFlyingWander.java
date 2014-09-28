package mods.minecraft.donington.rpgequip.common.entity.ai;

import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import mods.minecraft.donington.rpgequip.common.entity.EntityEliteFlying;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;


public class EliteAIFlyingWander extends EntityAIBase {
    private EntityLiving entity;
    private PathPoint homePathPoint, rngPathPoint;
    private double wanderSpeed, returnSpeed;
    private boolean returning;
    private int idleTickCount, idleTickMax;
	//private long timer;


    public EliteAIFlyingWander(EntityLiving entity, double wanderSpeed, double returnSpeed) {
    	this.entity = entity;
    	this.homePathPoint = new PathPoint(MathHelper.floor_double(entity.posX), MathHelper.floor_double(entity.posY), MathHelper.floor_double(entity.posZ));
    	this.wanderSpeed = wanderSpeed;
    	this.returnSpeed = returnSpeed;
    	this.returning = false;
    	entity.stepHeight = 1.9F;  // welcome to hell

    	this.setMutexBits(4);
    }


    private void resetIdle() {
    	idleTickCount = 0;
    	idleTickMax = entity.getRNG().nextInt(100) + 20;
    }


    /** can the entity fit here? **/
    // TODO: use hitboxes instead
    private boolean canFitAtPosition(int posX, int posY, int posZ) {
    	int height = MathHelper.ceiling_double_int(entity.height);
    	Block block;

    	for ( int i = 0; i < height; i++ ) {
    		block = entity.worldObj.getBlock(posX, posY+i, posZ);
    		if ( block.equals(Blocks.water) ) {
    			if ( !entity.isInWater() ) return false;
    			continue;
    		}
  		    if ( !entity.worldObj.isAirBlock(posX, posY+i, posZ) ) return false;
    	}
    	return true;
    }


    private boolean canSeeGroundFromHere(int posX, int posY, int posZ) {
    	for ( int i = 0; i < 10; i++ ) {
    	  if ( entity.worldObj.isAirBlock(posX, posY-i, posZ) ) continue;
    	  return true;
    	}
    	return false;
    }

    @Override
    public boolean shouldExecute() {
    	if ( entity.getAttackTarget() != null ) return false;
    	idleTickCount++;
    	if ( idleTickCount < idleTickMax ) return false;

    	// should I go home?
    	Vec3 position = Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ);

    	Vec3 vec3 = Vec3.createVectorHelper(entity.getRNG().nextDouble() * RPGECommonProxy.eliteWanderDistance, 0.0, 0.0);
        vec3.rotateAroundX(entity.getRNG().nextFloat());
    	vec3.rotateAroundY(entity.getRNG().nextFloat());
    		
    	int worldX = MathHelper.floor_double(entity.posX + vec3.xCoord);
    	int worldY = MathHelper.floor_double(entity.posY + vec3.yCoord);
    	int worldZ = MathHelper.floor_double(entity.posZ + vec3.zCoord);

    	if ( !canSeeGroundFromHere(worldX, worldY, worldZ) )
    		worldY -= 10;

    	if ( canFitAtPosition(worldX, worldY, worldZ) ) {
    		rngPathPoint = new PathPoint(worldX, worldY, worldZ);
    		//System.out.printf("fly wander to point (%d, %d, %d)\n", worldX, worldY, worldZ);
    		return true;
    	}

    	return false;
    }


    @Override
    public void startExecuting() {
    	PathEntity path;
    	this.resetIdle();
    }


    @Override
    public boolean continueExecuting() {
    	if ( entity.getAttackTarget() != null ) return false;

    	double speed = wanderSpeed;
    	if ( returning ) {
    		speed = returnSpeed;
    		returning = false;
    	}

    	Vec3 position = Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ);
        if ( position.squareDistanceTo(rngPathPoint.xCoord, rngPathPoint.yCoord, rngPathPoint.zCoord) < 16 )
        	return false;

        Vec3 point = Vec3.createVectorHelper(rngPathPoint.xCoord - entity.posX, rngPathPoint.yCoord - entity.posY, rngPathPoint.zCoord - entity.posZ).normalize();
    	entity.motionX += point.xCoord * speed * 0.03;
    	entity.motionY += point.yCoord * speed * 0.03;
    	entity.motionZ += point.zCoord * speed * 0.03;
        entity.rotationYaw = -((float)Math.atan2(entity.motionX, entity.motionZ)) * 180.0F / (float)Math.PI;
    	return true;
    }

}
