package com.ichmed.trinketeers.entity.pickup;

import org.lwjgl.util.vector.Vector4f;

import com.ichmed.trinketeers.entity.Player;
import com.ichmed.trinketeers.util.render.light.ILight;
import com.ichmed.trinketeers.util.render.light.SimpleLight;
import com.ichmed.trinketeers.world.World;

public class ManaBottle extends Pickup
{
	public ManaBottle(World w, float min, float max)
	{
		super(w);
		this.name = "healthBottle";
		this.mana = min + (float) Math.random() * (max - min);
		this.size.x = this.size.y = (float) (Math.sqrt(this.mana)) / 100f;
		this.movementDelay = 15;
	}

	public ManaBottle(World w)
	{
		this(w, 15, 30);
	}

	public float mana = 1;

	@Override
	public boolean pickUp(World w, Player p)
	{
		if (p.mana < p.maxMana)
		{
			p.mana += Math.min(this.mana, p.maxMana - p.mana);
			return true;
		}
		return false;
	}

	public ILight createLight()
	{
		SimpleLight l = new SimpleLight();
		l.setColor(new Vector4f(1.4f, 1.4f, 6f, 1));
		return l;
	}

	@Override
	public boolean movesTowardPlayer()
	{
		return false;
	}

}
