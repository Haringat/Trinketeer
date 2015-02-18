package com.ichmed.trinketeers.util.editor;

import static java.awt.GridBagConstraints.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ichmed.trinketeers.savefile.DataLoader;
import com.ichmed.trinketeers.savefile.data.EntityData;

public class EntityEditor extends JPanel implements ItemListener, ActionListener, FocusListener {

	private static final long serialVersionUID = 1840428064298141218L;

	private HashMap<String, EntityData> ents = new HashMap<>();
	private JComboBox<String> entityselector = new JComboBox<>();
	private JTextField behaviourfield = new JTextField();
	private HashMap<String, JComponent> fields = new HashMap<>();
	private JButton entityadd = new JButton("+");
	

	public EntityEditor(){
		
		loadEntitys();
		//entityselector = new JComboBox<EntityData>();
		ents.put(new EntityData().getName(), new EntityData());
		EntityData ent2 = new EntityData();
		ent2.setName("hallo");
		ents.put(ent2.getName(), ent2);
		for(String key: ents.keySet()){
			entityselector.addItem(key);
		}
		
		fields.put("name", new JTextField(10));
		fields.put("rarity", new JTextField(10));
		fields.put("strength", new JTextField(10));
		fields.put("sizex", new JTextField(10));
		fields.put("sizey", new JTextField(10));
		fields.put("rendersizex", new JTextField(10));
		fields.put("rendersizey", new JTextField(10));
		fields.put("class", new JTextField(10));
		fields.put("behaviours", new JList<String>());
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5,5,5,5);
		c.gridx = 0;
		c.gridwidth = 2;
		c.fill = HORIZONTAL;
		add(entityselector, c);
		c.gridx = 2;
		c.fill = NONE;
		c.gridwidth = 1;
		add(entityadd, c);
		
		c.gridx = 0;
		c.gridwidth = 1;
		c.anchor = WEST;
		c.gridy = 1;
		add(new JLabel("name"), c);
		c.gridy = 2;
		add(new JLabel("rarity"), c);
		c.gridy = 3;
		add(new JLabel("strength"), c);
		c.gridy = 4;
		add(new JLabel("size(x)"), c);
		c.gridy = 5;
		add(new JLabel("size(y)"), c);
		c.gridy = 6;
		add(new JLabel("rendersize(x)"), c);
		c.gridy = 7;
		add(new JLabel("rendersize(y)"), c);
		c.gridy = 8;
		add(new JLabel("classpath"), c);
		
		c.gridx = 1;
		c.gridwidth = 2;
		c.fill = HORIZONTAL;
		c.gridy = 1;
		add(fields.get("name"), c);
		c.gridy = 2;
		add(fields.get("rarity"), c);
		c.gridy = 3;
		add(fields.get("strength"), c);
		c.gridy = 4;
		add(fields.get("sizex"), c);
		c.gridy = 5;
		add(fields.get("sizey"), c);
		c.gridy = 6;
		add(fields.get("rendersizex"), c);
		c.gridy = 7;
		add(fields.get("rendersizey"), c);
		c.gridy = 8;
		add(fields.get("class"), c);
		
		c.gridx = 0;
		c.gridy = 9;
		add(new JLabel("behaviours"), c);
		
		c.gridy = 10;
		c.gridwidth = REMAINDER;
		c.fill = BOTH;
		((JList<?>) fields.get("behaviours")).setLayoutOrientation(JList.VERTICAL);
		add(fields.get("behaviours"), c);
		
		c.gridwidth = 1;
		c.gridy = 11;
		add(behaviourfield, c);
		
		entityselector.addItemListener(this);
		entityadd.addActionListener(this);
		fields.get("name").addFocusListener(this);
	}
	
	private void selectEntity(EntityData entity){
		// TODO: save state of current entity
		((JTextField) fields.get("name")).setText(entity.getName());
		((JTextField) fields.get("rarity")).setText(String.valueOf(entity.getRarity()));
		((JTextField) fields.get("strength")).setText(String.valueOf(entity.getStrength()));
		((JTextField) fields.get("sizex")).setText(String.valueOf(entity.getSize().getX()));
		((JTextField) fields.get("sizey")).setText(String.valueOf(entity.getSize().getY()));
		((JTextField) fields.get("rendersizex")).setText(String.valueOf(entity.getRenderSize().getX()));
		((JTextField) fields.get("rendersizey")).setText(String.valueOf(entity.getRenderSize().getY()));
	}
	
	private void addEntity(){
		if(!ents.containsKey("new Entity")){
			ents.put("new Entity", new EntityData());
		}
		selectEntity(ents.get("new Entity"));
		entityselector.setSelectedItem("new Entity");
	}
	
	public void saveEntitys(){
		DataLoader.saveEntitys(ents);
	}
	
	private void loadEntitys(){
		DataLoader.loadEntitys();
		ents = EntityData.entityData;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource().equals(entityselector)){
			if(e.getStateChange() == ItemEvent.SELECTED){
				selectEntity(ents.get((String) e.getItem()));
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(entityadd)){
			addEntity();
		}
		
	}

	@Override
	public void focusGained(FocusEvent e) {}

	@Override
	public void focusLost(FocusEvent e) {
		if(e.getSource() instanceof JTextField){
			JTextField src = (JTextField) e.getSource();
			if(e.getSource().equals(fields.get("name"))){
				// if the name already exists put the focus back to the name
				// field to avoid duplicate names
				if(ents.containsKey(src.getText()))
					src.grabFocus();
				else{
					String oldname = new String((String) entityselector.getSelectedItem());
					String newname = new String(src.getText());
					int index = entityselector.getSelectedIndex();
					EntityData buffer = ents.get(oldname);
					buffer.setName(newname);
					ents.remove(oldname);
					ents.put(newname, buffer);
					entityselector.removeItemAt(index);
					entityselector.insertItemAt(newname, index);
				}
			}
		}
	}

}
