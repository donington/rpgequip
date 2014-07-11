package mods.minecraft.donington.blocky3d.proxy;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import mods.minecraft.donington.blocky3d.Blocky3DMod;
import mods.minecraft.donington.blocky3d.item.ItemWorldWand;
import mods.minecraft.donington.blocky3d.network.B3DPacketPipeline;
import mods.minecraft.donington.blocky3d.render.B3DRegionRenderer;
import net.minecraft.item.Item;


public class B3DCommonProxy {
  Item itemWorldWand;

/*
  PotionRegistry potionRegistry;
  WorldWandRegistry wwRegistry;
 */

  public void allocateHandlers() {
//	Blocky3DMod.regionRenderer = new B3DRegionRenderer();
//	itemWorldWand = new ItemWorldWand();
  }


  public void registerHandlers() {
//	GameRegistry.registerItem(itemWorldWand, "worldwand");
    //potionRegistry = new PotionRegistry(256);
  }
}
