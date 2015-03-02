package com.ichmed.trinketeers.entity.mob;

import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.world.World;

public class Mob extends Entity
{
	public Mob(World w)
	{
		super(w);
	}

	@Override
	public boolean isHostile()
	{
		return true;
	}	
}
