package com.tennizoom.scraper.configurator.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.tennizoom.scraper.domain.Category;

public class CategoriesTabModel  implements TableModel {

	private String [] columnHeaders = new String[] {
			"Lp.", "Nazwa kategorii", "Adres www kategorii"
		};
	private Class<?>[] columnClasses = new Class<?>[]{
			Integer.class, String.class, String.class
	};
	
	List<Category> categories = new ArrayList<Category>();
	
	private List<TableModelListener> tableModelListeners = new ArrayList<TableModelListener>();
	
	public void loadData(List<Category> categories){
		this.categories = categories;
		for(TableModelListener listener : this.tableModelListeners){
			listener.tableChanged(new TableModelEvent(this));
		}
	}
	
	public void addCategory(Category category){
		this.categories.add(category);
		for(TableModelListener listener : this.tableModelListeners){
			listener.tableChanged(new TableModelEvent(this,  TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
		}
	}
	
	@Override
	public void addTableModelListener(TableModelListener l) {
		tableModelListeners.add(l);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		
		return columnClasses[columnIndex];
	}

	@Override
	public int getColumnCount() {
		return columnHeaders.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnHeaders[columnIndex];
	}

	@Override
	public int getRowCount() {
		return categories.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex){
		case 0:
			return rowIndex + 1;
		case 1:
			return categories.get(rowIndex).getName();
		case 2:
			return categories.get(rowIndex).getUrl();
		}
		return null;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		int index = tableModelListeners.indexOf(l);
		if(index > -1 ){
			tableModelListeners.remove(index);
		}	
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		throw new UnsupportedOperationException("This operation is not supported.");
	}

	public Category getEntry(int row) {
		return categories.get(row);
	}

	public void add(Category category) {
		categories.add(category);
		for(TableModelListener listener : this.tableModelListeners){
			listener.tableChanged(new TableModelEvent(this,  categories.indexOf(category), TableModelEvent.INSERT));
		}
	}


    public void remove(Category category) {
        int row = categories.indexOf(category);
        categories.remove(category);
        for(TableModelListener listener : this.tableModelListeners){
            listener.tableChanged(new TableModelEvent(this,  row -1, row, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE ));
        }
    }
}
