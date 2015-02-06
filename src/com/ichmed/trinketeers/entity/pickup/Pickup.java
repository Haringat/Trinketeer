package com.ichmed.trinketeers.entity.pickup;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.entity.Player;
import com.ichmed.trinketeers.world.World;

public abstract class Pickup extends Entity
{
	int pickUpCooldown = 20;
	float pickupRange = 0.1f;
	int movementDelay = 0;

	public Pickup()
	{
		this.isSolid = false;
		this.despawnCountDown = 1;
	}

	@Override
	public void tick(World world)
	{
		if (!this.isDead)
		{
			this.movementDelay--;
			this.pickUpCooldown--;
			if (this.movesTowardPlayer() && this.movementDelay <= 0)
			{
				this.speed = 0.02f;
				Vector2f p = world.player.getCenter();
				Vector2f z = this.getCenter();
				this.preferredDirection = (Vector2f) new Vector2f(p.x - z.x, p.y - z.y);
			} else this.speed = 0;

			if (this.pickUpCooldown <= 0) if (canBePickedUp(world) && this.pickUp(world, world.player)) this.kill(world);
		}
		super.tick(world);
	}

	public boolean isPlayerInPickupRange(Player e)
	{
		Vector2f dist = new Vector2f(e.getCenter().x - this.getCenter().x, e.getCenter().y - this.getCenter().y);
		return dist.length() <= this.pickupRange;
	}

	public boolean canBePickedUp(World w)
	{
		return this.isPlayerInPickupRange(w.player);
	}

	public boolean movesTowardPlayer()
	{
		return true;
	}

	public abstract boolean pickUp(World w, Player p);

	@Override
	protected void renderHitBox(World w)
	{
		if (isPlayerInPickupRange(w.player)) GL11.glColor3f(1, 0, 0);
		super.renderHitBox(w);
	}
	
	public boolean isClosestPickupToPlayer(World w)
	{
		if(w.getClosestEntityToSource(w.player, this.pickupRange, Pickup.class) == this) return true;
		return false;
	}
	
	public boolean isClosestSpecificPickupToPlayer(World w)
	{
		if(w.getClosestEntityToSource(w.player, this.pickupRange, this.getClass()) == this) return true;
		return false;
	}
}
