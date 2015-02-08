package com.ichmed.trinketeers.util.editor;

import static java.awt.GridBagConstraints.*;
import static javax.swing.JScrollPane.*;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.ichmed.trinketeers.spell.element.Element;
import com.ichmed.trinketeers.savefile.DataLoader;

public class Editor implements MouseListener, ActionListener
{
	private List<Element> elements;
	private JFrame editorframe = new JFrame();
	private JPanel editor;
	private JButton add;
	private JButton save;
	private JButton edittl;
	private JScrollPane sp;
	
	private HashMap<Integer, int[]> id = new HashMap<Integer, int[]>();

	public Editor(){
		editorframe.setResizable(false);
		DataLoader.loadElements();
		editor = new JPanel();
		GridBagConstraints c = new GridBagConstraints();
		elements = new ArrayList<Element>(Element.elements.values());
		Container pane = editorframe.getContentPane();
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
		editorframe.setMaximumSize(new Dimension(
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
		editorframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		addHeaders();
		addRows();
		addButtons();
		
		editor.setMaximumSize(new Dimension(editor.getSize().width+10, editor.getSize().height + 10));
		editorframe.pack();
		editorframe.setVisible(true);
	}

	private void addRows(){
		for(int i = 1; i <= elements.size(); i++){
			addRow(elements.get(i-1),i);
		}
	}

	private void addHeaders(){
		GridBagConstraints c = new GridBagConstraints();
		int[] numbers = new int[8];
		int current = editor.getComponentCount();
		//id.put("labels", )
		c.insets = new Insets(5,5,5,5);
		c.gridy = 0;
		c.anchor = NORTH;
		
		c.gridx = 0;
		numbers[0] = current;
		editor.add(new JLabel("texture"), c, current);
		
		c.gridx = 1;
		current++;
		numbers[1] = current;
		editor.add(new JLabel("name"), c, current);
		
		c.gridx = 2;
		current++;
		numbers[2] = current;
		editor.add(new JLabel("damage"), c, current);
		
		c.gridx = 3;
		current++;
		numbers[3] = current;
		editor.add(new JLabel("color"), c, current);
		
		c.gridx = 4;
		current++;
		numbers[4] = current;
		editor.add(new JLabel("brightness"), c, current);
		
		c.gridx = 5;
		current++;
		numbers[5] = current;
		editor.add(new JLabel("breaks on impact"), c, current);
		
		c.gridx = 6;
		current++;
		numbers[6] = current;
		editor.add(new JLabel("density"), c, current);
		
		c.gridx = 7;
		current++;
		numbers[7] = current;
		editor.add(new JLabel("remove"), c, current);
		
		id.put(new Integer(0), numbers);
	}

	private void addRow(){
		if(elements.size() == 0){
			editor.removeAll();
			addHeaders();
			elements.add(new Element());
			editorframe.invalidate();
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
		int[] numbers = new int[8];
		int current = editor.getComponentCount();
		GridBagConstraints c = new GridBagConstraints();
		
		c.insets = new Insets(5,5,5,5);
		c.gridy = i+1;
		c.gridx = 0;
		c.anchor = CENTER;
		Preview preview = new Preview(e.getTexture());
		preview.setVisible(true);
		preview.repaint();
		numbers[0] = current;
		editor.add(preview, c, current);
		
		c.gridx = 1;
		//c.fill = HORIZONTAL;
		current++;
		numbers[1] = current;
		editor.add(new JTextField(e.getName(), 10), c, current);
		
		c.gridx = 2;
		current++;
		numbers[2] = current;
		editor.add(new JTextField(String.valueOf(e.getDamage()), 4), c, current);
		
		c.gridx = 3;
		ColorField colorfield = new ColorField(e.getColor());
		current++;
		numbers[3] = current;
		editor.add(colorfield, c, current);
		
		c.gridx = 4;
		JTextField brightness = new JTextField(String.valueOf(e.getBrightness()),5);
		current++;
		numbers[4] = current;
		editor.add(brightness, c, current);
		
		c.gridx = 5;
		current++;
		numbers[5] = current;
		editor.add(new JCheckBox("", e.shouldBreakOnImpact()), c, current);
		
		c.gridx = 6;
		JTextField density = new JTextField(String.valueOf(e.getDensity()),5);
		current++;
		numbers[6] = current;
		editor.add(density, c, current);
		
		c.gridx = 7;
		JButton remove = new JButton("X");
		remove.setName("remove "+String.valueOf(i-1));
		addButtons();
		remove.addActionListener(this);
		current++;
		numbers[7] = current;
		editor.add(remove, c, current);
		editorframe.pack();
		id.put(new Integer(i+1), numbers);
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
		editorframe.pack();
	}

	private void removeRow(int i){
		System.out.println("deleting line "+String.valueOf(i));
		for(int a : id.get(new Integer(i+1))){
			System.out.println("removing component at "+String.valueOf(a));
			editor.remove(a);
		}
		editor.repaint();
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
			edittl();
		}
		if(e.getSource() instanceof JButton
				&& ((JButton) e.getSource()).getName() != null
				&& ((JButton) e.getSource()).getName().contains("remove")){
			int index = Integer.valueOf(((JButton) e.getSource()).getName().substring(
					((JButton) e.getSource()).getName().indexOf(" ")+1));
			/*elements.remove(index);
			editorframe.setIgnoreRepaint(true);
			editor.removeAll();
			addHeaders();
			addRows();
			addButtons();
			editorframe.setIgnoreRepaint(false);
			editorframe.repaint();*/
			elements.remove(index);
			removeRow(index);
		}
	}

	private void edittl() {
		// TODO Auto-generated method stub
		
	}
}
