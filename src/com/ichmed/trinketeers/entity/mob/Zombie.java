package com.ichmed.trinketeers.entity.mob;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.trinketeers.ai.BehaviourAttackTarget;
import com.ichmed.trinketeers.ai.BehaviourFollowTarget;
import com.ichmed.trinketeers.ai.BehaviourHiveMindFollowTarget;
import com.ichmed.trinketeers.ai.BehaviourTurnToCurrentWaypoint;
import com.ichmed.trinketeers.ai.BehaviourWander;
import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.entity.Player;
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
		this.renderWhenDead = true;
		this.lootRange = 0.1f;
		this.dropLootOnDeath = true;
		this.entityType = "zombie";
		this.behaviours.add(new BehaviourTurnToCurrentWaypoint(w));
		this.behaviours.add(new BehaviourFollowTarget(w, 0.005f, Player.class));
		this.behaviours.add(new BehaviourWander(w, 0.001f, 110, 30));
		this.behaviours.add(new BehaviourAttackTarget(w, 2f, 0.1f, Player.class));
		this.behaviours.add(new BehaviourHiveMindFollowTarget(w, Player.class, Zombie.class, this.visionRange * 2.5f, 0.005f));
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
}
