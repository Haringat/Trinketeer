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
import javax.swing.filechooser.FileFilter;

import com.ichmed.trinketeers.savefile.DataLoader;
import com.ichmed.trinketeers.spell.element.Element;

public class Preview extends JComponent implements MouseListener{
	
	private static final long serialVersionUID = 4743724722914037048L;

	Image image;
	String path;

	public Preview(String path) {
		setMinimumSize(new Dimension(32,32));
		setPreferredSize(new Dimension(32,32));
		setMaximumSize(new Dimension(32,32));
		setBackground(new Color(255,0,255));
		setOpaque(true);
		setToolTipText("change spell texture");
		addMouseListener(this);
		this.path = path;
		setImage(this.path);
	}

	public void setImage(String path) throws NullPointerException{
		image = DataLoader.loadImage(path, this);
		if(image == null){
			image = DataLoader.loadImage(new Element().getTexture(), this);
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

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1 && this.contains(e.getPoint())){
			JFileChooser file = new JFileChooser();
			file.setFileSelectionMode(JFileChooser.FILES_ONLY);
			file.setSelectedFile(new File(path).getAbsoluteFile());
			file.setFileFilter(new FileFilter(){

				@Override
				public boolean accept(File f) {
					if(f.isDirectory())
						return true;
					int index = f.getName().lastIndexOf('.'); 
					if( index == -1)
						return false;
					if(f.getName().substring(index).equalsIgnoreCase(".png"))
						return true;
					return false;
				}

				@Override
				public String getDescription() {
					return "Portable Network Graphic (.png)";
				}
			});
			file.setMultiSelectionEnabled(false);
			if(file.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
				setImage(new File(".").getAbsoluteFile().toURI().relativize(
						file.getSelectedFile().toURI()).getPath());
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
