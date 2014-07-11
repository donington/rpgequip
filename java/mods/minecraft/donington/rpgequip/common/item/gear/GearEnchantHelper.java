package mods.minecraft.donington.rpgequip.common.item.gear;

import java.util.Random;

import net.minecraft.item.ItemStack;
import mods.minecraft.donington.rpgequip.common.RPGEAttributes;
import mods.minecraft.donington.rpgequip.common.item.ItemGear;
import mods.minecraft.donington.rpgequip.common.item.ItemRing;


public abstract class GearEnchantHelper {
  private static Random random = new Random();


  /** pick a random integer */
  public static int rngInt(int min, int max) {
    return random.nextInt((max - min) + 1) + min;
  }


  /** pick a random float */
  public static float rngFloat(float min, float max) {
	return random.nextFloat() * (max - min) + min;
  }


  /** enchanting function with setable minimum */
  public static int rngEnchant(int min, int max) {
    // force max for testing
    //return max;
    return random.nextInt((max - min) + 1) + min;
  }


  /** minimum possible level is half maximum */
  public static int rngEnchant(int max) {
    int min = max / 2;
    return rngEnchant(min, max);
  }


  /** rollSockets: returns up to max sockets
   * with chance percent per socket increasing by decay
   * @return a number of sockets no greater than max (or EquipSocket.getMaxSockets())
   */
  public static int rollSockets(int max, int chance, int decay) {
    int n = EquipSocket.getMaxSockets();
    if ( max > n ) max = n;
    n = 0;
    while ( n < max ) {
      if ( rngInt(1,100) < chance )
        return n;
      n++;
      chance += decay;
    }
    return n;
  }


  /** Maybe TODO: add hooks for unique items in a config */
  public static RPGEAttributes rollEquipAttributes(ItemStack stack, ItemGear gear) {

    switch ( gear.getEquipType() ) {
      case Helmet:
      case Chest:
      case Pants:
      case Boots:
        break;
      case Belt:
        EquipSocket.addSockets(rollSockets(3, 60, 10), stack);
        return rollBeltAttributes(gear);
      case Ring:
        EquipSocket.addSockets(rollSockets(1, 1, 0), stack);
        return rollRingAttributes((ItemRing) gear);
      case RESERVED:
        break;
      case Amulet:
        EquipSocket.addSockets(rollSockets(1, 99, 0), stack);
        return rollAmuletAttributes((ItemAmulet) gear);
      case Bracer:
        EquipSocket.addSockets(rollSockets(2, 65, 15), stack);
        return rollBracerAttributes(gear);
      case Talisman:
        break;
      default:
    }
    return null;
  }


  private static RPGEAttributes rollBeltAttributes(ItemGear gear) {
    RPGEAttributes attrs = new RPGEAttributes();

    attrs.setArmor(gear.getArmorValue());

    return attrs;
  }


  private static RPGEAttributes rollRingAttributes(ItemRing ring) {
    RPGEAttributes attrs = new RPGEAttributes();
    int rng;

	switch ( ring.getEquipMaterial() ) {
    case IRON:
      attrs.setDefense(1);
      break;
    case GOLD:
      rng = rngInt(1,2);
      if ( rng == 1 ) attrs.setFireResist(1);
      if ( rng == 2 ) attrs.setMagicResist(1);
      break;
    default:
      return null;
  }

  EquipSocket gem = ring.getGem();

  if ( gem != null ) switch ( gem ) {
    case DIAMOND:
      attrs.setMovement(rngEnchant(5));
      break;
    case EMERALD:
      attrs.setJumpPower(rngEnchant(20));
      break;
    case LAPIS:
      break;
    case QUARTZ:
      attrs.setHealth(rngEnchant(1,3));
      break;
    case REDSTONE:
      attrs.setStrength(1);
      break;
    case OBSIDIAN:
      attrs.setDefense(attrs.getDefense()+1);
      break;
    default:
	}

    return attrs;
  }


  private static RPGEAttributes rollAmuletAttributes(ItemAmulet amulet) {
    RPGEAttributes attrs = new RPGEAttributes();
    int rng;

    switch ( amulet.getGem() ) {
      case DIAMOND:
        attrs.setMovement(rngEnchant(10));
        break;
      case EMERALD:
        attrs.setJumpPower(rngEnchant(45));
        break;
      case LAPIS:
    	//attrs.setMovement(3);
    	//attrs.setArmor(99);
    	//attrs.setHealth(99);
    	//attrs.setStrength(99);
    	//attrs.setDefense(99);
    	//attrs.setFireResist(99);
    	//attrs.setMagicResist(99);
    	//attrs.setMovement(99);
    	//attrs.setJumpPower(99);
        break;
      case QUARTZ:
  	    attrs.setHealth(rngEnchant(5));
  	    rng = rngInt(1,2);
  	    if ( rng == 1 ) attrs.setMovement(rngEnchant(3));
  	    if ( rng == 2 ) attrs.setJumpPower(rngEnchant(10));
  	    break;
      case REDSTONE:
        attrs.setStrength(rngEnchant(3));
  	    rng = rngInt(1,2);
  	    if ( rng == 1 ) attrs.setMovement(rngEnchant(2));
  	    if ( rng == 2 ) attrs.setJumpPower(rngEnchant(5));
  	    break;
      case OBSIDIAN:
        attrs.setDefense(rngEnchant(3));
  	    rng = rngInt(1,2);
  	    if ( rng == 1 ) attrs.setMovement(rngEnchant(2));
  	    if ( rng == 2 ) attrs.setJumpPower(rngEnchant(5));
  	    break;
      default:
    }
    return attrs;
  }


  private static RPGEAttributes rollBracerAttributes(ItemGear gear) {
    RPGEAttributes attrs = new RPGEAttributes();
    int rng;
    int level = gear.getEquipMaterial().getEnchantability();

    rng = rngInt(1,10);
    if ( rng > 3 ) attrs.setHealth(rngEnchant(level/4));

    switch ( rngInt(1,2) ) {
      case 1:
        if ( rng > 3 )  attrs.setStrength(rngEnchant(level/8));
        else            attrs.setStrength(rngEnchant(level/5));
        break;
      case 2:
        if ( rng > 3 )  attrs.setDefense(rngEnchant(level/8));
        else            attrs.setDefense(rngEnchant(level/5));
        break;
    }
    return attrs;
  }

}
