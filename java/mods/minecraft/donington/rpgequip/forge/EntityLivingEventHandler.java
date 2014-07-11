package mods.minecraft.donington.rpgequip.forge;

import mods.minecraft.donington.blocky3d.network.B3DPacketDispatcher;
import mods.minecraft.donington.rpgequip.RPGEquipMod;
import mods.minecraft.donington.rpgequip.common.RPGEAttributes;
import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import mods.minecraft.donington.rpgequip.common.RPGEDamageSource;
import mods.minecraft.donington.rpgequip.common.entity.EntityEliteFlying;
import mods.minecraft.donington.rpgequip.common.entity.EntityEliteMob;
import mods.minecraft.donington.rpgequip.common.entity.elite.EliteHelper;
import mods.minecraft.donington.rpgequip.common.entity.living.EliteAura;
import mods.minecraft.donington.rpgequip.common.item.gear.GearEnchantHelper;
import mods.minecraft.donington.rpgequip.common.player.GearInventory;
import mods.minecraft.donington.rpgequip.network.GearInventoryPacket;
import mods.minecraft.donington.rpgequip.network.PlayerAttributePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandClearInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class EntityLivingEventHandler {


  /** onEntityConstructing: add extended attributes to eligible living entities */
  @SubscribeEvent
  public void onEntityConstructing(EntityConstructing event) {
	Entity e = event.entity;

    if ( e instanceof EntityPlayer ) {
      EntityLivingBase entity = (EntityLivingBase) event.entity;

      // register entity extended information if it is not already available
      if ( GearInventory.get(entity) == null )   GearInventory.register(entity);
      if ( RPGEAttributes.get(entity) == null )  RPGEAttributes.register(entity);
      return;
    }


	if ( e instanceof EntityLiving ) {
		EntityLivingBase entity = (EntityLivingBase) event.entity;
		if ( EliteAura.get(entity) == null )  EliteAura.register(entity);
		e.getDataWatcher().addObject(RPGECommonProxy.eliteAuraDataWatcherId, 0);
		return;
	}
  }


  /** onPlayerDeath: Preserve inventory and attributes on death if the gamerules allow */
  @SubscribeEvent
  public void onPlayerDeath(LivingDeathEvent event) {
    if ( RPGECommonProxy.isClient() ) return;
    if ( !(event.entity instanceof EntityPlayer) ) return;

    EntityLivingBase entity = event.entityLiving;

    /* drop GearInventory on death if keepInventory is false */
    if ( !event.entity.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory") ) {
      IInventory gearInv = GearInventory.get(entity).getGearInventory();
      World world = entity.worldObj;

      for ( int i = 0;  i < gearInv.getSizeInventory();  i++ ) {
    	ItemStack stack = gearInv.getStackInSlot(i);
    	if ( stack != null ) {
          world.spawnEntityInWorld(new EntityItem(world, entity.posX, entity.posY, entity.posZ, stack));
          gearInv.setInventorySlotContents(i, null);
    	}
      }
      return;
    }

    /* preserve GearInventory on death if keepInventory is true */
    GearInventory.preserve(entity);
  }


  /** onEntityJoinWorld: take appropriate action when a new entity enters the game world **/
  @SubscribeEvent
  public void onEntityJoinWorld(EntityJoinWorldEvent event) {
	  if ( RPGECommonProxy.isClient() ) {
		  Minecraft mc = Minecraft.getMinecraft();
		  if ( event.entity.equals(mc.thePlayer) )
			  mc.thePlayer.addChatMessage(new ChatComponentText(String.format("%s%s %s", EnumChatFormatting.GOLD, RPGEquipMod.MOD_NAME, RPGEquipMod.MOD_VERSION)));

		  return;
	  }

	  if ( event.entity instanceof EntityPlayer ) {
		  onPlayerRespawn((EntityPlayer) event.entity);
		  return;
	  }

	  if ( event.entity instanceof EntityLiving ) {
		  EntityLiving entity = (EntityLiving) event.entity;

		  /* this check is a mess since DataWatcher of aura is not preserved.
		   * well, it is preserved in EntityEliteMob and EntityEliteFlying, but because
		   * they have a specific load/save of the aura to the nbt.
		   *
		   * FIXME: create EliteAura extends IExtendedEntityProperties which
		   *        is used to preserve aura data between saves and loads.
		   *          - if EliteAura.get() returns null, then the spawn is new and run rng process below
		   *          - if EliteAura.get() returns 0 or valid aura,
		   *            set the data watcher and do nothing else
		   *            (RPGEAttributes should be preserved independently)
		   */

		  // if an aura is already set, the entity must already be configured.
		  // This would mean it is an elite loaded from nbt data, so nothing needs to be done
		  EliteAura savedAura = EliteAura.get(entity);

		  // if aura was already valid, just set it and return
		  if ( savedAura.isValid() ) {
			  int aura = savedAura.getAura();

			  if ( aura == 0 ||
				   entity instanceof EntityEliteMob ||
				   entity instanceof EntityEliteFlying )
				  return;

			  System.out.printf("(reload) Promoted Elite %s has joined the world\n", entity.getClass().getSimpleName());
			  EliteHelper.promoteElite(entity, savedAura.getAura());
			  return;
		  }

		  // handle EntityEliteMob, EntityEliteFlying
		  else if ( entity instanceof EntityEliteMob ||
				    entity instanceof EntityEliteFlying ) {

			  // EntityElite* are set to have the lowest possible spawn chance by setSpawn,
			  // which is about as rare as an Enderman.

			  // Lower the chance of EntityElite* spawn even more with this check
			  if ( entity.getRNG().nextDouble() < RPGEquipMod.eliteEntityChance ) {
				  event.setCanceled(true);
				  return;
			  }
		  }

		  double chance = 0;

		  // deny these entities from gaining elite status here
		  if ( entity instanceof EntityEliteMob ||  // already elite
		       entity instanceof EntityEliteFlying ||  // already elite
			   entity instanceof EntityPigZombie )       // pulled from EntityZombie (ai doesn't work)
			  return;

		  // allow the following entities to gain elite status
		  else if ( entity instanceof EntityZombie ||     // zombies
		  	        entity instanceof EntitySkeleton ) {  // skeletons
			  chance = RPGEquipMod.eliteMonsterChance;
		  }

		  // allow animals to gain elite status
		  else if ( entity instanceof EntityAnimal ) {
			  chance = RPGEquipMod.eliteAnimalChance;
		  }

		  else return;  // zero chance

		  if ( Math.random() >= chance ) {
			  FMLLog.info("Promoted Elite %s has joined the world\n", entity.getClass().getSimpleName());
			  EliteHelper.promoteElite(entity);
		  }
		  return;
	  }

  }


  /** onPlayerRespawn: Restore extended entity information, send update packets to player */
  private void onPlayerRespawn(EntityPlayer player) {
	GearInventory gear = GearInventory.restore(player);
    RPGEAttributes attrs = RPGEAttributes.get(player);
	gear.recalculate(attrs);
	attrs.applyModifiers(player);
    B3DPacketDispatcher.sendTo(new GearInventoryPacket(gear), (EntityPlayerMP) player);
    B3DPacketDispatcher.sendTo(new PlayerAttributePacket(attrs), (EntityPlayerMP) player);
  }


  /** onEntityLivingUpdate: handle entities with RPGAttributes */
  @SubscribeEvent
  public void onEntityLivingUpdate(LivingUpdateEvent event) {
	if ( !(event.entity instanceof EntityCreature) ) return;
    EntityCreature entity = (EntityCreature) event.entityLiving;
    RPGEAttributes attrs = RPGEAttributes.get(entity);
    if ( attrs == null ) return;

    // wherewasi cache to return when deaggro'd?
    // aggro list to consider when hating on players? might be too much

    EntityLivingBase target = entity.getAttackTarget();

    if ( target != null ) {
    	if ( entity.getDistanceToEntity(target) > RPGECommonProxy.eliteAggroRange ) {
    		target = null;
    	}
    }
    	
    if ( target == null ) {
    	target = entity.worldObj.getClosestPlayer(entity.posX, entity.posY, entity.posZ, RPGECommonProxy.eliteAggroRange);
    }

    entity.setAttackTarget(target);
  }


  // CLIENT/SERVER EVENT
  /** onEntityLivingJump: apply jump boost */
  @SubscribeEvent
  public void onEntityLivingJump(LivingJumpEvent event) {
    EntityLivingBase entity = event.entityLiving;
    RPGEAttributes attrs = RPGEAttributes.get(entity);
    if ( attrs == null ) return;
    float jumpBoost = attrs.getJumpPercent();
    if ( jumpBoost == 0.0F ) return;

    /** scale up the jump by the jumpBoost percentage increase */
    double increase = ( entity.motionY / 3 ) * jumpBoost;  // 1/3 * boost == approx 45%:0.5 block height ratio
    entity.motionY += increase;
  }


  // SERVER EVENT
  /** onEntityLivingFall: reduce fall damage relative to jump boost */
  @SubscribeEvent
  public void onEntityLivingFall(LivingFallEvent event) {
    if ( RPGECommonProxy.isClient() ) return;

    EntityLivingBase entity = event.entityLiving;
    RPGEAttributes attrs = RPGEAttributes.get(event.entityLiving);
    if ( attrs == null ) return;
    float jumpBoost = attrs.getJumpPercent(); 
    if ( jumpBoost == 0.0F ) return;

    /** scale down fall damage by the jumpBoost percentage increase */
    // testing indicates this scale isn't perfect, but it feels right
    double decrease = 1.14 * jumpBoost;  // estimated jump fall from one block = 1.14
    event.distance -= decrease;
  }


  // SERVER EVENT
  /** onEntityLivingHurt: mitigate incoming damage */
  @SubscribeEvent
  public void onEntityLivingHurt(LivingHurtEvent event) {
    if ( RPGECommonProxy.isClient() ) return;

    EntityLivingBase entity = event.entityLiving;
    DamageSource type = event.source;
    entity.setLastAttacker(event.source.getEntity());

    /* handle gear damage */
    GearInventory gear = GearInventory.get(entity);
    if ( gear != null && type.getDamageType() != "fall" && type.getDamageType() != "drown" ) {
      IInventory inv = gear.getGearInventory();
      for ( int i = 0;  i < inv.getSizeInventory();  i++ ) {
        ItemStack stack = inv.getStackInSlot(i);
        if ( stack != null ) {
          //System.out.println("damaging");
          stack.damageItem(1, entity);
        }
      }
    }

    // early out on non-applicable damage sources
    if ( type.isDamageAbsolute() ) return;
    if ( type.isExplosion() ) return;

    /* handle incoming damage */
    RPGEAttributes attrs = RPGEAttributes.get(entity);
    if ( attrs == null ) return;
    float mitigation = 0.0F;

    if ( type.isFireDamage() )
//      mitigation = attrs.getFireReduction();
    	return;
    else if ( type.isMagicDamage() )
//      mitigation = attrs.getMagicReduction();
    	return;
    else if ( type.isProjectile() || !type.isUnblockable() ) {
      // cancel default minecraft armor reduction so we can recalculate
      type.setDamageBypassesArmor();
      event.ammount *= attrs.getArmorReduction(entity);

      // defense mitigation
      mitigation = attrs.getDefenseMitigation();
    }

    /* apply damage mitigation */
    if ( mitigation > 0.0F ) {
      float roll = mitigation * GearEnchantHelper.rngFloat(0.5F, 1.0F);
      float cap = event.ammount / 2;
      if ( roll > cap ) roll = cap;  // maximum 50% damage reduction via defense
      //System.out.printf("DamageSource '%s' := %2.1f - %2.1f := %2.1f (%2.1f hearts)\n", type.damageType, event.ammount, roll, event.ammount-roll, ((event.ammount-roll)/2));
      event.ammount -= roll;

      // never allow completely reduced damage
      if ( event.ammount < 0.1F )
        event.ammount = 0.1F;
    }
  }


  // SERVER EVENT
  /** onEntityLivingAttack: add bonus damage on attack */
  @SubscribeEvent
  public void onEntityLivingAttack(LivingAttackEvent event) {
    if ( RPGECommonProxy.isClient() ) return;
    if ( event.ammount <= 0.1F ) return;

    DamageSource type = event.source;
    if ( type.isDamageAbsolute() ) return;
    if ( type.isExplosion() ) return;
    if ( type instanceof RPGEDamageSource ) return;

    /* LivingAttackEvent is from the receiver's perspective. */
    Entity e = type.getEntity();
    if (!(e instanceof EntityLivingBase)) return;
    EntityLivingBase entity = (EntityLivingBase) e;
    EntityLivingBase target = event.entityLiving;

    /* handle outgoing damage */
    RPGEAttributes attrs = RPGEAttributes.get((EntityLivingBase) entity);
    if ( attrs == null ) return;

    float power = 0.0F;

    if ( type.isFireDamage() ) return;
    else if ( type.isMagicDamage() ) return;
    else if ( type.isProjectile() || !type.isUnblockable() )
      power = attrs.getAttackPower();

    /* apply damage power */
	if ( power > 0.0F ) {
      float base = event.ammount;
      float roll = power * GearEnchantHelper.rngFloat(0.5F, 1.0F);
      event.setCanceled(true);

      //System.out.printf("DamageSource '%s' := %2.3f + %2.3f := %2.3f (%2.1f hearts)\n", type.damageType, base, roll, base+roll, ((base+roll)/2));
      if ( (base + roll > 0.0F) && target.attackEntityFrom(new RPGEDamageSource(type.damageType, entity), base + roll) ) {
        ItemStack beatstick = entity.getHeldItem();
        if ( beatstick != null ) {
        	// handle weapon damage
        	if ( beatstick.getItem() instanceof ItemTool )
        		beatstick.damageItem(2, entity);
        	else
        		beatstick.damageItem(1, entity);
        }
      }
    }

  }

 
  @SubscribeEvent
  public void EntityLootEvent(LivingDropsEvent event) {
	  EntityLivingBase entity = event.entityLiving;
	  if ( entity instanceof EntityPlayer ) return;
	  RPGEAttributes attrs = RPGEAttributes.get(entity);
	  if ( attrs != null )
		  EliteHelper.doRandomLoot(entity);
  }


	@SubscribeEvent
	public void onClearInventoryEvent(CommandEvent event) {
		if ( !(event.command instanceof CommandClearInventory) ) return;
		if ( !(event.sender instanceof EntityPlayerMP) ) return;

		CommandClearInventory command = (CommandClearInventory) event.command;
		String[] args = event.parameters;

        EntityPlayerMP player = args.length == 0 ? command.getCommandSenderAsPlayer(event.sender) : command.getPlayer(event.sender, args[0]);
        Item item = args.length >= 2 ? command.getItemByText(event.sender, args[1]) : null;
        int damage = args.length >= 3 ? command.parseIntWithMin(event.sender, args[2], 0) : -1;
        int count = 0;

        if (args.length >= 2 && item == null) return;

        GearInventory gear = GearInventory.get(player);
        if ( gear == null ) return;
        IInventory inv = gear.getGearInventory();
        for ( int i = 0;  i < inv.getSizeInventory(); i++ ) {
        	ItemStack stack = inv.getStackInSlot(i);
        	if ( stack != null && (item == null || stack.getItem() == item) && (damage <= -1 || stack.getItemDamage() == damage) ) {
        		count++;
        		inv.setInventorySlotContents(i, null);
        	}
        }

        if (count == 0) return;

        // I should really make this a function somewhere... third typing this sequence of functions
        RPGEAttributes attrs = RPGEAttributes.get(player);
        gear.recalculate(attrs);
        attrs.applyModifiers(player);

        command.notifyAdmins(event.sender, LanguageRegistry.instance().getStringLocalization("commands.clear.gearinventory"), new Object[] {player.getCommandSenderName(), Integer.valueOf(count)});
	}

}
