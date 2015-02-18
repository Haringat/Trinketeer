package com.ichmed.trinketeers.savefile.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

public class EntityData
{
	public static HashMap<String, EntityData> entityData = new HashMap<>();
	public static HashMap<String, List<EntityData>> entityDataByType = new HashMap<>();
	
	private String name;
	private String type;
	private int strength;
	private int rarity;
	
	@Override
	public String toString() {
		return name;
	}

	public List<String> behaviours;
	
	public Vector2f size, renderSize;
	
	public static List<EntityData> getAllEntitesOfType(String type)
	{
		List<EntityData> l = entityDataByType.get(type);
		if(l == null)
		{
			l = new ArrayList<>();
			for(String s : entityData.keySet())
				if(entityData.get(s).type.equals(type))l.add(entityData.get(s));
			entityDataByType.put(type, l);
		}
		return l;
	}

	public EntityData(String name, String type, int strength, int rarity,
			List<String> behaviours, Vector2f size, Vector2f renderSize) {
		this.name = name;
		this.type = type;
		this.strength = strength;
		this.rarity = rarity;
		this.behaviours = behaviours;
		this.size = size;
		this.renderSize = renderSize;
	}

	public EntityData(){
		this("new Entity", "misc", 0, 0, new ArrayList<String>(),
				new Vector2f(0.0f,0.0f), new Vector2f(0.0f,0.0f));
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int getRarity() {
		return rarity;
	}

	public void setRarity(int rarity) {
		this.rarity = rarity;
	}

	public List<String> getBehaviours() {
		return behaviours;
	}

	public void setBehaviours(List<String> behaviours) {
		this.behaviours = behaviours;
	}

	public Vector2f getSize() {
		return size;
	}

	public void setSize(Vector2f size) {
		this.size = size;
	}

	public Vector2f getRenderSize() {
		return renderSize;
	}

	public void setRenderSize(Vector2f renderSize) {
		this.renderSize = renderSize;
	}
}
