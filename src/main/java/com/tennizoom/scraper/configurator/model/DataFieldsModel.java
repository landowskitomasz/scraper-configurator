package com.tennizoom.scraper.configurator.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.tennizoom.scraper.domain.DataField;

public class DataFieldsModel implements ListModel{

	private List<DataField> dataFields = new ArrayList<DataField>();
	
	private List<ListDataListener> dataListeners = new ArrayList<ListDataListener>();

	@Override
	public void addListDataListener(ListDataListener arg0) {
		dataListeners.add(arg0);
	}
	
	public void loadData(List<DataField> dataFields){
		
		this.dataFields = dataFields;
		for(ListDataListener listener : dataListeners){
			listener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize()));
		}
	}

	@Override
	public Object getElementAt(int arg0) {
		return dataFields.get(arg0).getName();
	}

	@Override
	public int getSize() {
		return dataFields.size();
	}

	@Override
	public void removeListDataListener(ListDataListener arg0) {
		int index = dataListeners.indexOf(arg0);
		if(index > 0){
			dataListeners.remove(index);
		}
	}

	public List<DataField> getEntries() {
		return dataFields;
	}

	public void add(String name) {
		DataField field = new DataField();
		field.setName(name);
		field.setxPath("");
		dataFields.add(field);
		for(ListDataListener listener : dataListeners){
			listener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize()));
		}
	}

	public void remove(int index) {
		if(index > -1){
			dataFields.remove(index);
		}
		for(ListDataListener listener : dataListeners){
			listener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize()));
		}
	}

}