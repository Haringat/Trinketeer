package com.ichmed.trinketeers.util.editor;

import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public class EntityEditor extends JPanel {

	private static final long serialVersionUID = 1840428064298141218L;

	public EntityEditor() {
		this.setMinimumSize(new Dimension(100,100));
	}

	public EntityEditor(LayoutManager layout) {
		super(layout);
		// TODO Auto-generated constructor stub
	}

	public EntityEditor(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	public EntityEditor(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

}
