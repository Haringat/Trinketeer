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
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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

	public Editor(){
		DataLoader.loadElements();
		elements = new ArrayList<Element>(Element.elements.values());
		Container pane = editorframe.getContentPane();
		editor = new JPanel();
		sp = new JScrollPane(editor,VERTICAL_SCROLLBAR_AS_NEEDED,HORIZONTAL_SCROLLBAR_NEVER);
		pane.add(sp);
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
		c.insets = new Insets(5,5,5,5);
		c.gridy = -1;
		c.gridx= 0;
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
		elements.add(new Element());
		addRow(elements.get(elements.size()-1), elements.size());
	}

	private void addRow(Element e, int i) {
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5,5,5,5);
		c.gridy = i;
		c.anchor = CENTER;
		Preview preview = new Preview(elements.get(i-1).getTexture());
		preview.setVisible(true);
		preview.repaint();
		editor.add(preview, c);
		
		c.gridx = 1;
		//c.fill = HORIZONTAL;
		editor.add(new JTextField(e.getName(), 10), c);
		
		c.gridx = 2;
		editor.add(new JTextField(String.valueOf(e.getDamage()), 4), c);
		
		c.gridx = 3;
		ColorField colorfield = new ColorField(e.getColor());
		editor.add(colorfield, c);
		
		c.gridx = 4;
		JTextField brightness = new JTextField(String.valueOf(e.getBrightness()),5);
		editor.add(brightness, c);
		
		c.gridx = 5;
		editor.add(new JCheckBox("", e.shouldBreakOnImpact()), c);
		
		c.gridx = 6;
		JTextField density = new JTextField(String.valueOf(e.getDensity()),5);
		editor.add(density, c);
		
		c.gridx = 7;
		JButton remove = new JButton("X");
		remove.setName("remove "+String.valueOf(i-1));
		remove.addActionListener(this);
		editor.add(remove, c);
	}
	
	private void addButtons(){
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = elements.size()+1;
		c.fill = BOTH;
		c.anchor = SOUTH;
		c.gridx = 0;
		editor.add(add,c);
		
		c.gridx = 1;
		
		c.gridwidth = REMAINDER;
		editor.add(save,c);
		editorframe.pack();
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
			elements.remove(index);
			editorframe.setIgnoreRepaint(true);
			editor.removeAll();
			addHeaders();
			addRows();
			addButtons();
			editorframe.setIgnoreRepaint(false);
			editorframe.repaint();
		}
	}

	private void edittl() {
		// TODO Auto-generated method stub
		
	}

	/*public void start()
	{
		String s = getInput();

		if (s.equals("quit")) run = false;
		else if (s.startsWith("edit elements")) editElements();
	}*/

	/*private void editElements(File file)
	{
		boolean b = true;
		JSONObject elements = getJSONObjectFromFile(file);
		while (b)
		{
			System.out.println("-- Editor -- [Hub > Elements]");
			String s = getInput();
			if (s.equals("list"))
			{
				try
				{
					JSONArray a = elements.getJSONArray("elements");
					for (int i = 0; i < a.length(); i++)
					{
						System.out.println(((JSONObject) a.get(i)).get("name"));
					}
				} catch (JSONException e)
				{
					e.printStackTrace();
				}
			} else if (s.equals("save"))
			{
				try
				{
					FileWriter o = new FileWriter(elementsFile);
					o.write(elements.toString());
					o.flush();
					o.close();
					out(">> Saved data to " + elementsFile.getAbsolutePath() + "\n");
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			} else if (s.startsWith("quit")) b = false;
			else if (s.equals("create"))
			{
				try
				{
					out("Enter Name: ");
					String name = getInput();
					JSONObject newElement = new JSONObject();
					newElement.put("name", name);

					for (String key : defaultValues.keySet())
						newElement.put(key, defaultValues.get(key));

					JSONArray a = elements.getJSONArray("elements");
					a.put(newElement);
					elements.put("elements", a);

					out("Creted new element " + name + "\n");
				} catch (JSONException e)
				{}
			} else if (s.startsWith("edit"))
			{
				try
				{
					out("Enter name: ");
					String name = getInput();

					JSONObject element = getForNameFromArray(name, elements.getJSONArray("elements"));
					if (element != null)
					{
						boolean editB = true;
						while (editB)
						{
							out("-- Editor -- [Hub > Elements > " + name + "]\n");
							String com = getInput();
							if (com.equals("quit")) editB = false;
							else if (com.startsWith("put "))constraints
							{
								String key = com.split(" ")[1].toLowerCase();
								String value = com.split(" ")[2];
								element.put(key, value);
							} else if (com.equals("list"))
							{
								@SuppressWarnings("rawtypes")
								Iterator i = element.keys();
								while (i.hasNext())
								{
									String field = (String) i.next();
									System.out.println(field + ": " + element.get(field));
								}
							}
						}
					}
					else out("Element \"" + name + "\" does not exist\n");

				} catch (JSONException e)
				{
					e.printStackTrace();
				}

			} else out("Unknown command \"" + s + "\"\n");
		}
	}


	/*public String getInput()
	{
		try
		{
			return reader.readLine();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return "";
	}
	public void out(String s)
	{
		System.out.print(s);
	}*/
}
