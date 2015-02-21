package com.ichmed.trinketeers.savefile;

import static com.ichmed.trinketeers.world.Chunk.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ichmed.trinketeers.Game;
import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.util.JSONUtil;
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
		// System.out.println("starting " + f.getAbsolutePath());

		JSONObject jso = new JSONObject();
		JSONArray jsa = new JSONArray();

		try
		{
			f.createNewFile();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		try
		{
			for (int i = 0; i < clusterSize; i++)
				for (int j = 0; j < clusterSize; j++)
					for (int k = 0; k < clusterSize; k++)
					{
						StringBuilder tiles = new StringBuilder();
						JSONObject chunk = new JSONObject();
						Chunk c = getChunk(w, i + (x * clusterSize), j + (y * clusterSize), k + (z * clusterSize), false, false);
						if (c == null)
						{
							System.err.println("Chunk was null");
							for (StackTraceElement e : Thread.currentThread().getStackTrace())
								System.err.println(e.toString());
							continue;
						}
						for (int l = 0; l < chunkSize * chunkSize; l++)
						{
							tiles.append(c.tiles[l]);
							if (l < chunkSize * chunkSize - 1) tiles.append(",");
						}
						JSONArray ent = new JSONArray();
						for (Entity e : c.entities)
							ent.put(e.getSaveData());
						chunk.put("tiles", tiles);
						chunk.put("entities", ent);
						chunk.put("posX", (i + (x * clusterSize)));
						chunk.put("posY", (j + (y * clusterSize)));
						chunk.put("posZ", (k + (z * clusterSize)));
						jsa.put(chunk);

					}
		} catch (JSONException e1)
		{
			e1.printStackTrace();
		}
		
		try
		{
			jso.put("chunks", jsa);
			FileWriter fw = new FileWriter(f);
			fw.append(jso.toString());
			fw.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void loadCluster(World world, int posX, int posY, int posZ)
	{
		try
		{
			File f = new File("resc/data/world/" + world.name + "/" + posX + "x" + posY + "x" + posZ + ".ccd");
			JSONObject jso = JSONUtil.getJSONObjectFromFile(f);

			if (Game.debugMode)
			{
				System.out.println("loaded cluster " + f);
				for (StackTraceElement e : Thread.currentThread().getStackTrace())
					System.out.println(e);
			}
			
			JSONArray jsa = jso.getJSONArray("chunks");
			
			for(int i = 0; i < jsa.length(); i++)
			{
				JSONObject chunkData = jsa.getJSONObject(i);
				Chunk c = Chunk.createNewChunk(world, chunkData.getInt("posX"),  chunkData.getInt("posY"),  chunkData.getInt("posZ"), false);
				String tiles = chunkData.getString("tiles");
				for (int j = 0; j < tiles.split(",").length; j++)
					c.tiles[j] = Integer.valueOf(tiles.split(",")[j]);
				Chunk.chunks.put(c.getHashString(), c);
				JSONArray entities = chunkData.getJSONArray("entities");
				for(int j = 0; j < entities.length(); j++)
					world.spawn(Entity.createEntityFromSaveData(world, entities.getJSONObject(j).getString("name"), entities.getJSONObject(j)), false);				
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
