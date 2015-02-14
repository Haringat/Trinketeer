package com.ichmed.trinketeers.savefile.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EntityData
{
	public static HashMap<String, EntityData> entityData = new HashMap<>();
	public static HashMap<String, List<EntityData>> entityDataByType = new HashMap<>();
	
	public String name;
	public String type;
	public int strength;
	public int rarity;
	
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
}
