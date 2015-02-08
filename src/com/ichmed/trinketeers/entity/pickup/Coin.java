package com.ichmed.trinketeers.entity.pickup;

import com.ichmed.trinketeers.entity.Player;
import com.ichmed.trinketeers.world.World;

public class Coin extends Pickup
{
	public Coin()
	{
		this.name = "coin";
		this.size.x = this.size.y = 0.035f;
		this.movementDelay = 25;
	}

	@Override
	public boolean pickUp(World w, Player p)
	{
		p.coins++;
		return true;
	}

}
