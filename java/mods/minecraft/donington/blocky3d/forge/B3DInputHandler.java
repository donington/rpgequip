package mods.minecraft.donington.blocky3d.forge;

import mods.minecraft.donington.blocky3d.proxy.B3DCommonProxy;
import mods.minecraft.donington.blocky3d.region.B3DRegion;
import mods.minecraft.donington.blocky3d.region.B3DRegionCache;
import mods.minecraft.donington.blocky3d.region.B3DRegionSelector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class B3DInputHandler {
  private final KeyBinding nextKey;
  private final KeyBinding prevKey;
  private final KeyBinding addKey;
  private final KeyBinding removeKey;

  private static Minecraft mc;

  public B3DInputHandler() {
	nextKey = new KeyBinding("key.B3DWorldWandNext.desc", Keyboard.KEY_END, "key.Blocky3D.category");
	prevKey = new KeyBinding("key.B3DWorldWandPrev.desc", Keyboard.KEY_HOME, "key.Blocky3D.category");
	addKey = new KeyBinding("key.B3DWorldWandAdd.desc", Keyboard.KEY_INSERT, "key.Blocky3D.category");
	removeKey = new KeyBinding("key.B3DWorldWandRemove.desc", Keyboard.KEY_DELETE, "key.Blocky3D.category");

	ClientRegistry.registerKeyBinding(nextKey);
    ClientRegistry.registerKeyBinding(prevKey);
    ClientRegistry.registerKeyBinding(addKey);
    ClientRegistry.registerKeyBinding(removeKey);

    mc = Minecraft.getMinecraft();
  }


  @SubscribeEvent
  public void handleKeyInput(InputEvent.KeyInputEvent event) {
	EntityPlayer player = mc.thePlayer;
    ItemStack stack = player.getCurrentEquippedItem();
    if ( stack == null ) return;
    if ( stack.getItem() != B3DCommonProxy.itemWorldWand ) return;

    B3DRegionSelector select = B3DRegionCache.getSelector(player);

    // WorldWand input must be packet driven
    // TODO: B3DRegionPacket

	if ( nextKey.isPressed() ) {
	  System.out.println("next");
	  select.next();
	  return;
	}

	if ( prevKey.isPressed() ) {
	  System.out.println("prev");
	  select.prev();
	  return;
	}

	if ( addKey.isPressed() ) {
	  System.out.println("add");
	  select.put(new B3DRegion());
	  return;
	}

	if ( removeKey.isPressed() ) {
	  System.out.println("remove");
	  select.removeSelectedRegion();
	  return;
	}

  }

}
