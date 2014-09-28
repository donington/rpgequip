package mods.minecraft.donington.rpgequip;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import mods.minecraft.donington.rpgequip.client.RPGEClientProxy;
import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;


@Mod(modid = RPGEquipMod.MOD_ID, name = RPGEquipMod.MOD_NAME, version = RPGEquipMod.MOD_VERSION, acceptableRemoteVersions=RPGEquipMod.MOD_VERSION)
public class RPGEquipMod {
  public static final String MOD_ID       = "rpgequipmod";
  public static final String MOD_NAME     = "RPGEquip Mod";
  public static final String MOD_VERSION  = "build-1052";

  // configuration parameters
  public static double eliteMonsterChance = 0.96;      // ~3% chance
  public static double eliteAnimalChance = 0.993;  // ~0.7% chance
  public static double eliteMobEntityChance = 0.5;    // ~50% chance (with lowest spawn rng)
  public static double eliteFlyingEntityChance = 0.4;    // ~40% chance (with lowest spawn rng)
  

  //@SidedProxy(clientSide = "mods.minecraft.donington.rpgequip.client.RPGEClientProxy", serverSide = "mods.minecraft.donington.rpgequip.common.RPGECommonProxy")
  public static RPGECommonProxy proxy;

  @Instance(MOD_ID)
  public static RPGEquipMod instance;

  private static Configuration config;
//  public static boolean finiteFire;  // cannot use;  block override is not modless compat


  /*
  private void loadConfig(File configFile) {
	  config = new Configuration(configFile);
	  config.load();

	  finiteFire = config.get(Configuration.CATEGORY_GENERAL, "finiteFire", true).getBoolean(true);
  }
   */


  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
  	//loadConfig(event.getSuggestedConfigurationFile());

    if ( RPGECommonProxy.isClient() ) {
      proxy = new RPGEClientProxy();
    }

    else if ( RPGECommonProxy.isServer() ) {
    	//System.out.printf("%s %s\n", MOD_NAME, MOD_VERSION);
    	proxy = new RPGECommonProxy();
    }
    else throw new IllegalStateException("RPGEquip cannot detect CLIENT/SERVER state");

    proxy.preInit(event);
  }


  @EventHandler
  public void init(FMLInitializationEvent event) {
    proxy.init(event);
  }


  @EventHandler
  public void postinit(FMLPostInitializationEvent event) {
    proxy.postInit(event);
  }


  /** helper function for registering entities **/
  public static int registerEntity(Class entityClass, String name) {
    int entityID = EntityRegistry.findGlobalUniqueEntityId();

    EntityRegistry.registerGlobalEntityID(entityClass, name, entityID);
    EntityRegistry.registerModEntity(entityClass, name, entityID, instance, 64, 1, true);
    return entityID;
  }


  /** helper function for registering entities with egg colors **/
  public static int registerEntity(Class entityClass, String name, int eggColor, int spotColor) {
    int entityID = EntityRegistry.findGlobalUniqueEntityId();

    EntityRegistry.registerGlobalEntityID(entityClass, name, entityID, eggColor, spotColor);
    EntityRegistry.registerModEntity(entityClass, name, entityID, instance, 64, 1, true);
    return entityID;
  }

}
