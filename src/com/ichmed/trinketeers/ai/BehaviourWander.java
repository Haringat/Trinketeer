package com.ichmed.trinketeers.ai;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.trinketeers.ai.waypoint.SimpleWaypoint;
import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.world.World;

public class BehaviourWander extends Behaviour
{
	private int cooldown = 0, waitCooldown;
	SimpleWaypoint waypoint;
	private float speed;
	private int waitTime, waitTimeVariance;

	public BehaviourWander(World w, float wanderSpeed, int i, int j)
	{
		this.speed = wanderSpeed;
		this.waitTime = i;
		this.waitTimeVariance = j;
	}

	@Override
	public int getPriority(Entity e)
	{
		return 0;
	}

	@Override
	public boolean isActive(Entity e, World w)
	{
		return true;
	}

	@Override
	public boolean perform(Entity performer, World w)
	{
		waitCooldown--;
		if (waitCooldown <= 0)
		{
			this.cooldown--;
			if (this.waypoint == null || this.waypoint.isReached(performer)) waitCooldown = waitTime + (int) (Math.random() * waitTimeVariance);
			if (this.waypoint == null || this.cooldown <= 0 || this.waypoint.isReached(performer))
			{
				createNewWaypoint(performer);
				performer.currentWaypoint = this.waypoint;
				performer.preferredSpeed = this.speed;
				this.cooldown = 500 + (int) (Math.random() * 200);
				return true;
			}
		} else
		{
			performer.preferredSpeed = 0;
		}

		return false;
	}

	private void createNewWaypoint(Entity e)
	{
		this.waypoint = new SimpleWaypoint(new Vector2f(e.position.x + (float) (Math.random() - 0.5), e.position.y + (float) (Math.random() - 0.5)));
	}

}
