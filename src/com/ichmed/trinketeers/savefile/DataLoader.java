package com.ichmed.trinketeers.savefile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ichmed.trinketeers.spell.element.Element;
import com.ichmed.trinketeers.util.JSONUtil;

import static com.ichmed.trinketeers.util.DataRef.*;

public class DataLoader
{
	public static void loadElements()
	{
		try
		{
			JSONObject elements = JSONUtil.getJSONObjectFromFile(elementsFile);
			JSONArray a = elements.getJSONArray("elements");
			for (int i = 0; i < a.length(); i++)
			{
				JSONObject e = (JSONObject) a.get(i);
				Element elementGame = new Element((float) e.getDouble("color_red"),
						(float) e.getDouble("color_green"),
						(float) e.getDouble("color_blue"),
						(float) e.getDouble("brightness"),
						(float) e.getDouble("density"),
						e.getBoolean("break_on_impact"),
						(float) e.getDouble("damage"),
						e.getString("texture"),
						0f, 0f);
				String name = e.getString("name");
				
				/*@SuppressWarnings("unchecked")
				Iterator<String> iterator = e.keys();
				while (iterator.hasNext())
				{
					String field = iterator.next();
					elementGame.values.put(field, e.getString(field));
				}*/
				
				Element.elements.put(name, elementGame);
			}
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
}
