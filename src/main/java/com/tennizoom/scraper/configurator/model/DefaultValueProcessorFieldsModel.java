package com.tennizoom.scraper.configurator.model;


import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.tennizoom.scraper.domain.DefaultValueProcessorField;

public class DefaultValueProcessorFieldsModel implements ListModel{

	private List<DefaultValueProcessorField> defaultValueProcessorFields = new ArrayList<DefaultValueProcessorField>();
	
	private List<ListDataListener> dataListeners = new ArrayList<ListDataListener>();

	@Override
	public void addListDataListener(ListDataListener arg0) {
		dataListeners.add(arg0);
	}
	
	public void loadData(List<DefaultValueProcessorField> defaultValueProcessorFields){
		
		this.defaultValueProcessorFields = defaultValueProcessorFields;
		for(ListDataListener listener : dataListeners){
			listener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize()));
		}
	}

	@Override
	public Object getElementAt(int arg0) {
		return defaultValueProcessorFields.get(arg0).getName();
	}

	@Override
	public int getSize() {
		return defaultValueProcessorFields.size();
	}

	@Override
	public void removeListDataListener(ListDataListener arg0) {
		int index = dataListeners.indexOf(arg0);
		if(index > 0){
			dataListeners.remove(index);
		}
	}

	public List<DefaultValueProcessorField> getEntries() {
		return defaultValueProcessorFields;
	}

	public void add(String name) {
		DefaultValueProcessorField field = new DefaultValueProcessorField();
		field.setName(name);
		defaultValueProcessorFields.add(field);
		for(ListDataListener listener : dataListeners){
			listener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize()));
		}
	}

	public void remove(int selectedIndex) {
		if(selectedIndex > -1){
			defaultValueProcessorFields.remove(selectedIndex);
		}
		for(ListDataListener listener : dataListeners){
			listener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize()));
		}
	}

	public void refresh() {
		for(ListDataListener listener : dataListeners){
			listener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize()));
		}
	}

}
