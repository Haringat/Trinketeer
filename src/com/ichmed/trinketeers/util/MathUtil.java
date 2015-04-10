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
		if (v1 == null || v2 == null) return false;
		return v1.x == v2.x && v1.y == v2.y && v1.z == v2.z;
	}

	public static int[][] rotateCW(int[][] mat)
	{
		final int M = mat.length;
		final int N = mat[0].length;
		int[][] ret = new int[N][M];
		for (int r = 0; r < M; r++)
		{
			for (int c = 0; c < N; c++)
			{
				ret[c][M - 1 - r] = mat[r][c];
			}
		}
		return ret;
	}
	
	public static int[][] rotateCCW(int[][] mat)
	{
		final int M = mat.length;
		final int N = mat[0].length;
		int[][] ret = new int[N][M];
		for (int r = 0; r < M; r++)
		{
			for (int c = 0; c < N; c++)
			{
				ret[N - 1 - c][M - 1 - r] = mat[r][c];
			}
		}
		return ret;
	}
}
