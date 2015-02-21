package com.ichmed.trinketeers.entity.pickup;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import com.ichmed.trinketeers.entity.Player;
import com.ichmed.trinketeers.util.render.light.ILight;
import com.ichmed.trinketeers.util.render.light.SimpleLight;
import com.ichmed.trinketeers.world.World;

public class ManaBubble extends Pickup
{
	public ManaBubble(World w, float min, float max)
	{
		super(w);
		this.entityType = "manaBubble";
		this.mana = min + (float) Math.random() * (max - min);
		this.size.x = this.size.y = (float) (Math.sqrt(this.mana)) / 100f;
		this.movementDelay = 5;
	}

	public ManaBubble(World w)
	{
		this(w, 5, 10);
	}

	public float mana = 1;

	@Override
	public boolean pickUp(World w, Player p)
	{
		if(p.mana < p.maxMana) p.mana += Math.min(this.mana, p.maxMana - p.mana);
		p.maxMana += 0.1f;
		return true;
	}

	@Override
	public void tick(World world)
	{
		if(this.mana < 1) this.kill(world);
		super.tick(world);
	}

	public ILight createLight()
	{
		SimpleLight l = new SimpleLight();
		l.setPosition(new Vector2f(this.getCenter()));
		l.setColor(new Vector4f(.14f, .14f, .6f, 1));
		return l;
	}
}
