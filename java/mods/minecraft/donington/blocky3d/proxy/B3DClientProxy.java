package mods.minecraft.donington.blocky3d.proxy;

import cpw.mods.fml.common.FMLCommonHandler;
import mods.minecraft.donington.blocky3d.Blocky3DMod;
import mods.minecraft.donington.blocky3d.forge.B3DInputHandler;
import mods.minecraft.donington.blocky3d.region.B3DRegionSelectorRenderer;
import net.minecraftforge.common.MinecraftForge;


public class B3DClientProxy extends B3DCommonProxy {


  /*
  @Override
  public void allocateHandlers() {
    super.allocateHandlers();
    // ... //
  }
   */


  @Override
  public void registerHandlers() {
    super.registerHandlers();

    MinecraftForge.EVENT_BUS.register(new B3DRegionSelectorRenderer());

    FMLCommonHandler.instance().bus().register(new B3DInputHandler());
  }


  /*
  @Override
  public void reset() {
    super.reset();
    // ... //
  }
   */

}
