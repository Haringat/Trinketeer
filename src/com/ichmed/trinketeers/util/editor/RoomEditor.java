package com.ichmed.trinketeers.util.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.ichmed.trinketeers.util.render.TextureLibrary;

public class RoomEditor extends JPanel
{
	private static final long serialVersionUID = 3959603962532686473L;

	public RoomEditor()
	{
		Texture t = new Texture();
		t.setTexture("floorGrass");
		t.setSize(20, 20);
		t.setVisible(true);
		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createBevelBorder(1));
		
		p.add(t);
		add(t);
		JButton b = new JButton("Test");
		b.addActionListener(new ActionListener()
		{			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				t.setTexture("floorGrass");
				t.setSize(50, 50);
				t.setVisible(true);
				t.repaint();
			}
		});
		add(b);
	}
}
