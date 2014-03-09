package com.tennizoom.scraper.configurator.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.tennizoom.scraper.config.ValueProcessorConfig;
import com.tennizoom.scraper.config.ValueProcessorOption;

public class ValueProcessorsTabModel implements TableModel{

	private List<TableModelListener> tableModelListeners = new ArrayList<TableModelListener>();
	
	private List<ValueProcessorConfig> valueProcessorConfigs = new ArrayList<ValueProcessorConfig>();
	
	public void loadData(List<ValueProcessorConfig> valueProcessorConfigs ){
		this.valueProcessorConfigs = valueProcessorConfigs;
		for(TableModelListener listener : tableModelListeners){
			listener.tableChanged(new TableModelEvent(this));
		}
	}
	
	private String[] headers = new String[]{
			"Lp.", "Typ value processora", "Opcje"
	};
	
	private Class<?>[] types = new Class<?>[]{
		Integer.class, String.class, String.class	
	};
	
	@Override
	public void addTableModelListener(TableModelListener l) {
		tableModelListeners.add(l);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return types[columnIndex];
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
		return valueProcessorConfigs.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return rowIndex + 1;
		case 1:
			return valueProcessorConfigs.get(rowIndex).getProcessorType().name();
		case 2:
			return evaluateOptions(valueProcessorConfigs.get(rowIndex).getOptions());
		default:
			break;
		}
		return null;
	}

	private String evaluateOptions(List<ValueProcessorOption> options) {
		StringBuilder builder = new StringBuilder();
		for(ValueProcessorOption option : options){
			builder.append(option.getName()).append(": '").append(option.getValue()).append("', ");
		}
		return builder.toString();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		int index = tableModelListeners.indexOf(l);
		if(index > 0){
			tableModelListeners.remove(index);
		}
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		
		
	}

	public void moveDown(int row) {
		if( row < (valueProcessorConfigs.size() -1)){
			ValueProcessorConfig processorToMove = valueProcessorConfigs.get(row);
			valueProcessorConfigs.remove(row);
			valueProcessorConfigs.add(row + 1 , processorToMove);
			for(TableModelListener listener : tableModelListeners){
				listener.tableChanged(new TableModelEvent(this));
			}
		}
	}

	public void moveUp(int row) {
		if(row > 0){
			ValueProcessorConfig processorToMove = valueProcessorConfigs.get(row);
			valueProcessorConfigs.remove(row);
			valueProcessorConfigs.add(row - 1 , processorToMove);
	
			for(TableModelListener listener : tableModelListeners){
				listener.tableChanged(new TableModelEvent(this));
			}
		}
	}

	public ValueProcessorConfig getEntry(int row) {
		return valueProcessorConfigs.get(row);
	}

	public void change(ValueProcessorConfig valueProcessor,
			ValueProcessorConfig editedValueProcessor) {
		int index = valueProcessorConfigs.indexOf(valueProcessor);
		valueProcessorConfigs.remove(index);
		valueProcessorConfigs.add(index, editedValueProcessor);
		for(TableModelListener listener : tableModelListeners){
			listener.tableChanged(new TableModelEvent(this));
		}
	}

	public void add(ValueProcessorConfig valueProcessor) {
		valueProcessorConfigs.add(valueProcessor);
		for(TableModelListener listener : tableModelListeners){
			listener.tableChanged(new TableModelEvent(this));
		}
	}

	public void delete(int row) {
		if( row < (valueProcessorConfigs.size())){
			valueProcessorConfigs.remove(row);
		}
		for(TableModelListener listener : tableModelListeners){
			listener.tableChanged(new TableModelEvent(this));
		}
	}

}
