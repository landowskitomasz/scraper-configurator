package com.tennizoom.scraper.configurator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class EntriesListModel implements ListModel {

	
	private List<String> resultNodes = new ArrayList<String>();
	
	private List<ListDataListener> listeners = new ArrayList<ListDataListener>();
	
	public void loadData(List<Map<String, Object>> results){
		resultNodes.clear();
		for(Map<String, Object> result : results){
			resultNodes.add(result.get("value").toString());
		}
		for(ListDataListener listener : listeners){
			listener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize()));
		}
	}
	
	@Override
	public void addListDataListener(ListDataListener arg0) {
		listeners.add(arg0);
	}

	@Override
	public Object getElementAt(int arg0) {
		return resultNodes.get(arg0);
	}

	@Override
	public int getSize() {
		return resultNodes.size();
	}

	@Override
	public void removeListDataListener(ListDataListener arg0) {
		int index = listeners.indexOf(arg0);
		if(index > 0){
			listeners.remove(index);
		}
	}

	public void clear() {
		resultNodes.clear();
		for(ListDataListener listener : listeners){
			listener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize()));
		}
	}

}
