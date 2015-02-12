package com.ichmed.trinketeers.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import com.ichmed.trinketeers.ai.Behaviour;
import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.savefile.ChunkSave;
import com.ichmed.trinketeers.world.generator.WorldGenerator;

public class Chunk
{
	public static HashMap<String, Chunk> chunks = new HashMap<>();
	public List<Entity> entities = new ArrayList<Entity>();

	public static final int chunkSize = 8;
	public static final int clusterSize = 2;
	public int[] tiles = new int[chunkSize * chunkSize];

	public int posX, posY, posZ;

	public static Vector3f getClusterForPoint(World w, Vector3f point)
	{
		int intXTemp = (int) point.x;
		int intYTemp = (int) point.y;
		if (point.x < 0) intXTemp--;
		if (point.y < 0) intYTemp--;

		return getClusterForChunk(w, intXTemp, intYTemp, (int) point.z);
	}

	public static Vector3f getClusterForChunk(World w, Chunk c)
	{
		return getClusterForChunk(w, c.posX, c.posY, c.posZ);
	}

	public static Vector3f getClusterForChunk(World w, int x, int y, int z)
	{
		if (x < 0) x--;
		if (y < 0) y--;
		if (z < 0) z--;

		int intXFin = x / clusterSize;
		int intYFin = y / clusterSize;
		int intZFin = z / clusterSize;

		return new Vector3f(intXFin, intYFin, intZFin);
	}

	public static boolean isClusterAdjacent(World w, Vector3f v)
	{
		boolean b = (v.x >= w.currentCluster.x - 1 && v.x <= w.currentCluster.x + 1 && v.y >= w.currentCluster.y - 1 && v.y <= w.currentCluster.y + 1 && v.z == w.currentCluster.z)
				|| v.z == w.currentCluster.z + 1 || v.z == w.currentCluster.z - 1;
		return b;
	}

	public Chunk(int x, int y, int z)
	{
		this.posX = x;
		this.posY = y;
		this.posZ = z;
	}

	public static Chunk createNewChunk(World world, int x, int y, int z)
	{
		return createNewChunk(world, x, y, z, true);
	}

	public static Chunk createNewChunk(World world, int x, int y, int z, boolean generate)
	{
		Chunk c = new Chunk(x, y, z);
		chunks.put(getHashString(x, y, z), c);
		c.populate();
		if (generate)
		{
			WorldGenerator.generateChunk(world, x, y, z);
		}
		return c;
	}

	public void populate()
	{
		if (this.posZ == 0) for (int i = 0; i < chunkSize * chunkSize; i++)
			tiles[i] = 4;
		else for (int i = 0; i < chunkSize * chunkSize; i++)
			tiles[i] = 2;
	}

	public static String getHashString(int x, int y, int z)
	{
		return x + "x" + y + "x" + z;
	}

	public static void setTile(World w, int x, int y, int z, int id)
	{

		int chunkX = x / chunkSize;
		int chunkY = y / chunkSize;

		if (x < 0) chunkX--;
		if (y < 0) chunkY--;

		Chunk c = getChunk(w, chunkX, chunkY, z);
		int xTemp = x % chunkSize;
		if (xTemp < 0) xTemp += chunkSize;
		int yTemp = y % chunkSize;
		if (yTemp < 0) yTemp += chunkSize;
		c.setTileInChunk(xTemp, yTemp, id);
	}

	private void setTileInChunk(int x, int y, int id)
	{
		this.tiles[chunkSize * y + x] = id;
	}

	public void onUnload(World w)
	{
		for (Entity e : this.entities)
		{
			while (w.worldGraphics.contains(e))
				w.worldGraphics.remove(e);
			for (Behaviour b : e.behaviours)
				b.cleanUp(w);
		}
	}

	public static int getTile(World w, int x, int y, int z)
	{
		int chunkX = x / chunkSize;
		int chunkY = y / chunkSize;

		if (x < 0) chunkX--;
		if (y < 0) chunkY--;

		int xTemp = x % chunkSize;
		if (xTemp < 0) xTemp += chunkSize;
		int yTemp = y % chunkSize;
		if (yTemp < 0) yTemp += chunkSize;
		return getChunk(w, chunkX, chunkY, z).getTileFromChunk(xTemp, yTemp);
	}

	private int getTileFromChunk(int x, int y)
	{
		return this.tiles[chunkSize * y + x];
	}

	public static Chunk getChunk(World w, Vector3f p)
	{
		int x = (int) p.x;
		int y = (int) p.y;
		if (p.x < 0) x--;
		if (p.y < 0) y--;
		return getChunk(w, x, y, (int) p.z);
	}

	public static Chunk getChunk(World world, int x, int y, int z)
	{
		return getChunk(world, x, y, z, true, true);
	}

	public static Chunk getChunk(World world, int x, int y, int z, boolean generate, boolean loadFromDisk)
	{
		Chunk c = chunks.get(getHashString(x, y, z));
		if (c == null)
		{
			Vector3f cluster = getClusterForChunk(world, x, y, z);
			if (loadFromDisk && ChunkSave.isClusterOnDisk(world, (int) cluster.x, (int) cluster.y, (int) cluster.z))
			{
				ChunkSave.loadCluster(world, (int) cluster.x, (int) cluster.y, (int) cluster.z);
			}
			c = chunks.get(getHashString(x, y, z));
			return c == null ? createNewChunk(world, x, y, z, generate) : c;
		}
		return c;
	}

	public static void unloadCluster(World w, int x, int y, int z)
	{
		ChunkSave.saveChunkClusterToDisk(w, x, y, z);
		for (int i = 0; i < clusterSize; i++)
			for (int j = 0; j < clusterSize; j++)
				for (int k = 0; k < clusterSize; k++)
				{
					chunks.put(getHashString(i + (x * clusterSize), j + (y * clusterSize), k + (z * clusterSize)), null);
				}
	}

	public static List<Entity> getAllLoadedEntitiesForLayer(int layer)
	{
		ArrayList<Entity> ret = new ArrayList<>();
		for (String s : chunks.keySet())
			if (chunks.get(s) != null) for (Entity e : chunks.get(s).entities)
				if (!ret.contains(e) && e.position.z == layer) ret.add(e);
		return ret;
	}

	public static List<Entity> getAllLoadedEntities()
	{
		ArrayList<Entity> ret = new ArrayList<>();
		for (String s : chunks.keySet())
			if (chunks.get(s) != null)
			{
				List<Entity> l = chunks.get(s).entities;
				for (Entity e : l)
					if (!ret.contains(e)) ret.add(e);
			}
		return ret;
	}

	public static void unloadUnneededChunks(World w)
	{
		List<Vector3f> l = new ArrayList<>();
		for (String s : chunks.keySet())
		{
			Chunk c = chunks.get(s);
			if (c != null && !isClusterAdjacent(w, getClusterForChunk(w, c)))
			{
				Vector3f v = getClusterForChunk(w, c);
				if (!l.contains(v)) l.add(v);
			}
		}

		for (Vector3f v : l)
			unloadCluster(w, (int) v.x, (int) v.y, (int) v.z);
	}

	public static void saveAllChunks(World w)
	{
		List<Vector3f> l = new ArrayList<>();
		for (String s : chunks.keySet())
		{
			Chunk c = chunks.get(s);
			Vector3f v = getClusterForChunk(w, c);
			if (!l.contains(v)) l.add(v);
		}

		for (Vector3f v : l)
			ChunkSave.saveChunkClusterToDisk(w, (int) v.x, (int) v.y, (int) v.z);
	}
}
