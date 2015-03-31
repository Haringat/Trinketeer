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

	private Vector4f texcoords = new Vector4f(0f,0f,0f,0f);
	private Vector2f rendersize = new Vector2f(0f,0f);
	private Image texlib;

	public Texture() {
		Game.createDefaultTextureLibrary();
		TextureLibrary.loadTextureLibrary(DataRef.defaultLibrary);
		texlib = DataLoader.loadImage(DataRef.defaultLibrary + ".png", this);
		this.setMinimumSize(new Dimension(32, 32));
		this.setPreferredSize(new Dimension(32, 32));
		this.setMaximumSize(new Dimension(32, 32));
		setBackground(new Color(255,0,255));
		setOpaque(true);
	}
	
	public void setSize(float x, float y){
		rendersize.set(x * 100, y * 100);
		setSize((int) rendersize.getX(), (int) rendersize.getY());
		this.setSize(new Dimension((int) rendersize.getX(), (int) rendersize.getY()));
		this.setMinimumSize(new Dimension((int) rendersize.getX(), (int) rendersize.getY()));
		this.setPreferredSize(new Dimension((int) rendersize.getX(), (int) rendersize.getY()));
		this.setMaximumSize(new Dimension((int) rendersize.getX(), (int) rendersize.getY()));
		repaint();
	}
	
	public void setTexture(String name){
		texcoords = TextureLibrary.getTextureVector(name);
		repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		paintTexture(g, new Vector2f(0f,0f));
	}

	protected void paintTexture(Graphics g, Vector2f offset){
		int tx = (int) texcoords.getX();
		int ty = (int) texcoords.getY();
		int tw = (int) texcoords.getZ();
		int th = (int) texcoords.getW();
		int osx = (int) (getWidth() / 2 - (offset.getX() + rendersize.getX()) / 2);
		int osy = (int) (getHeight() / 2 - (offset.getY() + rendersize.getY()) / 2);
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(new Color(255,0,255));
		g.fillRect(osx, osy, (int) rendersize.getX(), (int) rendersize.getY());
		g.drawImage(texlib, osx, osy, osx + (int) rendersize.getX(), osy + (int) rendersize.getY(), tx, ty, tx + tw, ty + th, this);
	}
}
