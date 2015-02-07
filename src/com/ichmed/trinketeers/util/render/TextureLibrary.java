package com.ichmed.trinketeers.util.render;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

	private static String currentTexture = "";

	public static final int LIBRARY_SIZE = 1024;

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
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		return true;
	}

	public TextureLibrary(String path, List<String[]> data) throws Exception
	{
		BufferedImage imgLib = new BufferedImage(LIBRARY_SIZE, LIBRARY_SIZE, BufferedImage.TYPE_INT_ARGB);
		imgLib.getGraphics().setColor(new Color(255, 0, 255, 255));
		imgLib.getGraphics().fillRect(0, 0, LIBRARY_SIZE, LIBRARY_SIZE);
		if(!placeTextureRecursive(data, imgLib))
			if(!placeTextureRecursiveSkipFirst(data, imgLib))
				throw new Exception("Could not create Texture Library, LIBRARY_SIZE might be to small");
		File outputFile = new File(path + ".png");
		try
		{
			ImageIO.write(imgLib, "png", outputFile);
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
				private static final long serialVersionUID = 1L;});
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
				private static final long serialVersionUID = 1L;});
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
		dest.getGraphics().setColor(new Color(255, 0, 255, 255));
		dest.getGraphics().drawRect(x, y, src.getWidth(), src.getHeight());

	}

	public void putImage(BufferedImage src, int x, int y, BufferedImage dest)
	{
		dest.getGraphics().clearRect(x, y, src.getWidth(), src.getHeight());
		dest.getGraphics().drawImage(src, x, y, null);
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
}
