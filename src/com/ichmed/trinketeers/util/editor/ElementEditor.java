package com.ichmed.trinketeers.util.editor;

import static java.awt.GridBagConstraints.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.ichmed.trinketeers.spell.element.Element;
import com.ichmed.trinketeers.savefile.DataLoader;

public class ElementEditor extends JPanel implements MouseListener, ActionListener, FocusListener, ChangeListener
{
	private static final long serialVersionUID = -1549438543620749797L;
	private JButton add;
	private JButton save;
	
	private HashMap<String, Element> elements = new HashMap<String, Element>();
	private HashMap<String, Component[]> id = new HashMap<String, Component[]>();

	private String tempname;

	public ElementEditor(){
		DataLoader.loadElements();
		elements = Element.elements;
		add = new JButton("+");
		add.addActionListener(this);
		save = new JButton("save elements");
		save.addActionListener(this);
		this.addMouseListener(this);
		this.setLayout(new GridBagLayout());
		
		addHeaders();
		addRows();
		addButtons();
		
		this.setMaximumSize(new Dimension(this.getSize().width+10, this.getSize().height + 10));
		this.repaint();
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
		this.add(new JLabel("texture"), c);
		
		c.gridx = 1;
		this.add(new JLabel("name"), c);
		
		c.gridx = 2;
		this.add(new JLabel("damage"), c);
		
		c.gridx = 3;
		this.add(new JLabel("color"), c);
		
		c.gridx = 4;
		this.add(new JLabel("brightness"), c);
		
		c.gridx = 5;
		this.add(new JLabel("breaks on impact"), c);
		
		c.gridx = 6;
		this.add(new JLabel("density"), c);
		
		c.gridx = 7;
		this.add(new JLabel("remove"), c);
	}

	private void addRow(){
		Element e = new Element();
		elements.put(e.getName(), e);
		addRow(e, elements.size());
	}

	private void addRow(Element e, int i) {
		if(elements.containsValue(e)){
			this.remove(add);
			this.remove(save);
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
			this.add(comps[0], c);
		
			c.gridx = 1;
			comps[1] = new JTextField(e.getName(), 10);
			comps[1].setName("name");
			comps[1].addFocusListener(this);
			this.add(comps[1], c);
		
			c.gridx = 2;
			comps[2] = new JTextField(String.valueOf(e.getDamage()), 4);
			comps[2].addFocusListener(this);
			comps[2].setName("damage");
			this.add(comps[2], c);
		
			c.gridx = 3;
			comps[3] = new ColorField(e.getColor());
			((ColorField) comps[3]).addChangeListener(this);
			comps[3].setName("color");
			this.add(comps[3], c);
		
			c.gridx = 4;
			comps[4] = new JTextField(String.valueOf(e.getBrightness()),5);
			comps[4].addFocusListener(this);
			comps[4].setName("brightness");
			this.add(comps[4], c);
		
			c.gridx = 5;
			comps[5] = new JCheckBox("", e.shouldBreakOnImpact());
			((JCheckBox) comps[5]).addChangeListener(this);
			comps[5].setName("boi");
			this.add(comps[5], c);
		
			c.gridx = 6;
			comps[6] = new JTextField(String.valueOf(e.getDensity()),5);
			comps[6].addFocusListener(this);
			comps[6].setName("density");
			this.add(comps[6], c);
		
			c.gridx = 7;
			JButton remove = new JButton("X");
			remove.setName("remove "+e.getName());
			addButtons();
			remove.addActionListener(this);
			comps[7] = remove;
			this.add(remove, c);
			
			this.revalidate();
			this.repaint();
			
			id.put(e.getName(), comps);
		}
	}
	
	private void addButtons(){
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = elements.size()+2;
		c.fill = HORIZONTAL;
		c.anchor = SOUTH;
		c.gridx = 0;
		this.add(add,c);
		
		c.gridx = 1;
		c.gridwidth = REMAINDER;
		this.add(save,c);
		this.repaint();
	}

	private void removeRow(String index){
		elements.remove(index);
		for(Component a : id.get(index)){
			this.remove(a);
		}
		this.revalidate();
		this.repaint();
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
