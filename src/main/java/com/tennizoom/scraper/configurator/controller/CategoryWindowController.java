package com.tennizoom.scraper.configurator.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.tennizoom.scraper.config.DefaultValueProcessorsPropagation;
import com.tennizoom.scraper.config.ValueProcessorConfig;
import com.tennizoom.scraper.configurator.model.DataFieldsModel;
import com.tennizoom.scraper.configurator.model.EntriesListModel;
import com.tennizoom.scraper.configurator.model.PagesTableModel;
import com.tennizoom.scraper.configurator.model.ResultsTabModel;
import com.tennizoom.scraper.configurator.model.ValueProcessorsTabModel;
import com.tennizoom.scraper.domain.Category;
import com.tennizoom.scraper.domain.DataEntry;
import com.tennizoom.scraper.domain.DataField;
import com.tennizoom.scraper.domain.DefaultValueProcessorField;
import com.tennizoom.scraper.domain.DefaultValueProcessors;
import com.tennizoom.scraper.domain.Pagination;
import com.tennizoom.scraper.exception.FieldRequiredException;
import com.tennizoom.scraper.exception.HtmlLoaderException;
import com.tennizoom.scraper.worker.HtmlLoader;

public class CategoryWindowController {

	private Category category;

	private Document loadedDocument;
	
	HtmlLoader loader = new HtmlLoader();
	
	private EntriesListModel entriesListModel = new EntriesListModel();
	
	private DataFieldsModel dataFieldsModel = new DataFieldsModel();
	
	private ValueProcessorsTabModel valueProcessorsTabModel = new ValueProcessorsTabModel();

	private ResultsTabModel resultsTabModel = new ResultsTabModel();
	
	private PagesTableModel pagesTableModel = new PagesTableModel();

	public CategoryWindowController(Category category){
		this.category = category;
		this.dataFieldsModel.loadData(category.getDataEntries().get(0).getFields());
		this.getResultsTableModel().reconfigureModel(category.getDataEntries().get(0));
	}

	public void nameChanged(String newName) {
		if(newName != null && !newName.equals(category.getName())){
			category.setName(newName);
		}
	}

	public void urlChanged(String newUrl) {
		if(newUrl != null && !newUrl.equals(category.getUrl())){
			category.setUrl(newUrl);
		}
	}

	public void xPathChanged(String newXPath) {
		if(newXPath != null && !newXPath.equals(category.getDataEntries().get(0).getxPath())){
			category.getDataEntries().get(0).setxPath(newXPath);
			if(loadedDocument != null){
				reloadDataEntries();
				reloadResults();
			}
		}
	}

	public Category getCategory() {
		
		return category;
	}

	public void loadUrl() throws HtmlLoaderException {
		try{
			loadedDocument = loader.loadCleanHtml(category.getUrl());
		}
		catch(HtmlLoaderException e){
			loadedDocument = null;
			getResultsTableModel().clear();
			getEntriesListModel().clear();
			throw e;
		}
		reloadDataEntries();
		reloadResults();
		reloadPagesModel();
	}
	
	private void reloadDataEntries(){
		DataEntry entry = category.getDataEntries().get(0);
		DataEntry mockedEntry = new DataEntry();
		mockedEntry.setxPath(entry.getxPath());
		mockedEntry.setName("mock");
		List<DataField> fields = new ArrayList<DataField>();
		DataField mockedField = new DataFieldMock();
		mockedField.setName("value");
		fields.add(mockedField);
		mockedEntry.setFields(fields);
		List<Map<String, Object>> result = mockedEntry.findData(loadedDocument, new DefaultValueProcessors());
		entriesListModel.loadData(result);

	}
	
	@SuppressWarnings("unchecked")
	private void reloadResults(){
		try{
			Map<String, Object> data = category.findData(loadedDocument);
			getResultsTableModel().loadData((List<Map<String, Object>>) data.get(category.getDataEntries().get(0).getName()));
		}catch(Exception e){
			
		}
	}
	
	private class DataFieldMock extends DataField{
	
		@Override
		public String findFieldValue(Object node, List<ValueProcessorConfig> processors) throws FieldRequiredException {
			return this.toXml((Node)node).replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""); 
		}
	}

	public EntriesListModel getEntriesListModel() {
		return entriesListModel;
	}

	public DataFieldsModel getDataFieldsModel() {
		return dataFieldsModel;
	}

	public ValueProcessorsTabModel getValueProcessorsTabModel() {
		return valueProcessorsTabModel;
	}

	public DataField dataFieldSelected(int selectedIndex) {
		if(selectedIndex > - 1){
			DataField field = dataFieldsModel.getEntries().get(selectedIndex);
			valueProcessorsTabModel.loadData(field.getValueProcessors());
			return field;
		}
		else{
			return new DataField();
		}
	}

	public ResultsTabModel getResultsTableModel() {
		return resultsTabModel ;
	}

	public void currentFieldXPathModified(int selectedIndex, String text) {
		if(selectedIndex > -1){
			DataField field = dataFieldsModel.getEntries().get(selectedIndex);
			if(text != null && !text.equals(field.getxPath())){
				field.setxPath(text);
				reloadResults();
			}
		}
	}

	public void createNewField(String name) {
		getDataFieldsModel().add(name);
		getResultsTableModel().reconfigureModel(category.getDataEntries().get(0));
		reloadResults();
	}

	public void addValueProcessor(ValueProcessorConfig createdValueProcessor) {
		getValueProcessorsTabModel().add(createdValueProcessor);
		reloadResults();
	}

	public void applyValueProcessorEdit(ValueProcessorConfig valueProcessor,
			ValueProcessorConfig editedValueProcessor) {
		getValueProcessorsTabModel().change(valueProcessor, editedValueProcessor);

		reloadResults();
	}

	public ValueProcessorConfig getValueProcessorToEdit(int selectedRow) {
		return getValueProcessorsTabModel().getEntry(selectedRow);
	}

	public void deleteValueProcessor(int selectedRow) {
		getValueProcessorsTabModel().delete(selectedRow);
		reloadResults();
		
	}
	public void moveDownValueProcessor(int selectedRow) {
		getValueProcessorsTabModel().moveDown(selectedRow);
		reloadResults();
		
	}

	public void moveUpValueProcessor(int selectedRow) {
		getValueProcessorsTabModel().moveUp(selectedRow);
		reloadResults();
		
	}

	public void removeField(int index) {
		getDataFieldsModel().remove(index);
	}

	public void defaultValueProcessorsPropagationChanged(int selectedIndex,
			DefaultValueProcessorsPropagation defaultValueProcessorsPropagation) {
		DataField field = getDataFieldsModel().getEntries().get(selectedIndex);
		if(field.getCallDefaultValueProcessors() != defaultValueProcessorsPropagation){
			field.setCallDefaultValueProcessors(defaultValueProcessorsPropagation);
		}
	}
	
	public void moveValueProcessorToDefault(int selectedField, int selectedProcessor){
		if(selectedField > -1 && selectedProcessor > -1){
			DataField field = getDataFieldsModel().getEntries().get(selectedField);
			ValueProcessorConfig config = getValueProcessorsTabModel().getEntry(selectedProcessor);
			getValueProcessorsTabModel().delete(selectedProcessor);
			
			DefaultValueProcessors defaultValueProcessors = category.getDefaultValueProcessors();
			for(DefaultValueProcessorField defaultValueProcessorField: defaultValueProcessors.getFields()){
				if(defaultValueProcessorField.getName().equals(field.getName())){
					if(field.getCallDefaultValueProcessors() == DefaultValueProcessorsPropagation.after){
						defaultValueProcessorField.getValueProcessors().add(0, config);
					}else{
						defaultValueProcessorField.getValueProcessors().add(config);
					}
					return;
				}
			}
			
			DefaultValueProcessorField newDefaultValueProcessorField = new DefaultValueProcessorField();
			newDefaultValueProcessorField.setName(field.getName());
			newDefaultValueProcessorField.getValueProcessors().add(config);
			defaultValueProcessors.getFields().add(newDefaultValueProcessorField);
		}
	}

	public void removePagination() {
		category.setPagination(null);
	}

	public Pagination createPagination() {
		Pagination pagination = category.getPagination();
		if(pagination == null){
			pagination = new Pagination();
			category.setPagination(pagination);
		}
		
		return pagination;
	}

	public void paginationXPathChanged(String text) {
		Pagination pagination = category.getPagination();
		if(pagination != null){
			String xPath = pagination.getxPath();
			if(xPath == null || !xPath.equals(text)){
				pagination.setxPath(text);
			}
		}
		reloadPagesModel();
	}

	private void reloadPagesModel() {
		Pagination pagination = category.getPagination();
		if(pagination != null && loadedDocument != null){
			Set<String> pages = pagination.findPages(loadedDocument);
			getPagesTableModel().loadData(pages);
		}
	}

	public void paginationRegexChanged(String text) {
		Pagination pagination = category.getPagination();
		if(pagination != null){
			String regex = pagination.getRegex();
			if(regex == null || !regex.equals(text)){
				pagination.setRegex(text);
			}
		}
		reloadPagesModel();
	}
	
	public PagesTableModel getPagesTableModel(){
		return this.pagesTableModel;
	}
}
