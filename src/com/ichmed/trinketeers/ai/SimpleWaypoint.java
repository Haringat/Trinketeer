package com.ichmed.trinketeers.ai;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.trinketeers.entity.Entity;

public class SimpleWaypoint implements Waypoint
{
	Vector2f position;
	public SimpleWaypoint next;
	public float radius = 0.1f;
	
	public SimpleWaypoint(Vector2f position)
	{
		this.position = position;
	}
	
	public void setNext(SimpleWaypoint w)
	{
		this.next = w;
	}
	
	public void setRadius(float rad)
	{
		this.radius = rad;
	}
	
	public boolean isReached(Entity e)
	{
		return new Vector2f(this.position.x - e.position.x, this.position.y - e.position.y).length() <= this.radius;
	}

	@Override
	public Vector2f getPosition()
	{
		return this.position;
	}

	@Override
	public float getRadius()
	{
		return radius;
	}
}
