package mods.minecraft.donington.rpgequip.common;

import java.util.List;
import java.util.UUID;

import cpw.mods.fml.common.registry.LanguageRegistry;
import mods.minecraft.donington.rpgequip.common.item.gear.EquipSocket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class RPGEAttributes implements IExtendedEntityProperties {
  public static final String ExtendedPropertyName = "RPGEquipAttributes";

  // TODO: move to static UUID no need for random really
  private static final UUID mcAttrHealthID  = UUID.randomUUID();
  private static final UUID mcAttrMoveID    = UUID.randomUUID();

  private static final int mcAttrAdd = 0;
  private static final int mcAttrMult = 1;

  /* attribute values (CONSIDER: value map PROS extendable CONS slightly more complex) */
  private int  armor;        // physical reduction from armor
  private int  health;       // health increase
  private int  strength;     // physical damage (rng)
  private int  defense;      // physical resistance (rng)
  private int  fireResist;   // fire resist
  private int  magicResist;  // magic resist
  private int  movement;     // move speed
  private int  jumping;      // jump height

  /* attribute abstractions */
  private float  attackPower;
  private float  armorReduction;
  private float  defenseMitigation;
  private float  fireReduction;
  private float  magicReduction;
  private float  movePercent;
  private float  jumpPercent;


  public void reset() {
	armor = 0;
	health = 0;
	strength = 0;
	defense = 0;
	fireResist = 0;
	magicResist = 0;
	movement = 0;
	jumping = 0;

	attackPower = 0;
	armorReduction = 0;
	defenseMitigation = 0;
	fireReduction = 0;
	magicReduction = 0;
	movePercent = 0;
	jumpPercent = 0;
  }


  @Override
  public void saveNBTData(NBTTagCompound compound) {
	NBTTagCompound attrs = new NBTTagCompound();

	if ( armor != 0 )        attrs.setByte("armor", (byte) armor);
    if ( health != 0 )       attrs.setByte("health", (byte) health);
    if ( strength != 0 )     attrs.setByte("strength", (byte) strength);
    if ( defense != 0 )      attrs.setByte("defense", (byte) defense);
    if ( fireResist != 0 )   attrs.setByte("fireResist", (byte) fireResist);
    if ( magicResist != 0 )  attrs.setByte("magicResist", (byte) magicResist);
    if ( movement != 0 )     attrs.setByte("movement", (byte) movement);
    if ( jumping != 0 )      attrs.setByte("jumping", (byte) jumping);

    compound.setTag(ExtendedPropertyName, attrs);
  }


  @Override
  public void loadNBTData(NBTTagCompound compound) {
    NBTTagCompound attrs = (NBTTagCompound) compound.getTag(ExtendedPropertyName);
    if ( attrs == null ) {
      reset();
      return;
    }

    setArmor(attrs.getByte("armor"));
    setHealth(attrs.getByte("health"));
    setStrength(attrs.getByte("strength"));
    setDefense(attrs.getByte("defense"));
    setFireResist(attrs.getByte("fireResist"));
    setMagicResist(attrs.getByte("magicResist"));
    setMovement(attrs.getByte("movement"));
    setJumpPower(attrs.getByte("jumping"));
  }


  /** static initializer version of loadNBTData */
  public static RPGEAttributes newFromNBTData(NBTTagCompound nbt) {
	RPGEAttributes attrs = new RPGEAttributes();
	attrs.loadNBTData(nbt);
	return attrs;
  }


  @Override
  public void init(Entity entity, World world) {
//	System.out.println("RPGAttributes::init(): " + entity.getPersistentID().toString());
	reset();
  }


  public static final void register(EntityLivingBase entity) {
    entity.registerExtendedProperties(ExtendedPropertyName, new RPGEAttributes());
    //System.out.printf("registering entity %s (KEY=%s)\n", entity.getClass().getName(), entity.registerExtendedProperties(ExtendedPropertyName, new RPGEAttributes()));
  }


  public static final RPGEAttributes get(EntityLivingBase entity) {
    return (RPGEAttributes) entity.getExtendedProperties(ExtendedPropertyName);
  }


  /** add: adds attrs to this attribute instance.  no bounds checking for speed.
   *  must run validate once all arithmetic is complete. */
  public void add(RPGEAttributes attrs) {
    this.armor        += attrs.armor;
	this.health       += attrs.health;
	this.strength     += attrs.strength;
	this.defense      += attrs.defense;
	this.fireResist   += attrs.fireResist;
	this.magicResist  += attrs.magicResist;
	this.movement     += attrs.movement;
	this.jumping      += attrs.jumping;
  }


  /** compile attribute abstraction data upon completion of arithmetic, above */
  public void validate() {
    this.setArmor(armor);
	this.setHealth(health);
	this.setStrength(strength);
	this.setDefense(defense);
	this.setFireResist(fireResist);
	this.setMagicResist(magicResist);
	this.setMovement(movement);
	this.setJumpPower(jumping);
  }


  /** applyModifier: helper function for applyModifiers below */
  private void applyMod(UUID attrId, AttributeModifier mcAttrMod, IAttributeInstance mcAttr, int value) {
    if ( mcAttr.getModifier(attrId) != null )
        mcAttr.removeModifier(mcAttrMod);
      if ( value != 0 )
        mcAttr.applyModifier(mcAttrMod);
  }


  /** apply attributes that can be expressed as minecraft attribute modifiers */
  public void applyModifiers(EntityLivingBase entity) {
    AttributeModifier mcAttrMod;
    IAttributeInstance mcAttr;

//    SharedMonsterAttributes.knockbackResistance;

    // strength is now handled by an event to support an attack power model
    //mcAttrMod = new AttributeModifier(mcAttrStrID, "generic.strength", attackPower, mcAttrAdd).setSaved(false);
    //mcAttr = entity.getEntityAttribute(SharedMonsterAttributes.attackDamage);
    //applyMod(mcAttrStrID, mcAttrMod, mcAttr, strength);

    // health
    mcAttrMod = new AttributeModifier(mcAttrHealthID, "generic.health", (double) health, mcAttrAdd).setSaved(false);
    mcAttr = entity.getEntityAttribute(SharedMonsterAttributes.maxHealth);
    applyMod(mcAttrHealthID, mcAttrMod, mcAttr, health);

    // movement
    mcAttrMod = new AttributeModifier(mcAttrMoveID, "generic.movementSpeed", movePercent, mcAttrMult).setSaved(false);
    mcAttr = entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
    applyMod(mcAttrMoveID, mcAttrMod, mcAttr, movement);
  }


  /* base attributes */

  public int getArmor() {
    return this.armor;
  }


  public int getHealth() {
	return this.health;
  }


  public int getStrength() {
	return this.strength;
  }


  public int getDefense() {
	return this.defense;
  }


  public int getFireResist() {
    return this.fireResist;
  }


  public int getMagicResist() {
    return this.magicResist;
  }


  /** movement abstraction is presented as base attribute */
  public float getMovePercent() {
	return this.movePercent;
  }


  /** jump abstraction is presented as base attribute */
  public float getJumpPercent() {
	return this.jumpPercent;
  }


  /* abstract modifiers */

  public float getAttackPower() {
	return this.attackPower;
  }

  public float getArmorReduction(EntityLivingBase entity) {
	float impact = ((25.0F - entity.getTotalArmorValue() ) / 25.0F) - this.armorReduction;
	if ( impact > 1.0F ) impact = 1.0F;  // full damage
	if ( impact < 0.2F ) impact = 0.2F;    // maximum 80% reduction
	return impact;
  }

  /** defense applies to both melee and projectile damage */
  public float getDefenseMitigation() {
	return this.defenseMitigation;
  }


  public float getFireReduction() {
	return this.fireReduction;
  }


  public float getMagicReduction() {
	return this.magicReduction;
  }


  /* setter functions */


  public RPGEAttributes setArmor(int value) {
    if ( value > 80 ) value = 80;
    if ( value < 0 ) value = 0;
    this.armor = value;
    this.armorReduction = value / 100.0F;
    return this;
  }

  public RPGEAttributes setHealth(int value) {
    if ( value < -99 ) value = -99;
    if ( value > +99 ) value = +99;
    this.health = (byte) value;
    // health attribute does not have an abstraction
    return this;
  }


  public RPGEAttributes setStrength(int value) {
    if ( value < -99 ) value = -99;
    if ( value > +99 ) value = +99;
	this.strength = (byte) value;
	this.attackPower = (float) value / 2;
	return this;
  }


  public RPGEAttributes setDefense(int value) {
    if ( value < -99 ) value = -99;
    if ( value > +99 ) value = +99;
    this.defense = (byte) value;
    this.defenseMitigation = (float) value / 2;
    return this;
  }


  public RPGEAttributes setFireResist(int value) {
    if ( value < -99 ) value = -99;
    if ( value > +99 ) value = +99;
    this.fireResist = (byte) value;
    this.fireReduction = (float) value / 2;
    return this;
  }


  public RPGEAttributes setMagicResist(int value) {
    if ( value < -99 ) value = -99;
    if ( value > +99 ) value = +99;
    this.magicResist = (byte) value;
    this.magicReduction = (float) value / 2;
    return this;
  }


  public RPGEAttributes setMovement(int value) {
    if ( value < -80 ) value = -80;
	if ( value > +25 ) value = +25;
	this.movement = (byte) value;
	this.movePercent = (float) value / 100;
    return this;
  }


  public RPGEAttributes setJumpPower(int value) {
    if ( value < -99 ) value = -99;
    if ( value > +99 ) value = +99;
    this.jumping = (byte) value;
    this.jumpPercent = (float) value * 0.02F;
    return this;
  }


  /** helper function for addInformation */
  private static void addValue(List<String> list, String attrDesc, int value) {
    if ( value != 0 )
      list.add(String.format("%-11s %+5d", LanguageRegistry.instance().getStringLocalization(attrDesc), value));
  }


  /** helper function for addInformation */
  private static void addPercent(List<String> list, String attrDesc, float value) {
    if ( value != 0.0F )
      list.add(String.format("%-11s %+4.0f%%", LanguageRegistry.instance().getStringLocalization(attrDesc), value*100));
  }

  private static void addSockets(List<String> list, int numSockets) {
    if ( numSockets == 0 ) return;
    if ( numSockets == 1 )
      list.add(String.format("§o%d %s", numSockets, LanguageRegistry.instance().getStringLocalization("attr.socket_singular.desc")));
    else
      list.add(String.format("§o%d %s", numSockets, LanguageRegistry.instance().getStringLocalization("attr.socket_plural.desc")));
  }

  /** add stat information to an ItemStack Tooltip **/
  public void generateTooltip(ItemStack stack, List list) {
    
    List<String> l = list;

    addValue(l, "attr.armor.desc", getArmor());
    addValue(l, "attr.health.desc", getHealth());
    addValue(l, "attr.strength.desc", getStrength());
    addValue(l, "attr.defense.desc", getDefense());
    addValue(l, "attr.fireresist.desc", getFireResist());
    addValue(l, "attr.magicresist.desc", getMagicResist());
    addPercent(l, "attr.movement.desc", getMovePercent());
    addPercent(l, "attr.jumping.desc", getJumpPercent());

    if ( stack != null )
      addSockets(l, EquipSocket.getNumSockets(stack));
  }



}
