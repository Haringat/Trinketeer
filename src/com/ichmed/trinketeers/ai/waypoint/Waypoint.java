package com.ichmed.trinketeers.ai.waypoint;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.trinketeers.entity.Entity;

public interface Waypoint
{
	public Vector2f getPosition();
	public boolean isReached(Entity e);
	public float getRadius();
}
