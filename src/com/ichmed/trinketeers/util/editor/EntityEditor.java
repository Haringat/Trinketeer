package com.ichmed.trinketeers.util.editor;

import static java.awt.GridBagConstraints.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.savefile.DataLoader;
import com.ichmed.trinketeers.savefile.data.EntityData;

public class EntityEditor extends JPanel implements ItemListener, ActionListener {

	private static final long serialVersionUID = 1840428064298141218L;

	private JComboBox<EntityData> entityselector;
	private JTextField behaviourfield = new JTextField();
	private HashMap<String, JComponent> fields = new HashMap<>();
	private JButton entityadd = new JButton("+");
	private JButton behaveadd = new JButton("+");
	private JButton rename = new JButton("rename");
	private JButton behaveremove = new JButton("-");
	private JButton entityremove = new JButton("-");

	public EntityEditor(){
		
		loadEntitys();
		
		entityselector = new JComboBox<>(new DynamicComboBoxModel<>(EntityData.entityData));
		
		fields.put("name", new JTextField(10));
		fields.put("rarity", new JTextField(10));
		fields.put("strength", new JTextField(10));
		fields.put("sizex", new JTextField(10));
		fields.put("sizey", new JTextField(10));
		fields.put("rendersizex", new JTextField(10));
		fields.put("rendersizey", new JTextField(10));
		fields.put("class", new JTextField(10));
		fields.put("behaviours", new JList<String>());
		fields.put("type", new JTextField(10));
		fields.put("offsetx", new JTextField(10));
		fields.put("offsety", new JTextField(10));
		
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
		c.gridx = 3;
		add(entityremove, c);
		
		c.gridx = 4;
		add(rename, c);
		
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
		c.gridy = 9;
		add(new JLabel("type"), c);
		c.gridy = 10;
		add(new JLabel("offsetx"), c);
		c.gridy = 11;
		add(new JLabel("offsety"), c);
		
		c.gridx = 1;
		c.gridwidth = REMAINDER;
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
		c.gridy = 9;
		add(fields.get("type"), c);
		c.gridy = 10;
		add(fields.get("offsetx"), c);
		c.gridy = 11;
		add(fields.get("offsety"), c);
		
		c.gridx = 0;
		c.gridy = 12;
		add(new JLabel("behaviours"), c);
		
		c.gridy = 13;
		c.fill = BOTH;
		((JList<?>) fields.get("behaviours")).setLayoutOrientation(JList.VERTICAL);
		((JList<?>) fields.get("behaviours")).setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane sp = new JScrollPane(fields.get("behaviours"));
		sp.setSize(200, 200);
		add(sp, c);
		
		c.fill = HORIZONTAL;
		c.gridwidth = 3;
		c.gridy = 14;
		add(behaviourfield, c);
		
		c.gridwidth = 1;
		c.gridx = RELATIVE;
		add(behaveadd, c);
		
		add(behaveremove, c);
		
		entityselector.addItemListener(this);
		entityadd.addActionListener(this);
		behaveadd.addActionListener(this);
		rename.addActionListener(this);
		entityremove.addActionListener(this);
		behaveremove.addActionListener(this);
		selectEntity(EntityData.entityData.get(entityselector.getSelectedItem().toString()));
	}
	
	@SuppressWarnings("unchecked")
	private void refreshEntityData(){
		try {
			if(!EntityData.entityData.isEmpty() && entityselector.getModel().getSelectedItem() != null){
				EntityData.entityData.get(((EntityData)entityselector.getSelectedItem()).getName()).setRarity(Integer.valueOf(((JTextField)fields.get("rarity")).getText()));
				EntityData.entityData.get(((EntityData)entityselector.getSelectedItem()).getName()).setRenderSize(new Vector2f(Float.valueOf(((JTextField)fields.get("rendersizex")).getText()),Float.valueOf(((JTextField)fields.get("rendersizey")).getText())));
				EntityData.entityData.get(((EntityData)entityselector.getSelectedItem()).getName()).setSize(new Vector2f(Float.valueOf(((JTextField)fields.get("sizex")).getText()),Float.valueOf(((JTextField)fields.get("sizey")).getText())));
				EntityData.entityData.get(((EntityData)entityselector.getSelectedItem()).getName()).setStrength(Integer.valueOf(((JTextField)fields.get("strength")).getText()));
				EntityData.entityData.get(((EntityData)entityselector.getSelectedItem()).getName()).setType(((JTextField)fields.get("type")).getText());
				EntityData.entityData.get(((EntityData)entityselector.getSelectedItem()).getName()).setClasspath((Class<? extends Entity>) Class.forName(((JTextField)fields.get("class")).getText()));
				List<String> behaviours = new ArrayList<String>();
				for(int i = 0; i < ((JList<String>)fields.get("behaviours")).getModel().getSize(); i++){
					behaviours.add((String) ((JList<String>)fields.get("behaviours")).getModel().getElementAt(i));
				}
				EntityData.entityData.get(((EntityData)entityselector.getSelectedItem()).getName()).setRenderOffset(new Vector2f(Float.valueOf(((JTextField)fields.get("offsetx")).getText()),Float.valueOf(((JTextField)fields.get("offsety")).getText())));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void selectEntity(EntityData entity){
		((JTextField) fields.get("name")).setText(entity.getName());
		((JTextField) fields.get("rarity")).setText(String.valueOf(entity.getRarity()));
		((JTextField) fields.get("strength")).setText(String.valueOf(entity.getStrength()));
		((JTextField) fields.get("sizex")).setText(String.valueOf(entity.getSize().getX()));
		((JTextField) fields.get("sizey")).setText(String.valueOf(entity.getSize().getY()));
		((JTextField) fields.get("rendersizex")).setText(String.valueOf(entity.getRenderSize().getX()));
		((JTextField) fields.get("rendersizey")).setText(String.valueOf(entity.getRenderSize().getY()));
		((JTextField) fields.get("class")).setText(String.valueOf(entity.getClasspath().getCanonicalName()));
		((JTextField) fields.get("type")).setText(entity.getType());
		((JList<String>) fields.get("behaviours")).setListData(new Vector<>(entity.getBehaviours()));
		((JTextField) fields.get("offsetx")).setText(String.valueOf(entity.getRenderOffset().getX()));
		((JTextField) fields.get("offsety")).setText(String.valueOf(entity.getRenderOffset().getY()));
	}
	
	private void addEntity(){
		if(!EntityData.entityData.containsKey("new Entity")){
			EntityData.entityData.put("new Entity", new EntityData());
			entityselector.addItem(EntityData.entityData.get("new Entity"));
		}
		refreshEntityData();
		selectEntity(EntityData.entityData.get("new Entity"));
		entityselector.setSelectedItem(EntityData.entityData.get("new Entity"));
		for(int i = 0; i < entityselector.getItemCount(); i++){
			System.out.printf("%s\n", entityselector.getItemAt(i).getName());
		}
	}
	
	private void rename(EntityData e, String name){
		EntityData buffer = new EntityData(e);
		EntityData.entityData.remove(e.getName());
		entityselector.removeItem(e);
		buffer.setName(name);
		EntityData.entityData.put(name, buffer);
		entityselector.addItem(buffer);
		entityselector.setSelectedItem(buffer);
		entityselector.repaint();
	}
	
	private void loadEntitys(){
		DataLoader.loadEntitys();
	}
	
	void saveEntitys(){
		refreshEntityData();
		DataLoader.saveEntitys(EntityData.entityData);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource().equals(entityselector)){
			if(e.getStateChange() == ItemEvent.SELECTED){
				selectEntity( (EntityData) e.getItem());
			}
			if(e.getStateChange() == ItemEvent.DESELECTED){
				refreshEntityData();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(entityadd)){
			addEntity();
		}
		if(e.getSource().equals(behaveadd)){
			ListModel<String> model = ((JList<String>) fields.get("behaviours")).getModel();
			String[] data = new String[model.getSize()+1];
			for(int i = 0; i < model.getSize(); i++){
				data[i] = model.getElementAt(i);
			}
			data[model.getSize()] = behaviourfield.getText();
			((JList<String>) fields.get("behaviours")).setListData(data);
		}
		if(e.getSource().equals(behaveremove)){
			ListModel<String> model = ((JList<String>) fields.get("behaviours")).getModel();
			Vector<String> data = new Vector<>();
			for(int i = 0; i < model.getSize(); i++){
				if(i == ((JList<String>)fields.get("behaviours")).getSelectedIndex()){
					continue;
				}
				data.add(model.getElementAt(i));
			}
			((JList<String>) fields.get("behaviours")).setListData(data);
		}
		if(e.getSource().equals(rename)){
			rename(EntityData.entityData.get(((EntityData) entityselector.getSelectedItem()).getName()), ((JTextField)fields.get("name")).getText());
		}
		if(e.getSource().equals(entityremove)){
			EntityData buffer = (EntityData) entityselector.getSelectedItem();
			entityselector.setSelectedIndex(0);
			entityselector.removeItem(buffer);
			EntityData.entityData.remove(buffer.getName());
		}
	}
	
	

}
