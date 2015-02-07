package com.ichmed.trinketeers.util.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JColorChooser;
import javax.swing.JComponent;

import org.lwjgl.util.vector.Vector3f;

public class ColorField extends JComponent implements MouseListener {

	private static final long serialVersionUID = 7194452802650650644L;
	private Color color;
	
	
	public ColorField(int r, int g, int b){
		this(new Color(r,g,b));
	}

	public ColorField(Vector3f c){
		this(new Color(c.getX(), c.getY(), c.getZ()));
	}

	public ColorField(Color c){
		color = c;
		addMouseListener(this);
		setPreferredSize(new Dimension(32,32));
		setMinimumSize(new Dimension(32,32));
		setMaximumSize(new Dimension(32,32));
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(color);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1 && this.contains(e.getPoint())){
			color = JColorChooser.showDialog(this, "choose lightning color", color);
			repaint();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
