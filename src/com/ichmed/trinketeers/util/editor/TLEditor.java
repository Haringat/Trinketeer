package com.ichmed.trinketeers.util.editor;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;

public class TLEditor extends JFrame {

	public TLEditor() throws HeadlessException {
		super();
	}

	public TLEditor(GraphicsConfiguration gc) {
		super(gc);
	}

	public TLEditor(String title) throws HeadlessException {
		super(title);
	}

	public TLEditor(String title, GraphicsConfiguration gc) {
		super(title, gc);
	}

}
