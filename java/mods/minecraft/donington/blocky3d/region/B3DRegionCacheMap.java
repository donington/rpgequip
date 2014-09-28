package mods.minecraft.donington.blocky3d.region;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class B3DRegionCacheMap {
  private Map<Integer,Set<String>> cache;


  public B3DRegionCacheMap() {
	cache = new HashMap();
  }


  public void clear() {
	cache.clear(); 
  }


  public void insert(int cacheXZ, String name) {
	Set<String> list = cache.get(cacheXZ);
	if ( list == null ) {
      list = new HashSet();
      cache.put(cacheXZ, list);
	}
	list.add(name);
  }


  public void drop(int cacheXZ, String name) {
	Set<String> list = cache.get(cacheXZ);
	if ( list == null ) return;
    list.remove(name);
  }


  public Set<String> lookup(int cacheXZ) {
	return cache.get(cacheXZ);
  }


public void purge(String name) {
	for ( Set<String> map : cache.values() )
		map.remove(name);
}


/*
  public boolean contains(String name) {
	for ( Set<String> list : cache.values() )
	  if ( list.contains(name) )
	    return true;
	return false;
  }
 */

}
