package com.ichmed.trinketeers.util.editor;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.ichmed.trinketeers.savefile.DataLoader;

public class Preview extends JComponent implements MouseListener{
	
	private static final long serialVersionUID = 4743724722914037048L;

	Image image;

	public Preview(String path) {
		setSize( 32, 32 );
		this.setToolTipText("Spell texture");
		addMouseListener(this);
		setImage(path);
	}

	public void setImage(String path) throws NullPointerException{
		image = DataLoader.loadImage(path, this);
		if(image == null){
			throw new NullPointerException();
		}
		this.repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1 && this.contains(e.getPoint())){
			JFileChooser file = new JFileChooser();
			file.setFileFilter(new FileFilter(){

				@Override
				public boolean accept(File f) {
					if(f.isDirectory())
						return true;
					if(f.getName().substring(f.getName().lastIndexOf('.')).equalsIgnoreCase(".png"))
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
				setImage(file.getSelectedFile().getPath());
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
