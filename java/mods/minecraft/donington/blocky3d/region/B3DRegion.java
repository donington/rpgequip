package mods.minecraft.donington.blocky3d.region;

import java.util.Set;


public class B3DRegion {
  public static final long FLAGS_OFF = 0x0;
  public static final long FLAG_BLOCK_PROTECT = 0x1;
  private long flags;

  public int minX, minY, minZ;
  public int maxX, maxY, maxZ;

  // custom region color?
//  private float[] rgb = { 0.5F, 0.0F, 0.0F };
  private boolean regionValid;


  public B3DRegion() {
	regionValid = false;
	flags = FLAGS_OFF;
  }


  public B3DRegion(int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
	  this.minX = minX;
	  this.maxX = maxX;
	  this.minY = minY;
	  this.maxY = maxY;
	  this.minZ = minZ;
	  this.maxZ = maxZ;
	  regionValid = true;
}


public void addPointToRegion(int x, int y, int z) {
    if ( regionValid == false ) {
      minX = x;
      maxX = x+1;
      minY = y;
      maxY = y+1;
      minZ = z;
      maxZ = z+1;
      regionValid = true;
      return;
    }

    if ( x < minX ) minX = x;
    if ( x >= maxX ) maxX = x+1;
    if ( y < minY ) minY = y;
    if ( y >= maxY ) maxY = y+1;
    if ( z < minZ ) minZ = z;
    if ( z >= maxZ ) maxZ = z+1;
  }


  public void removePointFromRegion(int x, int y, int z) {
    if ( regionValid == false ) return;
    int min, max;

    if ( ( x >= minX ) && ( x < maxX ) ) {
      min = 1 + x - minX;
      max = maxX - x;
      if      ( min > max )  maxX -= max;
      else if ( min <= max )  minX += min;
//      if ( minX == maxX ) minX--;
    }

    if ( ( y >= minY ) && ( y < maxY ) ) {
        min = 1 + y - minY;
        max = maxY - y;
        if      ( min > max )  maxY -= max;
        else if ( min <= max )  minY += min;
//        if ( minY == maxY ) minY--;
      }

    if ( ( z >= minZ ) && ( z < maxZ ) ) {
        min = 1 + z - minZ;
        max = maxZ - z;
        if      ( min > max )  maxZ -= max;
        else if ( min <= max )  minZ += min;
//        if ( minZ == maxZ ) minZ--;
      }

  }


  public int[] getClosestCorner(double playerX, double playerY, double playerZ) {
	if ( this.regionValid == false ) return null;

	int axis, min, max;
	int[] retval = new int[3];

	axis = (int) Math.floor(playerX);
    min = 1 + axis - minX;
    max = maxX - axis;
    if      ( min > max )   retval[0] = maxX-1;
    else 					retval[0] = minX;

	axis = (int) Math.floor(playerY) -1;
    min = 1 + axis - minY;
    max = maxY - axis;
    if      ( min > max )   retval[1] = maxY-1;
    else 					retval[1] = minY;

    axis = (int) Math.floor(playerZ);
    min = 1 + axis - minZ;
    max = maxZ - axis;
    if      ( min > max )   retval[2] = maxZ-1;
    else 					retval[2] = minZ;

    return retval;

    /*
    min = 1 + x - minX;
    max = maxX - x;
    if      ( min > max )  maxX -= max;
    else if ( min <= max )  minX += min;
     */
  }


  public boolean isValid() {
    return regionValid;
  }


  public void invalidate() {
    regionValid = false;
  }


  /** check if a point is within the region **/
  public boolean contains(int x, int y, int z) {
	if ( x >= minX && x < maxX &&
		 y >= minY && y < maxY &&
		 z >= minZ && z < maxZ )
      return true;
	return false;
  }


  public boolean hasFlags(long mask) {
	return ( flags & mask ) != 0;
  }


  public void setFlags(long mask, boolean value) {
    if ( value == true )
      flags |= mask;
    else
      flags &= ~mask;

    //System.out.println("flags := " + flags);
  }


  public void getEffectedCacheXZ(Set<Integer> cacheXZ) {
    if ( regionValid == false ) return;

	// cache is world coordinate divided by 16 to convert to chunk coordinates
	// then divided by 4 to get cache coordinates
	int cacheMinX = minX / 64;
	int cacheMinZ = minZ / 64;
	int cacheMaxX = maxX / 64;
	int cacheMaxZ = maxZ / 64;

    for ( int x = cacheMinX;  x <= cacheMaxX;  x++ )
      for ( int z = cacheMinZ;  z <= cacheMaxZ;  z++ )
    	cacheXZ.add(x + (z<<16));
    
	return;
  }
}
