package mods.minecraft.donington.rpgequip.common.entity;

import cpw.mods.fml.common.FMLLog;
import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import mods.minecraft.donington.rpgequip.common.entity.elite.EliteHelper;
import mods.minecraft.donington.rpgequip.common.entity.elite.EliteCreatureIndex;
import mods.minecraft.donington.rpgequip.common.entity.elite.EliteNameGenerator;
import mods.minecraft.donington.rpgequip.common.entity.elite.EntityEliteHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.EntityMob;
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
    }


	@Override
    public void onUpdate() {
		if ( !isEliteReady ) {
			int creature = EntityEliteHelper.getEliteCreatureType(this);

			// create elite on server side
			if ( RPGECommonProxy.isServer() && creature <= 0 ) {
				EliteHelper.createEliteFlying(this, creature);
				EliteCreatureIndex.setRandomEliteFlying(this);
				return;
			}

			// detect elite on both sides and set ready
			if ( creature <= 0 ) return;

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
	
}
