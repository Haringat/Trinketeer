package com.ichmed.trinketeers.entity.pickup;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import com.ichmed.trinketeers.Game;
import com.ichmed.trinketeers.ai.BehaviourLight;
import com.ichmed.trinketeers.entity.Player;
import com.ichmed.trinketeers.util.AxisAllignedBoundingBox;
import com.ichmed.trinketeers.world.World;

public class Torch extends Pickup
{
	public Torch(World world)
	{
		super(world);
		this.size = new Vector2f(0.01f, 0.01f);
		this.speed = 0;
		this.name = "torch";
		this.isSolid = true;
		this.isVulnerable = false;
		this.behaviours.add(new BehaviourLight(world, new Vector4f(20f, 20f, 4f, 0f), 1.0f, 2f));
	}

	@Override
	public boolean movesTowardPlayer()
	{
		return false;
	}

	@Override
	public boolean pickUp(World w, Player p)
	{
		if (Game.isKeyDown(GLFW_KEY_SPACE))
		{
			p.fuel = 6000;
			return true;
		}
		return false;
	}

	@Override
	public AxisAllignedBoundingBox getRenderArea()
	{
		return new AxisAllignedBoundingBox(this.position.x - 0.0375f, this.position.y, this.size.x + 0.075f, this.size.y + 0.075f);
	}

	@Override
	public String getTextureForState(World w)
	{
		return this.name + "_" + (this.ticksExisted / 10) % 4;
	}
}
