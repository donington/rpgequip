package mods.minecraft.donington.rpgequip.common.entity.ai.special;

import mods.minecraft.donington.rpgequip.common.entity.magic.EntityMagicFire;
import mods.minecraft.donington.rpgequip.common.entity.magic.EntityMagicShield;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

public class EliteAIShield extends EntityAIBase {
	private final EntityLiving entity;
	private EntityLivingBase target;
	private int cooldown;
	private int activeTime;

	private static final int cooldownMax = 100;
	private static final int durationMax = 200;
	private static final float absorptionMax = 20F;


	public EliteAIShield(EntityLiving entity) {
		this.entity = entity;
	}


	@Override
	public boolean shouldExecute() {
        if ( cooldown > cooldownMax ) {
    		target = entity.getAttackTarget();
    		if ( target == null ) return false;

    		cooldown = 0;
        	activeTime = 0;
        	return true;
        }

    	cooldown++;
    	return false;
	}


	@Override
	public void startExecuting() {
		entity.worldObj.spawnEntityInWorld(new EntityMagicShield(entity.worldObj, entity, durationMax, absorptionMax));
	}


	@Override
    public boolean continueExecuting() {
		if ( activeTime > durationMax ) return false;
		activeTime++;
		return true;
	}

}
