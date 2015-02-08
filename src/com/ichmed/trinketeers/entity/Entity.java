package com.ichmed.trinketeers.entity;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.ichmed.trinketeers.Game;
import com.ichmed.trinketeers.ai.Behaviour;
import com.ichmed.trinketeers.ai.waypoint.Waypoint;
import com.ichmed.trinketeers.util.AxisAllignedBoundingBox;
import com.ichmed.trinketeers.util.Loot;
import com.ichmed.trinketeers.util.render.GLHelper;
import com.ichmed.trinketeers.util.render.IWorldGraphic;
import com.ichmed.trinketeers.util.render.TextureLibrary;
import com.ichmed.trinketeers.util.render.light.ILight;
import com.ichmed.trinketeers.world.World;

public class Entity implements IWorldGraphic, Waypoint
{
	public Vector2f position = new Vector2f();
	public Vector2f direction = new Vector2f(1, 0), size = new Vector2f(.1f, .1f);
	public Vector2f preferredDirection = new Vector2f(1, 0);
	public float speed = 0f;
	public float preferredSpeed = 0f;
	public float acceleration = 0.01f;
	public Vector3f color = this.getColor();
	public boolean isDead;
	protected int despawnCountDown = 100;
	public float health = 10, maxHealth = 10;
	protected int maxCollisionIterations = 10;
	protected int damageCooldown, maxDamageCooldown = 3;
	public int stun = 0;
	public boolean isSolid = true;
	public boolean isVulnerable = true;
	public int lifespan = -1;
	public float rotation = 0;
	public ILight light;
	public String texture = "default.png";
	public boolean isMoveable = true;
	public int ticksExisted = 0;
	public boolean renderWhenDead = false, solidWhenDead = false, dropLootOnDeath = false;
	public float lootRange = 2.0f;
	public int lootValue = 5;
	public List<Behaviour> behaviours = new ArrayList<>();
	public Waypoint currentWaypoint;
	public float visionRange = 0.7f;
	public int attackCooldown, maxAttackcooldown;
	public String name = "test";

	public void tick(World world)
	{
		if (this.getLight() != null) this.getLight().setPosition(this.getCenter());
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

		performRecursiveCollisionX(0, world, exclude);
		performRecursiveCollisionY(0, world, exclude);

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
		this.light = createLight();
		if (this.light != null) w.addLight(this.getLight());
	}

	public ILight createLight()
	{
		return null;
	}

	public ILight getLight()
	{
		return this.light;
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
			this.position.translate(direction.x * this.speed, 0);
			return true;
		}
		if (iteration >= maxCollisionIterations) return false;
		float speedMod = (float) Math.pow(2, iteration);
		Vector2f iteratedSpeed = new Vector2f((this.direction.x / speedMod) * this.speed, 0);
		AxisAllignedBoundingBox predictedPosition = new AxisAllignedBoundingBox(position.x + iteratedSpeed.x, position.y + iteratedSpeed.y, size.x, size.y);
		if (world.getListOfIntersectingEntities(predictedPosition, exclude, true).size() == 0)
		{
			this.position.translate(iteratedSpeed.x, iteratedSpeed.y);
			return true;
		}
		return performRecursiveCollisionX(iteration + 1, world, exclude);
	}

	private boolean performRecursiveCollisionY(int iteration, World world, ArrayList<Entity> exclude)
	{
		if (!this.isSolid)
		{
			this.position.translate(0, direction.y * this.speed);
			return true;
		}
		if (iteration >= maxCollisionIterations) return false;
		float speedMod = (float) Math.pow(2, iteration);
		Vector2f iteratedSpeed = new Vector2f(0, (this.direction.y / speedMod) * this.speed);
		AxisAllignedBoundingBox predictedPosition = new AxisAllignedBoundingBox(position.x + iteratedSpeed.x, position.y + iteratedSpeed.y, size.x, size.y);
		if (world.getListOfIntersectingEntities(predictedPosition, exclude, true).size() == 0)
		{
			this.position.translate(iteratedSpeed.x, iteratedSpeed.y);
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
		if (Game.renderHitBoxes) renderHitBox(w);
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
//		GLHelper.drawRect(this.size.x, this.size.y);
		GLHelper.renderTexturedQuad(0, 0, renderArea.size.x, renderArea.size.y, TextureLibrary.getTextureVector(this.getTextureForState(w)));
		glColor3f(1, 1, 1);
		glPopMatrix();
	}
	
	public String getTextureForState(World w)
	{
		if(this.isDead)return this.name + "_dead";
		if(this.speed > 0)return this.name + "_walking";
		return this.name + "_idle";
	}

	protected void renderHitBox(World w)
	{
		glPushMatrix();
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		glDisable(GL_TEXTURE_2D);
		glTranslatef(this.position.x, this.position.y, 0);
		GLHelper.drawRect(this.size.x, this.size.y);
		glColor3f(1, 1, 1);
		glEnable(GL_TEXTURE_2D);
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		glPopMatrix();
	}

	public boolean kill(World world)
	{
		if (this.isDead) return true;
		this.isDead = true;
		this.isSolid = this.solidWhenDead;
		this.onDeath(world);
		return this.isDead;
	}

	public Vector2f getCenter()
	{
		return new Vector2f(this.position.x + this.size.x / 2, this.position.y + this.size.y / 2);
	}

	public Entity setCenter(Vector2f v)
	{
		this.position = new Vector2f(v.x - this.size.x / 2, v.y - this.size.y / 2);
		return this;
	}

	public Entity setPosition(Vector2f v)
	{
		this.position = new Vector2f(v);
		return this;
	}

	protected void onDeath(World world)
	{
		world.removeLight(this.getLight());
		if (this.dropLootOnDeath)
		{

			Vector2f c = this.getCenter();
			List<Entity> l = Loot.getLootForValue(this.lootValue);
			for (int i = 0; i < l.size(); i++)
			{
				Vector2f p = new Vector2f(c.x + ((float) (Math.random() - 0.5) * this.lootRange), c.y + ((float) (Math.random() - 0.5) * this.lootRange));
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
		return this.position.y + this.size.y;
	}

	@Override
	public void render()
	{
	}

	public AxisAllignedBoundingBox getRenderArea()
	{
		return this.getColissionBox();
	}

	public boolean isHostile()
	{
		return false;
	}

	@Override
	public Vector2f getPosition()
	{
		return this.position;
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
}
