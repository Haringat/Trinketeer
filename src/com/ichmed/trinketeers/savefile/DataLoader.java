package com.ichmed.trinketeers.savefile;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ichmed.trinketeers.spell.element.Element;
import com.ichmed.trinketeers.util.JSONUtil;

import static com.ichmed.trinketeers.util.DataRef.*;

public class DataLoader
{
	/**
	 * loads an image from the file at <code>path</code>.
	 * @param path the path to the image file
	 * @param observer the observer which should be notified
	 * @return the loaded image or null if an error occured
	 */
	public static Image loadImage( String path, ImageObserver observer ){
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		MediaTracker tracker = new MediaTracker((Component) observer);
		Image tempimg = toolkit.getImage(path);
		tracker.addImage(tempimg, 0);
		try {
			tracker.waitForAll();
		} catch (InterruptedException e) {}
		if(tracker.isErrorAny()){
			return null;
		}
		BufferedImage img = new BufferedImage(tempimg.getWidth(observer), tempimg.getHeight(observer), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) img.getGraphics();
		g.drawImage(tempimg, 0, 0, tempimg.getWidth(observer), tempimg.getHeight(observer), observer);
		return img;
	}
	
	public static void loadElements()
	{
		try
		{
			JSONObject elements = JSONUtil.getJSONObjectFromFile(elementsFile);
			JSONArray a = elements.getJSONArray("elements");
			for (int i = 0; i < a.length(); i++)
			{
				JSONObject e = (JSONObject) a.get(i);
				Element elementGame = new Element(e.getString("name"),
						(float) e.getDouble("color_red"),
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

	public static void saveElements(HashMap<String, Element> elements) {
		try{
			JSONObject root = new JSONObject();
			JSONArray a = new JSONArray();
			for(String key: elements.keySet().toArray(new String[0])){
				JSONObject e = new JSONObject();
				e.put("color_red", elements.get(key).getColor().getX());
				e.put("color_green", elements.get(key).getColor().getY());
				e.put("color_blue", elements.get(key).getColor().getZ());
				e.put("brightness", elements.get(key).getBrightness());
				e.put("density", elements.get(key).getDensity());
				e.put("break_on_impact", elements.get(key).shouldBreakOnImpact());
				e.put("damage", elements.get(key).getDamage());
				e.put("texture", elements.get(key).getTexture());
				e.put("manamod", elements.get(key).getManaMod());
				e.put("sizemod", elements.get(key).getSizeMod());
				e.put("name", elements.get(key).getName());
				a.put(e);
			}
			root.put("elements", a);
			JSONUtil.putJSONObjectIntoFile(elementsFile, root);
			
		}catch(JSONException e){}
		
	}
}
