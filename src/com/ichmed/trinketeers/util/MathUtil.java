package com.ichmed.trinketeers.util;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class MathUtil
{
	public static float getDistanceBetweenPoints(Vector2f p1, Vector2f p2)
	{

		return new Vector2f(p1.x - p2.x, p1.y - p2.y).length();
	}
	
	public static boolean areVectors3fEqual(Vector3f v1, Vector3f v2)
	{
		return v1.x == v2.x && v1.y == v2.y && v1.z == v2.z;
	}
}
