package mods.minecraft.donington.rpgequip.client;

import mods.minecraft.donington.rpgequip.client.render.EntityEliteRenderer;
import mods.minecraft.donington.rpgequip.client.render.MagicFireRenderer;
import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import mods.minecraft.donington.rpgequip.common.entity.EntityEliteFlying;
import mods.minecraft.donington.rpgequip.common.entity.EntityEliteMob;
import mods.minecraft.donington.rpgequip.common.entity.elite.EliteCreatureIndex;
import mods.minecraft.donington.rpgequip.common.entity.magic.EntityMagicExplosion;
import mods.minecraft.donington.rpgequip.common.entity.magic.EntityMagicFire;
import mods.minecraft.donington.rpgequip.common.entity.magic.EntityMagicShield;
import mods.minecraft.donington.rpgequip.forge.ClientFMLHandler;
import mods.minecraft.donington.rpgequip.forge.ClientRenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.registry.GameRegistry;

public class RPGEClientProxy extends RPGECommonProxy {
  public static KeyBinding RPGEquipInventory;


  @Override
  public void preInit(FMLPreInitializationEvent event) {
    super.preInit(event);

    RPGECommonProxy.anvilProxy.clientPreInit();
  }


  @Override
  public void init(FMLInitializationEvent event) {
    super.init(event);
    RPGEquipInventory = new KeyBinding("key.RPGEquipInventory.desc", Keyboard.KEY_X, "key.RPGEquip.category");
    ClientRegistry.registerKeyBinding(RPGEquipInventory);

    RPGECommonProxy.anvilProxy.clientInit();
  }


  @Override
  public void postInit(FMLPostInitializationEvent event) {
    super.postInit(event);

    FMLCommonHandler.instance().bus().register(new ClientFMLHandler());
    MinecraftForge.EVENT_BUS.register(new ClientRenderHandler());

    RPGECommonProxy.anvilProxy.clientPostInit();

    RenderingRegistry.registerEntityRenderingHandler(EntityMagicFire.class, ClientRenderHandler.magicFireRenderer);
    RenderingRegistry.registerEntityRenderingHandler(EntityMagicShield.class, ClientRenderHandler.magicShieldRenderer);
    //RenderingRegistry.registerEntityRenderingHandler(EntityMagicExplosion.class, );

    /// custom elite monster registration
    EntityEliteRenderer eliteRenderer = new EntityEliteRenderer();
    RenderingRegistry.registerEntityRenderingHandler(EntityEliteMob.class, eliteRenderer);
    RenderingRegistry.registerEntityRenderingHandler(EntityEliteFlying.class, eliteRenderer);
  }

}
