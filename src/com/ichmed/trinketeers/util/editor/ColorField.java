package com.ichmed.trinketeers.util.editor;

import java.awt.Color;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.lwjgl.util.vector.Vector3f;

public class ColorField extends JComponent implements MouseListener, ActionListener {

	private static final long serialVersionUID = 7194452802650650644L;
	private Color color;
	private JColorChooser cc;
	private JDialog dialog;
    protected transient ChangeEvent changeEvent;
	
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

	public Color getColor(){
		return color;
	}
	
	public void setColor(Color c)
	{
		this.color = c;
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1 && this.contains(e.getPoint())){
			cc = new JColorChooser(color);
			dialog = JColorChooser.createDialog(this, "choose lightning color", true, cc, this, this);
			dialog.setVisible(true);
			fireStateChanged();
			repaint();
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

	@Override
	public void actionPerformed(ActionEvent e) {;
		if(e.getSource() instanceof JButton
				&& ((JButton) (e.getSource())).getText().equals(UIManager.get("ColorChooser.okText"))){
			color = cc.getColor();
		}
	}

}
