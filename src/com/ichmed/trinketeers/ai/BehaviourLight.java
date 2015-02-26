package com.ichmed.trinketeers.ai;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.util.render.light.SimpleLight;
import com.ichmed.trinketeers.world.World;

public class BehaviourLight extends Behaviour
{
	public SimpleLight light;
	private Vector4f color;
	private float brightness, flicker;
	private boolean init = false;
	
	public BehaviourLight(World w, String...objects)
	{
		light = new SimpleLight();
		float r = Float.valueOf(objects[0]);
		float g = Float.valueOf(objects[1]);
		float b = Float.valueOf(objects[2]);
		float a = Float.valueOf(objects[3]);
		
		this.color = new Vector4f(r, g, b, a);
		
		this.brightness = Float.valueOf(objects[4]);
		this.flicker = Float.valueOf(objects[5]);
		w.addLight(light);
		this.light.setActive(false);
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
		if(!init)
		{
			init = true;
			light.setActive(true);
		}
		light.setPosition(new Vector2f(performer.getCenter()));
		light.setColor((Vector4f) (new Vector4f(color).scale(brightness + (int)(((float) Math.sin(performer.ticksExisted / 30d) + 1) * flicker) * 0.1f)) );
		light.setLayer((int) performer.position.z);
		return true;
	}

	@Override
	public void cleanUp(World w)
	{
		w.removeLight(this.light);
	}
}
