package com.tennizoom.scraper.configurator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.tennizoom.scraper.domain.DataEntry;

public class ResultsTabModel implements TableModel{

	private List<Map<String, Object>> results = new ArrayList<Map<String,Object>>();
	
	private DataEntry dataEntry = new DataEntry();
	
	private List<TableModelListener> listeners = new ArrayList<TableModelListener>();
	
	public void reconfigureModel(DataEntry dataEntry){
		this.dataEntry = dataEntry;
		for(TableModelListener listener : listeners){
			listener.tableChanged(new TableModelEvent(this, TableModelEvent.ALL_COLUMNS));
		}
	}
	
	public void loadData(List<Map<String, Object>> results){
		this.results.clear();
		this.results.addAll(results);
		for(TableModelListener listener : listeners){
			listener.tableChanged(new TableModelEvent(this, TableModelEvent.ALL_COLUMNS));
		}
	}
	
	@Override
	public void addTableModelListener(TableModelListener l) {
		listeners.add(l);	
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if(columnIndex == 0){
			return Integer.class;
		}
		else{
			return String.class;
		}
	}

	@Override
	public int getColumnCount() {
		return dataEntry.getFields().size() + 1;
	}

	@Override
	public String getColumnName(int columnIndex) {
		if(columnIndex == 0){
			return "Lp.";
		}
		
		return dataEntry.getFields().get(columnIndex -1).getName();
	}

	@Override
	public int getRowCount() {
		return results.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(columnIndex == 0){
			return rowIndex + 1;
		}
		String key = dataEntry.getFields().get(columnIndex -1 ).getName();
		if(results.get(rowIndex).containsKey(key)){
			return results.get(rowIndex).get(key);
		}
		return null;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		int index = listeners.indexOf(l);
		if(index > -1){
			listeners.remove(index);
		}
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}

	public void clear() {
		results.clear();
		for(TableModelListener listener : listeners){
			listener.tableChanged(new TableModelEvent(this, TableModelEvent.ALL_COLUMNS));
		}
	}

}
