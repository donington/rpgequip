package mods.minecraft.donington.rpgequip.common.entity.ai.special;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

public class EliteAIDash extends EntityAIBase {
	private final EntityLiving entity;
	private EntityLivingBase target;
	private int cooldown;
	private int dashtime;

	private static final int cooldownMax = 20;
	private static final int dashtimeMax = 20;
	private static final double minimumDistance = 3.0;
	private static final double maximumDistance = 10.0;


	public EliteAIDash(EntityLiving entity) {
		this.entity = entity;
//        this.setMutexBits(1);
	}


	@Override
	public boolean shouldExecute() {
        if ( cooldown > cooldownMax ) {
        	if ( entity.isAirBorne ) return false;
    		target = entity.getAttackTarget();
    		if ( target == null ) return false;
    		float distance = entity.getDistanceToEntity(target);
    		if ( distance < minimumDistance || distance > maximumDistance ) return false;

        	cooldown = 0;
        	dashtime = 0;
        	return true;
        }

    	cooldown++;
    	return false;
	}


	@Override
	public void startExecuting() {}


	/** generate smoke particles (client side only) **/
	@Override
	@SideOnly(Side.CLIENT)
	public void updateTask() {
		Minecraft mc = Minecraft.getMinecraft();
		if ( !mc.thePlayer.canEntityBeSeen(entity) ) return;

		Random rng = entity.getRNG();
        double rngX = rng.nextGaussian() * 0.02D;
        double rngZ = rng.nextGaussian() * 0.02D;

		mc.theWorld.spawnParticle("largesmoke", entity.posX, entity.posY + 0.5D, entity.posZ, rngX, 0.1, rngZ);
	}


	@Override
    public boolean continueExecuting() {
		if ( dashtime > dashtimeMax ) {
			//System.out.println("dash expired");
			return false;
		}

		//if ( entity.isAirBorne ) return false;
		//if ( target.isDead ) return false;
		//if ( entity.getDistanceToEntity(target) < 1.0 ) return false;

		//Vec3 src = entity.getPosition(1.0F);
    	//Vec3 dest = target.getPosition(1.0F);
		Vec3 src =  Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ);
    	Vec3 dest = Vec3.createVectorHelper(target.posX, target.posY, target.posZ);
    	Vec3 ptr = dest.subtract(src);

    	if ( Math.abs(ptr.xCoord) < 1.0 && Math.abs(ptr.zCoord) < 1.0 ) {
    		// reboot path it's different now
    		entity.getNavigator().clearPathEntity();
    		//System.out.println("dash complete");
    		return false;
    	}

		// dash
    	ptr = ptr.normalize();
		entity.motionX -= ptr.xCoord * 0.3;
		entity.motionZ -= ptr.zCoord * 0.3;
		dashtime++;
		return true;
	}

}
