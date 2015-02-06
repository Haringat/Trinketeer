package com.ichmed.trinketeers.savefile;

import java.util.Iterator;

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
				JSONObject elementJSON = (JSONObject) a.get(i);
				Element elementGame = new Element();
				String name = elementJSON.getString("name");
				
				@SuppressWarnings("unchecked")
				Iterator<String> iterator = elementJSON.keys();
				while (iterator.hasNext())
				{
					String field = iterator.next();
					elementGame.values.put(field, elementJSON.getString(field));
				}
				
				Element.elements.put(name, elementGame);
			}
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
}
