package com.ichmed.trinketeers.entity.pickup;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.trinketeers.Game;
import com.ichmed.trinketeers.entity.Player;
import com.ichmed.trinketeers.util.AxisAllignedBoundingBox;
import com.ichmed.trinketeers.world.World;

public class Chest extends Pickup
{

	public Chest()
	{
		this.texture = "resc/textures/" + "chest_closed";
		this.renderWhenDead = true;
		this.despawnCountDown = 100000;
		this.size = new Vector2f(0.2f, 0.1f);
		this.isSolid = true;
		this.pickupRange = 0.2f;
		this.solidWhenDead = true;
		this.lootRange = 0.2f;
		this.dropLootOnDeath = true;
		this.isVulnerable = false;
		this.lootValue = 40 + (int)(Math.random() * 100);
	}

	@Override
	public boolean pickUp(World w, Player p)
	{
		if (Game.isKeyDown(GLFW_KEY_SPACE) && w.currentLevel.getNumberOfEnemies() == 0) return true;
		return false;
	}

	@Override
	public AxisAllignedBoundingBox getRenderArea()
	{
		return new AxisAllignedBoundingBox(position.x, this.position.y, this.size.x, this.size.y + 0.1f);
	}
	
	

	@Override
	public String getTextureForState(World w)
	{
		return this.isDead ? "chest_open" : "chest_closed";
	}

	@Override
	public void tick(World world)
	{
		AxisAllignedBoundingBox aabb = new AxisAllignedBoundingBox(this.position.x + 0.0001f, this.position.y + 0.0001f, this.size.x + 0.0002f, this.size.x + 0.0002f);
		if(world.getListOfIntersectingEntities(aabb, true).contains(world.player))
		{
			this.speed = 0.0001f;
			this.isMoveable = true;
			this.preferredDirection = new Vector2f(this.position).translate(world.player.position.x, world.player.position.y);
		}
		else
		{
			this.isMoveable = false;
			this.speed = 0;
		}
		super.tick(world);
	}

	@Override
	public boolean movesTowardPlayer()
	{
		return false;
	}

}
