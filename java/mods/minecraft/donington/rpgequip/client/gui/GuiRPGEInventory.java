package mods.minecraft.donington.rpgequip.client.gui;

import java.util.Iterator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.minecraft.donington.rpgequip.client.RPGEClientProxy;
import mods.minecraft.donington.rpgequip.common.inventory.ContainerRPGEInventory;
import mods.minecraft.donington.rpgequip.common.inventory.GearSlot;
import mods.minecraft.donington.rpgequip.common.item.ItemGear;
import mods.minecraft.donington.rpgequip.common.item.gear.EquipSocket;
import mods.minecraft.donington.rpgequip.common.item.gear.GearEnchantHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class GuiRPGEInventory extends GuiContainer {
  private ResourceLocation texture = new ResourceLocation("rpgequipmod:textures/gui/container/rpginventory.png");
  private float xSizeFloat;
  private float ySizeFloat;


  /** for some reason, this function is private in GuiContainer.  Recreated */
  private boolean isMouseOverSlot(Slot slot, int p_146981_2_, int p_146981_3_) {
    return this.func_146978_c(slot.xDisplayPosition, slot.yDisplayPosition, 16, 16, p_146981_2_, p_146981_3_);
  }


  public GuiRPGEInventory(EntityPlayer player) {
    super(new ContainerRPGEInventory(player.inventory));
    this.allowUserInput = true;
    this.xSize = 175;
    this.ySize = 175;
  }

  /** Called from the main game loop to update the screen. */
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


  @Override
  protected void drawGuiContainerForegroundLayer(int par1, int par2) {
    int i;

    for (i = 0;  i < this.inventorySlots.inventorySlots.size();  ++i) {
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
  protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(texture);

    int left = this.guiLeft;
    int top = this.guiTop;
    int modelX = left + 56;
    int modelY = top + 61;

    this.drawTexturedModalRect(left, top, 0, 0, this.xSize, this.ySize);
    drawPlayerModel(modelX, modelY, 20, (float)modelX - this.xSizeFloat, (float)modelY - 40 - this.ySizeFloat, this.mc.thePlayer);
  }


  /** TODO: rewrite */
  public static void drawPlayerModel(int posX, int posY, int scale, float p_147046_3_, float p_147046_4_, EntityLivingBase entity) {
    GL11.glEnable(GL11.GL_COLOR_MATERIAL);
    GL11.glPushMatrix();
      GL11.glTranslatef((float)posX, (float)posY, 50.0F);
      GL11.glScalef((float)(-scale), (float)scale, (float)scale);
      GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
      float f2 = entity.renderYawOffset;
      float f3 = entity.rotationYaw;
      float f4 = entity.rotationPitch;
      float f5 = entity.prevRotationYawHead;
      float f6 = entity.rotationYawHead;
      GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
      RenderHelper.enableStandardItemLighting();
      GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-((float)Math.atan((double)(p_147046_4_ / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
      entity.renderYawOffset = (float)Math.atan((double)(p_147046_3_ / 40.0F)) * 20.0F;
      entity.rotationYaw = (float)Math.atan((double)(p_147046_3_ / 40.0F)) * 40.0F;
      entity.rotationPitch = -((float)Math.atan((double)(p_147046_4_ / 40.0F))) * 20.0F;
      entity.rotationYawHead = entity.rotationYaw;
      entity.prevRotationYawHead = entity.rotationYaw;
      GL11.glTranslatef(0.0F, entity.yOffset, 0.0F);
      RenderManager.instance.playerViewY = 180.0F;
      RenderManager.instance.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
      entity.renderYawOffset = f2;
      entity.rotationYaw = f3;
      entity.rotationPitch = f4;
      entity.prevRotationYawHead = f5;
      entity.rotationYawHead = f6;
    GL11.glPopMatrix();
    RenderHelper.disableStandardItemLighting();
    GL11.glDisable(GL12.GL_RESCALE_NORMAL);
    OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
    GL11.glDisable(GL11.GL_TEXTURE_2D);
    OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
  }


  @Override
  protected void keyTyped(char par1, int par2) {
    if (par2 == RPGEClientProxy.RPGEquipInventory.getKeyCode())
      this.mc.thePlayer.closeScreen();
    super.keyTyped(par1, par2);
  }

}