package com.ichmed.trinketeers.util.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JComponent;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import com.ichmed.trinketeers.Game;
import com.ichmed.trinketeers.savefile.DataLoader;
import com.ichmed.trinketeers.util.DataRef;
import com.ichmed.trinketeers.util.render.TextureLibrary;

public class Texture extends JComponent {
	
	private static final long serialVersionUID = 4743724722914037048L;

	private Vector2f hitbox = new Vector2f(0f,0f);
	private Vector4f texcoords = new Vector4f(0f,0f,0f,0f);
	private Vector2f rendersize = new Vector2f(0f,0f);
	private Vector2f offset = new Vector2f(0f,0f);
	private Image texlib;

	public Texture() {
		Game.createDefaultTextureLibrary();
		TextureLibrary.loadTextureLibrary(DataRef.defaultLibrary);
		texlib = DataLoader.loadImage(DataRef.defaultLibrary + ".png", this);
		System.out.printf("Texture library is %dx%d\n", texlib.getWidth(this), texlib.getHeight(this));
		this.setMinimumSize(new Dimension(32, 32));
		this.setPreferredSize(new Dimension(32, 32));
		this.setMaximumSize(new Dimension(32, 32));
		setBackground(new Color(255,0,255));
		setOpaque(true);
	}
	
	public void setSize(float x, float y){
		rendersize.set(x * this.getWidth() * 4, y * this.getHeight() * 4);
		this.setSize((int) (offset.getX() + rendersize.getX() > hitbox.getX() ? offset.getX() + rendersize.getX() : hitbox.getX()), (int) (offset.getY() + rendersize.getY() > hitbox.getY() ? offset.getY() + rendersize.getY() : hitbox.getY()));
		repaint();
	}
	
	public void setTexture(String name){
		texcoords = TextureLibrary.getTextureVector(name);
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g1) {
		paintComponent(g1, new Vector2f(0f,0f));
	}

	public void paintComponent(Graphics g, Vector2f offset){
		super.paintComponent(g);
		int tx = (int) texcoords.getX();
		int ty = (int) texcoords.getY();
		int tw = (int) texcoords.getZ();
		int th = (int) texcoords.getW();
		int osx = (int) (getWidth() / 2 - (hitbox.getX() > rendersize.getX() ? hitbox.getX() : offset.getX() + rendersize.getX()) / 2);
		int osy = (int) (getHeight() / 2 - (hitbox.getY() > rendersize.getY() ? hitbox.getY() : offset.getY() + rendersize.getY()) / 2);
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(new Color(255,0,255));
		g.fillRect(osx, osy, (int) (hitbox.getX() > rendersize.getX() ? hitbox.getX() : rendersize.getX()), (int) (hitbox.getY() > rendersize.getY() ? hitbox.getY() : rendersize.getY()));
		g.drawImage(texlib, osx, osy, osx + (int) rendersize.getX(), osy + (int) rendersize.getY(), tx, ty, tx + tw, ty + th, this);
		g.setColor(new Color(0,0,255));
		g.drawRect( osx + (int) rendersize.getX() / 2 - (int) hitbox.getX() / 2, osy + (int) rendersize.getY() / 2 - (int) hitbox.getY() / 2, (int) hitbox.getX(), (int) hitbox.getY());
		System.out.printf("rendering image at (%d|%d) with %fx%f\n", osx, osy, rendersize.getX(), rendersize.getY());
		System.out.printf("source image is at (%f|%f) with %fx%f\n", texcoords.getX(), texcoords.getY(), texcoords.getZ(), texcoords.getW());
		System.out.printf("drawing hitbox with %dx%d\n", (int) hitbox.getX(), (int) hitbox.getY());
	}
}
