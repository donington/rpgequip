package mods.minecraft.donington.rpgequip.common.entity.ai;

import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class EliteAIAggro extends EntityAIBase {
	private EntityLiving entity;
    private EntityLivingBase target;
    private int timer;
    private int aggroRange;
    private int aggroRangeAngry;

    private static final int tickDelay = 5;


	public EliteAIAggro(EntityLiving entity, int aggroRange, int aggroRangeAngry) {
		this.entity = entity;
		this.aggroRange = aggroRange;
		this.aggroRangeAngry = aggroRangeAngry;
	}


	private boolean isTargetValid(EntityLivingBase target) {
        return target != null && ( target instanceof EntityPlayer && !((EntityPlayer)target).capabilities.isCreativeMode ) && entity.getDistanceToEntity(target) < aggroRangeAngry;
	}


	/** TODO: make multitarget aggro more interesting... **/
	private EntityLivingBase findPlayerToAttack() {
        EntityPlayer player = entity.worldObj.getClosestVulnerablePlayerToEntity(entity, aggroRange);
        if ( player != null && !player.capabilities.isCreativeMode && entity.canEntityBeSeen(player) )
        	return player;

        EntityLivingBase target = entity.getLastAttacker();
        if ( isTargetValid(target) && entity.canEntityBeSeen(target) )
        	return target;

       	return null;
    }


	public boolean shouldExecute() {
		//target = entity.getAttackTarget();
        //if ( target == null ) return true;
        //return false;
		return true;
	}


	public void startExecuting() {
		timer = 0;
		target = findPlayerToAttack();
		entity.setAttackTarget(target);
	}


	public boolean continueExecuting() {
		timer++;
		if ( timer < tickDelay ) return true;
		timer = 0;

		if ( !isTargetValid(target) ) {
			target = findPlayerToAttack();
			entity.setAttackTarget(target);
		}

		return true;
	}
}
