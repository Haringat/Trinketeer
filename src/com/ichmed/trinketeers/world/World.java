package com.ichmed.trinketeers.world;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

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
import com.ichmed.trinketeers.world.tile.Tile;

public class World
{
	public Player player = new Player();
	List<IGraphic> uiGraphics = new ArrayList<>();
	List<IWorldGraphic> worldGraphics = new ArrayList<>();
	List<ILight> lights = new ArrayList<>();
	
	public static final Vector3f LIGHT_DAYTIME = new Vector3f(0.5f, 0.5f, 0.65f), LIGHT_FULL_DARK = new Vector3f();

	public final String name = "world_0";

	public World()
	{
//		LightRenderer.setAmbientLight(1f, 1f, 1f);
		LightRenderer.setAmbientLight(0.0f, 0.0f, 0.0f);
		spawn(player);

		uiGraphics.add(new IGraphic()
		{

			@Override
			public void render()
			{
				boolean b1 = player.currentSpellLeft != null;
				boolean b2 = player.currentSpellRight != null;

				if (b1) GLHelper.renderTexturedQuad(-1, 0.8f, 0.2f, 0.2f, "scroll");
				if (b2) GLHelper.renderTexturedQuad(0.8f, 0.8f, 0.2f, 0.2f, "scroll");

				if (b1) GLHelper.renderTexturedQuad(-0.79f, 0.8f, 0.02f, 0.2f - Math.max(0, ((float) player.shotCooldownLeft / (float) player.currentSpellLeft.cooldown) * 0.2f), "spellCooldownBar");
				if (b2) GLHelper.renderTexturedQuad(0.78f, 0.8f, 0.02f, 0.2f - Math.max(0, ((float) player.shotCooldownRight / (float) player.currentSpellRight.cooldown) * 0.2f), "spellCooldownBar");

				if (b1)
				{
					GLHelper.renderTexturedQuad(-0.975f, 0.825f, 0.15f, 0.15f, player.currentSpellLeft.element.toLowerCase() + "Projectile");
				}

				if (b2)
				{
					GLHelper.renderTexturedQuad(0.825f, 0.825f, 0.15f, 0.15f, player.currentSpellRight.element.toLowerCase() + "Projectile");
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
				float mana = player.mana / player.maxMana;
				float start = -0.5f;
				GLHelper.renderTexturedQuad(start, 0.92f, 1, 0.05f, "manaBarEmpty");
				GLHelper.renderTexturedQuad(start + (1 - mana) / 2f, 0.92f, mana, 0.05f, "manaBarFull");
				GLHelper.renderText(0, 0.9f, "" + (int) player.mana, 0.002f, 0.002f, TrueTypeFont.ALIGN_CENTER);
			}
		});

		uiGraphics.add(new IGraphic()
		{

			@Override
			public void render()
			{
				float health = (player.health / player.maxHealth);
				float start = -0.5f;
				GLHelper.renderTexturedQuad(start, 0.85f, 1, 0.05f, "healthBarEmpty");
				GLHelper.renderTexturedQuad(start + (1 - health) / 2f, 0.85f, health, 0.05f, "healthBarFull");
				GLHelper.renderText(0, 0.83f, "" + (int) player.health, 0.002f, 0.002f, TrueTypeFont.ALIGN_CENTER);
				

				GLHelper.renderText(0,  -1f, "Level: " + player.position.z, 0.002f, 0.002f, TrueTypeFont.ALIGN_CENTER);
			}
		});
	}
	public void generateZombies(int amount, int layer)
	{
		for (int i = 0; i < amount; i++)
			spawn((new Zombie()).setCenter(new Vector3f((float) Math.random() - 0.5f, (float) Math.random() * 0.5f, layer)));
	}

	public void generateFlameElementals(int amount, int layer)
	{
		for (int i = 0; i < amount; i++)
			spawn((new FlameElemental()).setCenter(new Vector3f((float) Math.random() - 0.5f, (float) Math.random() * 0.5f, layer)));
	}

	public void generateTorches(int amount)
	{
		for (int i = 0; i < amount; i++)
			spawn((new Torch()).setPosition(new Vector2f((float) (Math.random() - 0.5) * 4, (float) (Math.random() - 0.5) * 4)));
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
		if(player.position.z == 0) LightRenderer.setAmbientLight(LIGHT_DAYTIME);
		else LightRenderer.setAmbientLight(LIGHT_FULL_DARK);
		for (Entity e : Chunk.getAllLoadedEntitiesForLayer((int)player.position.z))	
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
			if(g.shouldRender(this)) g.render(this);
		renderChunks(true);
		glPopMatrix();
		LightRenderer.renderLights(this, lights);
		TextureLibrary.rebind();
		for (IGraphic g : uiGraphics)
			g.render();
	}

	private void renderChunks(boolean b)
	{
		for (int i = -Chunk.chunkSize - 1; i < Chunk.chunkSize + 1; i++)
			for (int j = -Chunk.chunkSize - 1; j < Chunk.chunkSize + 1; j++)
			{
				int x = i + (int) ((player.getCenter().x) * Chunk.chunkSize);
				int y = j + (int) ((player.getCenter().y) * Chunk.chunkSize);
				Tile t = Tile.tiles[Chunk.getTile(this, x, y, (int)player.position.z)];
				if (t.renderInFront(this, x, y) == b) t.render(this, x, y);
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
			if (getListOfIntersectingEntities(aabb, checkSolidsOnly, (int)e.position.z).size() > 0) return false;
		}
		this.addEntityToChunk(e);
		this.worldGraphics.add(e);
//		if (e instanceof IShadow) this.shadows.add((IShadow) e);
		e.onSpawn(this);
		return true;
	}
	
	public void addEntityToChunk(Entity e)
	{
		Chunk.getChunk(this, new Vector3f(e.position)).entities.add(e);
	}
	
	public void removeEntityFromChunk(Entity e)
	{
		Chunk.getChunk(this, new Vector3f(e.position)).entities.remove(e);
	}

	public List<Entity> getEntitiesByDistance(Entity source, float maxDistance)
	{
		List<Entity> l = new ArrayList<>(Chunk.getAllLoadedEntitiesForLayer((int)source.position.z));
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
		int x1 = (int) (predictedPosition.pos.x * 8);
		int x2 = (int) ((predictedPosition.pos.x + predictedPosition.size.x) * 8);
		int y1 = (int) (predictedPosition.pos.y * 8);
		int y2 = (int) ((predictedPosition.pos.y + predictedPosition.size.y) * 8);

		if (predictedPosition.pos.x <= 0) x1--;
		if ((predictedPosition.pos.x + predictedPosition.size.x) > 0) x2++;
		if (predictedPosition.pos.y < 0) y1--;
		if((predictedPosition.pos.y + predictedPosition.size.y) > 0)y2++;

		int i = x1;
		do
		{
			int j = y1;
			do
			{

				if (isPointStuckInGeometry(new Vector3f(i, j, height))) return true;
				j++;
			} while (j < y2);
			i++;
		} while (i < x2);

		return false;
	}

	public boolean isPointStuckInGeometry(Vector3f point)
	{
		int pointX = (int) (point.x);
		int pointY = (int) (point.y);
		return Tile.tiles[Chunk.getTile(this, pointX, pointY, (int) point.z)].massive;
	}
}
