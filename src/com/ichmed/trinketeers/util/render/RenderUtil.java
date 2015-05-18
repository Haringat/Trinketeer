package com.ichmed.trinketeers.util.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.GL33.*;

import java.awt.Font;
import java.nio.*;
import java.util.logging.Level;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector4f;

import com.ichmed.trinketeers.Game;
import com.ichmed.trinketeers.world.World;

public class RenderUtil
{

	static Font font = new Font("Arial Black", Font.BOLD, 24);
	static TrueTypeFont ttf = new TrueTypeFont(font, false);
	
	public static void drawRect(float width, float height)
	{
		drawRect(0, 0, width, height);
	}

	public static void renderTexturedQuad(float x, float y, float width, float height, String s)
	{
		renderTexturedQuad(x, y, width, height, TextureLibrary.getTextureVector(s));
	}

	public static void renderTexturedQuad(float x, float y, float width, float height, Vector4f v)
	{
		renderTexturedQuad(x, y, width, height, v, 0, 0, 1, 1);
	}

	public static void renderTexturedQuad(float x, float y, float width, float height, Vector4f v, float textureOffSetX, float textureOffSetY, float scaleX, float scaleY)
	{
		TextureLibrary.bindTexture(TextureLibrary.textureLibrary.textureName);
		float tx = (v.x + textureOffSetX) / (float) TextureLibrary.LIBRARY_SIZE;
		float ty = (v.y + textureOffSetY) / (float) TextureLibrary.LIBRARY_SIZE;
		float th = v.z / (float) TextureLibrary.LIBRARY_SIZE * scaleX;
		float tw = v.w / (float) TextureLibrary.LIBRARY_SIZE * scaleY;

		int min = Math.min(Game.HEIGHT, Game.WIDTH);

		float qx = x + (1 - Game.WIDTH / min) * 0.5f;
		float qy = y + (1 - Game.HEIGHT / min) * 0.5f;
		float qw = width;
		float qh = height;
		
		/*glBegin(GL_QUADS);
		glTexCoord2d(tx, ty + tw);
		glVertex2d(qx, qy);
		glTexCoord2d(tx + th, ty + tw);
		glVertex2d(qx + qw, qy);
		glTexCoord2d(tx + th, ty);
		glVertex2d(qx + qw, qy + qh);
		glTexCoord2d(tx, ty);
		glVertex2d(qx, qy + qh);
		glEnd();*/
		
		FloatBuffer vertices = FloatBuffer.wrap(new float[]{
				tx, ty + tw, qy, qy,
				tx + th, ty + tw, qx + qw, qy,
				tx + th, ty, qx + qw, qy +qh,
				tx, ty, qx, qy + qh				
		});
		
		int vertexbufferobject = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vertexbufferobject);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STREAM_DRAW);
		try{
		int posatt = glGetAttribLocation(Shader.getShaderProgramId("default"), "position");
		int texatt = glGetAttribLocation(Shader.getShaderProgramId("default"), "texturecoords");
		glVertexAttribPointer(posatt, 2, GL_FLOAT, false, 2, 0);
		glVertexAttribPointer(texatt, 2, GL_FLOAT, false, 2, 2);
		}catch(Throwable t){
			Game.logger.log(Level.SEVERE, "Exception occured", t);
		}
		glDrawArrays(GL_QUADS, 0, 4 );
	}

	public static void drawRect(float x, float y, float width, float height)
	{
		FloatBuffer vertices = FloatBuffer.wrap(new float[]{x, y,
				x + width, y,
				x + width, y + height,
				x, y + height});
		int vertexbufferobject = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vertexbufferobject);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STREAM_DRAW);
	}

	/*public static void drawRect(float x, float y, float width, float height)
	{
		glBegin(GL_QUADS);
		glTexCoord2d(0, 1);
		glVertex2f(x, y);
		glTexCoord2d(1, 1);
		glVertex2f(x + width, y);
		glTexCoord2d(1, 0);
		glVertex2f(x + width, y + height);
		glTexCoord2d(0, 0);
		glVertex2f(x, y + height);
		glEnd();
	}*/

	public static void clearBuffer()
	{
		glClearColor(0f, 0f, 0f, 0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
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
		glBegin(GL_QUADS);
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
		glBegin(GL_QUADS);
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
		ttf.drawString(x, y, text, 0, text.length() - 1, scaleX, scaleY, allignMode);
	}
}
