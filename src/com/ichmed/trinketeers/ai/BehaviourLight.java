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
	
	
	public BehaviourLight(World w, Object...objects)
	{
		light = new SimpleLight();
		color = (Vector4f) objects[0];
		this.brightness = (Float)objects[1];
		this.flicker = (Float)objects[2];
		w.addLight(light);
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
		light.setPosition(new Vector2f(performer.getCenter()));
		light.setColor((Vector4f) (new Vector4f(color).scale(brightness + (float) Math.sin(performer.ticksExisted / 1000d) * flicker)));
		light.setLayer((int) performer.position.z);
		return true;
	}

	@Override
	public void cleanUp(World w)
	{
		w.lights.remove(this.light);
	}
}
