package com.ichmed.trinketeers.ai;

import org.json.JSONObject;

import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.world.World;

public abstract class Behaviour
{
	public Behaviour(Object... args)
	{
	}

	public static final int MAX_PRIORITY = 5;

	public abstract int getPriority(Entity e);

	public abstract boolean isActive(Entity e, World world);

	public abstract boolean perform(Entity performer, World w);

	public void cleanUp(World w)
	{

	}
	
	public JSONObject getSaveData()
	{
		return null;
	}
}
