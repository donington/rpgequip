package mods.minecraft.donington.blocky3d.forge;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import mods.minecraft.donington.blocky3d.region.B3DRegion;
import mods.minecraft.donington.blocky3d.region.B3DRegionCache;
import mods.minecraft.donington.blocky3d.region.B3DRegionListener;
import mods.minecraft.donington.blocky3d.region.B3DRegionSelectorRenderer;
import mods.minecraft.donington.blocky3d.region.B3DRegionSet;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;

public class B3DProtectionHandler {
  private B3DRegionCache regionCache;


  public B3DProtectionHandler(B3DRegionCache cache) {
	  regionCache = cache;
  }


  public void reset() {
    regionCache.clear();

    B3DRegion regionMap = new B3DRegion();
    regionMap.addPointToRegion(1, 1, 1);
    regionMap.addPointToRegion(-1, 128, -1);
    regionMap.setFlags(B3DRegion.FLAG_BLOCK_PROTECT, true);
    B3DRegionSet regionSet = new B3DRegionSet();
    regionSet.addRegion(regionMap);

    regionCache.add("testregion", 0, regionSet);
  }


  @SubscribeEvent
  public void blockBreakEvent(BlockEvent.BreakEvent event) {
	int dimension = event.world.provider.dimensionId;
	int cacheX = event.x / 64;
	int cacheZ = event.z / 64;
	int cacheXZ = cacheX + (cacheZ<<16);

	for ( B3DRegionSet set : regionCache.lookup(dimension, cacheXZ) ) {
      for ( B3DRegion map : set.iterator() ) {
	    if ( map.contains(event.x, event.y, event.z) &&
	    	 map.hasFlags(B3DRegion.FLAG_BLOCK_PROTECT) ) {
		  event.setCanceled(true);
		  return;
        }
	  }
	}
  }


  @SubscribeEvent
  public void onChunkLoad(ChunkEvent.Load event) {
	if ( event.world.isRemote ) return;
	int dimension = event.world.provider.dimensionId;
	B3DRegionListener cache = regionCache.cacheListener.get(dimension);
	if ( cache == null ) {
      cache = new B3DRegionListener();
      regionCache.cacheListener.put(dimension, cache);
	}

	Chunk chunk = event.getChunk();
	int cacheX = (chunk.xPosition / 4);
	int cacheZ = (chunk.zPosition / 4);
	int cacheXZ = cacheX + ( cacheZ << 16 );

	int count = cache.get(cacheXZ);
	count++;
	cache.put(cacheXZ, count);


	int size = cache.size();
	//if ( cache.lastSize() != size ) {
	  //System.out.printf("Load Cache(%d)[%d]: ", dimension, size);
	  //for ( Integer cv : cache.keySet() )
	  //  System.out.printf("%d(%d) ", cv, cache.get(cv));
	  //System.out.println();
	//}
  }


  @SubscribeEvent
  public void onChunkUnload(ChunkEvent.Unload event) {
	if ( event.world.isRemote ) return;
	int dimension = event.world.provider.dimensionId;
	B3DRegionListener cache = regionCache.cacheListener.get(dimension);
	if ( cache == null ) return;

	Chunk chunk = event.getChunk();
	int cacheX = (chunk.xPosition / 4);
	int cacheZ = (chunk.zPosition / 4);
	int cacheXZ = cacheX + ( cacheZ << 16 );

	int count = cache.get(cacheXZ);
	if ( count < 1 ) {
		//System.out.printf("Cache(%d)[%d]: %d: ignoring underflow\n", dimension, cache.size(), cacheXZ);
		return;
	}

	count--;
	if ( count < 1 )
		cache.remove(cacheXZ);
	else
		cache.put(cacheXZ, count);


	int size = cache.size();
	if ( cache.lastSize() != size ) {
	  //System.out.printf("Unload Cache(%d)[%d]: %d\n", dimension, size, cacheXZ);
	}
  }


  /* these will be the server to client packet handlers
  @SubscribeEvent
  public void chunkWatchEvent(ChunkWatchEvent.Watch event) {
  }

  @SubscribeEvent
  public void chunkUnwatchEvent(ChunkWatchEvent.UnWatch event) {
  }
  */
  
}
