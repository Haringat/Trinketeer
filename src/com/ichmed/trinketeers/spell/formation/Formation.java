package com.ichmed.trinketeers.spell.formation;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.entity.Projectile;
import com.ichmed.trinketeers.spell.Spell;
import com.ichmed.trinketeers.world.World;

public abstract class Formation
{
	public abstract Projectile[] apply(Projectile[] projectiles, World world, Entity controller, float x, float y, Vector2f direction, Spell spell);
}
