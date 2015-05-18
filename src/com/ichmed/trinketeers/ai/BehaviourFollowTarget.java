package com.ichmed.trinketeers.ai;

import com.ichmed.trinketeers.Game;
import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.world.World;

public class BehaviourFollowTarget extends Behaviour
{
	private float speed;
	private Class<? extends Entity> targetClass;

	@SuppressWarnings("unchecked")
	public BehaviourFollowTarget(World w, String... args)
	{
		this.speed = Float.valueOf(args[0]);
		try
		{
			this.targetClass = (Class<? extends Entity>)Class.forName(args[1]);
		} catch (ClassNotFoundException e)
		{
			Game.logger.throwing(BehaviourFollowTarget.class.getName(), "<init>", e);
		}
	}
	
	@Override
	public int getPriority(Entity e)
	{
		return 1;
	}

	@Override
	public boolean isActive(Entity e, World w)
	{
		return w.getClosestEntityToSource(e, e.visionRange, targetClass) != null;
	}

	@Override
	public boolean perform(Entity performer, World world)
	{
		performer.currentWaypoint = world.player;
		performer.preferredSpeed = this.speed;
		return true;
	}
}
