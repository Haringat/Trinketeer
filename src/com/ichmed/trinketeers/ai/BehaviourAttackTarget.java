package com.ichmed.trinketeers.ai;

import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.world.World;

public class BehaviourAttackTarget extends Behaviour
{
	private float damage, range;
	private Class<? extends Entity> target;

	@SuppressWarnings("unchecked")
	public BehaviourAttackTarget(World w, String... args)
	{
		this.damage = Float.valueOf(args[0]);
		this.range = Float.valueOf(args[1]);
		try
		{
			this.target = (Class<? extends Entity>)Class.forName(args[2]);
		} catch (ClassNotFoundException e)
		{}
	}

	@Override
	public int getPriority(Entity e)
	{
		return 0;
	}

	@Override
	public boolean isActive(Entity e, World world)
	{
		return true;
	}

	@Override
	public boolean perform(Entity performer, World w)
	{
		Entity e = w.getClosestEntityToSource(performer, range, target);
		if (e != null)
		{
			e.damage(damage);
			return true;
		}
		return false;
	}
}
