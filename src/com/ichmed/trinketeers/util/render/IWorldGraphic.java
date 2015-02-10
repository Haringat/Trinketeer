package com.ichmed.trinketeers.util.render;

import com.ichmed.trinketeers.world.World;

public interface IWorldGraphic extends IGraphic
{
	public float getY();
	public void render(World w);
	public boolean shouldRender(World w);
}
