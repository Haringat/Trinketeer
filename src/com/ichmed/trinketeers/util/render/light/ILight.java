package com.ichmed.trinketeers.util.render.light;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

public interface ILight
{
	public boolean isActive();
	
	//RGBA Value (A = Brightness)
	public Vector4f getColor();
	
	//Direction the light is pointing (doesn't matter for circular lights)
	public Vector2f getDirection();
	
	//The light sources center-point
	public Vector2f getPosition();
	
	//the angle of the light-cone (360 for circular light, must be <= 360)
	public float getAngle();
	
	//If the light source is farther than getMaxRange from the camera it will not be rendered
	public float getMaxRange();
	
	public void setPosition(Vector2f pos);
}
