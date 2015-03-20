package com.ichmed.trinketeers.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtil
{
	public static JSONObject getForNameFromArray(String name, JSONArray a)
	{
		for (int i = 0; i < a.length(); i++)
		{
			try
			{
				if (((JSONObject) a.get(i)).getString("name").equals(name)) return (JSONObject) a.get(i);
			} catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	public static JSONObject getJSONObjectFromFile(File f)
	{
		try
		{
			@SuppressWarnings("resource")
			String content = new Scanner(f).useDelimiter("\\Z").next();
			return new JSONObject(content);
		} catch (FileNotFoundException | JSONException e)
		{
			e.printStackTrace();
		}

		return null;
	}
	
	public static void putJSONObjectIntoFile(File f, JSONObject src){
		FileWriter w;
		try {
			String res = src.toString();
			if(res != null){
				w = new FileWriter(f);
				w.write(res);
				w.close();
			}
		}catch(IOException e){e.printStackTrace();}
	}
}
