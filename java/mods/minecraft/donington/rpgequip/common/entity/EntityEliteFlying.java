package mods.minecraft.donington.rpgequip.common.entity;

import cpw.mods.fml.common.FMLLog;
import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import mods.minecraft.donington.rpgequip.common.entity.elite.EliteHelper;
import mods.minecraft.donington.rpgequip.common.entity.elite.EliteCreatureIndex;
import mods.minecraft.donington.rpgequip.common.entity.elite.EliteNameGenerator;
import mods.minecraft.donington.rpgequip.common.entity.elite.EntityEliteHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeModContainer;

public class EntityEliteFlying extends EntityFlying {
	private boolean isEliteReady = false;

	public EntityEliteFlying(World world) {
		super(world);
	}


	@Override
    protected void entityInit() {
        super.entityInit();
    	this.getDataWatcher().addObject(RPGECommonProxy.eliteCreatureTypeDataWatcherId, Integer.valueOf(0));
    	this.getDataWatcher().addObject(RPGECommonProxy.eliteFuseDataWatcherId, Float.valueOf(0));
    }


	@Override
    public void onUpdate() {
		if ( !isEliteReady ) {
			int creature = EntityEliteHelper.getEliteCreatureType(this);

			// create elite on server side
			if ( RPGECommonProxy.isServer() ) {
				int aura = EntityEliteHelper.getEliteAura(this);
				if ( creature <= 0 ) {
					EliteCreatureIndex.setRandomEliteFlying(this);
					creature = EntityEliteHelper.getEliteCreatureType(this);
				}
				if ( aura <= 0 )
					EliteHelper.createEliteFlying(this, creature);
				else
					EliteHelper.createEliteFlying(this, creature, aura);
			}

			// detect if creature is valid on client
			else if ( creature <= 0 )
				return;

			setSize(EliteCreatureIndex.getWidth(creature), EliteCreatureIndex.getHeight(creature));
			setCustomNameTag(EliteNameGenerator.nameEliteLike(creature, EntityEliteHelper.getEliteAura(this)));
			isEliteReady = true;  // delay update until tick after isEliteReady is set to true
			if ( RPGECommonProxy.isServer() )
				FMLLog.info("EntityEliteFlying %s has joined the world\n", EliteCreatureIndex.getBaseEntity(creature).getClass().getSimpleName());
			return;
		}

    	super.onUpdate();
    }


	@Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        //this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
        //this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3.0D);
    }


	@Override
    protected boolean canDespawn() {
        return false;
    }


	@Override
    protected boolean isAIEnabled() {
        return true;
    }


    protected EntityLivingBase findPlayerToAttack() {
        EntityPlayer entityplayer = this.worldObj.getClosestVulnerablePlayerToEntity(this, RPGECommonProxy.eliteMobAggroRange);
        return entityplayer != null && this.canEntityBeSeen(entityplayer) ? entityplayer : null;
    }


    /** aggro is still broken, this does not fire for new ai.
     *  FIX: port to new ai duh
     **/
    //@Override
    //protected void updateEntityActionState() {}
    /*
	@Override
	protected void updateEntityActionState() {
		System.out.println("updateEntityActionState");
		EntityLivingBase target = this.getAttackTarget();

        if ( target == null )
            target = this.findPlayerToAttack();

        if ( target == null )
        	this.setAttackTarget(null);
        else if ( target instanceof EntityPlayer && ((EntityPlayer)target).capabilities.isCreativeMode )
        	this.setAttackTarget(null);
        else
        	this.setAttackTarget(target);
	}
	*/

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		DataWatcher dw = getDataWatcher();
		int creature = nbt.getInteger("creature");
		if ( creature > 0 ) {
			EntityEliteHelper.setEliteCreatureType(this, creature);
		}
	}


	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setInteger("creature", getDataWatcher().getWatchableObjectInt(RPGECommonProxy.eliteCreatureTypeDataWatcherId));
	}

}
