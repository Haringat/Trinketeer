package com.ichmed.trinketeers.entity.pickup;

import org.lwjgl.util.vector.Vector4f;

import com.ichmed.trinketeers.entity.Player;
import com.ichmed.trinketeers.util.render.light.ILight;
import com.ichmed.trinketeers.util.render.light.SimpleLight;
import com.ichmed.trinketeers.world.World;

public class HealthBottle extends Pickup
{
	public HealthBottle(World w, float min, float max)
	{
		this(w);
	}

	public HealthBottle(World w)
	{
		super(w);
		this.entityType = "healthBottle";
		this.size.x = this.size.y = 0.05f;
	}

	public float health = 1f;

	@Override
	public boolean pickUp(World w, Player p)
	{
		if (p.health < p.maxHealth)
		{
			p.health += Math.min(this.health, p.maxHealth - p.health);
			return true;
		}
		return false;
	}

	public ILight createLight()
	{
		SimpleLight l = new SimpleLight();
		l.setColor(new Vector4f(6f, 1.4f, 1.4f, 1));
		return l;
	}

	@Override
	public boolean movesTowardPlayer()
	{
		return false;
	}
}
