package mods.minecraft.donington.rpgequip.common.entity.magic;

import java.util.List;
import java.util.Random;

import mods.minecraft.donington.rpgequip.RPGEquipMod;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityMagicShield extends EntityMagic {
	private int sound;
	private boolean sizeSet = false;

	private int duration;
	private float absorption;


	public EntityMagicShield(World world) {
		super(world);
		this.setSize(1.0F, 1.0F);
		this.sound = 0;
	}


	public EntityMagicShield(World world, EntityLivingBase caster, int duration, float absorption) {
		this(world);
		this.caster = caster;

		posX = caster.posX;
		posY = caster.posY;
		posZ = caster.posZ;
		this.setPosition(posX, posY, posZ);

		this.duration = duration;
		this.absorption = absorption;
		caster.setAbsorptionAmount(absorption);
	}


	@Override
	public void onUpdate() {
		super.onUpdate();

		if ( !worldObj.isRemote ) {
			if ( caster == null || caster.isDead || caster.getAbsorptionAmount() <= 0F ) {
				setDead();
				return;
			}

			if ( ticksExisted > duration ) {
				caster.setAbsorptionAmount(0F);
				setDead();
				return;
			}
		}

		// initialize size if data is available
		if ( !sizeSet && caster != null ) {
			setSize(caster.width * 1.8F, caster.height * 1.0F);
			sizeSet = true;
		}

		// update position
		if ( caster != null ) {
			posX = caster.posX;
			posY = caster.posY;
			posZ = caster.posZ;
		}


		if ( !worldObj.isRemote ) {
			List<EntityLivingBase> victims = getLivingEntitiesHit();
			for ( EntityLivingBase hit : victims ) {
				hit.attackEntityFrom(DamageSource.magic, 1.0F);
				Vec3 src =  Vec3.createVectorHelper(posX, posY, posZ);
		    	Vec3 dest = Vec3.createVectorHelper(hit.posX, hit.posY, hit.posZ);
		    	Vec3 ptr = src.subtract(dest).normalize();
		    	hit.motionX += ptr.xCoord * 1.8;
		    	hit.motionY += 0.8;
		    	hit.motionZ += ptr.zCoord * 1.8;
		    	if ( hit.motionY > 0.8 ) hit.motionY = 0.8;
			}

			if ( victims.size() > 0 ) {
				playSound(RPGEquipMod.MOD_ID + ":magic.shield.hurt", 1.0F, 1.0F);
			}

			sound++;
			if ( sound > 8 ) {
				playSound(RPGEquipMod.MOD_ID + ":magic.shield", 1.0F, 1.0F);
				sound = 0;
			}
		}

		setPosition(posX, posY, posZ);
	}

}
