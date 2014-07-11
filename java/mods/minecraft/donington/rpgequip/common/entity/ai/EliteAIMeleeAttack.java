package mods.minecraft.donington.rpgequip.common.entity.ai;

import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.DamageSource;

public class EliteAIMeleeAttack extends EntityAIBase {
    private EntityLiving entity;
    private EntityLivingBase target;
    private int swingTimerCount;

    private final int swingSpeed = 5;
    private final float range = 1.5F;


    public EliteAIMeleeAttack(EntityLiving entity) {
	    this.entity = entity;
    	this.swingTimerCount = 0;
	}


	@Override
	public boolean shouldExecute() {
		if ( this.swingTimerCount > 0 ) {
	    	this.swingTimerCount--;
			return false;
		}

		target = this.entity.getAttackTarget();
        if ( target == null || !target.isEntityAlive() ) return false;
		if ( entity.getDistanceToEntity(target) > range ) return false;

        return true;
	}


	@Override
	public void startExecuting() {
		entity.swingItem();
		entity.attackEntityAsMob(target);
		//target.attackEntityFrom(null, range);
	}

}
