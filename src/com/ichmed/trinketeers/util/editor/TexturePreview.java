package com.ichmed.trinketeers.util.editor;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import com.ichmed.trinketeers.Game;
import com.ichmed.trinketeers.savefile.DataLoader;
import com.ichmed.trinketeers.util.DataRef;
import com.ichmed.trinketeers.util.render.TextureLibrary;

public class TexturePreview extends JComponent implements MouseListener{
	
	private static final long serialVersionUID = 4743724722914037048L;

	private Vector2f hitbox = new Vector2f(0f,0f);
	private Vector4f texcoords = new Vector4f(0f,0f,0f,0f);
	private Vector2f entsize = new Vector2f(0f,0f);
	private Vector2f rendersize = new Vector2f(0f,0f);
	private Vector2f offset = new Vector2f(0f,0f);
	// the container is just a dummy so that the method has something to notify
	private Image texlib;// = DataLoader.loadImage(DataRef.defaultLibrary + ".png", new Container());
    protected transient ChangeEvent changeEvent;

	public TexturePreview() {
		Game.createDefaultTextureLibrary();
		TextureLibrary.loadTextureLibrary(DataRef.defaultLibrary);
		//texcoords = TextureLibrary.getTextureVector(name);
	//	texlib = DataLoader.loadImage(DataRef.defaultLibrary + ".png", this);
	//	System.out.printf("%s\n",TextureLibrary.textureLibrary.textureName);
		texlib = DataLoader.loadImage(DataRef.defaultLibrary + ".png", this);
		System.out.printf("Texture library is %dx%d\n", texlib.getWidth(this), texlib.getHeight(this));
		this.setMinimumSize(new Dimension(32, 32));
		this.setPreferredSize(new Dimension(32, 32));
		this.setMaximumSize(new Dimension(32, 32));
		setBackground(new Color(255,0,255));
		setOpaque(true);
		addMouseListener(this);
	}

	public void setHitBox(float x, float y){
		hitbox.set(x, y);
		//hitbox.scale(1024f);
		repaint();
	}
	
	public void setRenderSize(float x, float y){
		rendersize.set(x, y);
		repaint();
	}
	
	public void setSize(float x, float y){
		entsize.set(x, y);
		
		repaint();
	}
	
	public void setOffset(float x, float y){
		offset.set(x, y);
		repaint();
	}
	
	public void setTexture(String name){
		texcoords = TextureLibrary.getTextureVector(name);
		//texcoords.scale(1024f);
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g1) {
		super.paintComponent(g1);
		g1.setColor(new Color(0,0,0));
		g1.fillRect(0, 0, getWidth(), getHeight());
		/*Graphics2D g = (Graphics2D) g1.create(getWidth() / 2 - (int) ((hitbox.getX()  > rendersize.getX()  ? hitbox.getX()  : rendersize.getX() ) / 2),
				getHeight() / 2 - (int) ((hitbox.getY()  > rendersize.getY()  ? hitbox.getY()  : rendersize.getY() ) / 2),
				(int) (hitbox.getX()  > offset.getX() + rendersize.getX()  ? hitbox.getX()  : offset.getX() + rendersize.getX() ),
				(int) (hitbox.getY()  > offset.getY() + rendersize.getY()  ? hitbox.getY()  : offset.getY() + rendersize.getY() ));*/
		int osx = (int) (getWidth() / 2 - (hitbox.getX() > rendersize.getX() ? hitbox.getX() : offset.getX() + rendersize.getX()) / 2);
		int osy = (int) (getHeight() / 2 - (hitbox.getY() > rendersize.getY() ? hitbox.getY() : offset.getY() + rendersize.getY()) / 2);
		int sizex = (int) (osx + offset.getX() + rendersize.getX());
		int sizey = (int) (osy + offset.getY() + rendersize.getY());
		int th = (int) (texcoords.getW() / (double) TextureLibrary.LIBRARY_SIZE * rendersize.getX());
		int tw = (int) (texcoords.getX() / (double) TextureLibrary.LIBRARY_SIZE * rendersize.getY());
		int tx = (int) (texcoords.getY() / (double) TextureLibrary.LIBRARY_SIZE);
		int ty = (int) (texcoords.getZ() / (double) TextureLibrary.LIBRARY_SIZE);
		System.out.printf("rendering image at (%d|%d) with %fx%f\n", osx, osy, rendersize.getX(), rendersize.getY());
		System.out.printf("source image is at (%f|%f) with %fx%f\n", texcoords.getW(), texcoords.getX(), texcoords.getY(), texcoords.getZ());
		g1.setColor(new Color(255,0,255));
		g1.fillRect((int) osx, osy, (int) (hitbox.getX() > sizex ? hitbox.getX() : sizex), (int) (hitbox.getY() > sizey ? hitbox.getY() : sizey));
		/*g1.drawImage(texlib, (int) offset.getX() + osx, (int) offset.getY() + osy, sizex, sizey,
				(int) ((texcoords.getX() + offset.getX()) / (double) TextureLibrary.LIBRARY_SIZE), (int)((texcoords.getX() + offset.getX()) / (double) TextureLibrary.LIBRARY_SIZE),
				(int) (texcoords.getZ() + texcoords.getX()), (int) (texcoords.getY() + texcoords.getW()), this);*/
		g1.drawImage(texlib, osx, osy, osx +sizex, osy + sizey,	tx, ty, tx + tw, ty + th, this);
		g1.setColor(new Color(0,0,255));
		g1.drawRect( 10, 10, (int) hitbox.getX(), (int) hitbox.getY());
		//g1.drawImage(texlib, 0, 0, 32, 32, 1, 33, 16, 48, this);
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
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ChangeListener.class) {
                // Lazily create the event:
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((ChangeListener)listeners[i+1]).stateChanged(changeEvent);
            }
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
