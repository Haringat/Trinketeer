package com.ichmed.trinketeers.spell.formation;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.entity.Projectile;
import com.ichmed.trinketeers.spell.Spell;
import com.ichmed.trinketeers.world.World;

public abstract class Formation
{
	public  void spawn(World world, Entity controller, float x, float y, Vector2f direction, Spell spell)
	{
		Projectile p = createProjectile(world, controller, x, y, direction, spell);
		modProjectile(p, world, controller, x, y, direction, spell);
		world.spawn(p, false);
	}
	
	public abstract void modProjectile(Projectile p, World world, Entity controller, float x, float y, Vector2f direction, Spell spell);

	protected final Projectile createProjectile(World world, Entity controller, float x, float y, Vector2f direction, Spell spell)
	{
		Projectile p = new Projectile(spell.size, spell.element, spell.childSpell, spell.wobble, spell.trailSpell);
		p.speed = p.preferredSpeed = spell.speed;
		p.direction = p.preferredDirection = new Vector2f(direction);

		p.setController(controller);
		p.setCenter(new Vector3f(x + p.direction.x * (controller.size.x / 2), y + p.direction.y * (controller.size.y / 2), controller.position.z));

		p.lifespan = spell.getLifespan();
		p.trailDelay = p.maxTrailDelay = spell.trailDelay;
		p.travelBeforeStandStill = spell.travelBeforeStandStill;
		p.destroyOnImpact = spell.destroyOnImpact;

		return p;
	}
}
