package com.ichmed.trinketeers.spell;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.trinketeers.spell.formation.Arc;

public class SpellSteamblast extends Spell
{
	public SpellSteamblast()
	{
		trailDelay = 50;
		element = "Steam";
		amount = 5;
		formation = new Arc();
		size = new Vector2f(0.04f, 0.04f);
		speed = 0.01f;
		lifespan = 60;
		lifespanVar = 20;
		wobble = 0.1f;
		maxCooldown = 1;
		texture = "steamPuff.png";
		this.destroyOnImpact = false;

		args.put("Spray", 0.4f);
		args.put("Inertia", 0.4f);
	}

}
