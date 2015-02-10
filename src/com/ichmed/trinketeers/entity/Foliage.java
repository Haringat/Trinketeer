package com.ichmed.trinketeers.entity;

import org.lwjgl.util.vector.Vector2f;


public class Foliage extends Entity
{
	public static final String[] names = {"bush", "grass1"};
	
	public Foliage()
	{
		this(names[(int)(Math.random() * names.length)]);
	}

	public Foliage(String name)
	{
		this.isMoveable = false;
		this.isSolid = false;
		this.isVulnerable = false;
		this.name = name;
		this.size = new Vector2f(.25f, .25f);
	}
	
}
