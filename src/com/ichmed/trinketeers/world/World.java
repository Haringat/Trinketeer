package com.ichmed.trinketeers.world;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.entity.Player;
import com.ichmed.trinketeers.entity.Torch;
import com.ichmed.trinketeers.entity.mob.FlameElemental;
import com.ichmed.trinketeers.entity.mob.Zombie;
import com.ichmed.trinketeers.util.AxisAllignedBoundingBox;
import com.ichmed.trinketeers.util.render.GLHelper;
import com.ichmed.trinketeers.util.render.GraphicSorterYAxis;
import com.ichmed.trinketeers.util.render.IGraphic;
import com.ichmed.trinketeers.util.render.IWorldGraphic;
import com.ichmed.trinketeers.util.render.TextureLibrary;
import com.ichmed.trinketeers.util.render.TrueTypeFont;
import com.ichmed.trinketeers.util.render.light.ILight;
import com.ichmed.trinketeers.util.render.light.LightRenderer;

public class World
{
	public Player player = new Player();
	List<IGraphic> uiGraphics = new ArrayList<>();
	public Level currentLevel;
	public int level = 0;
	
	public final String name = "world_0";

	public World()
	{
		currentLevel = new Level(this, level);
		currentLevel.init();
		 LightRenderer.setAmbientLight(1f, 1f, 1f);
//		LightRenderer.setAmbientLight(0.0f, 0.0f, 0.0f);
		spawn(player);

		uiGraphics.add(new IGraphic()
		{

			@Override
			public void render()
			{
				boolean b1 = player.currentSpellLeft != null;
				boolean b2 = player.currentSpellRight != null;

				TextureLibrary.bindTexture("resc/textures/scroll.png");
				if(b1)GLHelper.drawRect(-1, 0.8f, 0.2f, 0.2f);
				if(b2)GLHelper.drawRect(0.8f, 0.8f, 0.2f, 0.2f);

				TextureLibrary.bindTexture("resc/textures/spellCooldownBar.png");
				if (b1) GLHelper.drawRect(-0.79f, 0.8f, 0.02f, 0.2f - Math.max(0, ((float) player.shotCooldownLeft / (float) player.currentSpellLeft.cooldown) * 0.2f));
				if (b2) GLHelper.drawRect(0.78f, 0.8f, 0.02f, 0.2f - Math.max(0, ((float) player.shotCooldownRight / (float) player.currentSpellRight.cooldown) * 0.2f));

				if (b1)
				{
					TextureLibrary.bindTexture("resc/textures/spells/" + player.currentSpellLeft.texture);
					GLHelper.drawRect(-0.975f, 0.825f, 0.15f, 0.15f);
				}

				if (b2)
				{
					TextureLibrary.bindTexture("resc/textures/spells/" + player.currentSpellRight.texture);
					GLHelper.drawRect(0.825f, 0.825f, 0.15f, 0.15f);
				}
				if (b1)
				{
					GLHelper.renderText(-0.98f, 0.76f, player.currentSpellLeft.getName());
					GLHelper.renderText(-0.98f, 0.70f, "" + (int) player.currentSpellLeft.getManaCost());
				}
				if (b2)
				{
					GLHelper.renderText(0.98f, 0.76f, player.currentSpellRight.getName(), 0.001f, 0.001f, TrueTypeFont.ALIGN_RIGHT);
					GLHelper.renderText(0.98f, 0.70f, "" + (int) player.currentSpellRight.getManaCost(), 0.001f, 0.001f, TrueTypeFont.ALIGN_RIGHT);

				}
			}
		});

		uiGraphics.add(new IGraphic()
		{

			@Override
			public void render()
			{
				float mana = 10 * (player.mana / player.maxMana);
				float start = -0.5f;
				TextureLibrary.bindTexture("resc/textures/manaBarEmpty.png");
				GLHelper.renderBar(start, 0.92f, 10, 1);
				TextureLibrary.bindTexture("resc/textures/manaBarFull.png");
				GLHelper.renderBar(start + (10 - mana) / 20, 0.92f, mana, 1);
				GLHelper.renderText(0, 0.9f, "" + (int) player.mana, 0.002f, 0.002f, TrueTypeFont.ALIGN_CENTER);
			}
		});

		uiGraphics.add(new IGraphic()
		{

			@Override
			public void render()
			{
				float health = 10 * (player.health / player.maxHealth);
				float start = -0.5f;
				TextureLibrary.bindTexture("resc/textures/healthBarEmpty.png");
				GLHelper.renderBar(start, 0.84f, 10, 1);
				TextureLibrary.bindTexture("resc/textures/healthBarFull.png");
				GLHelper.renderBar(start + (10 - health) / 20, 0.84f, health, 1);
				GLHelper.renderText(0, 0.82f, "" + (int) player.health, 0.002f, 0.002f, TrueTypeFont.ALIGN_CENTER);

				GLHelper.renderText(0, -0.82f, "" + "Coins: " + player.coins, 0.002f, 0.002f, TrueTypeFont.ALIGN_CENTER);
			}
		});
	}

	public void nextLevel()
	{
		this.level++;
		this.currentLevel = new Level(this, this.level);
		this.currentLevel.init();
		spawn(player);
	}

	public void generateZombies(int amount)
	{
		for (int i = 0; i < amount; i++)
			spawn((new Zombie()).setPosition(new Vector2f((float) Math.random() - 0.5f, (float) Math.random() * 0.5f)));
	}

	public void generateFlameElementals(int amount)
	{
		for (int i = 0; i < amount; i++)
			spawn((new FlameElemental()).setPosition(new Vector2f((float) Math.random() - 0.5f, (float) Math.random() * 0.5f)));
	}

	public void generateTorches(int amount)
	{
		for (int i = 0; i < amount; i++)
			spawn((new Torch()).setPosition(new Vector2f((float) (Math.random() - 0.5) * 4, (float) (Math.random() - 0.5) * 4)));
	}

	public void addLight(ILight l)
	{
		if (!currentLevel.lights.contains(l)) currentLevel.lights.add(l);
	}

	public void removeLight(ILight l)
	{
		currentLevel.lights.remove(l);
	}

	public void tick()
	{
		if (currentLevel.entities.size() > 0) currentLevel.entitiesNextTick = new ArrayList<Entity>(currentLevel.entities);
		for (Entity e : currentLevel.entities)
			e.tick(this);
		currentLevel.entities = new ArrayList<>(currentLevel.entitiesNextTick);
		render();
	}

	public void render()
	{
//		GLHelper.renderBackground(this);
		glPushMatrix();
		Vector2f v = player.getCenter();
		glTranslatef(-v.x, -v.y, 0);
		renderChunks();
		currentLevel.worldGraphics.sort(new GraphicSorterYAxis());
		for (IWorldGraphic g : currentLevel.worldGraphics)
		{
			g.render(this);
		}
		glPopMatrix();
		LightRenderer.renderLights(this, currentLevel.lights);
		for (IGraphic g : uiGraphics)
			g.render();
	}

	private void renderChunks()
	{
		for(int i = -1; i < 17; i++)
			for(int j = -1; j < 17; j++)
			{
				int x = i + (int)((player.getCenter().x) * 8);
				int y = j + (int)((player.getCenter().y) * 8);
				Tile.tiles[Chunk.getTile(x, y)].render(x, y);
			}
	}

	public boolean removeEntity(Entity e)
	{
		currentLevel.entitiesNextTick.remove(e);
		currentLevel.worldGraphics.remove(e);
		currentLevel.shadows.remove(e);
		return currentLevel.entitiesNextTick.contains(e);
	}

	public List<Entity> getListOfIntersectingEntities(Entity e, boolean onlySolids)
	{
		return getListOfIntersectingEntities(e.getColissionBox(), e.getEntitiesExcludedFromCollision(), onlySolids);
	}

	public List<Entity> getListOfIntersectingEntities(AxisAllignedBoundingBox aabb, boolean onlySolids)
	{
		return getListOfIntersectingEntities(aabb, null, onlySolids);
	}

	public List<Entity> getListOfIntersectingEntities(AxisAllignedBoundingBox aabb, List<Entity> exclude, boolean onlySolids)
	{
		ArrayList<Entity> list = new ArrayList<>();
		for (Entity f : currentLevel.entitiesNextTick)
			if ((exclude == null || !(exclude.contains(f))) && AxisAllignedBoundingBox.doAABBsIntersect(aabb, f.getColissionBox())) if (!onlySolids || f.isSolid) list.add(f);
		return list;
	}

	public boolean spawn(Entity e)
	{
		return this.spawn(e, true);
	}

	public boolean spawn(Entity e, boolean checkForColission)
	{
		return this.spawn(e, checkForColission, true);
	}

	public boolean spawn(Entity e, boolean checkForColission, boolean checkSolidsOnly)
	{
		return this.currentLevel.spawn(e, checkForColission, checkSolidsOnly);
	}

	public List<Entity> getEntitiesByDistance(Entity source, float maxDistance)
	{
		List<Entity> l = new ArrayList<>(this.currentLevel.entitiesNextTick);
		l.sort(new Comparator<Entity>()
		{

			@Override
			public int compare(Entity o1, Entity o2)
			{
				float distA = new Vector2f(source.position.x - o1.position.x, source.position.y - o1.position.y).length();
				float distB = new Vector2f(source.position.x - o2.position.x, source.position.y - o2.position.y).length();
				return distA < distB ? -1 : distA == distB ? 0 : 1;
			}
		});
		List<Entity> temp = new ArrayList<>(l);
		l.remove(source);
		if (maxDistance > 0) for (Entity e : temp)
			if (new Vector2f(source.position.x - e.position.x, source.position.y - e.position.y).length() > maxDistance) l.remove(e);
		return l;
	}

	public Entity getClosestEntityToSource(Entity source, float maxDistance, Class<? extends Entity> entityClass)
	{
		return getClosestEntityToSource(source, maxDistance, entityClass, true);
	}
	

	public Entity getClosestEntityToSource(Entity source, float maxDistance, Class<? extends Entity> entityClass, boolean alliveOnly)
	{
		List<Entity> l = getEntitiesByDistance(source, maxDistance);
		for (Entity e : l)
			if (!e.isDead && entityClass.isAssignableFrom(e.getClass())) return e;
		return null;
	}
}
