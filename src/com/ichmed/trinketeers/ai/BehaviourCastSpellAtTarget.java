package com.ichmed.trinketeers.ai;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.spell.Spell;
import com.ichmed.trinketeers.world.World;

public class BehaviourCastSpellAtTarget extends Behaviour
{
	private Spell spell;
	private float range;
	private Class<? extends Entity> target;
	private int cooldown;

	public BehaviourCastSpellAtTarget(Spell spell, float range, Class<? extends Entity> target)
	{
		super();
		this.spell = spell;
		this.range = range;
		this.target = target;
	}

	@Override
	public int getPriority(Entity e)
	{
		return 0;
	}

	@Override
	public boolean isActive(Entity e, World world)
	{
		return true;
	}

	@Override
	public boolean perform(Entity performer, World w)
	{
		this.cooldown--;
		Entity e = w.getClosestEntityToSource(performer, range, target);
		if (this.cooldown <= 0 && e != null)
		{
			spell.cast(w, performer, performer.getCenter().x, performer.getCenter().y, Vector2f.sub(e.getCenter(), performer.getCenter(), null).normalise(null));
			this.cooldown = spell.cooldown;
			return true;
		}
		return false;
	}

}
