package com.ichmed.trinketeers.ai;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.world.World;

public class BehaviourTurnToCurrentWaypoint extends Behaviour
{
	public BehaviourTurnToCurrentWaypoint(World w)
	{
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
		if(performer.currentWaypoint == null) return false;
		Vector2f p = performer.currentWaypoint.getPosition();
		Vector3f z = performer.getCenter();
		performer.preferredDirection = (Vector2f) new Vector2f(p.x - z.x, p.y - z.y);
		return true;
	}

}
