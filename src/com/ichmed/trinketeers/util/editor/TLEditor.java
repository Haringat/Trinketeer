package com.ichmed.trinketeers.util.editor;

import static javax.swing.JScrollPane.*;

import java.awt.Container;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.util.ArrayList;
import java.util.List;

public class TLEditor extends JFrame {

	private static final long serialVersionUID = 1513302950342749937L;
	
	JPanel editor = new JPanel();

	List<String[]> content = new ArrayList<String[]>();

	public TLEditor() {
		super();
		JScrollPane sp = new JScrollPane(editor, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_AS_NEEDED);
		getContentPane().add(sp);
		
		editor.add(new JLabel("texture"));
		editor.add(new JLabel("name"));
		
	}
	
	private void addRow(){
		
	}

	private void addRow(int index){
		GridBagConstraints c = new GridBagConstraints();
		
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
