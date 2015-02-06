package com.ichmed.trinketeers.entity;

import com.ichmed.trinketeers.entity.mob.Zombie;
import com.ichmed.trinketeers.world.World;

public class Grave extends Entity
{
	public int spawnCooldown = 100;
	public Grave()
	{
		this.isSolid = false;
		this.isMoveable = false;
	}
	
	@Override
	public void tick(World world)
	{
		spawnCooldown--;
		if(this.spawnCooldown <= 0)
		{
			world.spawn(new Zombie().setCenter(this.getCenter()));
			this.spawnCooldown = 100;
		}
		super.tick(world);
	}
	
	
	
	

}
