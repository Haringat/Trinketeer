package com.ichmed.trinketeers.util.editor;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Window;

import javax.swing.JDialog;

public class TextureSelectDialog extends JDialog {

	private static final long serialVersionUID = -8328775335452798021L;

	public TextureSelectDialog() {
		this((Window) null);
	}

	public TextureSelectDialog(Frame owner) {
		this(owner, "");
	}

	public TextureSelectDialog(Dialog owner) {
		this(owner, "");
	}

	public TextureSelectDialog(Window owner) {
		this(owner, "");
	}

	public TextureSelectDialog(Frame owner, boolean modal) {
		this(owner, "", modal);
	}

	public TextureSelectDialog(Frame owner, String title) {
		this(owner, title, ModalityType.MODELESS);
	}

	public TextureSelectDialog(Dialog owner, boolean modal) {
		this(owner, "", modal);
	}

	public TextureSelectDialog(Dialog owner, String title) {
		this(owner, title, ModalityType.MODELESS);
	}

	public TextureSelectDialog(Window owner, ModalityType modalityType) {
		this(owner, "", modalityType);
	}

	public TextureSelectDialog(Window owner, String title) {
		this(owner, title, ModalityType.MODELESS);
	}

	public TextureSelectDialog(Frame owner, String title, boolean modal) {
		this(owner, title, modal ? DEFAULT_MODALITY_TYPE : ModalityType.MODELESS, (GraphicsConfiguration) null);
	}

	public TextureSelectDialog(Dialog owner, String title, boolean modal) {
		this(owner, title, modal ? DEFAULT_MODALITY_TYPE : ModalityType.MODELESS, (GraphicsConfiguration) null);
	}

	public TextureSelectDialog(Window owner, String title,
			ModalityType modalityType) {
		this(owner, title, modalityType, (GraphicsConfiguration) null);
	}

	public TextureSelectDialog(Frame owner, String title, boolean modal,
			GraphicsConfiguration gc) {
		this(owner, title, modal ? DEFAULT_MODALITY_TYPE : ModalityType.MODELESS, gc);
	}

	public TextureSelectDialog(Dialog owner, String title, boolean modal,
			GraphicsConfiguration gc) {
		this(owner, title, modal ? DEFAULT_MODALITY_TYPE : ModalityType.MODELESS, gc);
	}

	public TextureSelectDialog(Window owner, String title,
			ModalityType modalityType, GraphicsConfiguration gc) {
		super(owner, title, modalityType, gc);
		setSize(100, 100);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
		repaint();
	}
	
	@Override
	public void paint(Graphics graphic){
		super.paint(graphic);
		Graphics2D g = (Graphics2D) this.getContentPane().getGraphics();
		g.setColor(new Color(0,0,0));
		g.drawRect(10, 10, 80, 80);
	}

}
