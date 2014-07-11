package mods.minecraft.donington.rpgequip.common.entity.elite;

import java.util.ArrayList;
import java.util.HashMap;

import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBlaze;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.model.ModelEnderman;
import net.minecraft.client.model.ModelGhast;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EliteCreatureIndex {
    //private static ArrayList<EliteCommonNode> creatureIndex = null;
	private static HashMap<Integer,EliteCommonNode> creatureIndex = null;


    public EliteCreatureIndex() {
    	if ( creatureIndex != null ) return;

//    	creatureIndex = new ArrayList<EliteCommonNode>();
    	creatureIndex = new HashMap();

    	if ( RPGECommonProxy.isClient() )
    		this.tryInitClientIndex();  // required to not load client function data on stack
    	else
    		this.initServerIndex();
    }


	/** force initClientIndex() to not be loaded on server side with initializer **/
    private void tryInitClientIndex() {
    	this.initClientIndex();
    }


    private void initServerIndex() {
    	/* EntityEliteMob */
    	addElite(EliteCreatureType.Creeper,   new EliteCommonNode(new EntityCreeper(null)));
    	addElite(EliteCreatureType.Enderman,  new EliteCommonNode(new EntityEnderman(null)));
    	addElite(EliteCreatureType.Pigman,    new EliteCommonNode(new EntityPigZombie(null)));
    	/* EntityEliteFlying */
    	addElite(EliteCreatureType.Blaze,  new EliteCommonNode(new EntityBlaze(null)));
    	addElite(EliteCreatureType.Ghast,  new EliteCommonNode(new EntityGhast(null)));
	}


    @SideOnly(Side.CLIENT)
    private void initClientIndex() {
    	/* EntityEliteMob */
    	addElite(EliteCreatureType.Creeper,   new EliteClientNode(ModelCreeper.class, "textures/entity/creeper/creeper.png", new EntityCreeper(null)));
    	addElite(EliteCreatureType.Enderman,  new EliteClientNode(ModelEnderman.class, "textures/entity/enderman/enderman.png", new EntityEnderman(null)));
    	addElite(EliteCreatureType.Pigman,    new EliteClientNode(ModelZombie.class, "textures/entity/zombie_pigman.png", new EntityPigZombie(null)));		// TODO Auto-generated method stub
    	/* EntityEliteFlying */
    	addElite(EliteCreatureType.Blaze,  new EliteClientNode(ModelBlaze.class, "textures/entity/blaze.png", new EntityBlaze(null)));
    	addElite(EliteCreatureType.Ghast,  new EliteClientNode(ModelGhast.class, "textures/entity/ghast/ghast.png", new EntityGhast(null)));
    }


	/** elite common information **/
	private class EliteCommonNode {
		private final EntityLiving entity;
		private final float width;
		private final float height;

		protected EliteCommonNode(EntityLiving entity) {
			this.entity = entity;
			this.width = entity.width;
			this.height = entity.height;
		}
	}


	/** elite client information **/
	@SideOnly(Side.CLIENT)
	private class EliteClientNode extends EliteCommonNode {
		private final ModelBase model;
		private final ResourceLocation texture;

		protected EliteClientNode(Class modelClass, String texture, EntityLiving entity) {
			super(entity);
			ModelBase modelInstance = null;
			try {
				//modelInstance = (ModelBase) modelClass.getConstructor(float.class).newInstance(1.0F);
				modelInstance = (ModelBase) modelClass.newInstance();
			} catch (Exception e) {
				Minecraft.getMinecraft().crashed(new CrashReport("cannot instantiate model", e));
			}
			this.model = modelInstance;
			this.texture = new ResourceLocation(texture);
		}
	}


	/** add elite based off EntityMob to the index **/
	private void addElite(EliteCreatureType creatureType, EliteCommonNode creatureNode) {
		int pos = creatureType.getIndex();
    	creatureIndex.put(pos, creatureNode);
	}


	/** get a given creature model **/
    @SideOnly(Side.CLIENT)
    public static ModelBase getCreatureModel(int type) {
    	if ( type <= 0 ) return null;
    	return ((EliteClientNode)creatureIndex.get(type)).model;
    }


	/** get a given creature model texture **/
    @SideOnly(Side.CLIENT)
    public static ResourceLocation getCreatureTexture(int type) {
    	//if ( type <= 0 ) return null;
    	return ((EliteClientNode)creatureIndex.get(type)).texture;
    }


    /** width of given creature **/
	public static float getWidth(int type) {
    	if ( type <= 0 ) return 0;
		return creatureIndex.get(type).width;
	}


	/** height of given creature **/
	public static float getHeight(int type) {
    	if ( type <= 0 ) return 0;
		return creatureIndex.get(type).height;
	}


	/** return the base entity the creature type is based off of **/
	public static EntityLiving getBaseEntity(int type) {
    	if ( type <= 0 ) return null;
		return creatureIndex.get(type).entity;
	}


	/** set a random mob creature type **/
	public static void setRandomEliteMob(EntityLiving e) {
		int creature = e.getRNG().nextInt(EliteCreatureType.mobCount) + EliteCreatureType.mobOffset;
		e.getDataWatcher().updateObject(RPGECommonProxy.eliteCreatureTypeDataWatcherId, creature);
	}


	/** set a random flying creature type **/
	public static void setRandomEliteFlying(EntityLiving e) {
		int creature = e.getRNG().nextInt(EliteCreatureType.flyingCount) + EliteCreatureType.flyingOffset;
		e.getDataWatcher().updateObject(RPGECommonProxy.eliteCreatureTypeDataWatcherId, creature);
	}

}
