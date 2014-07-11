package mods.minecraft.donington.superioranvil.client.gui;

import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import mods.minecraft.donington.rpgequip.common.inventory.ContainerRPGEInventory;
import mods.minecraft.donington.rpgequip.common.item.ItemGear;
import mods.minecraft.donington.rpgequip.common.item.gear.EquipSocket;
import mods.minecraft.donington.superioranvil.common.SuperiorAnvilProxy;
import mods.minecraft.donington.superioranvil.common.inventory.ContainerSuperiorAnvil;
import mods.minecraft.donington.superioranvil.common.tileentity.SuperiorAnvilTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiSuperiorAnvil extends GuiContainer {
	private ResourceLocation texture = new ResourceLocation("rpgequipmod:textures/gui/container/anvilinventory.png");
	private SuperiorAnvilTileEntity tileEntity;
	private float xSizeFloat;
	private float ySizeFloat;


	/** for some reason, this function is private in GuiContainer.  Recreated */
	private boolean isMouseOverSlot(Slot slot, int p_146981_2_, int p_146981_3_) {
		return this.func_146978_c(slot.xDisplayPosition, slot.yDisplayPosition, 16, 16, p_146981_2_, p_146981_3_);
	}


	public GuiSuperiorAnvil(EntityPlayer player, SuperiorAnvilTileEntity tileEntity) {
		super(new ContainerSuperiorAnvil(player.inventory, tileEntity));
		this.tileEntity = tileEntity;
		this.allowUserInput = true;
		this.xSize = 175;
		this.ySize = 175;
	}


	@Override
	public void updateScreen() {}


	/** Adds the buttons (and other controls) to the screen in question. */
	@Override
	public void initGui() {
		this.buttonList.clear();
		super.initGui();
	}


	/** Draws the screen and all the components in it. */
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		super.drawScreen(par1, par2, par3);
		this.xSizeFloat = (float)par1;
		this.ySizeFloat = (float)par2;
	}


	/* KEEP: this is the beginnings of a new mod!  maybe.
	public void renderHealthBar(int posX, int posY, float value) {
        Tessellator tes = Tessellator.instance;
        GL11.glDisable(GL11.GL_TEXTURE_2D);

		int width = 13;
		double zLevel = this.zLevel + 100;

        tes.startDrawingQuads();
          tes.setColorRGBA_F(0.0F, 0.0F, 0.0F, 1.0F);
          tes.addVertex((double)(posX + 0), (double)(posY + 2), zLevel);
          tes.addVertex((double)(posX + width), (double)(posY + 2), zLevel);
          tes.addVertex((double)(posX + width), (double)(posY + 0), zLevel);
          tes.addVertex((double)(posX + 0), (double)(posY + 0), zLevel);
        tes.draw();

        tes.startDrawingQuads();
          tes.setColorRGBA_F(0.8F-value, 0.1F+value, 0.0F, 1.0F);
          tes.addVertex((double)(posX + 0), (double)(posY + 1), zLevel);
          tes.addVertex((double)(posX + width), (double)(posY + 1), zLevel);
          tes.addVertex((double)(posX + width), (double)(posY + 0), zLevel);
          tes.addVertex((double)(posX + 0), (double)(posY + 0), zLevel);
        tes.draw();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	 */

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		this.fontRendererObj.drawString(I18n.format("tile.superiorAnvil.name", new Object[0]), 32, 10, 4210752);
		//this.itemRender.renderItemIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), new ItemStack(RPGECommonProxy.anvil), 10, 8);
		//renderHealthBar(12, 21, 1.0F);

		// scan for mouseover slot and add socket information, if present
		for (int i = 0;  i < this.inventorySlots.inventorySlots.size();  ++i) {
			Slot slot = (Slot)this.inventorySlots.inventorySlots.get(i);
			if ( !(isMouseOverSlot(slot, par1, par2)) )
				continue;

			ItemStack stack = slot.getStack();
			if ( stack == null || !(stack.getItem() instanceof ItemGear) ) continue;

			Iterator<EquipSocket> sockets = EquipSocket.getSockets(stack);
			if ( sockets == null ) return;
			EquipSocket socket;

			this.zLevel = 201;
			this.mc.getTextureManager().bindTexture(EquipSocket.getTexture());
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.8F);

			i = 0;
			while ( sockets.hasNext() ) {
				socket = sockets.next();
				if ( socket == null ) break;
				EquipSocket.renderSocketInGui(this, slot, socket, i);
				i++;
			}

			this.zLevel = 0;
			GL11.glDisable(GL11.GL_BLEND);
			return;
		}
	}


	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2,
			int var3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(texture);

		int left = this.guiLeft;
		int top = this.guiTop;
		int modelX = left + 56;
		int modelY = top + 61;

		this.drawTexturedModalRect(left, top, 0, 0, this.xSize, this.ySize);
	}

}
