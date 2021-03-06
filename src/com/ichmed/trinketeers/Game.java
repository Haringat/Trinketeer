package com.ichmed.trinketeers;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.lwjgl.Sys;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.vector.Vector3f;

import com.ichmed.trinketeers.savefile.ChunkSave;
import com.ichmed.trinketeers.savefile.DataLoader;
import com.ichmed.trinketeers.util.DataRef;
import com.ichmed.trinketeers.util.editor.Editor;
import com.ichmed.trinketeers.util.render.TextureLibrary;
import com.ichmed.trinketeers.util.texturesystem.TextureCodecRegistry;
import com.ichmed.trinketeers.util.texturesystem.ktxplugin.KTXTexture;
import com.ichmed.trinketeers.world.Chunk;
import com.ichmed.trinketeers.world.World;

public class Game
{
	
	static{
		try{
			System.loadLibrary("ktxlibwrapper");
		} catch(UnsatisfiedLinkError e){
			System.out.printf("could not find the texture loader library\n");
			System.exit(-1);
		}
	}

	private World world;
	// We need to strongly reference callback instances.
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;

	public static int WIDTH = 1024;
	public static int HEIGHT = 1024;
	public static float zoom = -4;

	static public Map<Integer, Boolean> keys = new HashMap<>();

	// The window handle
	public static long window;
	public static boolean debugMode = false;
	public static boolean isEditor = false;

	public void run()
	{
		System.out.println("Hello LWJGL " + Sys.getVersion() + "!");

		try
		{
			init();
			world = new World();
			createDefaultTextureLibrary();
			TextureLibrary.loadTextureLibrary(DataRef.defaultLibrary);
			loop();

			// Release window and window callbacks
			glfwDestroyWindow(window);
			world.cleanUp();
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
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be
													// resizable

		// Create the window
		window = glfwCreateWindow(WIDTH, HEIGHT, "Trinketeers", NULL, NULL);
//		WIDTH = 1920;
//		HEIGHT = 1080;
		if (window == NULL) throw new RuntimeException("Failed to create the GLFW window");

		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
		// Setup a key callback. It will be called every time a key is pressed,
		// repeated or released.
		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback()
		{
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods)
			{
				if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) glfwSetWindowShouldClose(window, GL_TRUE);
				// if (key == GLFW_KEY_R && action == GLFW_RELEASE)
				// world.player.rerollSpells();
				// if (key == GLFW_KEY_Z && action == GLFW_RELEASE)
				// world.nextLevel();
				if (key == GLFW_KEY_F3 && action == GLFW_RELEASE) debugMode = !debugMode;
				if (key == GLFW_KEY_F5 && action == GLFW_RELEASE)
				{
					Vector3f v = Chunk.getClusterForPoint(world, world.player.position);
					ChunkSave.saveChunkClusterToDisk(world, (int) v.x, (int) v.y, (int) v.z);
				}
				if (key == GLFW_KEY_T && action == GLFW_RELEASE) Chunk.setTile(world, (int) (world.player.position.x * 8), (int) (world.player.position.y * 8), (int) world.player.position.z, 5);
				if (key == GLFW_KEY_KP_ADD && action == GLFW_RELEASE) world.player.changeHeight(world, 1);
				if (debugMode && key == GLFW_KEY_V && action == GLFW_RELEASE) world.player.isSolid = !world.player.isSolid;
				if (key == GLFW_KEY_KP_SUBTRACT && action == GLFW_RELEASE) world.player.changeHeight(world, -1);

				if (action == GLFW_PRESS) keys.put(key, true);
				if (action == GLFW_RELEASE) keys.put(key, false);

			}
		});

		// Get the resolution of the primary monitor
//		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		// Center our window
//		glfwSetWindowPos(window, (GLFWvidmode.width(vidmode) - WIDTH) / 2, (GLFWvidmode.height(vidmode) - HEIGHT) / 2);

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

	private void loop(){
		// GLContext.createFromCurrent();
		// glClearColor(1f, 0f, 0f, 0f);

		while (glfwWindowShouldClose(window) == GL_FALSE){

			// glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the
			// framebuffer
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
			world.tick();
			glfwSwapBuffers(window); // swap the color buffers

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
		}
	}

	public static void main(String[] args){
		
		for(int i = 0; i < args.length; i++){
			switch(args[i]){
			case "-loadtex":
				testloadtexlib();
				return;
			case "-editor":
				isEditor = true;
				new Editor();
				return;
			case "-debug":
				debugMode = true;
				break;
			default:
				System.out.printf("unknown parameter %s\n", args[i]);
				return;
			}
		}
		DataLoader.loadElements();
		DataLoader.loadEntitys();
		new Game().run();
	}

	private static void testloadtexlib() {
		new Game().init();
		TextureCodecRegistry.createTexture("resc/textures/defaultLibrary.ktx");
	}

	public static boolean isKeyDown(int key)
	{
		if (keys.get(key) != null) return keys.get(key);
		return false;
	}
	
	public static boolean createDefaultTextureLibrary()
	{
		File lib = new File(DataRef.defaultLibrary + ".png");
		File raw = DataRef.defaultLibraryRaw;
		List<File> files = new ArrayList<>();
		
		for (File file : raw.listFiles())
			if(file.getAbsolutePath().endsWith(".png")) files.add(file);
		for(File file : files)
			if(file.lastModified() > lib.lastModified())
			{
				List<String[]> l = new ArrayList<>();
				for(File f : files)
					l.add(new String[]{f.getAbsolutePath(), f.getName().substring(0, f.getName().lastIndexOf('.'))});
				try
				{
					if(debugMode) System.out.println("Found new Texture, generating texture library");
					TextureLibrary.generateTextureLibrary(lib.getAbsolutePath().substring(0, lib.getAbsolutePath().lastIndexOf('.')), l);
					if(debugMode) System.out.println("Succesfully generated texture library");
					
				} catch (Exception e)
				{
					e.printStackTrace();
				}
				return true;
			}
		return false;
	}
}
