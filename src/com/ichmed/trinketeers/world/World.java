package com.ichmed.trinketeers.world;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.ichmed.trinketeers.Game;
import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.entity.Player;
import com.ichmed.trinketeers.entity.mob.FlameElemental;
import com.ichmed.trinketeers.entity.mob.Zombie;
import com.ichmed.trinketeers.entity.pickup.Torch;
import com.ichmed.trinketeers.util.AxisAllignedBoundingBox;
import com.ichmed.trinketeers.util.DataRef;
import com.ichmed.trinketeers.util.InputUtil;
import com.ichmed.trinketeers.util.render.RenderUtil;
import com.ichmed.trinketeers.util.render.GraphicSorterYAxis;
import com.ichmed.trinketeers.util.render.IGraphic;
import com.ichmed.trinketeers.util.render.IWorldGraphic;
import com.ichmed.trinketeers.util.render.TextureLibrary;
import com.ichmed.trinketeers.util.render.TrueTypeFont;
import com.ichmed.trinketeers.util.render.light.ILight;
import com.ichmed.trinketeers.util.render.light.LightRenderer;
import com.ichmed.trinketeers.world.tile.Tile;

public class World
{
	List<IGraphic> uiGraphics = new ArrayList<>();
	List<IWorldGraphic> worldGraphics = new ArrayList<>();
	public List<ILight> lights = new ArrayList<>();
	public Player player = new Player(this);
	public Vector3f currentCluster = Chunk.getClusterForPoint(this, player.position);
	public int ageInYears = 0;

	public int ticksSinceLaunch;

	public static final Vector3f LIGHT_DAYTIME = new Vector3f(0.5f, 0.5f, 0.65f), LIGHT_FULL_DARK = new Vector3f();

	public final String name = "world_0";

	/*
	 * Handle with care!
	 */
	public static World wordObj;

	public World()
	{
		// LightRenderer.setAmbientLight(1f, 1f, 1f);
		LightRenderer.setAmbientLight(0.0f, 0.0f, 0.0f);
		spawn(player);
		wordObj = this;

		uiGraphics.add(new IGraphic()
		{

			int degLeft, degRight;
			float accLeft, accRight;

			@Override
			public void render()
			{
				if (degLeft % 360 == 0) degLeft = 0;
				if (player.currentSpellLeft != null)
				{
					if (accLeft < 20) accLeft += player.currentSpellLeft.charge / 100f;
					degLeft += accLeft;
					if (player.currentSpellLeft.charge == 0)
					{
						if (accLeft > 2) accLeft -= 10;
						else accLeft = 0;
					}
				}
				degLeft++;
				if (degRight % 360 == 0) degRight = 0;
				if (player.currentSpellRight != null)
				{
					if (accRight < 20) accRight += player.currentSpellRight.charge / 100f;
					degRight += accRight;
					if (player.currentSpellRight.charge == 0)
					{
						if (accRight > 2) accRight -= 10;
						else accRight = 0;
					}
				}
				degRight++;
				Vector2f v = InputUtil.getMouseRelativToScreenCenter();
				glPushMatrix();
				glTranslatef(v.x, v.y, 0);
				RenderUtil.renderTexturedQuad(-0.0625f, -0.0625f, 0.125f, 0.125f, "crosshair_1");
				glRotated(degLeft, 0, 0, 1);
				RenderUtil.renderTexturedQuad(-0.0625f, -0.0625f, 0.125f, 0.125f, "crosshair_0");
				glRotated(-degLeft, 0, 0, 1);
				glRotated(-degRight, 0, 0, 1);
				RenderUtil.renderTexturedQuad(-0.0625f, -0.0625f, 0.125f, 0.125f, "crosshair_2");
				glPopMatrix();
			}
		});

		uiGraphics.add(new IGraphic()
		{
			@Override
			public void render()
			{
				boolean b1 = player.currentSpellLeft != null;
				boolean b2 = player.currentSpellRight != null;

				if (b1) RenderUtil.renderTexturedQuad(-1, 0.8f, 0.2f, 0.2f, "scroll");
				if (b2) RenderUtil.renderTexturedQuad(0.8f, 0.8f, 0.2f, 0.2f, "scroll");

				if (b1)
				{
					RenderUtil.renderTexturedQuad(-0.79f, 0.8f, 0.02f, 0.2f - Math.max(0, ((float) player.currentSpellLeft.cooldown / (float) player.currentSpellLeft.maxCooldown) * 0.2f),
							"spellCooldownBar");
					RenderUtil.renderTexturedQuad(-0.76f, 0.8f, 0.02f, Math.max(0, ((float) player.currentSpellLeft.charge / (float) player.currentSpellLeft.maxCooldown) * 0.2f), "spellChargeBar");

				}
				if (b2)
				{
					RenderUtil.renderTexturedQuad(0.78f, 0.8f, 0.02f, 0.2f - Math.max(0, ((float) player.currentSpellRight.cooldown / (float) player.currentSpellRight.maxCooldown) * 0.2f),
							"spellCooldownBar");
					RenderUtil.renderTexturedQuad(0.75f, 0.8f, 0.02f, Math.max(0, ((float) player.currentSpellRight.charge / (float) player.currentSpellRight.maxCooldown) * 0.2f), "spellChargeBar");
				}

				if (b1)
				{
					RenderUtil.renderTexturedQuad(-0.975f, 0.825f, 0.15f, 0.15f, player.currentSpellLeft.element.toLowerCase() + "Projectile");
				}

				if (b2)
				{
					RenderUtil.renderTexturedQuad(0.825f, 0.825f, 0.15f, 0.15f, player.currentSpellRight.element.toLowerCase() + "Projectile");
				}
				if (b1)
				{
					RenderUtil.renderText(-0.98f, 0.76f, player.currentSpellLeft.getName());
					RenderUtil.renderText(-0.98f, 0.70f, "" + (int) player.currentSpellLeft.getManaCost());
				}
				if (b2)
				{
					RenderUtil.renderText(0.98f, 0.76f, player.currentSpellRight.getName(), 0.001f, 0.001f, TrueTypeFont.ALIGN_RIGHT);
					RenderUtil.renderText(0.98f, 0.70f, "" + (int) player.currentSpellRight.getManaCost(), 0.001f, 0.001f, TrueTypeFont.ALIGN_RIGHT);
				}
			}
		});

		uiGraphics.add(new IGraphic()
		{

			@Override
			public void render()
			{
				float mana = player.mana / player.maxMana;
				float start = -0.5f;
				RenderUtil.renderTexturedQuad(start, 0.92f, 1, 0.05f, "manaBarEmpty");
				RenderUtil.renderTexturedQuad(start + (1 - mana) / 2f, 0.92f, mana, 0.05f, "manaBarFull");
				RenderUtil.renderText(0, 0.9f, "" + (int) player.mana, 0.002f, 0.002f, TrueTypeFont.ALIGN_CENTER);
			}
		});

		uiGraphics.add(new IGraphic()
		{
			@Override
			public void render()
			{
				if (!Game.debugMode) return;
				RenderUtil.renderText(-1f, -0.9f, "E: " + Chunk.getAllLoadedEntities().size());
				RenderUtil.renderText(-1, -0.5f, "X: " + player.position.x);
				RenderUtil.renderText(-1, -0.55f, "Y: " + player.position.y);
				RenderUtil.renderText(-1, -0.6f, "Height: " + (int) player.position.z);
				Chunk c = Chunk.getChunk(World.wordObj, player.position);
				RenderUtil.renderText(-1, -0.67f, "Chunk: " + Chunk.getHashString(c.posX, c.posY, c.posZ));
				Vector3f v = Chunk.getClusterForChunk(wordObj, c);
				RenderUtil.renderText(-1, -0.72f, "Cluster: " + v.x + " " + v.y + " " + v.z);
			}
		});

		uiGraphics.add(new IGraphic()
		{
			@Override
			public void render()
			{
				float health = (player.health / player.maxHealth);
				float start = -0.5f;
				RenderUtil.renderTexturedQuad(start, 0.85f, 1, 0.05f, "healthBarEmpty");
				RenderUtil.renderTexturedQuad(start + (1 - health) / 2f, 0.85f, health, 0.05f, "healthBarFull");
				RenderUtil.renderText(0, 0.83f, "" + (int) player.health, 0.002f, 0.002f, TrueTypeFont.ALIGN_CENTER);
			}
		});
	}

	public void generateZombies(int amount, int layer)
	{
		for (int i = 0; i < amount; i++)
			spawn((new Zombie(this)).setCenter(new Vector3f((float) Math.random() - 0.5f, (float) Math.random() * 0.5f, layer)));
	}

	public void generateFlameElementals(int amount, int layer)
	{
		for (int i = 0; i < amount; i++)
			spawn((new FlameElemental(this)).setCenter(new Vector3f((float) Math.random() - 0.5f, (float) Math.random() * 0.5f, layer)));
	}

	public void generateTorches(int amount)
	{
		for (int i = 0; i < amount; i++)
			spawn((new Torch(this)).setPosition(new Vector2f((float) (Math.random() - 0.5) * 4, (float) (Math.random() - 0.5) * 4)));
	}

	public void addLight(ILight l)
	{
		lights.add(l);
	}

	public void removeLight(ILight l)
	{
		lights.remove(l);
	}

	public void tick()
	{
		ticksSinceLaunch++;
		currentCluster = Chunk.getClusterForPoint(this, player.position);
		if (ticksSinceLaunch % 20 == 0) Chunk.unloadUnneededChunks(this);
		if (player.position.z >= 0) LightRenderer.setAmbientLight(LIGHT_DAYTIME);
		else LightRenderer.setAmbientLight(LIGHT_FULL_DARK);
		for (Entity e : Chunk.getAllLoadedEntitiesForLayer((int) player.position.z))
			e.tick(this);
		render();
	}

	public void render()
	{
		// GLHelper.renderBackground(this);
		glPushMatrix();
		Vector3f v = player.getCenter();
		glTranslatef(-v.x, -v.y, 0);
		renderChunks(false);
		worldGraphics.sort(new GraphicSorterYAxis());
		for (IWorldGraphic g : worldGraphics)
			if (g.shouldRender(this)) g.render(this);
		renderChunks(true);
		glPopMatrix();
		LightRenderer.renderLights(this, lights);
		TextureLibrary.bindTexture(DataRef.defaultLibrary + ".ktx");
		for (IGraphic g : uiGraphics)
			g.render();
	}

	private void renderChunks(boolean b)
	{
		for (int i = -2 * Chunk.chunkSize; i < 2 * Chunk.chunkSize + 10; i++)
			for (int j = -2 * Chunk.chunkSize; j < 2 * Chunk.chunkSize + 10; j++)
			{
				int x = i + (int) ((player.getCenter().x) * Chunk.chunkSize);
				int y = j + (int) ((player.getCenter().y) * Chunk.chunkSize);
				Tile t = Tile.tiles[Chunk.getTile(this, x, y, (int) player.position.z)];
				if (b) t.renderOnTop(this, x, y);
				else t.renderBeneath(this, x, y);
			}
	}

	public boolean removeEntity(Entity e)
	{
		removeEntityFromChunk(e);
		worldGraphics.remove(e);
		return true;
	}

	public List<Entity> getListOfIntersectingEntities(Entity e, boolean onlySolids, int layer)
	{
		return getListOfIntersectingEntities(e.getColissionBox(), e.getEntitiesExcludedFromCollision(), onlySolids, layer);
	}

	public List<Entity> getListOfIntersectingEntities(AxisAllignedBoundingBox aabb, boolean onlySolids, int layer)
	{
		return getListOfIntersectingEntities(aabb, null, onlySolids, layer);
	}

	public List<Entity> getListOfIntersectingEntities(AxisAllignedBoundingBox aabb, List<Entity> exclude, boolean onlySolids, int layer)
	{
		ArrayList<Entity> list = new ArrayList<>();
		for (Entity f : Chunk.getAllLoadedEntitiesForLayer(layer))
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
		if (checkForColission)
		{
			AxisAllignedBoundingBox aabb = e.getColissionBox();
			if (getListOfIntersectingEntities(aabb, checkSolidsOnly, (int) e.position.z).size() > 0) return false;
		}
		this.addEntityToChunk(e);
		this.worldGraphics.add(e);
		// if (e instanceof IShadow) this.shadows.add((IShadow) e);
		e.onSpawn(this);
		return true;
	}

	public void addEntityToChunk(Entity e)
	{
		Chunk.getChunk(this, new Vector3f(e.position)).entities.add(e);
	}

	public void removeEntityFromChunk(Entity e)
	{
		List<Entity> l = Chunk.getChunk(this, new Vector3f(e.position)).entities;
		while (l.contains(e))
			l.remove(e);
	}

	public List<Entity> getEntitiesByDistance(Entity source, float maxDistance)
	{
		List<Entity> l = new ArrayList<>(Chunk.getAllLoadedEntitiesForLayer((int) source.position.z));
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

	public int getNumberOfEnemies(int layer)
	{
		int i = 0;
		for (Entity e : Chunk.getAllLoadedEntities())
			if (e.isHostile() && e.position.z == layer) i++;
		return i;
	}

	public boolean isPositionStuckInGeometry(AxisAllignedBoundingBox predictedPosition, int height)
	{
		if (isPointStuckInGeometry(new Vector3f(predictedPosition.pos.x, predictedPosition.pos.y, height))) return true;
		if (isPointStuckInGeometry(new Vector3f(predictedPosition.pos.x + predictedPosition.size.x, predictedPosition.pos.y, height))) return true;
		if (isPointStuckInGeometry(new Vector3f(predictedPosition.pos.x + predictedPosition.size.x, predictedPosition.pos.y + predictedPosition.size.y, height))) return true;
		if (isPointStuckInGeometry(new Vector3f(predictedPosition.pos.x, predictedPosition.pos.y + predictedPosition.size.y, height))) return true;

		return false;
	}

	public boolean isPointStuckInGeometry(Vector3f point)
	{
		int pointX = (int) (point.x * Chunk.chunkSize);
		int pointY = (int) (point.y * Chunk.chunkSize);
		if (point.x < 0) pointX--;
		if (point.y < 0) pointY--;
		return Tile.tiles[Chunk.getTile(this, pointX, pointY, (int) point.z)].massive;
	}

	public void cleanUp()
	{
		Chunk.saveAllChunks(this);
	}
}
