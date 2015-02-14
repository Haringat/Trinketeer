package com.ichmed.trinketeers.util.editor;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.ichmed.trinketeers.entity.Entity;

public class EntityEditor extends JPanel {

	private static final long serialVersionUID = 1840428064298141218L;

	private HashMap<String, Entity> ents = new HashMap<>();
	private JComboBox<com.ichmed.trinketeers.entity.Entity> entityselector;

	public EntityEditor() {
		setLayout(new GridBagLayout());
		setMinimumSize(new Dimension(100,100));
		entityselector = new JComboBox<com.ichmed.trinketeers.entity.Entity>();
		add(entityselector);
		loadEntitys();
	}
	
	private void addEntity(Entity e, int i){
		
	}
	
	public void saveEntitys(){
		// TODO: save data
	}
	
	private void loadEntitys(){
		// TODO: load data
	}

}
