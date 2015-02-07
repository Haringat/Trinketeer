package com.ichmed.trinketeers.entity;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.trinketeers.util.AxisAllignedBoundingBox;
import com.ichmed.trinketeers.util.render.light.IShadow;

public class Wall extends Entity implements IShadow
{
	public Wall()
	{
		this.isMoveable = false;
		this.isVulnerable = false;
		this.texture = "resc/textures/" + "wall2.png";
	}

	@Override
	public Vector2f[] getVertices()
	{
		Vector2f[] v = new Vector2f[4];
		v[0] = new Vector2f(this.position);
		v[1] = new Vector2f(this.position.x + this.size.x, this.position.y);
		v[2] = new Vector2f(this.position.x + this.size.x, this.position.y + this.size.y);
		v[3] = new Vector2f(this.position.x, this.position.y + this.size.y);
		return v;
	}

	@Override
	public AxisAllignedBoundingBox getRenderArea()
	{
		return new AxisAllignedBoundingBox(this.position.x - this.size.x / 2, this.position.y, this.size.x * 2, this.size.y * 2);
	}
}
