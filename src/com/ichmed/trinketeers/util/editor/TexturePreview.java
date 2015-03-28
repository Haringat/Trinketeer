package com.ichmed.trinketeers.util.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import com.ichmed.trinketeers.Game;
import com.ichmed.trinketeers.savefile.DataLoader;
import com.ichmed.trinketeers.util.DataRef;
import com.ichmed.trinketeers.util.render.TextureLibrary;

public class TexturePreview extends Texture implements MouseListener{
	
	private static final long serialVersionUID = 4743724722914037048L;

	private Vector2f hitbox = new Vector2f(0f,0f);
	private Vector4f texcoords = new Vector4f(0f,0f,0f,0f);
	private Vector2f rendersize = new Vector2f(0f,0f);
	private Vector2f offset = new Vector2f(0f,0f);
	private Image texlib;
    protected transient ChangeEvent changeEvent;

	public TexturePreview() {
		super();
		addMouseListener(this);
	}

	public void setHitBox(float x, float y){
		hitbox.set(x * this.getWidth() * 4, y * this.getHeight() * 4);
		this.setSize((int) (offset.getX() + rendersize.getX() > hitbox.getX() ? offset.getX() + rendersize.getX() : hitbox.getX()), (int) (offset.getY() + rendersize.getY() > hitbox.getY() ? offset.getY() + rendersize.getY() : hitbox.getY()));
		repaint();
	}
	
	public void setOffset(float x, float y){
		offset.set(x, y);
		this.setSize((int) (offset.getX() + rendersize.getX() > hitbox.getX() ? offset.getX() + rendersize.getX() : hitbox.getX()), (int) (offset.getY() + rendersize.getY() > hitbox.getY() ? offset.getY() + rendersize.getY() : hitbox.getY()));
		repaint();
	}
	
	public void setTexture(String name){
		texcoords = TextureLibrary.getTextureVector(name);
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g1) {
		super.paintComponent(g1);
		int tx = (int) texcoords.getX();
		int ty = (int) texcoords.getY();
		int tw = (int) texcoords.getZ();
		int th = (int) texcoords.getW();
		int osx = (int) (getWidth() / 2 - (hitbox.getX() > rendersize.getX() ? hitbox.getX() : offset.getX() + rendersize.getX()) / 2);
		int osy = (int) (getHeight() / 2 - (hitbox.getY() > rendersize.getY() ? hitbox.getY() : offset.getY() + rendersize.getY()) / 2);
		g1.setColor(new Color(0,0,0));
		g1.fillRect(0, 0, getWidth(), getHeight());
		g1.setColor(new Color(255,0,255));
		g1.fillRect(osx, osy, (int) (hitbox.getX() > rendersize.getX() ? hitbox.getX() : rendersize.getX()), (int) (hitbox.getY() > rendersize.getY() ? hitbox.getY() : rendersize.getY()));
		g1.drawImage(texlib, osx, osy, osx + (int) rendersize.getX(), osy + (int) rendersize.getY(), tx, ty, tx + tw, ty + th, this);
		g1.setColor(new Color(0,0,255));
		g1.drawRect( osx + (int) rendersize.getX() / 2 - (int) hitbox.getX() / 2, osy + (int) rendersize.getY() / 2 - (int) hitbox.getY() / 2, (int) hitbox.getX(), (int) hitbox.getY());
		System.out.printf("rendering image at (%d|%d) with %fx%f\n", osx, osy, rendersize.getX(), rendersize.getY());
		System.out.printf("source image is at (%f|%f) with %fx%f\n", texcoords.getX(), texcoords.getY(), texcoords.getZ(), texcoords.getW());
		System.out.printf("drawing hitbox with %dx%d\n", (int) hitbox.getX(), (int) hitbox.getY());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	public void addChangeListener(ChangeListener l){
		listenerList.add(ChangeListener.class, l);
	}

	public ChangeListener[] getChangeListeners() {
        return listenerList.getListeners(ChangeListener.class);
    }
	
	protected void fireStateChanged() {
        ChangeListener[] listeners = listenerList.getListeners(ChangeListener.class);
        for (ChangeListener l: listeners) {
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                l.stateChanged(changeEvent);
        }
    }
	
	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

}
