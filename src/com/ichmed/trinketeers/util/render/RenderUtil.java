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

	static{
		try{
			//int posatt = glGetAttribLocation(Shader.getShaderProgramId("default"), "position");
			//int texatt = glGetAttribLocation(Shader.getShaderProgramId("default"), "texturecoords");
			//glVertexAttribPointer(posatt, 2, GL_FLOAT, false, 2, 0);
			//glVertexAttribPointer(texatt, 2, GL_FLOAT, false, 2, 2);
		}catch(Throwable t){
			Game.logger.throwing(RenderUtil.class.getName(), "<clinit>", t);
		}
	}
	
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
		checkerror("glGenBuffers");
		glBindBuffer(GL_ARRAY_BUFFER, vertexbufferobject);
		checkerror("glBindBuffer");
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STREAM_DRAW);
		checkerror("glBufferData");
		
		glDrawArrays(GL_QUADS, 0, 4 );
		checkerror("glDrawArrays");
	}

	public static void drawRect(float x, float y, float width, float height)
	{
		FloatBuffer vertices = FloatBuffer.wrap(new float[]{x, y,
				x + width, y,
				x + width, y + height,
				x, y + height});
		int vertexbufferobject = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vertexbufferobject);
		//checkerror("glBindBuffer");
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STREAM_DRAW);
		checkerror("glBufferData");
		glDrawArrays(GL_QUADS, 0, 4 );
		checkerror("glDrawArrays");
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

	public static void renderBackground(World w)
	{
		TextureLibrary.bindTexture("resc/textures/floorMud.png");
		glColor3f(1, 1, 1);
		float textureScale = 8f;
		float x = w.player.position.x * 2f;
		float y = w.player.position.y * 2f;

		/*glPushMatrix();
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
		glPopMatrix();*/
		FloatBuffer vertices = FloatBuffer.wrap(new float[]{
				-2f, -2f, x, y,
				2f, -2f, textureScale + x, y,
				2f, 2f, textureScale + x, textureScale + y,
				-2f, 2f, x, textureScale + y
		});
		int vertexbufferobject = glGenBuffers();
		checkerror("glGenBuffer");
		glBindBuffer(GL_ARRAY_BUFFER, vertexbufferobject);
		checkerror("glBindBuffer");
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STREAM_DRAW);
		checkerror("glBufferData");
		glDrawArrays(GL_QUADS, 0, 4 );
		checkerror("glDrawArrays");
		
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
		checkerror("glEnd");
	}

	public static void renderText(float x, float y, String text)
	{
		renderText(x, y, text, 0.001f, 0.001f, TrueTypeFont.ALIGN_LEFT);
	}

	public static boolean checkerror(String method){
		switch(glGetError()){
		case GL_NO_ERROR:
		//	Game.logger.log(Level.FINEST, "no error occurred");
			Game.logger.getHandlers()[0].flush();
			return true;
		case GL_INVALID_ENUM:
			Game.logger.log(Level.WARNING, method+":invalid enum given");
			Game.logger.getHandlers()[0].flush();
			return false;
		case GL_INVALID_VALUE:
			Game.logger.log(Level.WARNING, method+":invalid value given");
			Game.logger.getHandlers()[0].flush();
			return false;
		case GL_INVALID_OPERATION:
			Game.logger.log(Level.WARNING, method+":invalid operation invoked");
			Game.logger.getHandlers()[0].flush();
			return false;
		case GL_INVALID_FRAMEBUFFER_OPERATION:
			Game.logger.log(Level.WARNING, method+":invalid framebuffer operation invoked");
			Game.logger.getHandlers()[0].flush();
			return false;
		case GL_OUT_OF_MEMORY:
			Game.logger.log(Level.SEVERE, method+":OpenGL ran out of memory");
			Game.logger.getHandlers()[0].flush();
			return false;
		case GL_STACK_UNDERFLOW:
			Game.logger.log(Level.WARNING, method+":Tried to perform a buffer underflow");
			Game.logger.getHandlers()[0].flush();
			return false;
		case GL_STACK_OVERFLOW:
			Game.logger.log(Level.WARNING, method+":Tried to perform a buffer overflow");
			Game.logger.getHandlers()[0].flush();
			return false;
		default:
			Game.logger.log(Level.SEVERE, method+":unknown error occured");
			Game.logger.getHandlers()[0].flush();
			return false;
		}
	}

	public static void renderText(float x, float y, String text, float scaleX, float scaleY, int allignMode)
	{
		ttf.drawString(x, y, text, 0, text.length() - 1, scaleX, scaleY, allignMode);
	}
}
