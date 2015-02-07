package com.ichmed.trinketeers.entity.pickup;

import org.lwjgl.util.vector.Vector4f;

import com.ichmed.trinketeers.entity.Player;
import com.ichmed.trinketeers.util.render.light.ILight;
import com.ichmed.trinketeers.util.render.light.SimpleLight;
import com.ichmed.trinketeers.world.World;

public class ManaBottle extends Pickup
{
	public ManaBottle(float min, float max)
	{
		this.texture = "resc/textures/" + "manaBottle.png";
		this.mana = min + (float) Math.random() * (max - min);
		this.size.x = this.size.y = (float) (Math.sqrt(this.mana)) / 100f;
		this.movementDelay = 15;
	}

	public ManaBottle()
	{
		this(15, 30);
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
