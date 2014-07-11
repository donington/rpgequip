package mods.minecraft.donington.rpgequip.common;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import mods.minecraft.donington.blocky3d.network.B3DPacketPipeline;
import mods.minecraft.donington.rpgequip.RPGEquipMod;
import mods.minecraft.donington.rpgequip.common.entity.EntityEliteFlying;
import mods.minecraft.donington.rpgequip.common.entity.EntityEliteMob;
import mods.minecraft.donington.rpgequip.common.entity.elite.EliteCreatureIndex;
import mods.minecraft.donington.rpgequip.common.entity.magic.EntityMagicExplosion;
import mods.minecraft.donington.rpgequip.common.entity.magic.EntityMagicFire;
import mods.minecraft.donington.rpgequip.common.entity.magic.EntityMagicShield;
import mods.minecraft.donington.rpgequip.common.item.DoomScepter;
import mods.minecraft.donington.rpgequip.common.item.gear.GearRegistrationHelper;
import mods.minecraft.donington.rpgequip.forge.EntityLivingEventHandler;
import mods.minecraft.donington.rpgequip.network.GearInventoryPacket;
import mods.minecraft.donington.rpgequip.network.PlayerAttributePacket;
import mods.minecraft.donington.rpgequip.network.RPGEOpenInventoryPacket;
import mods.minecraft.donington.superioranvil.common.SuperiorAnvilProxy;
import mods.minecraft.donington.superioranvil.common.block.SuperiorAnvil;
import mods.minecraft.donington.superioranvil.common.item.SmithingHammer;
import mods.minecraft.donington.superioranvil.common.item.crafting.SuperiorAnvilCraftingManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;


public class RPGECommonProxy {
  private static final Map<String, NBTTagCompound> extendedEntityData = new HashMap<String, NBTTagCompound>();

  // GUI IDs
  public static final int GuiRPGEquipInventoryId = 0;
  public static final int GuiSuperiorAnvilId = 1;

  // data watchers
  public static final int eliteCreatureTypeDataWatcherId = 28;
  public static final int eliteAuraDataWatcherId = 29;
  public static final int eliteFuseDataWatcherId = 27;
  public static final int magicDataWatcherId = 27;

  // aura stuff
  //public static int auraCount = 8;  // this is now handled by EliteCreatureAura

  // elite stuff
  public static final int eliteAggroRange = 16;     // distance until elite will begin attack
  public static final int eliteAggroRangeMax = 32;  // distance until elite gives up pursuit
  public static final int eliteWanderRange = 48*48;       // farthest natural wander distance (squared comparison)
  public static final int eliteWanderDistance = 10;       // maximum random wander position
  public static final int eliteWanderDistanceCheck = 12;  // ensure rng is in bounds
  public static final float eliteAbsoluteRange = 64.0F;   // absolute maximum distance from home
  public static final int eliteSwingSpeed = 20;


  public static SuperiorAnvilProxy anvilProxy = new SuperiorAnvilProxy();

  public static final CreativeTabs tabGear = new CreativeTabs("tabGear") {
      @Override
      public Item getTabIconItem() {
        return GearRegistrationHelper.diamondAmulet;
      }
  };

  // Superior Anvil and Smithing Hammer
  public static SuperiorAnvil   anvil   = new SuperiorAnvil();
  public static SmithingHammer  hammer  = new SmithingHammer();



  public void preInit(FMLPreInitializationEvent event) {

	GearRegistrationHelper.registerGear();
    anvilProxy.commonPreInit();
    SuperiorAnvilCraftingManager.getInstance().sortRecipes();

    // test item: world ending nonsense
    Item doomScepter = new DoomScepter();
    GameRegistry.registerItem(doomScepter, doomScepter.getUnlocalizedName());

    // magic!
    RPGEquipMod.registerEntity(EntityMagicFire.class, "magic_fire");
    RPGEquipMod.registerEntity(EntityMagicShield.class, "magic_shield");
    RPGEquipMod.registerEntity(EntityMagicExplosion.class, "magic_explosion");

    // custom elite monster registration
    new EliteCreatureIndex();  // instantiate the elite creature index

    // EntityMob derivative
    RPGEquipMod.registerEntity(EntityEliteMob.class, "eliteMob", 0, Color.MAGENTA.getRGB());
    EntityRegistry.addSpawn(EntityEliteMob.class, 1, 1, 1, EnumCreatureType.monster);

    // EntityFlying derivative (disabled for now, source of most crashes at the moment)
    //RPGEquipMod.registerEntity(EntityEliteFlying.class, "eliteFlying", 0, Color.MAGENTA.getRGB());
    //EntityRegistry.addSpawn(EntityEliteFlying.class, 1, 1, 1, EnumCreatureType.monster);
  }


  public void init(FMLInitializationEvent event) {
    B3DPacketPipeline.getInstance().registerPacket(GearInventoryPacket.class);
    B3DPacketPipeline.getInstance().registerPacket(PlayerAttributePacket.class);
    B3DPacketPipeline.getInstance().registerPacket(RPGEOpenInventoryPacket.class);
    //B3DPacketPipeline.getInstance().registerPacket(EntityAuraPacket.class);

    anvilProxy.commonInit();
  }


  public void postInit(FMLPostInitializationEvent event) {
    MinecraftForge.EVENT_BUS.register(new EntityLivingEventHandler());
    NetworkRegistry.INSTANCE.registerGuiHandler(RPGEquipMod.instance, new RPGEGuiHandler());

    anvilProxy.commonPostInit();
  }


  public static void preserveEntityData(String name, NBTTagCompound compound) {
    //System.out.printf("preserving tag '%s'\n", name);
    extendedEntityData.put(name, compound);
  }


  public static NBTTagCompound restoreEntityData(String name) {
	//if ( extendedEntityData.containsKey(name) )
	  //System.out.printf("restoring tag '%s'\n", name);
    return extendedEntityData.remove(name);
  }


  public static boolean isClient() {
	return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT;
  }


  public static boolean isServer() {
    return FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER;
  }

}
