package com.ichmed.trinketeers.savefile;

import static com.ichmed.trinketeers.world.Chunk.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import com.ichmed.trinketeers.Game;
import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.world.Chunk;
import com.ichmed.trinketeers.world.World;

public class ChunkSave
{
	public static boolean isClusterOnDisk(World w, int x, int y, int z)
	{
		File f = new File("resc/data/world/" + w.name + "/" + x + "x" + y + "x" + z + ".ccd");
		return f.exists();
	}

	public static void saveChunkClusterToDisk(World w, int x, int y, int z)
	{
		File f = new File("resc/data/world/" + w.name + "/" + x + "x" + y + "x" + z + ".ccd");
//		System.out.println("starting " + f.getAbsolutePath());

		try
		{
			f.createNewFile();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		StringBuilder data = new StringBuilder();

		for (int i = 0; i < clusterSize; i++)
			for (int j = 0; j < clusterSize; j++)
				for (int k = 0; k < clusterSize; k++)
				{
					Chunk c = getChunk(w, i + (x * clusterSize), j + (y * clusterSize), k + (z * clusterSize), false, false);
					if (c == null)
					{
						System.err.println("Chunk was null");
						for (StackTraceElement e : Thread.currentThread().getStackTrace())
							System.err.println(e.toString());
						continue;
					}
					data.append("[" + (i + (x * clusterSize)) + "x" + (j + (y * clusterSize)) + "x" + (k + (z * clusterSize)) + "{");
					for (int l = 0; l < chunkSize * chunkSize; l++)
					{
						data.append(c.tiles[l]);
						if (l < chunkSize * chunkSize - 1) data.append(",");
					}
					data.append("}{");
					for (Entity e : c.entities)
					{
						// System.out.println(e.getSaveData());
						data.append(e.getSaveData());
						data.append(",");
					}
					data.append("}");
					data.append("]");
					data.append("\n");
				}
		try
		{
			FileWriter fw = new FileWriter(f);
			fw.append(data);
			fw.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		// System.out.println("finished " + f.getAbsolutePath());
	}

	@SuppressWarnings("resource")
	public static void loadCluster(World world, int posX, int posY, int posZ)
	{
		try
		{
			File f = new File("resc/data/world/" + world.name + "/" + posX + "x" + posY + "x" + posZ + ".ccd");
			String content = (new Scanner(f)).useDelimiter("\\Z").next();

			if (Game.debugMode)
			{
//				 System.out.println("loaded cluster " + f);
//				 System.out.println("Cluster Data:\n" + content);
//				 for (StackTraceElement e :
//				 Thread.currentThread().getStackTrace())
//				 System.out.println(e);
			}

			int clusterIndexB = 0;
			String clusterData = null;
			while (clusterData != "")
			{
				int clusterIndexA = content.indexOf('[', clusterIndexB);
				clusterIndexB = content.indexOf(']', clusterIndexA);

				if (clusterIndexA >= 0 && clusterIndexB >= 0)
				{
					clusterData = content.substring(clusterIndexA + 1, clusterIndexB);
				} else break;

				int cdIndex = clusterData.indexOf('{', 0);

				String posData = clusterData.substring(0, cdIndex);

				int x = Integer.valueOf(posData.split("x")[0]);
				int y = Integer.valueOf(posData.split("x")[1]);
				int z = Integer.valueOf(posData.split("x")[2]);

				String tileData = clusterData.substring(clusterData.indexOf('{', cdIndex) + 1, clusterData.indexOf('}', cdIndex));
				cdIndex = clusterData.indexOf('{', cdIndex);

				Chunk c = new Chunk(x, y, z);
				String tiles[] = tileData.split(",");
				for (int i = 0; i < tiles.length; i++)
					c.tiles[i] = Integer.valueOf(tiles[i]);

				Chunk.chunks.put(Chunk.getHashString(x, y, z), c);
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}
}
