package com.ichmed.trinketeers.ai;

import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.world.World;

public class BehaviourHiveMindFollowTarget extends Behaviour
{
	private Class<?> target, hive;
	private float range, speed;

	@SuppressWarnings("unchecked")
	public BehaviourHiveMindFollowTarget(World world, String...args)
	{try
	{
		this.target = (Class<? extends Entity>)Class.forName(args[0]);
	} catch (ClassNotFoundException e)
	{}try
	{
		this.hive = (Class<? extends Entity>)Class.forName(args[1]);
	} catch (ClassNotFoundException e)
	{}
		this.range = Float.valueOf(args[2]);
		this.speed = Float.valueOf(args[3]);
	}
	
	@Override
	public int getPriority(Entity e)
	{
		return 3;
	}

	@Override
	public boolean isActive(Entity e, World world)
	{
		return true;
	}

	@Override
	public boolean perform(Entity performer, World w)
	{
		for (Entity e : w.getEntitiesByDistance(performer, range))
			if (!e.isDead && e.currentWaypoint != null && hive.isAssignableFrom(e.getClass()))
				if (e.currentWaypoint != performer.currentWaypoint && target.isAssignableFrom(e.currentWaypoint.getClass()))
			{
				performer.currentWaypoint = e.currentWaypoint;
				performer.preferredSpeed = this.speed;
				return true;
			}
		return false;
	}
}
