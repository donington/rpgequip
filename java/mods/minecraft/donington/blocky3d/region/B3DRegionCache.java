package mods.minecraft.donington.blocky3d.region;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;


public class B3DRegionCache {
  // need better selection editing, make B3DRegionSelector 
  private static Map<EntityPlayer,B3DRegionSelector> playerSelection;

  public static Map<Integer,B3DRegionCacheMap> cacheMap;
  public static Map<Integer,B3DRegionList> cacheList;
  public static Map<Integer,B3DRegionListener> cacheListener; 


  public B3DRegionCache() {
	playerSelection = new HashMap();
	cacheMap = new HashMap();
    cacheList = new HashMap();
    cacheListener = new HashMap();
  }


  public void clear() {
	playerSelection.clear();
	cacheMap.clear();
	cacheList.clear();
	cacheListener.clear();
  }


  public static B3DRegionSelector getSelector(EntityPlayer player) {
	B3DRegionSelector select = playerSelection.get(player);
	if ( select == null ) {
		select = new B3DRegionSelector(player.dimension);
		playerSelection.put(player, select);
		return select;
	}
    select.validateDimension(player.dimension);
	return select;
  }


  /* just use getSelector and selectSet*
  public static void clearSelector(EntityPlayer player) {
	B3DRegionSelector select = playerSelection.get(player);
	if ( select == null ) return;
	select.clear(player.dimension);
  }
   */


  public static boolean contains(String name, int dimension) {
	B3DRegionList list = cacheList.get(dimension);
	if ( list == null ) return false;
	return list.keySet().contains(name);
  }


  public static void add(String name, int dimension, B3DRegionSet data) {
	B3DRegionList list = cacheList.get(dimension);
	if ( list == null ) {
	  list = new B3DRegionList();
	  cacheList.put(dimension, list);
	}
    B3DRegionCacheMap map = cacheMap.get(dimension);
    if ( map == null ) {
      map = new B3DRegionCacheMap();
      cacheMap.put(dimension, map);
    }

	list.put(name, data);

	Set<Integer> cacheXZ = new HashSet();
	for ( B3DRegion regionMap : data.iterator() ) {
	  regionMap.getEffectedCacheXZ(cacheXZ);
	}
	for ( Integer c : cacheXZ ) {
	  map.insert(c, name);
	}
  }


  public static void remove(String name, int dimension) {
	  B3DRegionList list = cacheList.get(dimension);
	  if ( list == null ) return;
	  B3DRegionCacheMap map = cacheMap.get(dimension);
	  if ( map == null ) return;

	  list.remove(name);
	  map.purge(name);
  }


  public Set<B3DRegionSet> lookup(int dimension, int cacheXZ) {
    B3DRegionList list = cacheList.get(dimension);
    if ( list == null ) return null;
    B3DRegionCacheMap map = cacheMap.get(dimension);
    if ( map == null ) return null;

    Set<B3DRegionSet> set = new HashSet();
    Set<String> names = map.lookup(cacheXZ);
    if ( names == null ) return set;

    for ( String name : names ) {
      set.add(list.get(name));
    }

    return set;
  }
}
