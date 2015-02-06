package com.ichmed.trinketeers.util;

import org.lwjgl.util.vector.Vector2f;

public class AxisAllignedBoundingBox
{
	public Vector2f pos, size;
	
	public AxisAllignedBoundingBox(Vector2f pos, Vector2f size)
	{
		this(pos.x, pos.y, size.x, size.y);		
	}
	
	public AxisAllignedBoundingBox(float posX, float posY, float sizeX, float sizeY)
	{
		this.pos = new Vector2f(posX, posY);
		this.size = new Vector2f(sizeX, sizeY);
	}
	
	public static boolean doAABBsIntersect(AxisAllignedBoundingBox a, AxisAllignedBoundingBox b)
	{
		return isPointInAABB(a.pos.x, a.pos.y, b) || isPointInAABB(a.pos.x + a.size.x, a.pos.y, b) 
				|| isPointInAABB(a.pos.x + a.size.x, a.pos.y + a.size.y, b) || isPointInAABB(a.pos.x, a.pos.y + a.size.y, b) ||
				isPointInAABB(b.pos.x, b.pos.y, a) || isPointInAABB(b.pos.x + b.size.x, b.pos.y, a) 
				|| isPointInAABB(b.pos.x + b.size.x, b.pos.y + b.size.y, a) || isPointInAABB(b.pos.x, b.pos.y + b.size.y, a);
	}
	
	public static boolean isPointInAABB(float x, float y, AxisAllignedBoundingBox aabb)
	{
		return (x > aabb.pos.x && y > aabb.pos.y && x <= aabb.pos.x + aabb.size.x && y <= aabb.pos.y + aabb.size.y);
		
	}
	
	public static boolean isPointInAABB(Vector2f point, AxisAllignedBoundingBox aabb)
	{
		return isPointInAABB(point.x, point.y, aabb);
	}
	
	public String toString()
	{
		return "Pos: X: " + this.pos.x + " Y: " + this.pos.y + "  Size X: " + this.size.x + " Y: " + this.size.y;
	}
}
