package com.ichmed.trinketeers.world.generator;

import org.lwjgl.util.vector.Vector3f;

import com.ichmed.trinketeers.entity.pickup.Chest;
import com.ichmed.trinketeers.entity.pickup.Ladder;
import com.ichmed.trinketeers.entity.pickup.SpellScroll;
import com.ichmed.trinketeers.entity.pickup.Torch;
import com.ichmed.trinketeers.world.Chunk;
import com.ichmed.trinketeers.world.World;

public class WorldGenerator
{
	public static void generateChunk(World world, int x, int y, int z)
	{
		int tileX = x * Chunk.chunkSize;
		int tileY = y * Chunk.chunkSize;
		if (x == 0 && y == 0 && z > -20)
		{
			for (int i = -8; i < 8; i++)
			{
				if (z != 0) Chunk.setTile(world, i, -8, z, 3);
				for (int j = -7; j < 8; j++)

					Chunk.setTile(world, tileX + i, tileY + j, z, 1);

			}
			for (int i = 0; i < 4; i++)
			{

				for (int j = 0; j < 4; j++)
				{
					Chunk.setTile(world, tileX - 2 + i, tileY + 7 + j, z, 1);
					Chunk.setTile(world, tileX - 2 + i, tileY - 11 + j, z, 1);
				}
				if (z != 0) Chunk.setTile(world, -2 + i, -11, z, 3);
			}
			world.spawn((new Torch(world)).setCenter(new Vector3f(x + 0.1f, y + 0.8f, z)));
			world.spawn((new Torch(world)).setCenter(new Vector3f(x - 0.1f, y + 0.8f, z)));
			world.spawn((new Torch(world)).setCenter(new Vector3f(x - 0.1f, y - 0.8f, z)));
			world.spawn((new Torch(world)).setCenter(new Vector3f(x + 0.1f, y - 0.8f, z)));
			world.spawn((new Ladder(world, z % 2 == 0)).setCenter(new Vector3f(x, y + 1.1f, z)), false);
			if(z < 0) world.spawn((new Ladder(world, z % 2 != 0)).setCenter(new Vector3f(x, y - .9f, z)), false);
			world.spawn((new Chest(world)).setCenter(new Vector3f(x + 0.0f, y + 0.6f, z)));
			world.generateZombies(-z, z);
			world.generateFlameElementals(-z / 5, z);

			if (z == 0)
			{
				for (int i = 0; i < 5; i++)
					world.spawn((new SpellScroll(world)).setCenter(new Vector3f(-0.8f + (i / 10f) * 4, 0f, z)), false, false);
			}
		}
		if(z == 0)
		{
			int r = (int)(Math.random() * 5) + 5;
//			for(int i = 0; i < r; i++)
//				world.spawn((new Foliage(world)).setCenter(new Vector3f(x + 1 - (float)Math.random(), y + 1 - (float)Math.random(), z)));
		}
	}
}
