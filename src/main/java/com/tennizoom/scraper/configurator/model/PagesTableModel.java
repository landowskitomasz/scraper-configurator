package com.tennizoom.scraper.configurator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class PagesTableModel implements TableModel{

	private String[] headers = new String[]{
			"Lp.", "Adres strony"
	};
	
	private Class<?>[] classes = new Class<?>[]{
			Integer.class, String.class
	};
	
	private List<String> pages = new ArrayList<String>();
	
	private List<TableModelListener> listeners = new ArrayList<TableModelListener>();
	
	@Override
	public void addTableModelListener(TableModelListener l) {
		listeners.add(l);	
	}


	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return classes[columnIndex];
	}

	@Override
	public int getColumnCount() {
		return headers.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return headers[columnIndex];
	}

	@Override
	public int getRowCount() {
		
		return pages.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(columnIndex == 0){
			return rowIndex + 1;
		}
		else{
			return pages.get(columnIndex);
		}
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
	
	public void loadData(Set<String> pages){
		this.pages.clear();
		this.pages.addAll(pages);
		for(TableModelListener listener : listeners){
			listener.tableChanged(new TableModelEvent(this, TableModelEvent.ALL_COLUMNS));
		}
	}

}
