package com.ichmed.trinketeers.entity.particle;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.util.render.RenderUtil;
import com.ichmed.trinketeers.util.render.TextureLibrary;
import com.ichmed.trinketeers.world.World;

public class Particle extends Entity
{
	private String texture;
	private Vector2f textureOffSet = new Vector2f();
	private Vector2f scale = new Vector2f(1, 1);
	public Vector2f center;

	public void setCenter(Vector2f center)
	{
		this.center = center;
	}

	public Particle(World w)
	{
		super(w);
		this.isSolid = false;
		this.isEssential = false;
		this.despawnCountDown = 1;
	}

	@Override
	protected void actualRender(World w)
	{
		float yMod = this.ticksExisted < 30 ? (float) (Math.sin(this.ticksExisted / 30d * Math.PI) * 0.1f) : 0;
		RenderUtil.renderTexturedQuad(this.position.x, this.position.y + yMod, this.size.x, this.size.y, TextureLibrary.getTextureVector(this.texture), this.textureOffSet.x, this.textureOffSet.y,
				this.scale.x, this.scale.y);
	}

	@Override
	public boolean isHostile()
	{
		return false;
	}

	public void setLifeSpan(int i)
	{
		this.lifespan = i;
	}

	public void setTexture(String s)
	{
		this.texture = s;
	}

	public void setTextureOffSet(int x, int y)
	{
		this.textureOffSet = new Vector2f(x, y);
	}

	public void setTextureOffSet(Vector2f v)
	{
		this.textureOffSet = new Vector2f(v);
	}

	public void setScale(float x, float y)
	{
		this.scale = new Vector2f(x, y);
	}

	public void setScale(Vector2f v)
	{
		this.scale = new Vector2f(v);
	}
}
