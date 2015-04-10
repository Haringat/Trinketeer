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
		super();
		this.add(new TileEditor());
	}
}
