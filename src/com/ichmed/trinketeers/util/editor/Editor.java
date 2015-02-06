package com.ichmed.trinketeers.util.editor;

import static com.ichmed.trinketeers.util.DataRef.elementsFile;
import static com.ichmed.trinketeers.util.JSONUtil.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
//import java.io.BufferedReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStreamReader;
import java.util.HashMap;
//import java.util.Iterator;









import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ichmed.trinketeers.spell.element.Element;

public class Editor implements MouseListener
{
	//BufferedReader reader;
	boolean run = true;
	public HashMap<String, String> defaultValues = new HashMap<>();

	public Editor()
	{
		defaultValues.put("damage", "10.0");

		defaultValues.put("color_red", "1.0");
		defaultValues.put("color_green", "1.0");
		defaultValues.put("color_blue", "1.0");

		defaultValues.put("break_on_impact", "true");
		defaultValues.put("density", "0.001");

		defaultValues.put("brightness", "0");

		defaultValues.put("texture", "defaultSpell.png");

		JFrame editor = new JFrame();

/*		JFileChooser fc = new JFileChooser();


		fc.setFileFilter(new FileFilter(){

			@Override
			public boolean accept(File f) {
				if(f.isDirectory())
					return true;
				int index = f.getName().lastIndexOf('.');
				if(index == -1)
					return false;
				if(f.getName().substring(index).equalsIgnoreCase(".json"))
					return true;
				else
					return false;
			}

			@Override
			public String getDescription() {
				return "JSON file (.json)";
			}
			
		});
		int returnVal = fc.showOpenDialog(editor);*/
		List<Element> = loadValues(elementsFile);
		editor.addMouseListener(this);
		GridBagLayout l = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		editor.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		JColorChooser cc = new JColorChooser();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	private List<Element> loadElements(File f){
		
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
							else if (com.startsWith("put "))
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
