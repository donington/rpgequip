package mods.minecraft.donington.blocky3d.render;


import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class Blocky3DModel {
  private IModelCustom      model;
  private ResourceLocation  texture;


  public Blocky3DModel(String modAsset, String modelAsset, String textureAsset) {
    //model      = AdvancedModelLoader.loadModel("/assets/" + modAsset + "/" + modelAsset);
    model      = AdvancedModelLoader.loadModel(new ResourceLocation(modAsset, modelAsset));
    texture    = new ResourceLocation(modAsset, textureAsset);
    System.out.println("model := " + model.toString());
  }


  public ResourceLocation getTexture() {
	return texture;
  }


  public void doRender() {
    model.renderAll();
  }

}
