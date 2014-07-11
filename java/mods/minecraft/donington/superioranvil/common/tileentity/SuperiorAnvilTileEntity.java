package mods.minecraft.donington.superioranvil.common.tileentity;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.network.NetworkRegistry;
import mods.minecraft.donington.blocky3d.world.Blocky3DTileEntity;
import mods.minecraft.donington.superioranvil.common.SuperiorAnvilProxy;
import mods.minecraft.donington.superioranvil.common.block.SuperiorAnvil;
import mods.minecraft.donington.superioranvil.common.item.crafting.SuperiorAnvilCraftingManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class SuperiorAnvilTileEntity extends Blocky3DTileEntity {
	private InventoryBasic inv;
	private ItemStack craftStack;


	public SuperiorAnvilTileEntity() {
		inv = new InventoryBasic("", false, SuperiorAnvil.invSize);
		craftStack = null;
	}


	private void applyRandomEntityItemVelocity(EntityItem entity) {
        entity.motionX = 2 * ( 0.5 - Math.random() ) * 0.05;
        entity.motionY = ( Math.random() * 0.05 ) + 0.2;
        entity.motionZ = 2 * ( 0.5 - Math.random() ) * 0.05;
	}


	public InventoryBasic getInventory() {
		return inv;
	}


	public void releaseInventoryIntoWorld(World world, int posX, int posY, int posZ) {
		if ( world.isRemote ) return;
		ItemStack stack;
		EntityItem entity;

		for ( int i = 0;  i < inv.getSizeInventory();  i++ ) {
			stack = inv.getStackInSlot(i);
			if ( stack != null ) {
				entity = new EntityItem(world, posX + 0.5D, posY + 0.5D, posZ + 0.5D, stack);
				applyRandomEntityItemVelocity(entity);
				world.spawnEntityInWorld(entity);
				inv.setInventorySlotContents(i, null);
			}
		}
		craftStack = null;
		this.markDirty();
		this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
	}


	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		NBTTagCompound stackNBT;

		for ( int i = 0; i < SuperiorAnvil.invSize; i++ ) {
			ItemStack stack = inv.getStackInSlot(i);
			if ( stack != null ) {
				stackNBT = new NBTTagCompound();
				stack.writeToNBT(stackNBT);
				nbt.setTag("inv" + i, stackNBT);
			} else
				nbt.removeTag("inv" + i);
		}
		if ( craftStack != null ) {
			stackNBT = new NBTTagCompound();
			craftStack.writeToNBT(stackNBT);
			nbt.setTag("craftstack", stackNBT);
		} else
			nbt.removeTag("craftstack");
	}


	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		NBTTagCompound stackNBT;

		for ( int i = 0; i < SuperiorAnvil.invSize; i++ ) {
			stackNBT = nbt.getCompoundTag("inv" + i);
			if ( stackNBT.hasNoTags() )
				inv.setInventorySlotContents(i, null);
			else
				inv.setInventorySlotContents(i, ItemStack.loadItemStackFromNBT(stackNBT));
		}
		stackNBT = nbt.getCompoundTag("craftstack");
		if ( stackNBT.hasNoTags() )
			craftStack = null;
		else
			craftStack = ItemStack.loadItemStackFromNBT(stackNBT);
	}


	public void setCraftResult(ItemStack craftStack) {
		this.craftStack = craftStack;
		this.markDirty();
		this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
	}


	public boolean createCraftResult(World world, EntityPlayer player) {
		//System.out.println("craftStack := " + craftStack);
		if ( craftStack == null ) return false;
	    //if ( world.isRemote ) return true;

		// add crafted item directly to player inventory
		if ( !(player.inventory.addItemStackToInventory(craftStack.copy())) ) {
			// direct inventory failed;  spawn in world instead
			EntityItem entity = new EntityItem(world, player.posX, player.posY + 0.5D, player.posZ, craftStack.copy());
			applyRandomEntityItemVelocity(entity);
			entity.delayBeforeCanPickup = 50;
			world.spawnEntityInWorld(entity);
		}

    	this.depleteInventory();
    	this.markDirty();
    	world.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
		return true;
	}


	private void depleteInventory() {
		// deplete inventory contents
		for ( int i = 0;  i < inv.getSizeInventory();  i++ ) {
			inv.decrStackSize(i, 1);
		}

		// invalidate craftStack if the matching recipe has changed
		ItemStack stack = SuperiorAnvilCraftingManager.getInstance().findMatchingRecipe(inv);
		if ( stack == null || !stack.isItemEqual(craftStack) ) craftStack = null;
	}

}