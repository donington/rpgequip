package mods.minecraft.donington.blocky3d;

import mods.minecraft.donington.blocky3d.command.B3DCommandRegion;
import mods.minecraft.donington.blocky3d.network.B3DPacketPipeline;
import mods.minecraft.donington.blocky3d.network.packet.WorldWandSelectionPacket;
import mods.minecraft.donington.blocky3d.network.packet.WorldWandUpdatePacket;
//import cpw.mods.fml.common.network.NetworkMod;
import mods.minecraft.donington.blocky3d.proxy.B3DCommonProxy;
import mods.minecraft.donington.rpgequip.network.packet.GearInventoryPacket;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;


@Mod(modid = Blocky3DMod.MOD_ID, name = Blocky3DMod.MOD_NAME, version = Blocky3DMod.MOD_VERSION)
//@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class Blocky3DMod {
  public static final String MOD_ID = "blocky3d";
  public static final String MOD_NAME = "Blocky3D";
  public static final String MOD_VERSION = "0.3";

  @Instance(MOD_ID)
  public static Blocky3DMod instance;

  @SidedProxy(clientSide = "mods.minecraft.donington.blocky3d.proxy.B3DClientProxy", serverSide = "mods.minecraft.donington.blocky3d.proxy.B3DCommonProxy")
  public static B3DCommonProxy proxy;


  /* protected data */

  /* private data */
  //private static Configuration config;


  @EventHandler
  public void PreInit(FMLPreInitializationEvent event) {
    //config = new Configuration(event.getSuggestedConfigurationFile());
    //config.load();

    // derp = config.get(Configuration.CATEGORY_ITEM, "derp",
    // defaultDerp).getDerp();

    // instantiate the packet handling pipeline
    B3DPacketPipeline packetHandler = new B3DPacketPipeline();

    proxy.allocateHandlers();
  }


  @EventHandler
  public void load(FMLInitializationEvent event) {
    proxy.registerHandlers();

    B3DPacketPipeline.getInstance().init();

    B3DPacketPipeline.getInstance().registerPacket(WorldWandSelectionPacket.class);
    B3DPacketPipeline.getInstance().registerPacket(WorldWandUpdatePacket.class);
  }


  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {
	B3DPacketPipeline.getInstance().postInit();
  }


  @EventHandler
  public void serverPreStarting(FMLServerAboutToStartEvent event) {
	proxy.reset();
  }

  @EventHandler
  public void serverStarting(FMLServerStartingEvent event) {
    event.registerServerCommand(new B3DCommandRegion());
  }


  /*
  protected static void info(String msg) {
    //FMLLog.log(Blocky3DMod.modName, Level.INFO, msg, (Object) null);
    System.out.println(Blocky3DMod.MOD_NAME + " [INFO] " + msg);
  }


  protected static void info(String fmt, Object... data) {
    //FMLLog.log(Blocky3DMod.modName, Level.INFO, fmt, data);
    System.out.printf(Blocky3DMod.MOD_NAME + " [INFO] " + fmt, data);
  }


  protected static void severe(String msg) {
    //FMLLog.log(Blocky3DMod.modName, Level.SEVERE, msg, (Object[]) null);
    System.out.println(Blocky3DMod.MOD_NAME + " [SEVERE] " + msg);
  }


  protected static void severe(String fmt, Object... data) {
    //FMLLog.log(Blocky3DMod.modName, Level.SEVERE, fmt, data);
    System.out.printf(Blocky3DMod.MOD_NAME + " [SEVERE] " + fmt, data);
  }
*/
}
