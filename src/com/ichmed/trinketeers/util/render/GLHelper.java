package com.ichmed.trinketeers.util.render;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;

import com.ichmed.trinketeers.world.World;

public class GLHelper
{
	
	static Font font = new Font("Arial Black", Font.BOLD, 24);
	static TrueTypeFont ttf = new TrueTypeFont(font, false);
	
	public static void drawRect(float width, float height)
	{
		drawRect(0, 0, width, height);
	}
	
	public static void renderTexturedQuad(float x, float y, float width, float height, Vector4f v)
	{
		TextureLibrary.bindTexture(TextureLibrary.textureLibrary.textureName);
		double dx = v.x / (double) TextureLibrary.LIBRARY_SIZE;
		double dy = v.y / (double) TextureLibrary.LIBRARY_SIZE;
		double dz = v.z / (double) TextureLibrary.LIBRARY_SIZE;
		double dw = v.w / (double) TextureLibrary.LIBRARY_SIZE;
		
		glBegin(GL11.GL_QUADS);
		glTexCoord2d(dx, dy + dw);
		glVertex2f(x, y);
		glTexCoord2d(dx + dz, dy + dw);
		glVertex2f(x + width, y);
		glTexCoord2d(dx + dz, dy);
		glVertex2f(x + width, y + height);
		glTexCoord2d(dx, dy);
		glVertex2f(x, y + height);
		glEnd();		
	}

	public static void drawRect(float x, float y, float width, float height)
	{
		glBegin(GL11.GL_QUADS);
		glTexCoord2d(0, 1);
		glVertex2f(x, y);
		glTexCoord2d(1, 1);
		glVertex2f(x + width, y);
		glTexCoord2d(1, 0);
		glVertex2f(x + width, y + height);
		glTexCoord2d(0, 0);
		glVertex2f(x, y + height);
		glEnd();
	}

	public static void clearBuffer()
	{
		glClearColor(0f, 0f, 0f, 0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public static void setColor256Bit(float r, float g, float b)
	{
		glColor3f(r / 255, g / 255, b / 255);
	}

	public static void renderBackground(World w)
	{
		TextureLibrary.bindTexture("resc/textures/floorMud.png");
		glColor3f(1, 1, 1);
		double textureScale = 8;
		float x = w.player.position.x * 2;
		float y = w.player.position.y * 2;

		glPushMatrix();
		glBegin(GL11.GL_QUADS);
		glTexCoord2d(x, y);
		glVertex2f(-2, -2);
		glTexCoord2d(textureScale + x, y);
		glVertex2f(2, -2);
		glTexCoord2d(textureScale + x, textureScale + y);
		glVertex2f(2, 2);
		glTexCoord2d(x, textureScale + y);
		glVertex2f(-2, 2);
		glEnd();
		glPopMatrix();
	}

	public static void renderBar(float x, float y, float width, float height)
	{
		glBegin(GL11.GL_QUADS);
		glTexCoord2d(0, 0);
		glVertex2f(x, y);
		glTexCoord2d(width, 0);
		glVertex2f(x + width / 10, y);
		glTexCoord2d(width, height);
		glVertex2f(x + width / 10, y + height / 20f);
		glTexCoord2d(0, height);
		glVertex2f(x, y + height / 20f);
		glEnd();
	}
	

	public static void renderText(float x, float y, String text)
	{
		renderText(x, y, text, 0.001f, 0.001f, TrueTypeFont.ALIGN_LEFT);
	}

	public static void renderText(float x, float y, String text, float scaleX, float scaleY, int allignMode)
	{
		ttf.drawString(x, y, text, 0, text.length()-1, scaleX, scaleY, allignMode);
		TextureLibrary.rebind();
	}
}
