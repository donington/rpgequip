package mods.minecraft.donington.rpgequip.common.entity.magic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import net.minecraft.block.Block;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class EntityMagic extends Entity {
	protected static final int casterDataWatcher = 29;

	protected EntityLivingBase caster;
	protected Random rng;

	private int blockX, blockY, blockZ;
	private boolean firstUpdate = true;
	private boolean isReady = false;


	public EntityMagic(World world) {
		super(world);
		this.rng = new Random();
	}


	public EntityMagic(World world, EntityLivingBase caster) {
		this(world);
		this.caster = caster;
	}


	protected List<EntityLivingBase> getLivingEntitiesWithinAABB(AxisAlignedBB boundingBox) {
		List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox);
		List<EntityLivingBase> victims = new ArrayList<EntityLivingBase>();
		for (int i = 0; i < list.size(); i++) {
			Entity hit = (Entity)list.get(i);
			if ( hit == null ) continue;
			if ( !(hit instanceof EntityLivingBase) ) continue;
			if ( hit.getEntityId() == caster.getEntityId() ) continue;
			if ( hit.canBeCollidedWith() )
				victims.add((EntityLivingBase) hit);
		}

		return victims;		
	}


	protected List<EntityLivingBase> getLivingEntitiesHit() {
		return getLivingEntitiesWithinAABB(this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
//		List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
//		List<EntityLivingBase> victims = new ArrayList<EntityLivingBase>();
//      ...
	}


	protected void updateBlockHit() {
		blockX = MathHelper.floor_double(posX);
		blockY = MathHelper.floor_double(posY);
		blockZ = MathHelper.floor_double(posZ);
	}


	protected Block getRelativeBlock(int x, int y, int z) {
		return worldObj.getBlock(blockX + x, blockY + y, blockZ + z);
	}


	protected boolean setRelativeBlock(int x, int y, int z, Block block) {
		return worldObj.setBlock(blockX + x, blockY + y, blockZ + z, block);
	}


	protected int getCasterId() {
		return getDataWatcher().getWatchableObjectInt(casterDataWatcher);
	}


	protected boolean isDataWatcherReady(Object data) {
		if ( !isReady ) {
			if ( !worldObj.isRemote )
				getDataWatcher().updateObject(RPGECommonProxy.magicDataWatcherId, data);
			isReady = true;
			return false;
		}
		return true;
	}


	public EntityLivingBase getCaster() {
		return caster;
	}


	public Random getRNG() {
		return rng;
	}


	@Override
	protected void entityInit() {
		DataWatcher dw = getDataWatcher();
		getDataWatcher().addObject(casterDataWatcher, 0);
	}


	@Override
	public boolean canBeCollidedWith() {
		return false;
	}


	@Override
	public float getBrightness(float f) {
		return 1.0F;
	}


	/** you must call this method to update client side caster **/
	@Override
	public void onUpdate() {
		if ( firstUpdate ) {
			if ( caster != null )
				getDataWatcher().updateObject(casterDataWatcher, caster.getEntityId());
			firstUpdate = false;
		}
		else if ( caster == null ) {
			Entity e = worldObj.getEntityByID(getCasterId());
			if ( e instanceof EntityLivingBase )
				caster = (EntityLivingBase) e;
		}

	}


	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {}


	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {}

}
