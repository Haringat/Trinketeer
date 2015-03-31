package com.ichmed.trinketeers.ai;

import java.util.HashMap;
import java.util.Map;

public class BehaviourRef
{
	public static Map<String, String> behaviours = new HashMap<String, String>();
	
	public static void addBehaviourReference(String name, String param)
	{
		behaviours.put(name, name.trim() + " " + param.trim());
	}
	
	static
	{
		addBehaviourReference("Light", "f_0-255_red f_0-255_green f_0-255_blue f_0-255_alpha f_brightness f_flicker");
		
	}
}
