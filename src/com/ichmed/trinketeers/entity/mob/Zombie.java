package com.ichmed.trinketeers.entity.mob;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.trinketeers.ai.BehaviourAttackTarget;
import com.ichmed.trinketeers.ai.BehaviourFollowTarget;
import com.ichmed.trinketeers.ai.BehaviourTurnToCurrentWaypoint;
import com.ichmed.trinketeers.ai.BehaviourWander;
import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.entity.Player;
import com.ichmed.trinketeers.util.AxisAllignedBoundingBox;
import com.ichmed.trinketeers.world.World;

public class Zombie extends Entity
{
	public int attackCooldown = 0;

	public Zombie()
	{
		super();
		this.size = new Vector2f(.075f, .075f);
		this.speed = 0.005f;
		this.preferredSpeed = 0.005f;
		this.texture = "resc/textures/" + "zombie.png";
		this.despawnCountDown = 8000;
		this.renderWhenDead = true;
		this.lootRange = 0.1f;
		this.dropLootOnDeath = true;
		this.name = "zombie";
		this.behaviours.add(new BehaviourTurnToCurrentWaypoint());
		this.behaviours.add(new BehaviourFollowTarget(0.005f, Player.class));
		this.behaviours.add(new BehaviourWander(0.001f));
		this.behaviours.add(new BehaviourAttackTarget(2f, 0.1f, Player.class));
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
