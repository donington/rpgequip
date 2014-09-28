package mods.minecraft.donington.blocky3d.proxy;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import mods.minecraft.donington.blocky3d.Blocky3DMod;
import mods.minecraft.donington.blocky3d.forge.B3DProtectionHandler;
import mods.minecraft.donington.blocky3d.forge.B3DWorldWandHandler;
import mods.minecraft.donington.blocky3d.item.ItemWorldWand;
import mods.minecraft.donington.blocky3d.network.B3DPacketPipeline;
import mods.minecraft.donington.blocky3d.region.B3DRegion;
import mods.minecraft.donington.blocky3d.region.B3DRegionCache;
import mods.minecraft.donington.blocky3d.region.B3DRegionSelectorRenderer;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;


public class B3DCommonProxy {
  protected static B3DRegionCache regionCache;
  private static B3DProtectionHandler protectionHandler;
  private static B3DWorldWandHandler worldwandHandler;

  public static Item itemWorldWand;

/*
  PotionRegistry potionRegistry;
  WorldWandRegistry wwRegistry;
 */

  public void allocateHandlers() {
	regionCache = new B3DRegionCache();
	protectionHandler = new B3DProtectionHandler(regionCache);
	worldwandHandler = new B3DWorldWandHandler();

	itemWorldWand = new ItemWorldWand();
  }


  public void registerHandlers() {
	MinecraftForge.EVENT_BUS.register(protectionHandler);
	MinecraftForge.EVENT_BUS.register(worldwandHandler);

	GameRegistry.registerItem(itemWorldWand, "worldwand");

	//potionRegistry = new PotionRegistry(256);
  }


  public void reset() {
	regionCache.clear();
	protectionHandler.reset();
  }
}
