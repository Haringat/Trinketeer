package com.ichmed.trinketeers.util.editor;

import static java.awt.GridBagConstraints.*;
import static javax.swing.JScrollPane.*;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;

import com.ichmed.trinketeers.spell.element.Element;
import com.ichmed.trinketeers.savefile.DataLoader;
import com.ichmed.trinketeers.util.render.TextureLibrary;

public class Editor extends JFrame implements MouseListener, ActionListener
{
	private static final long serialVersionUID = -1549438543620749797L;
	private List<Element> elements;
	//private JFrame this = new JFrame();
	private JPanel editor;
	private JButton add;
	private JButton save;
	private JButton edittl;
	private JScrollPane sp;
	
	private HashMap<JButton, Component[]> id = new HashMap<JButton, Component[]>();

	public Editor(){
		//this.setResizable(false);
		DataLoader.loadElements();
		editor = new JPanel();
		GridBagConstraints c = new GridBagConstraints();
		elements = new ArrayList<Element>(Element.elements.values());
		Container pane = this.getContentPane();
		pane.setLayout(new GridBagLayout());
		c.gridx = 2;
		c.gridy = 0;
		c.gridheight = 3;
		c.fill = VERTICAL;
		c.anchor = EAST;
		JScrollBar vbar = new JScrollBar(JScrollBar.VERTICAL);
		vbar.setToolTipText("hallo");
		pane.add(vbar);
		vbar.setVisible(true);
		sp = new JScrollPane(editor);
		sp.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
		sp.setVerticalScrollBar(vbar);
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 3;
		pane.add(sp);
		//pane.add(editor);
		this.setMaximumSize(new Dimension(
				(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,
				(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2));
		edittl = new JButton("edit texturelib");
		edittl.addActionListener(this);
		add = new JButton("+");
		add.addActionListener(this);
		save = new JButton("save");
		save.addActionListener(this);
		editor.addMouseListener(this);
		editor.setLayout(new GridBagLayout());
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		addHeaders();
		addRows();
		addButtons();
		
		editor.setMaximumSize(new Dimension(editor.getSize().width+10, editor.getSize().height + 10));
		this.pack();
		this.setVisible(true);
	}

	private void addRows(){
		for(int i = 1; i <= elements.size(); i++){
			addRow(elements.get(i-1),i);
		}
	}

	private void addHeaders(){
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5,5,5,5);
		c.gridy = 0;
		c.anchor = NORTH;
		
		c.gridx = 0;
		editor.add(new JLabel("texture"), c);
		
		c.gridx = 1;
		editor.add(new JLabel("name"), c);
		
		c.gridx = 2;
		editor.add(new JLabel("damage"), c);
		
		c.gridx = 3;
		editor.add(new JLabel("color"), c);
		
		c.gridx = 4;
		editor.add(new JLabel("brightness"), c);
		
		c.gridx = 5;
		editor.add(new JLabel("breaks on impact"), c);
		
		c.gridx = 6;
		editor.add(new JLabel("density"), c);
		
		c.gridx = 7;
		editor.add(new JLabel("remove"), c);
	}

	private void addRow(){
		if(elements.size() == 0){
			editor.removeAll();
			addHeaders();
			elements.add(new Element());
			this.invalidate();
			addRow(elements.get(elements.size()-1), elements.size());
			//addButtons();
		} else {
			elements.add(new Element());
			addRow(elements.get(elements.size()-1), elements.size());
		}
	}

	private void addRow(Element e, int i) {
		editor.remove(add);
		editor.remove(save);
		editor.remove(edittl);
		Component[] comps = new Component[8];
		GridBagConstraints c = new GridBagConstraints();
		
		c.insets = new Insets(5,5,5,5);
		c.gridy = i+1;
		c.gridx = 0;
		c.anchor = CENTER;
		Preview preview = new Preview(e.getTexture());
		preview.setVisible(true);
		preview.repaint();
		comps[0] = preview;
		editor.add(comps[0], c);
		
		c.gridx = 1;
		comps[1] = new JTextField(e.getName(), 10);
		editor.add(comps[1], c);
		
		c.gridx = 2;
		comps[2] = new JTextField(String.valueOf(e.getDamage()), 4);
		editor.add(comps[2], c);
		
		c.gridx = 3;
		ColorField colorfield = new ColorField(e.getColor());
		comps[3] = colorfield;
		editor.add(comps[3], c);
		
		c.gridx = 4;
		JTextField brightness = new JTextField(String.valueOf(e.getBrightness()),5);
		comps[4] = brightness;
		editor.add(comps[4], c);
		
		c.gridx = 5;
		comps[5] = new JCheckBox("", e.shouldBreakOnImpact());
		editor.add(comps[5], c);
		
		c.gridx = 6;
		JTextField density = new JTextField(String.valueOf(e.getDensity()),5);
		comps[6] = density;
		editor.add(comps[6], c);
		
		c.gridx = 7;
		JButton remove = new JButton("X");
		remove.setName("remove "+String.valueOf(i-1));
		addButtons();
		remove.addActionListener(this);
		comps[7] = remove;
		editor.add(remove, c);
		this.pack();
		id.put(remove, comps);
	}
	
	private void addButtons(){
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = elements.size()+2;
		c.fill = HORIZONTAL;
		c.anchor = SOUTH;
		c.gridx = 0;
		editor.add(add,c);
		
		c.gridx = 1;
		editor.add(edittl,c);
		
		c.gridx = 2;
		c.gridwidth = REMAINDER;
		editor.add(save,c);
		this.pack();
	}

	private void removeRow(JButton index){
		for(Component a : id.get(index)){
			editor.remove(a);
		}
		editor.repaint();
		this.pack();
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JButton
				&& e.getSource().equals(add)){
			addRow();
		}
		if(e.getSource() instanceof JButton
				&& e.getSource().equals(save)){
			DataLoader.saveElements(elements);
		}
		if(e.getSource() instanceof JButton
				&& e.getSource().equals(edittl)){
			File f = edittl();
			if(f != null && f.isDirectory() == true){
				List<String[]> textures = new ArrayList<String[]>();
				for( File file: f.listFiles()){
					String[] texture = new String[2];
					texture[1] = file.getName().subSequence(0, file.getName().lastIndexOf('.')).toString();
					texture[0] = file.getAbsolutePath();
					textures.add(texture);
				}
				try {
					TextureLibrary.generateTextureLibrary(f.getAbsolutePath(), textures);
				} catch (Exception e1) {
					e1.printStackTrace();
					JDialog dia = new JDialog(this, e1.getLocalizedMessage());
					dia.setVisible(true);
				}
			}
		}
		if(e.getSource() instanceof JButton
				&& ((JButton) e.getSource()).getName() != null
				&& ((JButton) e.getSource()).getName().contains("remove")){
			int index = Integer.valueOf(((JButton) e.getSource()).getName().substring(
					((JButton) e.getSource()).getName().indexOf(" ")+1));
			elements.remove(index);
			removeRow((JButton)e.getSource());
		}
	}

	private File edittl() {
		JFileChooser file = new JFileChooser();
		file.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		file.setSelectedFile(new File(".").getAbsoluteFile());
		file.setFileFilter(new FileFilter(){
			@Override
			public boolean accept(File f) {
				if(f.isDirectory())
					return true;
				return false;
			}
			@Override
			public String getDescription() {
				return "Folder";
			}
		});
		file.setMultiSelectionEnabled(false);
		if(file.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
			return file.getSelectedFile();
		} else {
			return null;
		}
	}
}
