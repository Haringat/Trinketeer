package com.ichmed.trinketeers.entity.pickup;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.trinketeers.Game;
import com.ichmed.trinketeers.entity.Player;
import com.ichmed.trinketeers.util.AxisAllignedBoundingBox;
import com.ichmed.trinketeers.world.World;

public class Chest extends Pickup
{

	public Chest(World w)
	{
		super(w);
		this.renderWhenDead = true;
		this.despawnCountDown = 100000;
		this.size = new Vector2f(0.25f, 0.125f);
		this.isSolid = true;
		this.pickupRange = 0.2f;
		this.solidWhenDead = false;
		this.lootRange = 0.2f;
		this.dropLootOnDeath = true;
		this.isVulnerable = false;
		this.lootValue = 40 + (int)(Math.random() * 100);
	}

	@Override
	public boolean pickUp(World w, Player p)
	{
		if (Game.isKeyDown(GLFW_KEY_SPACE) && w.getNumberOfEnemies((int) this.position.z) == 0) return true;
		return false;
	}

	@Override
	public AxisAllignedBoundingBox getRenderArea()
	{
		return new AxisAllignedBoundingBox(position.x, this.position.y, this.size.x, this.size.y + 0.125f);
	}
	
	

	@Override
	public String getTextureForState(World w)
	{
		return this.isDead ? "chestOpen" : "chestClosed";
	}

	@Override
	public boolean movesTowardPlayer()
	{
		return false;
	}

}
