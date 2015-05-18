package com.ichmed.trinketeers.savefile;

import static com.ichmed.trinketeers.util.DataRef.elementsFile;
import static com.ichmed.trinketeers.util.DataRef.entitiesFile;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lwjgl.util.vector.Vector2f;

import com.ichmed.trinketeers.Game;
import com.ichmed.trinketeers.savefile.data.ElementData;
import com.ichmed.trinketeers.savefile.data.EntityData;
import com.ichmed.trinketeers.util.JSONUtil;

public class DataLoader
{
	/**
	 * loads an image from the file at <code>path</code>.
	 * 
	 * @param path
	 *            the path to the image file
	 * @param observer
	 *            the observer which should be notified
	 * @return the loaded image or null if an error occurred
	 */
	public static Image loadImage(String path, ImageObserver observer)
	{
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		MediaTracker tracker = new MediaTracker((Component) observer);
		Image tempimg = toolkit.getImage(path);
		tracker.addImage(tempimg, 0);
		try
		{
			tracker.waitForAll();
		} catch (InterruptedException e){}
		if (tracker.isErrorAny())
		{
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
				ElementData elementGame = new ElementData(e.getString("name"), (float) e.getDouble("color_red") / 255, (float) e.getDouble("color_green") / 255,
						(float) e.getDouble("color_blue") / 255, (float) e.getDouble("brightness"), (float) e.getDouble("density"), e.getBoolean("break_on_impact"), (float) e.getDouble("damage"), "",
						0f, 0f);
				String name = e.getString("name");

				/*
				 * @SuppressWarnings("unchecked") Iterator<String> iterator =
				 * e.keys(); while (iterator.hasNext()) { String field =
				 * iterator.next(); elementGame.values.put(field,
				 * e.getString(field)); }
				 */

				ElementData.elements.put(name, elementGame);
			}
		} catch (JSONException e)
		{
			Game.logger.throwing(DataLoader.class.getName(), "loadElements", e);
		}
	}

	public static void saveElements(HashMap<String, ElementData> elements)
	{
		try
		{
			JSONObject root = new JSONObject();
			JSONArray a = new JSONArray();
			for (ElementData element : ElementData.elements.values())
			{
				JSONObject e = new JSONObject();
				e.put("color_red", element.getColor().getX() * 255);
				e.put("color_green", element.getColor().getY() * 255);
				e.put("color_blue", element.getColor().getZ() * 255);
				e.put("brightness", element.getBrightness());
				e.put("density", element.getDensity());
				e.put("break_on_impact", element.shouldBreakOnImpact());
				e.put("damage", element.getDamage());
				e.put("name", element.getName());
				a.put(e);
			}
			root.put("elements", a);
			JSONUtil.putJSONObjectIntoFile(elementsFile, root);

		} catch (JSONException e)
		{
			Game.logger.throwing(DataLoader.class.getName(), "saveElements", e);
		}
	}

	public static void loadEntities()
	{
		try
		{
			JSONObject entitys = JSONUtil.getJSONObjectFromFile(entitiesFile);

			JSONArray a = entitys.getJSONArray("entities");
			for (int i = 0; i < a.length(); i++)
			{
				JSONObject e = (JSONObject) a.get(i);
				EntityData entityGame = new EntityData();
				try{
					entityGame.setName(e.getString("name"));
				} catch (JSONException ex){
					entityGame.setName("unknown entity");
				}
				try{
					entityGame.setType(e.getString("type"));
				} catch (JSONException ex){
					entityGame.setType("misc");
				}
				try{
					entityGame.setStrength(e.getInt("strength"));
				} catch (JSONException ex){
					entityGame.setStrength(0);
				}
				try{
					entityGame.setSize(new Vector2f((float) e.getDouble("sizex"), (float) e.getDouble("sizey")));
				} catch (JSONException ex){
					entityGame.setSize(new Vector2f(0f, 0f));
				}
				try{
					entityGame.setRenderSize(new Vector2f((float) e.getDouble("rendersizex"), (float) e.getDouble("rendersizey")));
				} catch (JSONException ex){
					entityGame.setRenderSize(new Vector2f(0f, 0f));
				}
				try{
					entityGame.setRarity(e.getInt("rarity"));
				} catch (JSONException ex){
					entityGame.setRarity(0);
				}
				try{
					entityGame.setRenderOffset(new Vector2f((float) e.getDouble("offsetx"), (float) e.getDouble("offsety")));
				} catch (JSONException ex){
					entityGame.setRenderOffset(new Vector2f(0f, 0f));
				}
				try{
					entityGame.setClasspath(e.getString("classpath"));
				} catch (Exception ex){
					entityGame.setClasspath("com.ichmed.trinketeers.entity.Entity");
				}
				try{
					JSONArray behavioursjson = e.getJSONArray("behaviours");
					List<String> behaviours = new ArrayList<>();
					for (int j = 0; j < behavioursjson.length(); j++)
						behaviours.add(behavioursjson.getString(j));
					entityGame.setBehaviours(behaviours);
				} catch (JSONException ex){
					entityGame.setBehaviours(new ArrayList<String>());
				}
				try{
					JSONArray texturesjson = e.getJSONArray("textures");
					for (int j = 0; j < texturesjson.length(); j++)
						entityGame.setTexture(texturesjson.getJSONObject(j).getString("action"), texturesjson.getJSONObject(j).getString("texname"));
				} catch (JSONException ex){
					// do nothing
				}
				EntityData.entityData.put(entityGame.getName(), entityGame);
			}
		} catch (JSONException e)
		{
			Game.logger.throwing(DataLoader.class.getName(), "loadEntities", e);
		}
	}

	public static void saveEntities(HashMap<String, EntityData> entities)
	{
		try
		{
			JSONObject root = new JSONObject();
			JSONArray a = new JSONArray();
			for (String key : entities.keySet().toArray(new String[0]))
			{
				JSONObject e = new JSONObject();
				e.put("name", entities.get(key).getName());
				e.put("type", entities.get(key).getType());
				e.put("strength", entities.get(key).getStrength());
				e.put("sizex", entities.get(key).getSize().getX());
				e.put("sizey", entities.get(key).getSize().getY());
				e.put("rendersizex", entities.get(key).getRenderSize().getX());
				e.put("rendersizey", entities.get(key).getRenderSize().getY());
				e.put("rarity", entities.get(key).getRarity());
				e.put("classpath", entities.get(key).getClasspath().getName());
				JSONArray behavioursjson = new JSONArray();
				for (String behaviour : entities.get(key).getBehaviours())
					behavioursjson.put(behaviour);
				e.put("behaviours", behavioursjson);
				JSONArray texturesjson = new JSONArray();
				for(String action: entities.get(key).getTexturedActions()){
					JSONObject texture = new JSONObject();
					texture.put("action", action);
					texture.put("texname", entities.get(key).getTexture(action));
					texturesjson.put(texture);
				}
				e.put("textures", texturesjson);
				e.put("offsetx", entities.get(key).getRenderOffset().getX());
				e.put("offsety", entities.get(key).getRenderOffset().getY());
				a.put(e);
			}
			root.put("entities", a);
			JSONUtil.putJSONObjectIntoFile(entitiesFile, root);
		} catch (JSONException e){
			Game.logger.throwing(DataLoader.class.getName(), "saveEntities", e);
		}
	}

	public static native byte[] loadTextureFile( String file );
}
