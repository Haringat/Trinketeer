package com.ichmed.trinketeers.entity.mob;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.trinketeers.ai.BehaviourAttackTarget;
import com.ichmed.trinketeers.ai.BehaviourFollowTarget;
import com.ichmed.trinketeers.ai.BehaviourHiveMindFollowTarget;
import com.ichmed.trinketeers.ai.BehaviourTurnToCurrentWaypoint;
import com.ichmed.trinketeers.ai.BehaviourWander;
import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.util.AxisAllignedBoundingBox;
import com.ichmed.trinketeers.world.World;

public class Zombie extends Entity
{
	public int attackCooldown = 0;

	public Zombie(World w)
	{
		super(w);
		this.size = new Vector2f(.075f, .075f);
		this.speed = 0.005f;
		this.preferredSpeed = 0.005f;
		this.despawnCountDown = 8000;
		this.lootRange = 0.1f;
		this.dropLootOnDeath = true;
		this.entityType = "zombie";
		this.behaviours.add(new BehaviourTurnToCurrentWaypoint(w));
		this.behaviours.add(new BehaviourFollowTarget(w, "0.005", "com.ichmed.trinketeers.entity.Player"));
		this.behaviours.add(new BehaviourWander(w, "0.001", "110", "30"));
		this.behaviours.add(new BehaviourAttackTarget(w, "2", "0.1", "com.ichmed.trinketeers.entity.Player"));
		this.behaviours.add(new BehaviourHiveMindFollowTarget(w, "com.ichmed.trinketeers.entity.Player", "com.ichmed.trinketeers.entity.mob.Zombie", "000.25", "0.005"));
	}

	@Override
	public AxisAllignedBoundingBox getRenderArea()
	{
		return new AxisAllignedBoundingBox(this.position.x - 0.0375f, this.position.y, this.size.x + 0.075f, this.size.y + 0.075f);
	}

	@Override
	public boolean isHostile()
	{
		return !this.isDead;
	}

	@Override
	public void tick(World world)
	{
		super.tick(world);
	}

	@Override
	protected void onDeath(World world)
	{
		super.onDeath(world);
	}
}
