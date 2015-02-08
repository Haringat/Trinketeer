package com.ichmed.trinketeers.util.render.light;

import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.ichmed.trinketeers.Game;
import com.ichmed.trinketeers.world.World;

public class LightRenderer
{
	private static int shaderProgram;
	private static int fragmentShader;

	private static int lightFrameBuffer = glGenFramebuffersEXT();
	private static int lightTexture = glGenTextures();
	private static int lightDepthBuffer = glGenRenderbuffersEXT();

	private static Vector3f ambientLight = new Vector3f();

	public static void setAmbientLight(float r, float g, float b)
	{
		ambientLight = new Vector3f(r, g, b);
	}

	public static void renderLights(World world, List<ILight> list)
	{
		glViewport(0, 0, Game.WIDTH, Game.HEIGHT); // set The Current Viewport
													// to the fbo
		// size

		glBindTexture(GL_TEXTURE_2D, 0); // unlink textures because if we dont
											// it all is gonna fail
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, lightFrameBuffer); // switch to
																	// rendering
																	// on our
																	// FBO

		glClearColor(ambientLight.x, ambientLight.y, ambientLight.z, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear Screen And
															// Depth Buffer on
															// the fbo to red
		glLoadIdentity();
		for (ILight light : list)
		{
			if (!light.isActive()) continue;
			glColorMask(false, false, false, false);
			glStencilFunc(GL_ALWAYS, 1, 1);
			glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);

//			for (IShadow block : world.currentLevel.shadows)
//			{
//				Vector2f[] vertices = block.getVertices();
//				for (int i = 0; i < vertices.length; i++)
//				{
//					Vector2f currentVertex = vertices[i];
//					Vector2f nextVertex = vertices[(i + 1) % vertices.length];
//					Vector2f edge = Vector2f.sub(nextVertex, currentVertex, null);
//					Vector2f normal = new Vector2f(edge.getY(), -edge.getX());
//					Vector2f lightToCurrent = Vector2f.sub(currentVertex, light.getPosition(), null);
//					if (Vector2f.dot(normal, lightToCurrent) > 0)
//					{
//						Vector2f point1 = Vector2f.add(currentVertex, (Vector2f) Vector2f.sub(currentVertex, light.getPosition(), null).scale(800), null);
//						Vector2f point2 = Vector2f.add(nextVertex, (Vector2f) Vector2f.sub(nextVertex, light.getPosition(), null).scale(800), null);
//						glDisable(GL_TEXTURE_2D);
//						glColor3f(0, 0, 0);
//						glBegin(GL_QUADS);
//						{
//							glVertex2f(currentVertex.getX(), currentVertex.getY());
//							glVertex2f(point1.getX(), point1.getY());
//							glVertex2f(point2.getX(), point2.getY());
//							glVertex2f(nextVertex.getX(), nextVertex.getY());
//						}
//						glEnd();
//						glColor3f(1, 1, 1);
//					}
//				}
//			}

			glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
			glStencilFunc(GL_EQUAL, 0, 1);
			glColorMask(true, true, true, true);

			Vector2f pos = new Vector2f((light.getPosition().x - world.player.getCenter().x) / 2 + 0.5f, (light.getPosition().y - world.player.getCenter().y) / 2 + 0.5f);

			glUseProgram(shaderProgram);
			glUniform2f(glGetUniformLocation(shaderProgram, "lightLocation"), pos.x * Game.WIDTH, pos.y * Game.HEIGHT);
			glUniform3f(glGetUniformLocation(shaderProgram, "lightColor"), light.getColor().x, light.getColor().y, light.getColor().z);
			glEnable(GL_BLEND);
			glBlendFunc(GL_ONE, GL_ONE);

			glBegin(GL_QUADS);
			{
				glVertex2f(-1, -1);
				glVertex2f(-1, 1);
				glVertex2f(1, 1);
				glVertex2f(1, -1);
			}
			glEnd();

			glUseProgram(0);
			glClear(GL_STENCIL_BUFFER_BIT);
		}

		glEnable(GL_TEXTURE_2D); // enable texturing
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0); // switch to rendering on

		glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_ZERO);

		glBindTexture(GL_TEXTURE_2D, lightTexture); // bind our FBO texture
		glBegin(GL_QUADS);
		{
			glTexCoord2d(0, 0);
			glVertex2f(-1, -1);
			glTexCoord2d(0, 1);
			glVertex2f(-1, 1);
			glTexCoord2d(1, 1);
			glVertex2f(1, 1);
			glTexCoord2d(1, 0);
			glVertex2f(1, -1);
		}
		glEnd();

		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		glViewport(0, 0, Game.WIDTH, Game.HEIGHT);
		// glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	static
	{
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, lightFrameBuffer);

		// initialize color texture
		glBindTexture(GL_TEXTURE_2D, lightTexture); // Bind the colorbuffer
													// texture
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, Game.WIDTH, Game.HEIGHT, 0, GL_RGBA, GL_INT, (java.nio.ByteBuffer) null);
		glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, lightTexture, 0);
		// initialize depth renderbuffer
		glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, lightDepthBuffer);
		glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL14.GL_DEPTH_COMPONENT24, Game.WIDTH, Game.HEIGHT);
		glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, lightDepthBuffer);
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
		shaderProgram = glCreateProgram();
		fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		StringBuilder fragmentShaderSource = new StringBuilder();

		try
		{
			String line;
			@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader("resc/shaders/lighting.frag"));
			while ((line = reader.readLine()) != null)
			{
				fragmentShaderSource.append(line).append("\n");
			}
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		glShaderSource(fragmentShader, fragmentShaderSource);
		glCompileShader(fragmentShader);
		if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE)
		{
			System.err.println("Fragment shader not compiled!");
		}

		glAttachShader(shaderProgram, fragmentShader);
		glLinkProgram(shaderProgram);
		glValidateProgram(shaderProgram);
		glEnable(GL_STENCIL_TEST);
	}

	public static void setAmbientLight(Vector3f light)
	{
		setAmbientLight(light.x, light.y, light.z);
	}
}
