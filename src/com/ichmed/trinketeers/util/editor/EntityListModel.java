package com.ichmed.trinketeers.util.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;

import com.ichmed.trinketeers.savefile.data.EntityData;

public class EntityListModel<E extends EntityData> extends AbstractListModel<E> implements MutableComboBoxModel<E> {

	private static final long serialVersionUID = 5381750179378656750L;
	private List<EntityData> data;
	private E selectedItem;
	
	@SuppressWarnings("unchecked")
	public EntityListModel(HashMap<String, E> data){
		this.data = new ArrayList<EntityData>();
		for(EntityData value : data.entrySet().toArray(new EntityData[0])){
			this.data.add(value);
		}
		selectedItem = (E) this.data.get(0);
	}

	public int getSize() {
		return data.size();
	}

	@SuppressWarnings("unchecked")
	public E getElementAt(int index) {
		return (E) data.get(index);
	}

	@Override
	public void addElement(E item) {
		data.add(item);
		
	}

	@Override
	public void removeElement(Object obj) {
		data.remove(obj);
		
	}

	@Override
	public void insertElementAt(E item, int index) {
		data.add(index, item);
		
	}

	@Override
	public void removeElementAt(int index) {
		data.remove(index);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setSelectedItem(Object anItem) {
		selectedItem = (E) anItem;
		
	}

	@Override
	public Object getSelectedItem() {
		return selectedItem;
	}

}
