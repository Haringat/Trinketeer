package com.ichmed.trinketeers.util.editor;

import javax.swing.JComponent;

public class TileEditor extends JComponent
{
	private static final long serialVersionUID = 1069593025607803272L;

	public TileEditor()
	{
		super();
		setSize(100, 200);
		Texture t = new Texture();
		t.setTexture("ZombieIdle_0");
		add(t);
	}
}
