package com.ichmed.trinketeers.util.render.light;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

public class SimpleLight implements ILight
{
	private Vector4f color = new Vector4f(10, 10, 10, 0);
	private Vector2f direction = new Vector2f();
	private Vector2f position = new Vector2f();
	private float angle = 360;
	private boolean isActive = true;
	private float maxRange;

	@Override
	public float getMaxRange()
	{
		return maxRange;
	}

	public void setMaxRange(float maxRange)
	{
		this.maxRange = maxRange;
	}

	public void setActive(boolean b)
	{
		this.isActive = b;
	}
	
	public void toggle()
	{
		this.isActive = !this.isActive;
	}

	@Override
	public Vector4f getColor()
	{
		return color;
	}

	public void setColor(Vector4f color)
	{
		this.color = new Vector4f(color);
	}

	/**
	 * Sets the POINTER of the direction to a Vector2f
	 * @param direction
	 */
	public void setDirection(Vector2f direction)
	{
		this.direction = direction;
	}
	
	public void setDirectionB(Vector2f direction)
	{
		this.setDirection(new Vector2f(direction));
	}

	/**
	 * Sets the POINTER of the position to a Vector2f
	 * @param direction
	 */
	public void setPosition(Vector2f position)
	{
		this.position = position;
	}
	
	public void setPositionB(Vector2f position)
	{
		this.setPosition(new Vector2f(position));
	}

	public void setAngle(float angle)
	{
		this.angle = angle;
	}

	@Override
	public Vector2f getDirection()
	{
		return direction;
	}

	@Override
	public Vector2f getPosition()
	{
		return new Vector2f(this.position);
	}

	@Override
	public float getAngle()
	{
		return angle;
	}

	@Override
	public boolean isActive()
	{
		return isActive;
	}

}
