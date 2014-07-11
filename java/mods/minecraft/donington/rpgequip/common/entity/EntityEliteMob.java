package mods.minecraft.donington.rpgequip.common.entity;

import cpw.mods.fml.common.FMLLog;
import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import mods.minecraft.donington.rpgequip.common.entity.elite.EliteHelper;
import mods.minecraft.donington.rpgequip.common.entity.elite.EliteCreatureIndex;
import mods.minecraft.donington.rpgequip.common.entity.elite.EliteNameGenerator;
import mods.minecraft.donington.rpgequip.common.entity.elite.EntityEliteHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeModContainer;

public class EntityEliteMob extends EntityMob {
	private boolean isEliteReady = false;

	public EntityEliteMob(World world) {
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
					EliteCreatureIndex.setRandomEliteMob(this);
					creature = EntityEliteHelper.getEliteCreatureType(this);
				}
				if ( aura <= 0 )
					EliteHelper.createEliteMob(this, creature);
				else
					EliteHelper.createEliteMob(this, creature, aura);
			}

			// detect if creature is valid on client
			else if ( creature <= 0 )
				return;

			setSize(EliteCreatureIndex.getWidth(creature), EliteCreatureIndex.getHeight(creature));
			setCustomNameTag(EliteNameGenerator.nameEliteLike(creature, EntityEliteHelper.getEliteAura(this)));
			isEliteReady = true;  // delay update until tick after isEliteReady is set to true
			if ( RPGECommonProxy.isServer() )
				FMLLog.info("EntityEliteMob %s has joined the world\n", EliteCreatureIndex.getBaseEntity(creature).getClass().getSimpleName());
			return;
		}

    	super.onUpdate();
    }


	@Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        //this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3.0D);
    }


	@Override
    protected boolean canDespawn() {
        return false;
    }


	@Override
    protected boolean isAIEnabled() {
        return true;
    }


	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		DataWatcher dw = getDataWatcher();
//		int aura = nbt.getInteger("aura");
		int creature = nbt.getInteger("creature");
		// elite will be regenerated if this fails
//		if ( creature > 0 && aura > 0 ) {
		if ( creature > 0 ) {
			EntityEliteHelper.setEliteCreatureType(this, creature);
//			EntityEliteHelper.setEliteAura(this, aura);
		}
	}


	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		//nbt.setInteger("aura", getDataWatcher().getWatchableObjectInt(RPGECommonProxy.eliteAuraDataWatcherId));
		nbt.setInteger("creature", getDataWatcher().getWatchableObjectInt(RPGECommonProxy.eliteCreatureTypeDataWatcherId));
	}

}
