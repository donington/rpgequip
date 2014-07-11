package mods.minecraft.donington.blocky3d.proxy;

import mods.minecraft.donington.blocky3d.Blocky3DMod;
import mods.minecraft.donington.blocky3d.render.B3DRegionRenderer;
import net.minecraftforge.common.MinecraftForge;


public class B3DClientProxy extends B3DCommonProxy {

  @Override
  public void registerHandlers() {
    super.registerHandlers();

	//Blocky3DMod.regionRenderer = new B3DRegionRenderer();
    //MinecraftForge.EVENT_BUS.register(Blocky3DMod.regionRenderer);

    
  }

}
