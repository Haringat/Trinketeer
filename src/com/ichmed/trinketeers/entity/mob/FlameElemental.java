package com.ichmed.trinketeers.entity.mob;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.trinketeers.ai.BehaviourAttackTarget;
import com.ichmed.trinketeers.ai.BehaviourCastSpellAtTarget;
import com.ichmed.trinketeers.ai.BehaviourFollowTarget;
import com.ichmed.trinketeers.ai.BehaviourHoldDistanceToTarget;
import com.ichmed.trinketeers.ai.BehaviourTurnToCurrentWaypoint;
import com.ichmed.trinketeers.ai.BehaviourWander;
import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.entity.Player;
import com.ichmed.trinketeers.spell.SpellFireball;
import com.ichmed.trinketeers.util.AxisAllignedBoundingBox;
import com.ichmed.trinketeers.world.World;

public class FlameElemental extends Entity
{
	public FlameElemental(World w)
	{
		super(w);
		this.size = new Vector2f(.075f, .075f);
		this.speed = 0.005f;
		this.preferredSpeed = 0.005f;
		this.despawnCountDown = 100;
		this.renderWhenDead = true;
		this.lootRange = 0.1f;
		this.dropLootOnDeath = true;
		this.name = "flameElemental";
		this.behaviours.add(new BehaviourTurnToCurrentWaypoint(w));
		this.behaviours.add(new BehaviourFollowTarget(w, 0.008f, Player.class));
		this.behaviours.add(new BehaviourWander(w, 0.003f, 0, 0));
		this.behaviours.add(new BehaviourHoldDistanceToTarget(w, 0.45f, Player.class));
		this.behaviours.add(new BehaviourCastSpellAtTarget(w, new SpellFireball(), 0.55f, Player.class));
		this.behaviours.add(new BehaviourAttackTarget(w, 1f, 0.01f, Player.class));
	}
	
//	public ILight createLight()
//	{
//		SimpleLight lightSource = new SimpleLight();
//		lightSource.setActive(true);
//		lightSource.setColor(new Vector4f(20f, 20f, 4f, 0f));
//		return lightSource;
//	}

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
