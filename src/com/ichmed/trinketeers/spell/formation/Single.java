package com.ichmed.trinketeers.spell.formation;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.entity.Projectile;
import com.ichmed.trinketeers.spell.Spell;
import com.ichmed.trinketeers.world.World;

public class Single extends Formation
{
	@Override
	public void modProjectile(Projectile p, World world, Entity controller, float x, float y, Vector2f direction, Spell spell)
	{
		if(controller == null) System.out.println(Thread.currentThread().getStackTrace());
	}

}
