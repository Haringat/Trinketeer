package com.ichmed.trinketeers.ai;

import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.world.World;

public class BehaviourHoldDistanceToTarget extends Behaviour
{

	private float distance;
	private Class<? extends Entity> target;
	
	@SuppressWarnings("unchecked")
	public BehaviourHoldDistanceToTarget(World w, String... args)
	{
		this.distance = Float.valueOf(args[0]);
		try
		{
			this.target = (Class<? extends Entity>)Class.forName(args[1]);
		} catch (ClassNotFoundException e)
		{}
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
		if(w.getClosestEntityToSource(performer, this.distance, target) != null) performer.speed = 0;
		return true;
	}

}
