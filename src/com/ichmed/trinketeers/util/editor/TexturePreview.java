package com.ichmed.trinketeers.util.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import org.lwjgl.util.vector.Vector4f;

import com.ichmed.trinketeers.Game;
import com.ichmed.trinketeers.savefile.DataLoader;
import com.ichmed.trinketeers.savefile.data.ElementData;
import com.ichmed.trinketeers.util.DataRef;
import com.ichmed.trinketeers.util.render.TextureLibrary;

public class TexturePreview extends JComponent implements MouseListener{
	
	private static final long serialVersionUID = 4743724722914037048L;

	private Image image;
    protected transient ChangeEvent changeEvent;

	public TexturePreview(String name) {
		Game.createDefaultTextureLibrary();
		TextureLibrary.loadTextureLibrary(DataRef.defaultLibrary);
		Vector4f vec = TextureLibrary.getTextureVector(name);
		setMinimumSize(new Dimension(32,32));
		setPreferredSize(new Dimension(32,32));
		setMaximumSize(new Dimension(32,32));
		setBackground(new Color(255,0,255));
		setOpaque(true);
		addMouseListener(this);
	}

	public void setImage(String path) throws NullPointerException{
		image = DataLoader.loadImage(path, this);
		if(image == null){
			image = DataLoader.loadImage(new ElementData().getTexture(), this);
		}
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(new Color(255,0,255));
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
	}
	
	public String getPath(){
		return path;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1 && this.contains(e.getPoint())){
			
		}
		
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
