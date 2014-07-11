package mods.minecraft.donington.rpgequip.common.entity.ai.special;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.minecraft.donington.rpgequip.common.entity.elite.EliteHelper;
import mods.minecraft.donington.rpgequip.common.entity.magic.EntityMagicShield;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class EliteAIEnder extends EntityAIBase {
	private final EntityLiving entity;
	private EntityLivingBase target;
	private int cooldown;
	private int activeTime;

	private static final int teleportRangeXZ = 12;
	private static final int teleportRangeY = 5;
	private static final int cooldownAggroMax = 80;
	private static final int cooldownIdleMax = 200;
	private static final int durationMax = 20;
	private static final double hostileTeleportChance = 0.6;  // ~40% chance for hostile teleport


	public EliteAIEnder(EntityLiving entity) {
		this.entity = entity;
	}


	@Override
	public boolean shouldExecute() {

		// allow idle teleports
		if ( cooldown > cooldownIdleMax ) {
    		cooldown = 0;
        	activeTime = 0;
        	return true;
		}

		if ( cooldown > cooldownAggroMax ) {
    		target = entity.getAttackTarget();
    		if ( target == null ) return false;
    		if ( target instanceof EntityPlayer && ((EntityPlayer)target).capabilities.isCreativeMode ) return false;

    		cooldown = 0;
        	activeTime = 0;
        	return true;
        }

    	cooldown++;
    	return false;
	}


	@Override
	public void startExecuting() {
        entity.worldObj.playSoundEffect(entity.posX, entity.posY, entity.posZ, "mob.endermen.portal", 1.0F, 1.0F);

		EntityLivingBase target = entity.getAttackTarget();
        if ( target != null && entity.getRNG().nextDouble() > hostileTeleportChance ) {
			if ( entity.canEntityBeSeen(target) && entity.getDistanceToEntity(target) < teleportRangeXZ ) {
				if ( EliteHelper.teleportToEntity(entity, target, teleportRangeY) )
					entity.playSound("mob.endermen.portal", 1.0F, 1.0F);
				return;
			}
		}

		if ( EliteHelper.teleportRandomly(entity, teleportRangeXZ, teleportRangeY) )
			entity.playSound("mob.endermen.portal", 1.0F, 1.0F);

		// reboot path it's different now
		entity.getNavigator().clearPathEntity();
	}


	@Override
    public boolean continueExecuting() {
		if ( activeTime > durationMax ) return false;
		activeTime++;
		return true;
	}


	/** generate ender particles **/
	@Override
	@SideOnly(Side.CLIENT)
	public void updateTask() {
		Minecraft mc = Minecraft.getMinecraft();
		if ( !mc.thePlayer.canEntityBeSeen(entity) ) return;

        Random rng = entity.getRNG();
        double offsetX = rng.nextGaussian() * entity.width * 2.0;
        double offsetY = rng.nextGaussian() * entity.height;
        double offsetZ = rng.nextGaussian() * entity.width * 2.0;
        double rngX = (0.5 - rng.nextGaussian()) * 0.2;
        double rngY = (0.5 - rng.nextGaussian()) * 0.2;
        double rngZ = (0.5 - rng.nextGaussian()) * 0.2;

		mc.theWorld.spawnParticle("portal", entity.posX + offsetX, entity.posY + offsetY, entity.posZ + offsetZ, rngX, rngY, rngZ);
	}

}
