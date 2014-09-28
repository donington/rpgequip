package mods.minecraft.donington.rpgequip.common.entity.elite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import mods.minecraft.donington.rpgequip.common.LootChance;
import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import mods.minecraft.donington.rpgequip.common.RPGEAttributes;
import mods.minecraft.donington.rpgequip.common.entity.EntityEliteFlying;
import mods.minecraft.donington.rpgequip.common.entity.EntityEliteMob;
import mods.minecraft.donington.rpgequip.common.entity.ai.*;
import mods.minecraft.donington.rpgequip.common.entity.ai.special.*;
import mods.minecraft.donington.rpgequip.common.entity.living.EliteAura;
import mods.minecraft.donington.rpgequip.common.item.ItemGear;
import mods.minecraft.donington.rpgequip.common.item.gear.GearEnchantHelper;
import mods.minecraft.donington.rpgequip.common.item.gear.GearRegistrationHelper;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAICreeperSwell;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EliteHelper {
	private static final Random rng = new Random();
	private static final float twoPi = (float) (Math.PI * 2);


	// random loot list for elites
	private static final List<LootChance> eliteLootListBasic = new ArrayList<LootChance>();
	private static final List<Item> eliteLootListEquip = new ArrayList<Item>();
	static {
		eliteLootListBasic.add(new LootChance(Items.iron_ingot, 2, 5));
		eliteLootListBasic.add(new LootChance(Items.gold_ingot, 2, 5));
		eliteLootListBasic.add(new LootChance(Items.emerald, 1, 3));
		eliteLootListBasic.add(new LootChance(Items.diamond, 1, 3));

		eliteLootListEquip.add(Items.iron_helmet);
		eliteLootListEquip.add(Items.iron_chestplate);
		eliteLootListEquip.add(Items.iron_leggings);
		eliteLootListEquip.add(Items.iron_boots);
		eliteLootListEquip.add(Items.chainmail_helmet);
		eliteLootListEquip.add(Items.chainmail_chestplate);
		eliteLootListEquip.add(Items.chainmail_leggings);
		eliteLootListEquip.add(Items.chainmail_boots);
		eliteLootListEquip.add(Items.iron_pickaxe);
		eliteLootListEquip.add(Items.iron_axe);
		eliteLootListEquip.add(Items.iron_shovel);
		eliteLootListEquip.add(Items.diamond_pickaxe);
		eliteLootListEquip.add(Items.diamond_axe);
		eliteLootListEquip.add(Items.diamond_shovel);
		eliteLootListEquip.add(GearRegistrationHelper.leatherBelt);
		eliteLootListEquip.add(GearRegistrationHelper.leatherBracer);
		eliteLootListEquip.add(GearRegistrationHelper.ironBracer);
		eliteLootListEquip.add(GearRegistrationHelper.goldBracer);
		eliteLootListEquip.add(GearRegistrationHelper.diamondBracer);
	}


	private static void registerTask(EntityAITasks tasks, EntityAIBase task, int pos) {
		//System.out.println("Registering task " + pos + " as " + task.getClass().getSimpleName());
		tasks.addTask(pos, task);
	}


	/** remove all tasks from an entity (if they use the new ai system) **/
	private static void lobotomize(EntityLiving entity) {
		entity.tasks.taskEntries.clear();
		entity.targetTasks.taskEntries.clear();
	}


	/* can we:
	 *  force a flying mob check and set ai appropriately?
	 *  override an entity function call on construction?  (could be expensive)
	 *  best solution still seems to be rewrite trouble entities :(
	 */

	/** overriding ai is unfornuately a complex situation.
	 *  - pigzombies, slimes, blazes do not use the task handler
	 *  - some entities have built in mechanics that mess up
	 *  - neutral and ranged entities don't have melee attack code
	 *  - there's probably more problems.
	 **/
	private static void createEliteBrain(EntityLiving entity, boolean isFlying, int creature) {
		//System.out.println("createEliteBrain :: creature := " + creature);

		EntityAITasks tasklist;
		int task;

		// cache new aggro task so we can inject it in both task lists
		EntityAIBase aggroTask;
		if ( isFlying )
			aggroTask = new EliteAIAggro(entity, RPGECommonProxy.eliteFlyingAggroRange, RPGECommonProxy.eliteFlyingAggroRangeMax);
		else
			aggroTask = new EliteAIAggro(entity, RPGECommonProxy.eliteMobAggroRange, RPGECommonProxy.eliteMobAggroRangeMax);

		lobotomize(entity);

		/* prepare target tasks */
		tasklist = entity.targetTasks;
		task = 0;

		/* elite aggro (currently only works for EntityEliteFlying due to EntityMob?) */
		//if ( isFlying )
			registerTask(tasklist, aggroTask, task++);

		/* basic melee attack */
		registerTask(tasklist, new EliteAIMeleeAttack(entity), task++);

		/* racial attacks */
		if ( EliteCreatureType.Creeper.isCreature(creature) ) {
			registerTask(tasklist, new EliteAIDetonate(entity), task++);
		}

		/* movement enhancers */
		if ( isFlying ) {					// flying mob
		  if ( rng.nextDouble() < 0.6 )		// ~60% chance dash
			registerTask(tasklist, new EliteAIDash(entity), task++);
		  else								// ~40% chance ender
			registerTask(tasklist, new EliteAIEnder(entity), task++);
		} else {							// ground mob
		  if ( rng.nextDouble() < 0.85 )	// ~85% chance dash
			registerTask(tasklist, new EliteAIDash(entity), task++);
		  else								// ~15% chance ender
			registerTask(tasklist, new EliteAIEnder(entity), task++);
		
		}

		/* aggro behavior */
		// TODO

		/* chase the target */
		if ( isFlying )
			registerTask(tasklist, new EliteAIFlyingHarassTarget(entity), task++);
		else
			registerTask(tasklist, new EliteAIGroundChargeTarget(entity), task++);

		/* special attacks */
		registerTask(tasklist, new EliteAIFireball(entity), task++);
		registerTask(tasklist, new EliteAIShield(entity), task++);


		/* prepare normal tasks */
		tasklist = entity.tasks;
		task = 0;

		/* elite aggro (currently only works for EntityEliteFlying due to EntityMob?) */
		//if ( isFlying )
			registerTask(tasklist, aggroTask, task++);

		/* racial abilities */
		if ( EliteCreatureType.Enderman.isCreature(creature) )
			registerTask(tasklist, new EliteAIEnder(entity), task++);

		/* don't drown */
		registerTask(tasklist, new EntityAISwimming(entity), task++);

		/* wandering */
		if ( isFlying )
			registerTask(tasklist, new EliteAIFlyingWander(entity, 0.7, 1.3), task++);	
		else {
			registerTask(tasklist, new EliteAILeaper(entity), task++);
			registerTask(tasklist, new EliteAISprinter(entity), task++);
			registerTask(tasklist, new EliteAIGroundWander(entity, 0.7, 1.3), task++);
		}

	}


	/** TODO: specific auras **/
	private static void applyRandomAura(EntityLiving entity) {
//		int aura = rng.nextInt(RPGECommonProxy.auraCount)+1;
		int aura = rng.nextInt(EliteCreatureAura.getNumAuras())+1;
		applyAura(entity, aura);
	}


	/** apply an aura to an entity who can receive an aura **/ 
	public static void applyAura(EntityLiving entity, int aura) {
		EliteAura savedAura = EliteAura.get(entity);
		if ( savedAura == null ) {
			throw new RuntimeException("aura applied to invalid entity");
		}

		EliteCreatureAura creatureAura = EliteCreatureAura.getCreatureAura(aura);
		if ( creatureAura == null ) return;

		RPGEAttributes attrs = RPGEAttributes.get(entity);
		if ( attrs == null ) {
			RPGEAttributes.register(entity);
			attrs = RPGEAttributes.get(entity);
		}

		// set aura attributes
		creatureAura.setAuraAttributes(attrs);

		// set elite attributes
		attrs.setHealth(100);
		attrs.setArmor(20);

		attrs.applyModifiers(entity);
		entity.setHealth(entity.getMaxHealth());
		savedAura.setAura(aura);
		EntityEliteHelper.setEliteAura(entity, aura);
	}


	/** promote an EntityLiving into an elite **/
	public static void promoteElite(EntityLiving entity) {
		EliteHelper.createEliteBrain(entity, false, 0);
		applyRandomAura(entity);
		entity.setCustomNameTag(EliteNameGenerator.nameEliteLike(entity, EntityEliteHelper.getEliteAura(entity)));
	}


	/** promote an EntityLiving into an elite **/
	public static void promoteElite(EntityLiving entity, int aura) {
		EliteHelper.createEliteBrain(entity, false, 0);
		applyAura(entity, aura);
		entity.setCustomNameTag(EliteNameGenerator.nameEliteLike(entity, EntityEliteHelper.getEliteAura(entity)));
	}


	/** create an EntityEliteMob **/
	public static void createEliteMob(EntityEliteMob entity, int creature) {
		EliteHelper.createEliteBrain(entity, false, creature);
		applyRandomAura(entity);
	}


	/** create an EntityEliteMob with a specific aura **/
	public static void createEliteMob(EntityEliteMob entity, int creature, int aura) {
		EliteHelper.createEliteBrain(entity, false, creature);
		applyAura(entity, aura);
	}


	/** create an EntityEliteFlying **/
	public static void createEliteFlying(EntityEliteFlying entity, int creature) {
		EliteHelper.createEliteBrain(entity, true, creature);
		applyRandomAura(entity);
	}


	/** create an EntityEliteFlying with a specific aura **/
	public static void createEliteFlying(EntityEliteFlying entity, int creature, int aura) {
		EliteHelper.createEliteBrain(entity, true, creature);
		applyAura(entity, aura);
	}


	/** teleportAbsolute: teleport to world aligned coordinate **/
	public static boolean teleportAbsolute(EntityLiving elite, World world, int posX, int posZ, int rangeY) {
		int posY = (int)Math.floor(elite.posY);
		int height = (int)Math.ceil(elite.height);
		int offset = -rangeY;

		// calibrate teleport (sweep rangeY for lowest air block above non air block)
		while ( offset < rangeY ) {
			if ( world.getBlock(posX, posY+offset, posZ).getMaterial().blocksMovement() )
				offset++;
			else
				break;
		}

		// check for room
		for ( int i = 1; i < height; i++ ) {
			//if ( world.getBlock(posX, posY+offset+i, posZ) == Blocks.air ) continue;
			if ( world.getBlock(posX, posY+offset+i, posZ).getMaterial().blocksMovement() )
				return false;
		}

		// room for teleport, commence
		elite.setPosition(posX + 0.5, posY+offset, posZ + 0.5);

		// fail teleport if collisions
		if ( !elite.worldObj.getCollidingBoundingBoxes(elite, elite.boundingBox).isEmpty() ) {
			elite.setPosition(elite.lastTickPosX, elite.lastTickPosY, elite.lastTickPosZ);
			return false;
		}
			//elite.setJumping(true);
		return true;

	}


	/** teleportRelative: teleport to relatively aligned coordinate **/
	public static boolean teleportRelative(EntityLiving elite, World world, int posX, int posZ, int rangeY) {
		if ( posX == 0 && posZ == 0 ) return false;
		posX += (int)Math.floor(elite.posX);
		int posY = (int)Math.floor(elite.posY);
		posZ += (int)Math.floor(elite.posZ);
		return teleportAbsolute(elite, world, posX, posZ, rangeY);
	}


	/** teleportRandomly(): teleport the elite randomly within range **/
	public static boolean teleportRandomly(EntityLiving elite, int rangeXZ, int rangeY) {
		double facing = rng.nextDouble()*twoPi;
		int posX = (int)Math.floor(Math.sin(facing) * rng.nextDouble() * rangeXZ);
		//int posY = (int)Math.floor((0.5-rng.nextDouble()) * rangeY * 2);
		int posZ = (int)Math.floor(Math.cos(facing) * rng.nextDouble() * rangeXZ);
		//System.out.printf("teleportRandomly(%s) trying relative position (%d %d)\n", elite.getClass().getSimpleName(), posX, posZ);
		return teleportRelative(elite, elite.worldObj, posX, posZ, rangeY);
	}


	/** teleportToEntity(): teleport the elite to the specified entity **/
	public static boolean teleportToEntity(EntityLiving elite, EntityLivingBase target, int rangeY) {
		int posX = (int)Math.floor(target.posX + (0.5-rng.nextDouble() * 2.0));
		int posZ = (int)Math.floor(target.posZ + (0.5-rng.nextDouble() * 2.0));
		//System.out.printf("teleportToEntity(%s) trying absolute position (%d %d)\n", elite.getClass().getSimpleName(), posX, posZ);
		return teleportAbsolute(elite, elite.worldObj, posX, posZ, rangeY);
	}


	/** random loot helper for elites **/
	public static void doRandomLoot(EntityLivingBase elite) {
		LootChance loot;
		int amount;
		Item item;
		ItemStack equip;

		loot = eliteLootListBasic.get(rng.nextInt(eliteLootListBasic.size()));
		amount = rng.nextInt(loot.max) + loot.min;
		item = eliteLootListEquip.get(rng.nextInt(eliteLootListEquip.size()));
		equip = new ItemStack(item);

		if ( item instanceof ItemGear ) {
			RPGEAttributes attrs = GearEnchantHelper.rollEquipAttributes(equip, (ItemGear)item);
	    	if ( attrs != null )
	    		attrs.saveNBTData(equip.stackTagCompound);
		} else
			EnchantmentHelper.addRandomEnchantment(rng, equip, item.getItemEnchantability());
		
		elite.dropItem(loot.item, amount);
		elite.entityDropItem(equip, 0.0F);

        int bonusXP = 5 + rng.nextInt(5);
        while ( bonusXP > 0 ) {
            int xp = EntityXPOrb.getXPSplit(bonusXP);
            bonusXP -= xp;
            elite.worldObj.spawnEntityInWorld(new EntityXPOrb(elite.worldObj, elite.posX, elite.posY, elite.posZ, xp));
        }

	}

}
