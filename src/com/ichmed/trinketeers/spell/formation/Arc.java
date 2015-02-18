package com.ichmed.trinketeers.spell.formation;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.entity.Projectile;
import com.ichmed.trinketeers.spell.Spell;
import com.ichmed.trinketeers.world.World;

public class Arc extends Formation
{
	@Override
	public Projectile[] apply(Projectile[] projectiles, World world, Entity controller, float x, float y, Vector2f direction, Spell spell)
	{
		float mod;
		if(spell.args.get("SprayMod") != null) mod = Float.valueOf((Float)spell.args.get("SprayMod"));
		else mod = 0;
		float spray = Math.max(0, Float.valueOf((Float)spell.args.get("Spray")) - mod);
		for (Projectile p : projectiles)
		{
			p.preferredDirection.x += ((float) Math.random() - 0.5f) * spray;
			p.preferredDirection.y += ((float) Math.random() - 0.5f) * spray;
		}
		return projectiles;
	}

}
