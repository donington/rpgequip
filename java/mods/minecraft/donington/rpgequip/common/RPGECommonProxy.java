package mods.minecraft.donington.rpgequip.common;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import mods.minecraft.donington.blocky3d.network.B3DPacketPipeline;
import mods.minecraft.donington.rpgequip.RPGEquipMod;
import mods.minecraft.donington.rpgequip.block.BlockFiniteFire;
import mods.minecraft.donington.rpgequip.common.entity.EntityEliteFlying;
import mods.minecraft.donington.rpgequip.common.entity.EntityEliteMob;
import mods.minecraft.donington.rpgequip.common.entity.elite.EliteCreatureIndex;
import mods.minecraft.donington.rpgequip.common.entity.magic.EntityMagicExplosion;
import mods.minecraft.donington.rpgequip.common.entity.magic.EntityMagicFire;
import mods.minecraft.donington.rpgequip.common.entity.magic.EntityMagicShield;
import mods.minecraft.donington.rpgequip.common.item.WandDoomScepter;
import mods.minecraft.donington.rpgequip.common.item.caster.WandDeadlyForce;
import mods.minecraft.donington.rpgequip.common.item.gear.GearRegistrationHelper;
import mods.minecraft.donington.rpgequip.forge.EntityLivingEventHandler;
import mods.minecraft.donington.rpgequip.network.packet.GearInventoryPacket;
import mods.minecraft.donington.rpgequip.network.packet.PlayerAttributePacket;
import mods.minecraft.donington.rpgequip.network.packet.RPGEOpenInventoryPacket;
import mods.minecraft.donington.superioranvil.common.SuperiorAnvilProxy;
import mods.minecraft.donington.superioranvil.common.block.SuperiorAnvil;
import mods.minecraft.donington.superioranvil.common.item.SmithingHammer;
import mods.minecraft.donington.superioranvil.common.item.crafting.SuperiorAnvilCraftingManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.ExistingSubstitutionException;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.Type;
import cpw.mods.fml.relauncher.Side;


public class RPGECommonProxy {
  private static final Map<String, NBTTagCompound> extendedEntityData = new HashMap<String, NBTTagCompound>();

  // GUI IDs
  public static final int GuiRPGEquipInventoryId = 0;
  public static final int GuiSuperiorAnvilId = 1;

  // data watchers
  public static final int eliteCreatureTypeDataWatcherId = 28;
  public static final int eliteAuraDataWatcherId = 29;
  public static final int eliteFuseDataWatcherId = 27;
  public static final int magicDataWatcherId = 27;
  public static final int specialDataWatcherId = 26;

  // elite stuff
  public static final int eliteMobAggroRange = 16;     // distance until elite will begin attack
  public static final int eliteMobAggroRangeMax = 32;  // distance until elite gives up pursuit
  public static final int eliteFlyingAggroRange = 24;     // distance until elite will begin attack
  public static final int eliteFlyingAggroRangeMax = 64;  // distance until elite gives up pursuit

  public static final int eliteWanderRange = 48*48;       // farthest natural wander distance (squared comparison)
  public static final int eliteWanderDistance = 10;       // maximum random wander position
  public static final int eliteWanderDistanceCheck = 12;  // ensure rng is in bounds
  public static final float eliteAbsoluteRange = 64.0F;   // absolute maximum distance from home
  public static final int eliteSwingSpeed = 20;


  public static SuperiorAnvilProxy anvilProxy = new SuperiorAnvilProxy();

  public static final CreativeTabs tabGear = new CreativeTabs("tabGear") {
      @Override
      public Item getTabIconItem() {
        return GearRegistrationHelper.diamondAmulet;
      }
  };

  // Superior Anvil and Smithing Hammer
  public static SuperiorAnvil   anvil   = new SuperiorAnvil();
  public static SmithingHammer  hammer  = new SmithingHammer();
  protected static BlockFiniteFire blockFiniteFire;


  private void registerItem(Item item) {
	  GameRegistry.registerItem(item, item.getUnlocalizedName());
  }


  public void preInit(FMLPreInitializationEvent event) {

	GearRegistrationHelper.registerGear();
    anvilProxy.commonPreInit();
    SuperiorAnvilCraftingManager.getInstance().sortRecipes();

    // wand registration
    registerItem(new WandDoomScepter());
    registerItem(new WandDeadlyForce());

    // magic!
    RPGEquipMod.registerEntity(EntityMagicFire.class, "magic_fire");
    RPGEquipMod.registerEntity(EntityMagicShield.class, "magic_shield");
    RPGEquipMod.registerEntity(EntityMagicExplosion.class, "magic_explosion");

    // custom elite monster registration
    new EliteCreatureIndex();  // instantiate the elite creature index

    // EntityMob derivative
    RPGEquipMod.registerEntity(EntityEliteMob.class, "eliteMob", Color.decode("#A0803F").getRGB(), Color.decode("#0F10A0").getRGB());
    EntityRegistry.addSpawn(EntityEliteMob.class, 1, 1, 1, EnumCreatureType.monster);

    // EntityFlying derivative
    RPGEquipMod.registerEntity(EntityEliteFlying.class, "eliteFlying", Color.decode("#A0803F").getRGB(), Color.decode("#A0100F").getRGB());
    EntityRegistry.addSpawn(EntityEliteFlying.class, 1, 1, 1, EnumCreatureType.monster);

    // Override BlockFire with BlockFiniteFire (extinguishes fire when gamerule doFireTick false)
   	blockFiniteFire = new BlockFiniteFire();
   	Item itemFiniteFire = new ItemMultiTexture(blockFiniteFire, blockFiniteFire, new String[] { "default" } );

   	try {
		GameRegistry.addSubstitutionAlias("minecraft:fire", Type.ITEM, itemFiniteFire);
		GameRegistry.addSubstitutionAlias("minecraft:fire", Type.BLOCK, blockFiniteFire);
	} catch (ExistingSubstitutionException e) {
		System.out.println("Failed to inject BlockFiniteFire!");
	}
  }


  public void init(FMLInitializationEvent event) {
    B3DPacketPipeline.getInstance().registerPacket(GearInventoryPacket.class);
    B3DPacketPipeline.getInstance().registerPacket(PlayerAttributePacket.class);
    B3DPacketPipeline.getInstance().registerPacket(RPGEOpenInventoryPacket.class);

    anvilProxy.commonInit();
  }


  public void postInit(FMLPostInitializationEvent event) {
    MinecraftForge.EVENT_BUS.register(new EntityLivingEventHandler());
    NetworkRegistry.INSTANCE.registerGuiHandler(RPGEquipMod.instance, new RPGEGuiHandler());

    anvilProxy.commonPostInit();
  }


  public static void preserveEntityData(String name, NBTTagCompound compound) {
    //System.out.printf("preserving tag '%s'\n", name);
    extendedEntityData.put(name, compound);
  }


  public static NBTTagCompound restoreEntityData(String name) {
	//if ( extendedEntityData.containsKey(name) )
	  //System.out.printf("restoring tag '%s'\n", name);
    return extendedEntityData.remove(name);
  }


  public static boolean isClient() {
	return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT;
  }


  public static boolean isServer() {
    return FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER;
  }

}
