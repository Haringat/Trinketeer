package com.ichmed.trinketeers.util.render;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector4f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import com.ichmed.trinketeers.savefile.DataLoader;

public class TextureLibrary
{
	private static HashMap<String, Texture> libraryTextures = new HashMap<>();
	private HashMap<String, Vector4f> textureCoords = new HashMap<>();
	public static TextureLibrary textureLibrary;
	public String textureName;

	private static String currentTexture = "";

	public static final int LIBRARY_SIZE = 1024;
	private Graphics2D destGraphics;
	private List<String> nonExistentTextures = new ArrayList<>();

	public static Vector4f getTextureVector(String name)
	{
		Vector4f v = textureLibrary.textureCoords.get(name);
		if(v == null)
		{
			String s = name.split("_")[0];
			s += "_0";
			v = textureLibrary.textureCoords.get(s);			
		}
		if(v == null)
		{
			String s = name.split("_")[0];
			v = textureLibrary.textureCoords.get(s);			
		}
		if (v == null)
		{
			if(!textureLibrary.nonExistentTextures.contains(name))textureLibrary.nonExistentTextures.add(name);
			v = new Vector4f(0, 0, 16, 16);
		}
		return v;
	}

	public void cleanUp()
	{
		if (this.nonExistentTextures.size() > 0)
		{
			try
			{
				String p = textureName.split("\\.")[0].split("/")
						[textureName.split("\\.")[0].split("/").length - 1];
				File f = new File("resc/error/" + p + "_Textures_Not_Found.tef");
				f.createNewFile();
				FileWriter writer = new FileWriter(f);
				for (String s : this.nonExistentTextures)
					writer.append(String.format(s + "%n"));
				writer.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static boolean bindTexture(String path)
	{
		if (currentTexture.equals(path)) return true;
		if (!libraryTextures.containsKey(path))
		{
			try
			{
				libraryTextures.put(path, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(path)));
			} catch (IOException e)
			{
				System.out.println("Could not load \"" + path + "\"");
				e.printStackTrace();
				return false;
			}
		}
		libraryTextures.get(path).bind();
		currentTexture = path;
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		return true;
	}

	public static void loadTextureLibrary(String path)
	{
		textureLibrary = new TextureLibrary();
		textureLibrary.textureName = path + ".png";
		bindTexture(textureLibrary.textureName);
		File f = new File(path + ".tld");
		try
		{
			@SuppressWarnings({ "resource" })
			String content = new Scanner(f).useDelimiter("\\Z").next();
			for (String s : content.split("\\|"))
				if (!s.equals(""))
				{
					String floats = s.split(":")[1].trim();
					textureLibrary.textureCoords.put(s.split(":")[0],
							new Vector4f(Float.valueOf(floats.split(" ")[0]), Float.valueOf(floats.split(" ")[1]), Float.valueOf(floats.split(" ")[2]), Float.valueOf(floats.split(" ")[3])));
				}
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public static void generateTextureLibrary(String path, List<String[]> data) throws Exception
	{
		TextureLibrary t = new TextureLibrary();
		BufferedImage imgLib = new BufferedImage(LIBRARY_SIZE, LIBRARY_SIZE, BufferedImage.TYPE_INT_ARGB);
		t.destGraphics = imgLib.createGraphics();
		t.destGraphics.setColor(new Color(255, 0, 255, 255));
		t.destGraphics.setBackground(new Color(0, 0, 0, 0));
		t.destGraphics.fillRect(0, 0, LIBRARY_SIZE, LIBRARY_SIZE);
		if (!t.placeTextureRecursive(data, imgLib)) if (!t.placeTextureRecursiveSkipFirst(data, imgLib)) throw new Exception("Could not create Texture Library, LIBRARY_SIZE might be to small");
		try
		{
			File outputFileImage = new File(path + ".png");
			ImageIO.write(imgLib, "png", outputFileImage);
			File outputFileData = new File(path + ".tld");
			StringBuilder stringBuilder = new StringBuilder();
			for (String s : t.textureCoords.keySet())
			{
				stringBuilder.append(s + ": ");
				Vector4f v = t.textureCoords.get(s);
				stringBuilder.append(v.x + " " + v.y + " " + v.z + " " + v.w + "|");
			}
			FileWriter fw = new FileWriter(outputFileData);
			fw.append(stringBuilder);
			fw.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public boolean placeTextureRecursive(List<String[]> data, BufferedImage dest)
	{
		ArrayList<String[]> temp = new ArrayList<>(data);
		for (int i = 0; i < temp.size(); i++)
		{
			BufferedImage img = (BufferedImage) DataLoader.loadImage(temp.get(i)[0], new Component()
			{
				private static final long serialVersionUID = 1L;
			});
			String name = data.get(i)[1];
			if (tryToPlaceImage(img, dest, temp.get(i)[1]))
			{
				data.remove(temp.get(i));
				if (placeTextureRecursive(data, dest)) return true;
				else if (placeTextureRecursiveSkipFirst(data, dest)) return true;
				else removeImage(img, (int) textureCoords.get(name).x, (int) textureCoords.get(name).y, dest);
			} else return false;
		}
		return true;
	}

	public boolean placeTextureRecursiveSkipFirst(List<String[]> data, BufferedImage dest)
	{
		ArrayList<String[]> temp = new ArrayList<>(data);
		for (int i = 1; i < temp.size(); i++)
		{
			BufferedImage img = (BufferedImage) DataLoader.loadImage(temp.get(i)[0], new Component()
			{
				private static final long serialVersionUID = 1L;
			});
			String name = data.get(i)[1];
			if (tryToPlaceImage(img, dest, temp.get(i)[1]))
			{
				data.remove(temp.get(i));
				if (placeTextureRecursive(data, dest)) return true;
				else if (placeTextureRecursiveSkipFirst(data, dest)) return true;
				else removeImage(img, (int) textureCoords.get(name).x, (int) textureCoords.get(name).y, dest);
			} else return false;
		}
		return true;
	}

	public boolean tryToPlaceImage(BufferedImage src, BufferedImage dest, String name)
	{
		for (int i = 0; i < LIBRARY_SIZE; i++)
			for (int j = 0; j < LIBRARY_SIZE; j++)
			{
				if (isSpaceEmpty(i, j, src.getWidth(), src.getHeight(), dest))
				{
					putImage(src, i, j, dest);
					textureCoords.put(name, new Vector4f(i, j, src.getWidth(), src.getHeight()));
					return true;
				}
			}
		return false;
	}

	public void removeImage(BufferedImage src, int x, int y, BufferedImage dest)
	{
		destGraphics.setColor(new Color(255, 0, 255, 255));
		destGraphics.drawRect(x, y, src.getWidth(), src.getHeight());

	}

	public void putImage(BufferedImage src, int x, int y, BufferedImage dest)
	{
		destGraphics.clearRect(x, y, src.getWidth(), src.getHeight());
		destGraphics.drawImage(src, x, y, null);
	}

	public boolean isSpaceEmpty(int x, int y, int width, int height, BufferedImage bfrdImg)
	{
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				if (!isPixelTransparent(x + i, y + j, bfrdImg)) return false;
		return true;
	}

	public void clearSpace(int x, int y, int width, int height, BufferedImage bfrdImg)
	{
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				bfrdImg.setRGB(x + i, y + j, 0);

	}

	public boolean isPixelTransparent(int x, int y, BufferedImage bfrdImg)
	{
		Color c = new Color(bfrdImg.getRGB(x, y));
		return c.getRed() == 255 && c.getBlue() == 255;
	}

	public static void rebind()
	{
//		System.out.println(currentTexture);
		bindTexture("resc/textures/shadow.png");
		libraryTextures.get(currentTexture).bind();
	}
}
