package mods.minecraft.donington.rpgequip.common.entity.magic;

import java.util.List;
import java.util.Random;

import mods.minecraft.donington.rpgequip.RPGEquipMod;
import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import net.minecraft.block.Block;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityMagicFire extends EntityMagic {
	private double mX, mZ;
	private float reach = 1.3F;
	private float explosionForce;
	private int fireballIndex;
	private boolean isDestructive;


	public EntityMagicFire(World world) {
		super(world);
		this.setSize(1.0F, 1.0F);
		fireballIndex = -1;
	}


	//public EntityMagicFire(World world, EntityLivingBase caster) {
	//	this(world, caster, caster.getLookVec(), 1.0F);
	//}


	public EntityMagicFire(World world, EntityLivingBase caster, float force, int fbIndex) {
		this(world, caster, caster.getLookVec(), force);
		this.fireballIndex = fbIndex;
		
	}


	public EntityMagicFire(World world, EntityLivingBase caster, Vec3 direction, float force) {
		this(world);
		this.caster = caster;

		double mX = direction.xCoord * 0.5;
		double mZ = direction.zCoord * 0.5;

		this.motionX = mX;
		this.motionY = direction.yCoord * 0.5;
		this.motionZ = mZ;

		this.explosionForce = force;

		if ( force > 2.0F ) {
			this.isDestructive = true;
		}
		else {
			this.isDestructive = false;
		}

		this.fireballIndex = 0;

		this.setSize(1.0F, 1.0F);
		this.setPosition(caster.posX + mX, caster.posY + caster.getEyeHeight(), caster.posZ + mZ);
		this.playSound(RPGEquipMod.MOD_ID + ":magic.fireball", 1.0F, 1.0F - this.getRNG().nextFloat() * 0.3F);
	}


	@Override
	public void entityInit() {
		super.entityInit();
		DataWatcher dw = this.getDataWatcher();
		dw.addObject(RPGECommonProxy.magicDataWatcherId, Integer.valueOf(-1));
	}


	@Override
	public void onUpdate() {
		if ( !isDataWatcherReady(fireballIndex) ) return;

		if ( fireballIndex < 0 )
			fireballIndex = getDataWatcher().getWatchableObjectInt(RPGECommonProxy.magicDataWatcherId);

		//if ( 0 == 0 ) return;
		super.onUpdate();

		if ( !worldObj.isRemote ) {
			if ( ticksExisted > 200 || caster == null || caster.isDead ) {
				setDead();
				return;
			}
		}

		// emit smoke
		else {  // not remote
			double smokeRange = this.width * 0.33;
			worldObj.spawnParticle("smoke", posX + rng.nextGaussian()*smokeRange, posY, posZ + rng.nextGaussian()*smokeRange, rng.nextGaussian()*0.002, 0.1, rng.nextGaussian()*0.005);
			worldObj.spawnParticle("smoke", posX + rng.nextGaussian()*smokeRange, posY, posZ + rng.nextGaussian()*smokeRange, rng.nextGaussian()*0.002, 0.1, rng.nextGaussian()*0.005);
			worldObj.spawnParticle("smoke", posX + rng.nextGaussian()*smokeRange, posY, posZ + rng.nextGaussian()*smokeRange, rng.nextGaussian()*0.002, 0.1, rng.nextGaussian()*0.005);
		}

		// update position
		if ( caster != null ) {
			posX += motionX;
			posY += motionY;
			posZ += motionZ;
		}


		if ( !worldObj.isRemote ) {

			// burn victims
			List<EntityLivingBase> victims = this.getLivingEntitiesHit();
			int hitCount = 0;
			for ( EntityLivingBase hit : victims ) {
				if ( this.getDistanceToEntity(hit) > reach ) continue;  // decrease chance of direct hit
				hitCount++;
				hit.attackEntityFrom(DamageSource.inFire, 5.0F);
			}
			if ( hitCount > 0 ) {
				worldObj.newExplosion(this, posX, posY, posZ, explosionForce, !isDestructive, isDestructive);
				setDead();
				return;
			}

			// hit blocks
			updateBlockHit();
			Block block = getRelativeBlock(0, 0, 0);

			// fire explosion on impact
//			if ( block != Blocks.air && block != Blocks.fire ) {
			if ( block.getMaterial().blocksMovement() ) {
				//if ( getRelativeBlock(0, 1, 0) == Blocks.air )
				if ( getRelativeBlock(0, 1, 0).getMaterial().isReplaceable() )
					setRelativeBlock(0, 1, 0, Blocks.fire);
				worldObj.newExplosion(this, posX, posY+1, posZ, explosionForce, !isDestructive, isDestructive);
				setDead();
			}
		}

		setPosition(posX, posY, posZ);
	}


	public int getColorIndex() {
		return fireballIndex;
	}

/*
	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		if ( nbt.hasKey("direction", 9) ) {
			NBTTagList dirTag = nbt.getTagList("direction", 6);
			this.motionX = dirTag.func_150309_d(0);
			this.motionY = dirTag.func_150309_d(1);
			this.motionZ = dirTag.func_150309_d(2);
		}
		else {
			System.out.println("nope");
			this.setDead();
		}
	}


	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		nbt.setTag("direction", this.newDoubleNBTList(new double[] {this.motionX, this.motionY, this.motionZ}));
	}
 */

}
