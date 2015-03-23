package com.ichmed.trinketeers.entity;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.ichmed.trinketeers.Game;
import com.ichmed.trinketeers.ai.Behaviour;
import com.ichmed.trinketeers.ai.waypoint.Waypoint;
import com.ichmed.trinketeers.entity.particle.Particle;
import com.ichmed.trinketeers.savefile.data.EntityData;
import com.ichmed.trinketeers.util.AxisAllignedBoundingBox;
import com.ichmed.trinketeers.util.Loot;
import com.ichmed.trinketeers.util.render.IWorldGraphic;
import com.ichmed.trinketeers.util.render.RenderUtil;
import com.ichmed.trinketeers.world.World;

public class Entity implements IWorldGraphic, Waypoint
{

	// Universal
	public float acceleration = 0.01f;
	public String entityType = "test";
	public float visionRange = 0.7f;
	public int lootValue = 5; // implied via Mob-Strength
	public float maxHealth = 10;
	public List<Behaviour> behaviours = new ArrayList<>();
	public String type = "Misc";
	public boolean isEssential = true;

	// Specific
	protected final int MAX_COLLISION_ITERATIONS = 10;

	public Vector3f position = new Vector3f();
	public float mana = -1, maxMana = -1;
	public Vector2f direction = new Vector2f(1, 0), size = new Vector2f(.125f, .125f);
	public Vector2f preferredDirection = new Vector2f(1, 0);
	public float speed = 0f;
	public float preferredSpeed = 0f;
	public Vector3f color = this.getColor();
	public boolean isDead;
	protected int despawnCountDown = 100;
	public float health = 10;
	protected int damageCooldown, maxDamageCooldown = 3;
	public int stun = 0;
	public boolean isSolid = true;
	public boolean isVulnerable = true;
	public int lifespan = -1;
	public float rotation = 0;
	public boolean isMoveable = true;
	public int ticksExisted = 0;
	public boolean renderWhenDead = false, solidWhenDead = false, dropLootOnDeath = false;
	public float lootRange = 2.0f;
	public Waypoint currentWaypoint;
	public String behaviourString = null;
	private int age;
	protected Vector2f renderSize = new Vector2f(), renderOffset = new Vector2f();
	private GibMode gibMode = GibMode.DEFAULT;

	public Entity(World w)
	{
	}

	public void tick(World world)
	{
		behaviourString = null;
		if (direction.length() > 0) direction.normalise();
		if (this.isDead) despawnCountDown--;
		if (this.lifespan == 0) this.kill(world);
		this.damageCooldown--;
		this.lifespan--;
		this.stun--;
		if (this.health <= 0) this.kill(world);
		if (this.despawnCountDown <= 0) world.removeEntity(this);
		this.ticksExisted++;

		if (!this.isDead) for (int i = 0; i < Behaviour.MAX_PRIORITY; i++)
			for (Behaviour b : behaviours)
				if (b.getPriority(this) == i && b.isActive(this, world)) b.perform(this, world);

		ArrayList<Entity> exclude = new ArrayList<>();
		exclude.add(this);

		if (this.damageCooldown > 0) this.color = new Vector3f(0.75f, 0.2f, 0.2f);
		else this.color = this.getColor();

		if (!this.isDead && this.isMoveable)
		{
			world.removeEntityFromChunk(this);
			performRecursiveCollisionX(0, world, exclude);
			performRecursiveCollisionY(0, world, exclude);
			world.addEntityToChunk(this);
		}

		if (this.stun <= 0)
		{
			// Adjust speed
			if (this.speed > this.preferredSpeed) this.speed -= Math.min(this.speed - this.preferredSpeed, this.acceleration);
			if (this.speed < this.preferredSpeed) this.speed += Math.min(this.preferredSpeed - this.speed, this.acceleration);
			this.direction = new Vector2f(this.preferredDirection);

		} else if (this.speed > 0) this.speed -= Math.min(this.speed, this.acceleration);
		if (this.isDead) this.speed = 0;

	}

	public void onSpawn(World w)
	{
		System.out.println("Spawned \"" + this.entityType + "\" at " + this.position); 
	}

	public void damage(float damage)
	{
		if (this.damageCooldown <= 0)
		{
			this.health -= damage;
			this.damageCooldown = this.maxDamageCooldown;
		}
	}

	private boolean performRecursiveCollisionX(int iteration, World world, ArrayList<Entity> exclude)
	{
		if (!this.isSolid)
		{
			this.position.translate(direction.x * this.speed, 0, 0);
			return true;
		}
		if (iteration >= MAX_COLLISION_ITERATIONS) return false;
		float speedMod = (float) Math.pow(2, iteration);
		Vector2f iteratedSpeed = new Vector2f((this.direction.x / speedMod) * this.speed, 0);
		AxisAllignedBoundingBox predictedPosition = new AxisAllignedBoundingBox(position.x + iteratedSpeed.x, position.y + iteratedSpeed.y, size.x, size.y);
		if (world.getListOfIntersectingEntities(predictedPosition, exclude, true, (int) this.position.z).size() == 0
				&& !world.isPositionStuckInGeometry(predictedPosition, (int) world.player.position.z))
		{
			this.position.translate(iteratedSpeed.x, iteratedSpeed.y, 0);
			return true;
		}
		return performRecursiveCollisionX(iteration + 1, world, exclude);
	}

	private boolean performRecursiveCollisionY(int iteration, World world, ArrayList<Entity> exclude)
	{
		if (!this.isSolid)
		{
			this.position.translate(0, direction.y * this.speed, 0);
			return true;
		}
		if (iteration >= MAX_COLLISION_ITERATIONS) return false;
		float speedMod = (float) Math.pow(2, iteration);
		Vector2f iteratedSpeed = new Vector2f(0, (this.direction.y / speedMod) * this.speed);
		AxisAllignedBoundingBox predictedPosition = new AxisAllignedBoundingBox(position.x + iteratedSpeed.x, position.y + iteratedSpeed.y, size.x, size.y);
		if (world.getListOfIntersectingEntities(predictedPosition, exclude, true, (int) this.position.z).size() == 0
				&& !world.isPositionStuckInGeometry(predictedPosition, (int) world.player.position.z))
		{
			this.position.translate(iteratedSpeed.x, iteratedSpeed.y, 0);
			return true;
		}
		return performRecursiveCollisionY(iteration + 1, world, exclude);
	}

	public List<Entity> getEntitiesExcludedFromCollision()
	{
		ArrayList<Entity> exclude = new ArrayList<>();
		exclude.add(this);
		return exclude;
	}

	public void render(World w)
	{
		if (!this.isDead || this.renderWhenDead) this.actualRender(w);
		if (Game.debugMode) renderHitBox(w);
	}

	protected void actualRender(World w)
	{
		AxisAllignedBoundingBox renderArea = this.getRenderArea();
		glPushMatrix();
		glColor3f(this.color.x, this.color.y, this.color.z);
		glTranslatef(renderArea.pos.x, renderArea.pos.y, 0);
		glTranslated(renderArea.size.x / 2, renderArea.size.x / 2, 0);
		glRotated(this.rotation, 0, 0, 1);
		glTranslated(-renderArea.size.x / 2, -renderArea.size.y / 2, 0);
		RenderUtil.renderTexturedQuad(0, 0, renderArea.size.x, renderArea.size.y, this.getTextureForState(w));
		glColor3f(1, 1, 1);
		glPopMatrix();
	}

	public String getTextureForState(World w)
	{
		if (this.behaviourString != null) return this.entityType + behaviourString;
		if (this.isDead) return this.entityType + "Dead";
		if (this.speed > 0) return this.entityType + "Moving";
		return this.entityType + "Idle_" + (this.ticksExisted % 240) / 60;
	}

	protected void renderHitBox(World w)
	{
		glPushMatrix();
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		glDisable(GL_TEXTURE_2D);
		glTranslatef(this.position.x, this.position.y, 0);
		RenderUtil.renderTexturedQuad(0, 0, this.size.x, this.size.y, "test");
		glColor3f(1, 1, 1);
		glEnable(GL_TEXTURE_2D);
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		glPopMatrix();
	}

	public boolean kill(World world)
	{
		if (this.isDead) return true;
		if(this.gibMode == GibMode.DEFAULT) this.renderWhenDead = true;
		this.isDead = true;
		this.isSolid = this.solidWhenDead;
		this.onDeath(world);
		return this.isDead;
	}

	public Vector3f getCenter()
	{
		return new Vector3f(this.position.x + this.size.x / 2, this.position.y + this.size.y / 2, this.position.z);
	}

	public Entity setCenter(Vector3f v)
	{
		this.position = new Vector3f(v.x - this.size.x / 2, v.y - this.size.y / 2, v.z);
		return this;
	}

	public Entity setPosition(Vector2f v)
	{
		this.position = new Vector3f(v.x, v.y, this.position.z);
		return this;
	}
	
	public Entity setPosition(Vector3f v)
	{
		this.position = new Vector3f(v.x, v.y, v.z);
		return this;
	}

	protected void onDeath(World world)
	{
		for (Behaviour b : this.behaviours)
			b.cleanUp(world);
		
		if(this.gibMode  == GibMode.GIB)
		{
			this.gib(world);
		}

		if (this.dropLootOnDeath)
		{
			Vector3f c = this.getCenter();
			List<Entity> l = Loot.getLootForValue(world, this.lootValue);
			for (int i = 0; i < l.size(); i++)
			{
				Vector3f p = new Vector3f(c.x + ((float) (Math.random() - 0.5) * this.lootRange), c.y + ((float) (Math.random() - 0.5) * this.lootRange), this.position.z);
				world.spawn(l.get(i).setCenter(p), false);
			}
		}
	}

	public AxisAllignedBoundingBox getColissionBox()
	{
		return new AxisAllignedBoundingBox(this.position, this.size);
	}

	public Vector3f getColor()
	{
		return new Vector3f(1f, 1f, 1f);
	}

	public Vector2f getSize()
	{
		return size;
	}

	@Override
	public float getY()
	{
		return this.position.y;
	}

	@Override
	public void render()
	{
	}

	public AxisAllignedBoundingBox getRenderArea()
	{
		if(this.renderSize == null) return this.getColissionBox();
		Vector3f v = this.getCenter();
		if(this.renderOffset == null)
			return new AxisAllignedBoundingBox(v.x - this.renderSize.x , v.y - this.renderSize.y, this.renderSize.x, this.renderSize.y);

		else return new AxisAllignedBoundingBox(v.x - this.renderSize.x + this.renderOffset.x, v.y - this.renderSize.y + this.renderOffset.y, this.renderSize.x, this.renderSize.y);
	}

	public boolean isHostile()
	{
		return false;
	}

	@Override
	public Vector2f getPosition()
	{
		return new Vector2f(this.position.x, this.position.y);
	}

	public boolean isReached(Entity e)
	{
		return new Vector2f(this.position.x - e.position.x, this.position.y + e.position.y).length() <= this.getRadius();
	}

	@Override
	public float getRadius()
	{
		return this.size.length();
	}

	@Override
	public boolean shouldRender(World w)
	{
		return this.position.z == w.player.position.z && this.position.x > w.player.position.x - 1.5f && this.position.x < w.player.position.x + 1.5f && this.position.y > w.player.position.y - 1.5f
				&& this.position.y < w.player.position.y + 1.5f;
	}

	public boolean changeHeight(World w, int heightMod)
	{
		w.removeEntityFromChunk(this);
		this.position.z += heightMod;
		w.addEntityToChunk(this);
		return true;
	}

	public JSONObject getSaveData()
	{
		JSONObject j = new JSONObject();
		try
		{
			j.put("name", this.entityType);
			j.put("posX", this.position.x);
			j.put("posY", this.position.y);
			j.put("posZ", this.position.z);
			j.put("speed", this.speed);
			j.put("dirX", this.direction.x);
			j.put("dirY", this.direction.y);
			j.put("age", this.age);
			for(int i = 0; i < this.behaviours.size(); i++)
			{
				Behaviour b = this.behaviours.get(i);
				if(b.getSaveData() != null) j.put("behaviour" + i, b.getSaveData());
			}
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
		return j;
	}
	
	public static Entity createEntityFromSaveData(World w, String entityName, JSONObject jso)
	{
		System.out.println(entityName);
		EntityData universalData = EntityData.entityData.get(entityName);
		Entity dummy = null;
		if(universalData == null)
		{
			System.out.println("No data found");
			return null;
		}
		try
		{
			dummy = universalData.getClasspath().getConstructor(World.class).newInstance(w);
		} catch (Exception e)
		{
			return null;
		}
		
		dummy.size = universalData.getSize();
		dummy.renderSize = universalData.getRenderSize();
		dummy.renderOffset = universalData.getRenderOffset();
		dummy.entityType = entityName;
		
		for(String b : universalData.behaviours)
		{
			System.out.println(b);
			String[] s = new String[b.split(" ").length - 1];
			for(int i = 1; i < b.split(" ").length; i++)s[i - 1] = b.split(" ")[i];
			try
			{
				dummy.behaviours.add((Behaviour) Class.forName("com.ichmed.trinketeers.ai.Behaviour" + b).getConstructor(World.class, Object[].class).newInstance(w, s));
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		try
		{
			dummy.setPosition(new Vector3f((float)jso.getDouble("posX"), (float)jso.getDouble("posY"), (float)jso.getDouble("posZ")));
			dummy.speed = (float) jso.getDouble("speed");
			dummy.direction = new Vector2f((float) jso.getDouble("dirX"), (float)jso.getDouble("dirY"));
			dummy.age = jso.getInt("age");
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		return dummy;
	}
	
	public void gib(World world)
	{		
		int r = (int) (Math.random() * 5) + 5;
		for (int i = 0; i < r; i++)
		{
			Particle p = new Particle(world);
			p.setTexture(this.entityType + "Dead");
			p.speed = 0.02f;
			p.preferredSpeed = 0; 
			p.setLifeSpan((int) (Math.random() * 50) + 50);
			p.acceleration = 0.0005f;
			p.setTextureOffSet((int)(Math.random() * 24), (int)(Math.random() * 16) + 8);
			p.setScale(0.25f, 0.25f);
			Vector3f v = this.getCenter();
			p.size = new Vector2f(0.05f, 0.05f);
			p.setCenter(new Vector3f(v.x + (float)(Math.random() - 0.5) * 0.1f, v.y + (float)(Math.random() - 0.5f) * 0.1f, v.z));
			p.direction = p.preferredDirection = new Vector2f(v.x - p.getCenter().x, v.y - p.getCenter().y);
			world.spawn(p, false);
		}
		r = (int) (Math.random() * 5) + 5;
		for (int i = 0; i < r; i++)
		{
			Particle p = new Particle(world);
			p.setTexture("blood_1");
			p.speed = 0.01f + (float)Math.random() * 0.02f;
			p.preferredSpeed = 0;
			p.setLifeSpan((int) (Math.random() * 50) + 50);
			p.acceleration = 0.0005f;
			Vector3f v = this.getCenter();
			float f = (float)Math.random() * 0.05f + 0.02f;
			p.size = new Vector2f(f, f);
			p.setCenter(new Vector3f(v.x + (float)(Math.random() - 0.5) * 0.1f, v.y + (float)(Math.random() - 0.5f) * 0.1f, v.z));
			p.direction = p.preferredDirection = new Vector2f(v.x - p.getCenter().x, v.y - p.getCenter().y);
			world.spawn(p, false);
		}
	}
	
	public static enum GibMode
	{
		GIB, DISSAPEAR, DEFAULT;
	}
}
