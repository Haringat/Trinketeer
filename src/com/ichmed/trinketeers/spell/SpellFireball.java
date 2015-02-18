package com.ichmed.trinketeers.spell;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.trinketeers.spell.formation.Arc;
import com.ichmed.trinketeers.spell.formation.Single;

public class SpellFireball extends Spell
{
	public SpellFireball()
	{
		childSpell = new Child();
		trailDelay = 50;
		element = "Fire";
		amount = 1;
		formation = new Single();
		size = new Vector2f(0.1f, 0.1f);
		speed = 0.02f;
		lifespan = 100;
		lifespanVar = 0;
		wobble = 0.1f;
		maxCooldown = 50;
		texture = "fireballRed.png";
	}

	private class Child extends Spell
	{

		private Child()
		{
			childSpell = null;
			element = "Fire";
			amount = 5;
			formation = new Arc();
			size = new Vector2f(0.02f, 0.02f);
			speed = 0.002f;
			lifespan = 100;
			lifespanVar = 100;
			wobble = 0.6f;
			texture = "fireballRed.png";
			maxCooldown = 1;

			args.put("Spray", 8f);
		}
	}
}
