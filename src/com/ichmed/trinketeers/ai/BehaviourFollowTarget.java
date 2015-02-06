package com.ichmed.trinketeers.ai;

import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.world.World;

public class BehaviourFollowTarget extends Behaviour
{
	private float speed;
	private Class<? extends Entity> targetClass;

	@SuppressWarnings("unchecked")
	public BehaviourFollowTarget(Object... args)
	{
		super(args);
		this.speed = (float)args[0];
		this.targetClass = (Class<? extends Entity>)args[1];
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
