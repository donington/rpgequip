package mods.minecraft.donington.blocky3d;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import mods.minecraft.donington.blocky3d.network.B3DPacketPipeline;
//import cpw.mods.fml.common.network.NetworkMod;
import mods.minecraft.donington.blocky3d.proxy.B3DCommonProxy;


@Mod(modid = "Blocky3DMod", name = Blocky3DMod.modName, version = Blocky3DMod.modVersion)
//@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class Blocky3DMod {

  @Instance("Blocky3DMod")
  public static Blocky3DMod instance;
  public static final String modName = "Blocky3D";
  public static final String modVersion = "0.2";

  @SidedProxy(clientSide = "mods.minecraft.donington.blocky3d.proxy.B3DClientProxy", serverSide = "mods.minecraft.donington.blocky3d.proxy.B3DCommonProxy")
  public static B3DCommonProxy proxy;
  //public static B3DRegionRenderer regionRenderer;

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
    proxy.registerHandlers();
  }


  @EventHandler
  public void load(FMLInitializationEvent event) {
	B3DPacketPipeline.getInstance().init();
  }


  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    B3DPacketPipeline.getInstance().postInit();
  }


  // @EventHandler
  // public void serverStarting(FMLServerStartingEvent event) {
  // }

  protected static void info(String msg) {
    //FMLLog.log(Blocky3DMod.modName, Level.INFO, msg, (Object) null);
    System.out.println(Blocky3DMod.modName + " [INFO] " + msg);
  }


  protected static void info(String fmt, Object... data) {
    //FMLLog.log(Blocky3DMod.modName, Level.INFO, fmt, data);
    System.out.printf(Blocky3DMod.modName + " [INFO] " + fmt, data);
  }


  protected static void severe(String msg) {
    //FMLLog.log(Blocky3DMod.modName, Level.SEVERE, msg, (Object[]) null);
    System.out.println(Blocky3DMod.modName + " [SEVERE] " + msg);
  }


  protected static void severe(String fmt, Object... data) {
    //FMLLog.log(Blocky3DMod.modName, Level.SEVERE, fmt, data);
    System.out.printf(Blocky3DMod.modName + " [SEVERE] " + fmt, data);
  }

}
