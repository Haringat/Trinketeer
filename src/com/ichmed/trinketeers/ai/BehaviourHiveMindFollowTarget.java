package com.ichmed.trinketeers.ai;

import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.world.World;

public class BehaviourHiveMindFollowTarget extends Behaviour
{
	private Class<?> target, hive;
	private float range, speed;

	public BehaviourHiveMindFollowTarget(Object...w)
	{
		this.target = (Class<?>)w[0];
		this.hive = (Class<?>)w[1];
		this.range = (float)w[2];
		this.speed = (float)w[3];
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
