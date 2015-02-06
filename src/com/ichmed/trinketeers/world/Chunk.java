package com.ichmed.trinketeers.world;

import java.util.HashMap;

public class Chunk
{
	public static HashMap<String, Chunk> chunks = new HashMap<>();

	public static final int chunkSize = 16;
	public static final int clusterSize = 4;
	public int[] tiles = new int[chunkSize * chunkSize];

	public static Chunk createNewChunk(int x, int y, int z)
	{
		Chunk c = new Chunk();
		c.populate();
		chunks.put(getHashString(x, y, z), c);
		return c;
	}

	public void populate()
	{
	}

	public static String getHashString(int x, int y, int z)
	{
		return x + "x" + y + "x" + z;
	}

	public static int getTile(int x, int y)
	{
		int chunkX = x / chunkSize;
		int chunkY = y / chunkSize;

		if (x < 0) chunkX--;
		if (y < 0) chunkY--;

		if (chunks.get(getHashString(chunkX, chunkY, 0)) == null) createNewChunk(chunkX, chunkY, 0);

		int xTemp = x % chunkSize;
		if (xTemp < 0) xTemp += chunkSize;
		int yTemp = y % chunkSize;
		if (yTemp < 0) yTemp += chunkSize;
		return chunks.get(getHashString(chunkX, chunkY, 0)).getTileFromChunk(xTemp, yTemp);
	}

	private int getTileFromChunk(int x, int y)
	{
		return tiles[chunkSize * y + x];
	}

	public static Chunk getChunk(int x, int y, int z)
	{
		Chunk c = chunks.get(getHashString(x, y, z));
		if (c == null) return createNewChunk(x, y, z);
		return c;
	}

	public void unloadCluster(int x, int y, int z)
	{
		for (int i = 0; i < clusterSize; i++)
			for (int j = 0; j < clusterSize; j++)
				for (int k = 0; k < clusterSize; k++)
				{
					chunks.put(getHashString(i + x * clusterSize, j + y * clusterSize, k + z * clusterSize), null);
				}
	}
}
