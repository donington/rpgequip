package mods.minecraft.donington.blocky3d.proxy;

public interface B3DIProxy {

	public void commonPreInit();
	public void commonInit();
	public void commonPostInit();

	public void clientPreInit();
	public void clientInit();
	public void clientPostInit();

}
