package mods.minecraft.donington.rpgequip.common.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class EliteAISprinter extends EntityAIBase {
	private EntityLiving entity;


	public EliteAISprinter(EntityLiving entity) {
		this.entity = entity;
        this.setMutexBits(2);
	}


	@Override
	public boolean shouldExecute() {
        return !entity.getNavigator().noPath();
	}


    @Override
    public boolean continueExecuting() {
    	if ( entity.getNavigator().noPath() ) return false;

    	PathEntity pe = entity.getNavigator().getPath();
    	//Vec3 src = entity.getPosition(1.0F);
		Vec3 src =  Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ);
    	Vec3 dest = pe.getVectorFromIndex(entity, pe.getCurrentPathIndex());
    	Vec3 ptr = dest.subtract(src);

    	// close enough logic
    	if ( Math.abs(ptr.xCoord) < 1.0 && Math.abs(ptr.zCoord) < 1.0 )
    		pe.incrementPathIndex();

    	// "sprint" if chosen path point is farther away
    	if ( dest.distanceTo(src) > 5.0 ) {
    		ptr = ptr.normalize();
    		entity.motionX -= ptr.xCoord * 0.02;
    		entity.motionZ -= ptr.zCoord * 0.02;
    	}

		return true;
	}

}
