package com.ichmed.trinketeers.entity.mob;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

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
import com.ichmed.trinketeers.util.render.light.ILight;
import com.ichmed.trinketeers.util.render.light.SimpleLight;
import com.ichmed.trinketeers.world.World;

public class FlameElemental extends Entity
{
	public FlameElemental()
	{
		super();
		this.size = new Vector2f(.075f, .075f);
		this.speed = 0.005f;
		this.preferredSpeed = 0.005f;
		this.texture = "zombie.png";
		this.despawnCountDown = 1;
		this.renderWhenDead = true;
		this.lootRange = 0.1f;
		this.maxAttackcooldown = 60;
		this.dropLootOnDeath = true;
		this.behaviours.add(new BehaviourTurnToCurrentWaypoint());
		this.behaviours.add(new BehaviourFollowTarget(0.008f, Player.class));
		this.behaviours.add(new BehaviourWander(0.003f));
		this.behaviours.add(new BehaviourHoldDistanceToTarget(0.5f, Player.class));
		this.behaviours.add(new BehaviourCastSpellAtTarget(new SpellFireball(), 0.5f, Player.class));
		this.behaviours.add(new BehaviourAttackTarget(1f, 0.01f, Player.class));
	}
	
	@Override
	public String getTexture(World w)
	{
		return "flameElemental.png";
	}

	@Override
	public ILight createLight()
	{
		SimpleLight lightSource = new SimpleLight();
		lightSource.setActive(true);
		lightSource.setColor(new Vector4f(20f, 20f, 4f, 0f));
		return lightSource;
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
