package mods.minecraft.donington.rpgequip.common.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

public class EliteAILeaper extends EntityAIBase {
	private EntityLiving entity;
	private int cooldown;

	private static final int cooldownMax = 20;
	private static final float idleChance = 0.08F;
	private float aggroChance;


	public EliteAILeaper(EntityLiving entity) {
		this.entity = entity;
		this.aggroChance = 0.15F + (entity.getRNG().nextFloat()*0.2F);
        this.setMutexBits(2);
	}


	@Override
	public boolean shouldExecute() {
		return true;
	}


    @Override
    public boolean continueExecuting() {
    	if ( cooldown > cooldownMax ) {

    		float check;

    		if ( entity.getAttackTarget() != null )
    			check = aggroChance;
    		else
    			check = idleChance;

   			if ( entity.getRNG().nextFloat() < check )
    			entity.getJumpHelper().setJumping();

    		cooldown = 0;
    		return true;
    	}

    	cooldown++;
		return true;
	}

}
