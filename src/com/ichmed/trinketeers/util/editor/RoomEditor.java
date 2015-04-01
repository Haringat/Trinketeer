package com.ichmed.trinketeers.util.editor;

import javax.swing.JPanel;

public class RoomEditor extends JPanel
{
	private static final long serialVersionUID = 3959603962532686473L;
	
	public RoomEditor()
	{
		super();
		this.add(new TileEditor());
	}
}
