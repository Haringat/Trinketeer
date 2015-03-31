package com.ichmed.trinketeers.util.editor;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.lwjgl.util.vector.Vector2f;

public class TexturePreview extends Texture{
	
	private static final long serialVersionUID = 4743724722914037048L;

	private Vector2f hitbox = new Vector2f(0f,0f);
	private Vector2f rendersize = new Vector2f(0f,0f);
	private Vector2f offset = new Vector2f(0f,0f);

    protected transient ChangeEvent changeEvent;

	public TexturePreview() {
		super();
	}

	public void setHitBox(float x, float y){
		hitbox.set(x * 100, y * 100);
		this.setSize((int) (offset.getX() + rendersize.getX() > hitbox.getX() ? offset.getX() + rendersize.getX() : hitbox.getX()), (int) (offset.getY() + rendersize.getY() > hitbox.getY() ? offset.getY() + rendersize.getY() : hitbox.getY()));
		repaint();
	}
	
	public void setOffset(float x, float y){
		offset.set(x, y);
		this.setSize((int) (offset.getX() + rendersize.getX() > hitbox.getX() ? offset.getX() + rendersize.getX() : hitbox.getX()), (int) (offset.getY() + rendersize.getY() > hitbox.getY() ? offset.getY() + rendersize.getY() : hitbox.getY()));
		repaint();
	}
	
	@Override
	public void setSize(float x, float y){
		super.setSize(offset.getX() + x > hitbox.getX() ? offset.getX() + x : hitbox.getX(), offset.getY() + y > hitbox.getY() ? offset.getY() + y : hitbox.getY());
	}
	
	@Override
	public void paint(Graphics g) {
		paintTexture(g, offset);
		paintHitBox(g);
	}

	protected void paintHitBox(Graphics g){
		int osx = (int) (getWidth() / 2 - (hitbox.getX() > rendersize.getX() ? hitbox.getX() : offset.getX() + rendersize.getX()) / 2);
		int osy = (int) (getHeight() / 2 - (hitbox.getY() > rendersize.getY() ? hitbox.getY() : offset.getY() + rendersize.getY()) / 2);
		g.setColor(new Color(0,0,255));
		g.drawRect( osx, osy, (int) hitbox.getX(), (int) hitbox.getY());
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

}
