package com.ichmed.trinketeers.util.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.UIManager;

import org.lwjgl.util.vector.Vector3f;

public class ColorField extends JComponent implements MouseListener, ActionListener {

	private static final long serialVersionUID = 7194452802650650644L;
	private Color color;
	private JColorChooser cc;
	private JDialog dialog;
	
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
			cc = new JColorChooser(color);
			dialog = JColorChooser.createDialog(this, "choose lightning color", true, cc, this, this);
			dialog.setVisible(true);
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

	@Override
	public void actionPerformed(ActionEvent e) {;
		if(e.getSource() instanceof JButton
				&& ((JButton) (e.getSource())).getText().equals(UIManager.get("ColorChooser.okText"))){
			color = cc.getColor();
		}
	}

}
