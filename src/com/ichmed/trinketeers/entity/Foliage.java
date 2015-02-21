package com.ichmed.trinketeers.entity;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.trinketeers.world.World;


public class Foliage extends Entity
{
	public static final String[] names = {"bush", "grass1"};
	
	public Foliage(World w)
	{
		this(w, names[(int)(Math.random() * names.length)]);
	}

	public Foliage(World w, String name)
	{
		super(w);
		this.isMoveable = false;
		this.isSolid = false;
		this.isVulnerable = false;
		this.entityType = name;
		this.size = new Vector2f(.25f, .25f);
	}
	
}
