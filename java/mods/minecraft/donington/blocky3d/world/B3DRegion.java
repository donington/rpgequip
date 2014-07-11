package mods.minecraft.donington.blocky3d.world;

import mods.minecraft.donington.blocky3d.helper.B3DRegionCache;

public class B3DRegion {
  public int minX, minY, minZ;
  public int maxX, maxY, maxZ;

  private float[] rgb = { 0.5F, 0.0F, 0.0F };
  private boolean regionValid;


  public B3DRegion() {
	regionValid = false;
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

    if ( ( x > minX ) && ( x < maxX ) ) {
      min = minX + x;
      max = maxX - x;
      if      ( min > max )  maxX -= x;
      else if ( min < max )  minX += x;
    }

    if ( ( y > minY ) && ( y < maxY ) ) {
      min = minY + y;
      max = maxY - y;
      if      ( min > max )  maxY -= y;
      else if ( min < max )  minY += y;
    }

    if ( ( z > minZ ) && ( z < maxZ ) ) {
      min = minZ + z;
      max = maxZ - z;
      if      ( min > max )  maxZ -= z;
      else if ( min < max )  minZ += z;
    }
  }


  public boolean isValid() {
    return regionValid;
  }


  public void invalidate() {
    regionValid = false;
  }
}
