package com.ichmed.trinketeers.world;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.entity.pickup.Chest;
import com.ichmed.trinketeers.entity.pickup.Ladder;
import com.ichmed.trinketeers.entity.pickup.SpellScroll;
import com.ichmed.trinketeers.entity.pickup.Torch;
import com.ichmed.trinketeers.util.render.IWorldGraphic;
import com.ichmed.trinketeers.util.render.light.ILight;
import com.ichmed.trinketeers.util.render.light.IShadow;

public class Level
{
	public List<Entity> entities = new ArrayList<Entity>();
	List<Entity> entitiesNextTick = new ArrayList<Entity>();
	List<IWorldGraphic> worldGraphics = new ArrayList<>();
	List<ILight> lights = new ArrayList<>();
	public List<IShadow> shadows = new ArrayList<>();

	World world;
	int level;

	public Level(World w, int level)
	{
		this.world = w;
		this.level = level;
	}


	public static void init(World world, int level)
	{
//		// world.player.setCenter(new Vector2f(0, -1.2f));
//		for (int i = 16; i < 24; i++)
//		{
//			for (int j = 0; j < 8; j++)
//			{
//				Chunk.setTile(i, j, level, 1);
//			}
//			if(level != 0) Chunk.setTile(i, 0, level, 3);
//		}
//		
//		for (int i = 8; i < 16; i++)
//		{
//			Chunk.setTile(i, 5, level, 1);
//			Chunk.setTile(i, 4, level, 1);
//			if(level != 0) Chunk.setTile(i, 3, level, 3);
//			else  Chunk.setTile(i, 3, level, 1);			
//		}
//		
//		
//		if(level < 0)world.spawn((new Ladder(false)).setCenter(new Vector2f(2.5f, 0.5f)));
//		
//		world.spawn((new Torch()).setCenter(new Vector2f(2.7f, 0.5f)));
//		world.spawn((new Torch()).setCenter(new Vector2f(2.3f, 0.5f)));
		
		
//		for (int i = -8; i < 8; i++)
//		{
//			if (level != 0) Chunk.setTile(i, -8, level, 3);
//			for (int j = -7; j < 8; j++)
//
//				Chunk.setTile(i, j, level, 1);
//
//		}
//		for (int i = 0; i < 4; i++)
//		{
//
//			for (int j = 0; j < 4; j++)
//			{
//				Chunk.setTile(-2 + i, 7 + j, level, 1);
//				Chunk.setTile(-2 + i, -11 + j, level, 1);
//			}
//			if (level != 0) Chunk.setTile(-2 + i, -11, level, 3);
//		}
//		world.spawn((new Torch()).setCenter(new Vector2f(-0.1f, 0.8f)));
//		world.spawn((new Torch()).setCenter(new Vector2f(0.1f, 0.8f)));
//		world.spawn((new Torch()).setCenter(new Vector2f(-0.1f, -0.8f)));
//		world.spawn((new Torch()).setCenter(new Vector2f(0.1f, -0.8f)));
//		world.spawn((new Ladder(true)).setCenter(new Vector2f(0.0f, 1.1f)));
//		world.spawn((new Chest()).setCenter(new Vector2f(0.0f, 0.6f)));
//		world.generateZombies(-level);
//		world.generateFlameElementals(-level / 5);
//
//		if (level == 0)
//		{
//			for (int i = 0; i < 5; i++)
//				world.spawn((new SpellScroll()).setCenter(new Vector2f(-0.8f + (i / 10f) * 4, 0f)), false, false);
//
//		}
	}
}
