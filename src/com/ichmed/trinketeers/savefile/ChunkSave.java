package com.ichmed.trinketeers.savefile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.world.Chunk;
import com.ichmed.trinketeers.world.World;

import static com.ichmed.trinketeers.world.Chunk.*;

public class ChunkSave
{
	public static boolean isChunkOnDisk(World w, int x, int y, int z)
	{
		File f = new File("resc/data/world/" + w.name + "/" + x + "x" + y + "x" + z + ".ccd");
		return f.exists();		
	}
	
	public static void saveChunkClusterToDisk(World w, int x, int y, int z)
	{
		File f = new File("resc/data/world/" + w.name + "/" + x + "x" + y + "x" + z + ".ccd");
		System.out.println("starting " + f.getAbsolutePath());
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
					Chunk c = getChunk(w, i + (x * clusterSize), j + (y * clusterSize), k + (z * clusterSize), false);
					if(c == null) continue;
					data.append("[" + i + "x" + j + "x" + k + "{");
					for (int l = 0; l < chunkSize * chunkSize; l++)
					{
						data.append(c.tiles[l]);
						if (l < chunkSize * chunkSize - 1) data.append(",");
					}
					data.append("}{");
					for (Entity e : c.entities)
					{
//						System.out.println(e.getSaveData());
						data.append(e.getSaveData());
						data.append(",");
					}
					data.append("}");
					data.append("]");
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
		System.out.println("finished " + f.getAbsolutePath());
	}
}
