package com.ichmed.trinketeers.util.editor;

import static javax.swing.JScrollPane.*;

import java.awt.Container;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class TLEditor extends JFrame {

	private static final long serialVersionUID = 1513302950342749937L;

	Container pane = getContentPane();
	JPanel editor = new JPanel();

	public TLEditor() throws HeadlessException {
		super();
		JScrollPane sp = new JScrollPane(editor, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_AS_NEEDED);
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
