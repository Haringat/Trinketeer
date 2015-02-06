package com.ichmed.trinketeers.world;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.entity.Torch;
import com.ichmed.trinketeers.entity.Wall;
import com.ichmed.trinketeers.entity.pickup.Chest;
import com.ichmed.trinketeers.entity.pickup.LevelExit;
import com.ichmed.trinketeers.entity.pickup.SpellScroll;
import com.ichmed.trinketeers.util.AxisAllignedBoundingBox;
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

	public int getNumberOfEnemies()
	{
		int i = 0;
		for (Entity e : entities)
			if (e.isHostile()) i++;
		return i;
	}

	public boolean spawn(Entity e, boolean checkForColission, boolean checkSolidsOnly)
	{
		if (checkForColission)
		{
			AxisAllignedBoundingBox aabb = e.getColissionBox();
			if (world.getListOfIntersectingEntities(aabb, checkSolidsOnly).size() > 0) return false;
		}
		this.entitiesNextTick.add(e);
		this.worldGraphics.add(e);
		if (e instanceof IShadow) this.shadows.add((IShadow) e);
		e.onSpawn(world);
		return true;
	}

	public void init()
	{
		world.player.setCenter(new Vector2f(0, -1.2f));
		for (int i = -10; i < -1; i++)
		{
			spawn((new Wall()).setCenter(new Vector2f(i / 10f, 1f)), false, false);
			spawn((new Wall()).setCenter(new Vector2f(i / 10f, -1f)), false, false);
		}
		for (int i = 2; i < 11; i++)
		{
			spawn((new Wall()).setCenter(new Vector2f(i / 10f, 1f)), false, false);
			spawn((new Wall()).setCenter(new Vector2f(i / 10f, -1f)), false, false);
		}
		for (int i = -9; i < 10; i++)
		{
			spawn((new Wall()).setCenter(new Vector2f(1, i / 10f)), false, false);
			spawn((new Wall()).setCenter(new Vector2f(-1, i / 10f)), false, false);

		}
		for (int i = 0; i < 3; i++)
		{
			spawn((new Wall()).setCenter(new Vector2f(-0.2f, 1.1f + i / 10f)), false, false);
			spawn((new Wall()).setCenter(new Vector2f(0.2f, 1.1f + i / 10f)), false, false);
			spawn((new Wall()).setCenter(new Vector2f(-0.2f, -1.1f - i / 10f)), false, false);
			spawn((new Wall()).setCenter(new Vector2f(0.2f, -1.1f - i / 10f)), false, false);
		}
		for (int i = 0; i < 5; i++)
		{
			spawn((new Wall()).setCenter(new Vector2f(-0.2f + i / 10f, 1.3f)), false, false);
			spawn((new Wall()).setCenter(new Vector2f(-0.2f + i / 10f, -1.3f)), false, false);
		}
		
		world.spawn((new Torch()).setCenter(new Vector2f(-0.1f, 0.8f)));
		world.spawn((new Torch()).setCenter(new Vector2f(0.1f, 0.8f)));
		world.spawn((new Torch()).setCenter(new Vector2f(-0.1f, -0.8f)));
		world.spawn((new Torch()).setCenter(new Vector2f(0.1f, -0.8f)));
		world.spawn((new LevelExit()).setCenter(new Vector2f(0.0f, 1.1f)));
		world.spawn((new Chest()).setCenter(new Vector2f(0.0f, 0.6f)));
		world.generateZombies(this.level);
		world.generateFlameElementals(this.level / 5);

		if (level == 0)
		{
			for (int i = 0; i < 5; i++)
				spawn((new SpellScroll()).setCenter(new Vector2f(-0.8f + (i / 10f) * 4, 0f)), false, false);
			for (int i = 0; i < 4; i++)
				spawn((new Torch()).setCenter(new Vector2f(-0.6f + (i / 10f) * 4, 0f)), false, false);

		}
	}
}
