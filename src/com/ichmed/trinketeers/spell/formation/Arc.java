package com.ichmed.trinketeers.spell.formation;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.entity.Projectile;
import com.ichmed.trinketeers.spell.Spell;
import com.ichmed.trinketeers.world.World;

public class Arc extends Formation
{

	@Override
	public void modProjectile(Projectile p, World world, Entity controller, float x, float y, Vector2f direction, Spell spell)
	{
		float spray = (Float)spell.args.get("Spray");
		for(int i = 0; i < spell.amount; i++)
		{
			p.preferredDirection.x += ((float)Math.random() - 0.5f) * spray;
			p.preferredDirection.y += ((float)Math.random() - 0.5f) * spray;
		}
	}

}
