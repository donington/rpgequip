package mods.minecraft.donington.rpgequip.common.entity.magic;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityMagicExplosion extends EntityMagic {
	private static final float twopi = (float) (Math.PI * 2);
	private static final int lifespan = 5;

	private float magnitude;


	public EntityMagicExplosion(World world) {
		super(world);
		this.setSize(0.0F, 0.0F);
		this.magnitude = -1;
	}


	public EntityMagicExplosion(EntityLivingBase caster, float magnitude) {
		this(caster.worldObj);
		this.caster = caster;
		this.magnitude = magnitude;
		this.setPosition(caster.posX, caster.posY, caster.posZ);
//		System.out.println("EntityMagicExplosion has entered world");
        this.playSound("random.explode", 4.0F, (0.7F + rng.nextFloat() * 0.3F));
	}


	@Override
	public void entityInit() {
		super.entityInit();
		DataWatcher dw = this.getDataWatcher();
		dw.addObject(RPGECommonProxy.magicDataWatcherId, Float.valueOf(-1));
	}


	@Override
	public void onUpdate() {
		if ( !isDataWatcherReady(magnitude) ) return;

		if ( magnitude <= 0 )
			magnitude = getDataWatcher().getWatchableObjectFloat(RPGECommonProxy.magicDataWatcherId);


//		System.out.printf("ready (magnitude := %f\n", magnitude);
		super.onUpdate();

		if ( !worldObj.isRemote ) {
			if ( ticksExisted > lifespan || caster == null || caster.isDead ) {
//				System.out.println("dead");
				setDead();
				return;
			}
		}

		else {
//			System.out.printf("exploding (magnitude := %f\n", magnitude);
			Random rng = getRNG();
			Vec3 point = Vec3.createVectorHelper(magnitude*rng.nextDouble(), 0, 0);
			point.rotateAroundX(twopi*rng.nextFloat());
			point.rotateAroundY(twopi*rng.nextFloat());
			worldObj.spawnParticle("largeexplode", posX + point.xCoord, posY + point.yCoord, posZ + point.zCoord, rng.nextGaussian()-0.5, rng.nextGaussian(), rng.nextGaussian()-0.5);
		}

		if ( !worldObj.isRemote ) {
			List<EntityLivingBase> victims = this.getLivingEntitiesWithinAABB(this.boundingBox.expand(magnitude, magnitude, magnitude));
			//int hitCount = 0;
			for ( EntityLivingBase hit : victims ) {
				hit.attackEntityFrom(DamageSource.causeMobDamage(caster).setExplosion().setDamageBypassesArmor(), 4.0F);
			}

		}

		setPosition(posX, posY, posZ);
	}


	/** never render this as a normal entity **/
    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double par1) {
    	return false;
    }

}
