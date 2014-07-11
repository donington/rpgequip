package mods.minecraft.donington.blocky3d.world;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public abstract class Blocky3DTileEntity extends TileEntity {
  private int direction;

  
  public int getDirection() {
    return this.direction;
  }


  public void setDirection(int direction) {
    this.direction = direction;
  }


  @Override
  public void writeToNBT(NBTTagCompound tc) {
    super.writeToNBT(tc);
    tc.setInteger("direction", this.direction);
  }


  @Override
  public void readFromNBT(NBTTagCompound tc) {
    super.readFromNBT(tc);
    this.direction = tc.getInteger("direction");
  }


  @Override
  public Packet getDescriptionPacket() {
    //System.out.println("Blocky3D: getDescriptionPacket()");
    NBTTagCompound nbtTag = new NBTTagCompound();
    this.writeToNBT(nbtTag);
    return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
  }


  @Override
  public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
    //System.out.println("Blocky3D: onDataPacket()");
    this.readFromNBT(packet.func_148857_g());  // packet.getNBTTagCompound()
  }

}
