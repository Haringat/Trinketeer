package com.ichmed.trinketeers;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.Sys;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import com.ichmed.trinketeers.savefile.ChunkSave;
import com.ichmed.trinketeers.savefile.DataLoader;
import com.ichmed.trinketeers.util.editor.Editor;
import com.ichmed.trinketeers.util.render.TextureLibrary;
import com.ichmed.trinketeers.world.Chunk;
import com.ichmed.trinketeers.world.World;

public class Game
{
	private World world;
	// We need to strongly reference callback instances.
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;

	public static final int WIDTH = 1024;
	public static final int HEIGHT = 1024;
	public static float zoom = -4;

	static public Map<Integer, Boolean> keys = new HashMap<>();

	// The window handle
	public static long window;
	public static boolean renderHitBoxes = false;

	public void run()
	{
		System.out.println("Hello LWJGL " + Sys.getVersion() + "!");

		try
		{
			init();
			world = new World();
			TextureLibrary.loadTextureLibrary("resc/textures/defaultLibrary");
			loop();

			// Release window and window callbacks
			glfwDestroyWindow(window);
			TextureLibrary.textureLibrary.cleanUp();
			keyCallback.release();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			// Terminate GLFW and release the GLFWerrorfun
			glfwTerminate();
			errorCallback.release();
		}
	}

	private void init()
	{
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (glfwInit() != GL11.GL_TRUE) throw new IllegalStateException("Unable to initialize GLFW");

		// Configure our window
		glfwDefaultWindowHints(); // optional, the current window hints are
									// already the default
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden
												// after creation
		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE); // the window will be
													// resizable

		// Create the window
		window = glfwCreateWindow(WIDTH, HEIGHT, "Trinketeers", NULL, NULL);
		if (window == NULL) throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed,
		// repeated or released.
		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback()
		{
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods)
			{
				if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) glfwSetWindowShouldClose(window, GL_TRUE);
//				if (key == GLFW_KEY_R && action == GLFW_RELEASE) world.player.rerollSpells();
//				if (key == GLFW_KEY_Z && action == GLFW_RELEASE) world.nextLevel();
				if (key == GLFW_KEY_F3 && action == GLFW_RELEASE) renderHitBoxes = !renderHitBoxes;
				if (key == GLFW_KEY_F5 && action == GLFW_RELEASE) ChunkSave.saveChunkClusterToDisk(world, 0, 0, 0);
				if (key == GLFW_KEY_T && action == GLFW_RELEASE) Chunk.setTile((int)(world.player.position.x * 8), (int)(world.player.position.y * 8), (int)world.player.position.z, 2);
				if (key == GLFW_KEY_KP_ADD && action == GLFW_RELEASE) zoom += 0.25;
				if (key == GLFW_KEY_KP_SUBTRACT && action == GLFW_RELEASE) zoom -= 0.25;

				if (action == GLFW_PRESS) keys.put(key, true);
				if (action == GLFW_RELEASE) keys.put(key, false);

			}
		});

		// Get the resolution of the primary monitor
		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		// Center our window
		glfwSetWindowPos(window, (GLFWvidmode.width(vidmode) - WIDTH) / 2, (GLFWvidmode.height(vidmode) - HEIGHT) / 2);

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
		
		InitGL();
	}

	private void InitGL()
	{
		GLContext.createFromCurrent();
		glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

		glMatrixMode(GL11.GL_PROJECTION);
		glLoadIdentity();

		glOrtho(0, WIDTH, HEIGHT, 0, 1, 1);
		glMatrixMode(GL11.GL_MODELVIEW);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		// glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glEnable(GL_STENCIL_TEST);
	}

	private void loop()
	{
		//GLContext.createFromCurrent();
		//glClearColor(1f, 0f, 0f, 0f);

		while (glfwWindowShouldClose(window) == GL_FALSE)
		{
			
			//glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the
																// framebuffer
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
			world.tick();
			glfwSwapBuffers(window); // swap the color buffers

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
		}
	}

	public static void main(String[] args)
	{
		for(String arg: args){
			if(arg.equals("-editor")){
				new Editor();
				return;
			}
		}
		DataLoader.loadElements();
		new Game().run();
	}

	public static boolean isKeyDown(int key)
	{
		if (keys.get(key) != null) return keys.get(key);
		return false;
	}
}
