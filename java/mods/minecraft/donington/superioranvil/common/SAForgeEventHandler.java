package mods.minecraft.donington.superioranvil.common;

import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import mods.minecraft.donington.superioranvil.common.block.SuperiorAnvil;
import mods.minecraft.donington.superioranvil.common.tileentity.SuperiorAnvilTileEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class SAForgeEventHandler {


	/** Stop creative mode hammer swings from removing the anvil from the world. **/
	@SubscribeEvent
	public void onPlayerSmithingHammerForge(PlayerInteractEvent event) {
		EntityPlayer player = event.entityPlayer;

		// early out if not left click block
		if ( event.action != PlayerInteractEvent.Action.LEFT_CLICK_BLOCK ) return;

		World world = player.worldObj;  // abort if block is not Superior Anvil
		if ( world.getBlock(event.x, event.y, event.z) != RPGECommonProxy.anvil ) return;

		ItemStack stack = player.getHeldItem();  // abort if item is not Smithing Hammer
		if ( stack == null || stack.getItem() != RPGECommonProxy.hammer ) return;

		// cancel normal block event and move on with our lives
		event.setCanceled(true);
		return;
	}


	/** Limit hammer swing speed.
	 * In my opinion all items should be limited by the swing progress code.
	 * Spam click damage is imbalanced and overpowered.
	 **/
	@SubscribeEvent
	public void onEntitySmithingHammerAttack(AttackEntityEvent event) {
		EntityLivingBase entity = event.entityLiving;
		ItemStack stack = entity.getHeldItem();
		if ( stack == null || stack.getItem() != RPGECommonProxy.hammer ) return;
		if ( event.entityLiving.isSwingInProgress && event.entityLiving.swingProgress > 0.0F ) {
			event.setCanceled(true);
		}
	}

}
