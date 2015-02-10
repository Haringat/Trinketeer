package com.ichmed.trinketeers.util.editor;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

public class Editor extends JFrame {
	
	private static final long serialVersionUID = -5209002133921099055L;

	private JPanel cp = new JPanel();

	public Editor() {
		JScrollPane sp = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JViewport vp = new JViewport();
		CardLayout cl = new CardLayout();
		this.setTitle("Trinketeers Editor");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		sp.setViewport(vp);
		this.getContentPane().add(sp);
		cl.addLayoutComponent(new ElementEditor(), "ElementEditor");
		cl.addLayoutComponent(new EntityEditor(), "EntityEditor");
		cp.setLayout(cl);
		cl.last(cp);
		vp.add(cp);
		this.pack();
		this.setVisible(true);
	}

}
