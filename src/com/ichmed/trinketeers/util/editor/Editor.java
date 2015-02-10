package com.ichmed.trinketeers.util.editor;

import static java.awt.GridBagConstraints.*;
import static javax.swing.JScrollPane.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

import com.ichmed.trinketeers.spell.element.Element;
import com.ichmed.trinketeers.savefile.DataLoader;
import com.ichmed.trinketeers.util.render.TextureLibrary;

public class Editor extends JFrame implements MouseListener, ActionListener, FocusListener, ChangeListener
{
	private static final long serialVersionUID = -1549438543620749797L;
	private JPanel editor;
	private JButton add;
	private JButton save;
	private JButton edittl;
	private JScrollPane sp;
	private JViewport vp;
	
	private HashMap<String, Element> elements = new HashMap<String, Element>();
	private HashMap<String, Component[]> id = new HashMap<String, Component[]>();

	private String tempname;

	public Editor(){
		DataLoader.loadElements();
		editor = new JPanel();
		GridBagConstraints c = new GridBagConstraints();
		elements = Element.elements;
		Container pane = this.getContentPane();
		pane.setLayout(new GridBagLayout());
		c.gridx = 2;
		c.gridy = 0;
		c.gridheight = 3;
		c.fill = VERTICAL;
		c.anchor = EAST;
		vp = new JViewport();
		vp.add(editor);
		sp = new JScrollPane(vp);
		sp.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 3;
		pane.add(sp);
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
		editor.repaint();
		this.pack();
		this.setVisible(true);
	}

	private void addRows(){
		String[] ea = elements.keySet().toArray(new String[0]);
		for(int i = 1; i <= elements.size(); i++){
			addRow(elements.get(ea[i-1]),i);
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
		Element e = new Element();
		elements.put(e.getName(), e);
		addRow(e, elements.size());
	}

	private void addRow(Element e, int i) {
		if(elements.containsValue(e)){
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
			comps[0].setName("texture");
			preview.addChangeListener(this);
			editor.add(comps[0], c);
		
			c.gridx = 1;
			comps[1] = new JTextField(e.getName(), 10);
			comps[1].setName("name");
			comps[1].addFocusListener(this);
			editor.add(comps[1], c);
		
			c.gridx = 2;
			comps[2] = new JTextField(String.valueOf(e.getDamage()), 4);
			comps[2].addFocusListener(this);
			comps[2].setName("damage");
			editor.add(comps[2], c);
		
			c.gridx = 3;
			comps[3] = new ColorField(e.getColor());
			((ColorField) comps[3]).addChangeListener(this);
			comps[3].setName("color");
			editor.add(comps[3], c);
		
			c.gridx = 4;
			comps[4] = new JTextField(String.valueOf(e.getBrightness()),5);
			comps[4].addFocusListener(this);
			comps[4].setName("brightness");
			editor.add(comps[4], c);
		
			c.gridx = 5;
			comps[5] = new JCheckBox("", e.shouldBreakOnImpact());
			((JCheckBox) comps[5]).addChangeListener(this);
			comps[5].setName("boi");
			editor.add(comps[5], c);
		
			c.gridx = 6;
			comps[6] = new JTextField(String.valueOf(e.getDensity()),5);
			comps[6].addFocusListener(this);
			comps[6].setName("density");
			editor.add(comps[6], c);
		
			c.gridx = 7;
			JButton remove = new JButton("X");
			remove.setName("remove "+e.getName());
			addButtons();
			remove.addActionListener(this);
			comps[7] = remove;
			editor.add(remove, c);
			
			editor.repaint();
			
			pack();
			id.put(e.getName(), comps);
		}
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
		editor.repaint();
		pack();
	}

	private void removeRow(String index){
		elements.remove(index);
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
			refreshHashmap();
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
			String index = ((JButton) e.getSource()).getName().substring(
					((JButton) e.getSource()).getName().indexOf(" ")+1);
			removeRow(index);
		}
	}

	private void refreshHashmap() {
		for(String key: elements.keySet().toArray(new String[0])){
			elements.remove(key);
		}
		for(String key: id.keySet().toArray(new String[0])){
			Component[] comps = id.get(key);
			for(Component comp: comps){
				Element e = new Element();
				switch(comp.getName()){
				case "texture":
					e.setTexture(((Preview)comp).getPath());
					break;
				case "name":
					e.setName(((JTextField)comp).getText());
					break;
				case "damage":
					e.setDamage(Float.valueOf(((JTextField)comp).getText()));
					break;
				case "color":
					Color c = ((ColorField)comp).getColor();
					e.setColor(c.getRed(),c.getGreen(),c.getBlue());
					break;
				case "brightness":
					e.setBrightness(Float.valueOf(((JTextField) comp).getText()));
					break;
				case "boi":
					e.setBreakOnImpact(((JCheckBox) comp).isSelected());
					break;
				case "density":
					e.setDensity(Float.valueOf(((JTextField)comp).getText()));
				}
				elements.put(e.getName(), e);
			}
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

	@Override
	public void stateChanged(ChangeEvent e) {
		refreshHashmap();
		
	}

	@Override
	public void focusGained(FocusEvent e) {
		if((JTextField)e.getSource() instanceof JTextField &&
				((JTextField)e.getSource()).getName().equals("name")){
			tempname = ((JTextField)e.getSource()).getText();
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		if((JTextField)e.getSource() instanceof JTextField &&
				!((JTextField)e.getSource()).getText().equals(tempname)){
			String newname = ((JTextField) e.getSource()).getText();
			Component[] tempcomps = id.get(tempname);
			Element tempelement = elements.get(tempname);
			id.remove(tempname);
			id.put(newname, tempcomps);
			elements.remove(tempname);
			elements.put(newname, tempelement);
			tempcomps[7].setName("remove "+newname);
		}
		try{
			refreshHashmap();
		} catch(NumberFormatException e1){
			((JTextField) e.getSource()).setText("0.0");
		}
	}
}
