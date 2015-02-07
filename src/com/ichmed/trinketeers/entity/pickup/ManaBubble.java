package com.ichmed.trinketeers.entity.pickup;

import org.lwjgl.util.vector.Vector4f;

import com.ichmed.trinketeers.entity.Player;
import com.ichmed.trinketeers.util.render.light.ILight;
import com.ichmed.trinketeers.util.render.light.SimpleLight;
import com.ichmed.trinketeers.world.World;

public class ManaBubble extends Pickup
{
	public ManaBubble(float min, float max)
	{
		this.texture = "resc/textures/" + "manaBubble.png";
		this.mana = min + (float) Math.random() * (max - min);
		this.size.x = this.size.y = (float) (Math.sqrt(this.mana)) / 100f;
		this.movementDelay = 5;
	}

	public ManaBubble()
	{
		this(5, 10);
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
		l.setPosition(this.getCenter());
		l.setColor(new Vector4f(.14f, .14f, .6f, 1));
		return l;
	}
}
