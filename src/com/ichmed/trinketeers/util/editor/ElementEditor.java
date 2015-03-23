package com.ichmed.trinketeers.util.editor;

import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.NORTH;
import static java.awt.GridBagConstraints.REMAINDER;
import static java.awt.GridBagConstraints.SOUTH;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.lwjgl.util.vector.Vector3f;

import com.ichmed.trinketeers.savefile.DataLoader;
import com.ichmed.trinketeers.savefile.data.ElementData;

public class ElementEditor extends JPanel
{
	private static final long serialVersionUID = -1549438543620749797L;

	JPanel view = new JPanel();

	private HashMap<String, ElementData> elements = new HashMap<String, ElementData>();
	private HashMap<String, Component[]> id = new HashMap<String, Component[]>();

	ElementData currentElementData;
	JComboBox<Object> list;
	ColorField colorChooser;

	JTextField damageText = new JTextField(5);
	JTextField densityText = new JTextField(5);
	JTextField brightnessText = new JTextField(5);
	JCheckBox breakBox = new JCheckBox();

	DefaultListModel<String> effectListModel = new DefaultListModel<String>();
	JList<String> effectList = new JList<String>(effectListModel);

	public ElementEditor()
	{
		DataLoader.loadElements();
		elements = ElementData.elements;

		JPanel elementList = new JPanel();
		list = new JComboBox<>(ElementData.elements.keySet().toArray());

		list.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (currentElementData != null)
				{
					safeCurrentElement();
				}

				// Change Entry
				currentElementData = ElementData.elements.get(list.getSelectedItem());
				for (String s : currentElementData.effects)
					effectListModel.addElement(s);
				Vector3f v = currentElementData.getColor();
				colorChooser.setColor(new Color((int) (v.x * 255), (int) (v.y * 255), (int) (v.z * 255)));
				brightnessText.setText("" + currentElementData.getBrightness());
				damageText.setText("" + currentElementData.getDamage());
				densityText.setText("" + currentElementData.getDensity());
				breakBox.setSelected(currentElementData.shouldBreakOnImpact());
			}
		});

		list.setMaximumSize(new Dimension(20, 5));
		elementList.add(list);
		JButton newElementButton = new JButton("New Element");
		newElementButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String s = JOptionPane.showInputDialog("Name:", "");
				if (s != null && !s.equals("")) ElementData.elements.put(s, new ElementData());
				list.addItem(s);
			}
		});
		elementList.add(newElementButton);
		JButton deleteElement = new JButton("Delete Element");
		elementList.add(deleteElement);

		setLayout(new GridLayout(0, 1));
		colorChooser = new ColorField(0, 0, 0);
		view.setLayout(new GridLayout(0, 1));
		addSegments();
		add(elementList);
		add(view);

		// this.setLayout(new GridBagLayout());
		//
		// addHeaders();
		// addRows();
		// addButtons();

		this.setMaximumSize(new Dimension(this.getSize().width + 10, this.getSize().height + 10));
		this.repaint();
	}

	public void safeCurrentElement()
	{
		float r = (float) colorChooser.getColor().getRed() / 255f;
		float g = (float) colorChooser.getColor().getGreen() / 255f;
		float b = (float) colorChooser.getColor().getBlue() / 255f;
		currentElementData.setColor(r, g, b);
		currentElementData.setDamage(Float.valueOf(damageText.getText()));
		currentElementData.setDensity(Float.valueOf(densityText.getText()));
		currentElementData.setBreakOnImpact(breakBox.isSelected());
		currentElementData.setBrightness(Float.valueOf(brightnessText.getText()));
		currentElementData.effects = new ArrayList<String>();
		for (int i = 0; i < effectListModel.size(); i++)
			currentElementData.effects.add(effectListModel.get(i));
		effectListModel.clear();
	}

	public void addSegments()
	{
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = NORTH;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;

		JPanel tempPanel;

		c.gridy = 0;
		JPanel light = new JPanel();
		light.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Lighting"));
		light.setLayout(new GridLayout(0, 1));
		light.add(colorChooser);
		tempPanel = new JPanel();
		tempPanel.add(new JLabel("Brightness"));
		tempPanel.add(brightnessText);
		light.add(tempPanel);
		view.add(light, c);

		c.gridy = 1;
		JPanel stats = new JPanel();
		stats.setLayout(new GridLayout(0, 2));
		tempPanel = new JPanel();
		tempPanel.add(new JLabel("Damage"));
		tempPanel.add(damageText);
		stats.add(tempPanel);

		tempPanel = new JPanel();
		tempPanel.add(new JLabel("Density"));
		tempPanel.add(densityText);
		stats.add(tempPanel);

		tempPanel = new JPanel();
		tempPanel.add(new JLabel("Break on hit"));
		tempPanel.add(breakBox);
		stats.add(tempPanel);

		stats.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Stats"));

		JPanel effects = new JPanel();
		effects.setLayout(new GridLayout(0, 1));
		JButton addEffect = new JButton("Add Effect");
		addEffect.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String s = JOptionPane.showInputDialog("Add new effect");
				if (s != null && !s.equals("")) effectListModel.addElement(s);
			}
		});
		effectList.addKeyListener(new KeyListener()
		{
			@Override
			public void keyTyped(KeyEvent e)
			{
			}

			@Override
			public void keyReleased(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_DELETE && !effectList.isSelectionEmpty() && JOptionPane.showConfirmDialog(null, "Delete effect?") == 0)
				{
					effectListModel.removeElement(effectList.getSelectedValue());
				}
			}

			@Override
			public void keyPressed(KeyEvent e)
			{
			}
		});
		tempPanel = new JPanel();
		tempPanel.add(addEffect);
		effects.add(tempPanel);
		effects.add(effectList);
		effects.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Effects"));

		view.add(stats, c);
		view.add(effects);
	}

}
