package com.ichmed.trinketeers.util.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class DynamicComboBoxModel<E> extends AbstractListModel<E> implements MutableComboBoxModel<E> {

	private static final long serialVersionUID = 5381750179378656750L;
	private List<E> data;
	private E selectedItem;
	
	public DynamicComboBoxModel(){
		data = new ArrayList<E>();
	}
	
	public <I> DynamicComboBoxModel(HashMap<I, E> data){
		this();
		for(I key : data.keySet()){
			this.data.add(data.get(key));
		}
		selectedItem = (E) this.data.get(0);
	}

	public int getSize() {
		return data.size();
	}

	public E getElementAt(int index) {
		return (E) data.get(index);
	}

	@Override
	public void addElement(E item) {
		data.add(item);
		for(ListDataListener l : listenerList.getListeners(ListDataListener.class)){
			l.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, 0, data.size()-1));
		}
	}

	@Override
	public void removeElement(Object obj) {
		data.remove(obj);
		for(ListDataListener l : listenerList.getListeners(ListDataListener.class)){
			l.intervalRemoved(new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, 0, data.size()-1));
		}
	}

	@Override
	public void insertElementAt(E item, int index) {
		data.add(index, item);
		for(ListDataListener l : listenerList.getListeners(ListDataListener.class)){
			l.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, 0, data.size()-1));
		}
	}

	@Override
	public void removeElementAt(int index) {
		data.remove(index);
		for(ListDataListener l : listenerList.getListeners(ListDataListener.class)){
			l.intervalRemoved(new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, 0, data.size()-1));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setSelectedItem(final Object anItem) {
		if(!(this.data.isEmpty()) && this.data.get(0).getClass().isInstance(anItem)){
			selectedItem = (E) anItem;
			for(ListDataListener l : listenerList.getListeners(ListDataListener.class)){
				l.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, data.size()-1));
			}
		}
	}

	@Override
	public Object getSelectedItem() {
		return selectedItem;
	}
	
}
