package com.tennizoom.scraper.configurator.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.IOUtils;


import com.tennizoom.scraper.config.DefaultValueProcessorsPropagation;
import com.tennizoom.scraper.config.ValueProcessorConfig;
import com.tennizoom.scraper.config.ValueProcessorType;
import com.tennizoom.scraper.configurator.model.CategoriesTabModel;
import com.tennizoom.scraper.configurator.model.DefaultValueProcessorFieldsModel;
import com.tennizoom.scraper.configurator.model.ValueProcessorsTabModel;
import com.tennizoom.scraper.domain.Category;
import com.tennizoom.scraper.domain.DataEntry;
import com.tennizoom.scraper.domain.DataField;
import com.tennizoom.scraper.domain.DefaultValueProcessorField;
import com.tennizoom.scraper.domain.DefaultValueProcessors;
import com.tennizoom.scraper.domain.Shop;

public class MainWindowController {

	private CategoriesTabModel categoriesTabModel;
	
	private ValueProcessorsTabModel defaultValueProcessorsTabModel;
	
	private DefaultValueProcessorFieldsModel defaultValueProcessorFieldsModel;
	
	private Shop shop;
	
	private File confFile;
	
	public MainWindowController(){
		categoriesTabModel = new CategoriesTabModel();
		defaultValueProcessorFieldsModel = new DefaultValueProcessorFieldsModel();
		defaultValueProcessorsTabModel = new ValueProcessorsTabModel();
	}
	
	public CategoriesTabModel getCategoriesTabModel(){
		return categoriesTabModel;
	}
	
	public DefaultValueProcessorFieldsModel getDefaultValueProcessorFieldsModel() {
		return defaultValueProcessorFieldsModel;
	}
	

	public ValueProcessorsTabModel getDefaultValueProcessorsTabModel() {
		return defaultValueProcessorsTabModel;
	}

	public void openShopConfiguration(File selectedFile) {
		confFile = selectedFile;
	    shop = Shop.getInstance(selectedFile.getAbsolutePath());
	    if(shop == null){
	    	throw new IllegalStateException("Unable to load configuration data. Probably file content is broken.");
	    }
		getCategoriesTabModel().loadData(shop.getCategories());
		getDefaultValueProcessorFieldsModel().loadData(shop.getDefaultValueProcessors().getFields());
	}

	public void newShopConfiguration() {
		confFile = null;
		shop = new Shop();
		getCategoriesTabModel().loadData(shop.getCategories());
		getDefaultValueProcessorFieldsModel().loadData(shop.getDefaultValueProcessors().getFields());
	}

	public void defaultValueProcessorFieldSelected(int index) {
		if(index > -1){
			DefaultValueProcessorField field = getDefaultValueProcessorFieldsModel().getEntries().get(index);
			getDefaultValueProcessorsTabModel().loadData(field.getValueProcessors());
		}else{
			getDefaultValueProcessorsTabModel().loadData(new ArrayList<ValueProcessorConfig>());
		}
	}

	public void moveDownDefaultValueProcessor(int selectedRow) {
		getDefaultValueProcessorsTabModel().moveDown(selectedRow);
		
	}

	public void moveUpDefaultValueProcessor(int selectedRow) {
		getDefaultValueProcessorsTabModel().moveUp(selectedRow);
		
	}

	public ValueProcessorConfig getValueProcessorToEdit(int selectedRow) {
		return getDefaultValueProcessorsTabModel().getEntry(selectedRow);
	}

	public void applyValueProcessorEdit(ValueProcessorConfig valueProcessor,
			ValueProcessorConfig editedValueProcessor) {
		getDefaultValueProcessorsTabModel().change(valueProcessor, editedValueProcessor);
		
	}

	public void addDefaultValueProcessor(
			ValueProcessorConfig valueProcessor) {
		getDefaultValueProcessorsTabModel().add(valueProcessor);
		
	}

	public void deleteDefaultValueProcessor(int selectedRow) {
		getDefaultValueProcessorsTabModel().delete(selectedRow);
		
	}

	public void createNewDefaultValueProcessorsField(String name) {
		getDefaultValueProcessorFieldsModel().add(name);
	}

	public Category getCategoryToOpen(int row) {
		Category category = getCategoriesTabModel().getEntry(row);
		category.setDefaultValueProcessors(shop.getDefaultValueProcessors());
		return category;
	}

	public Category createNewCategory() {
		Category category = new Category();
		DataEntry productEntry = new DataEntry();
		productEntry.setName("product");
		category.getDataEntries().add(productEntry);
		category.setName("Nowa kategoria");
		getCategoriesTabModel().add(category);
		category.setDefaultValueProcessors(shop.getDefaultValueProcessors());
		return category;
	}

	public Category copyCategory(int selectedRow) {
		Category category = getCategoriesTabModel().getEntry(selectedRow);
		Category newCategory = new Category();
		newCategory.setName(category.getName() + " (Kopia)");
		newCategory.setUrl(category.getUrl());
		DataEntry newDataEntry = new DataEntry();
		DataEntry dataEntry = category.getDataEntries().get(0);
		newDataEntry.setName(dataEntry.getName());
		newDataEntry.setxPath(dataEntry.getxPath());
		
		for(DataField field : dataEntry.getFields()){
			DataField newField = new DataField();
			newField.setName(field.getName());
			newField.setDebug(field.isDebug());
			newField.setRequired(field.isRequired());
			newField.setxPath(field.getxPath());
			newDataEntry.getFields().add(newField);
			
			for(ValueProcessorConfig valueProcessorConfig : field.getValueProcessors()){
				ValueProcessorConfig newValueProcessorConfig = new ValueProcessorConfig();
				newValueProcessorConfig.setProcessorType(valueProcessorConfig.getProcessorType());
				newValueProcessorConfig.setOptions(valueProcessorConfig.getOptions());
				newField.getValueProcessors().add(newValueProcessorConfig);
			}
		}
		newCategory.getDataEntries().add(newDataEntry);
		
		getCategoriesTabModel().add(newCategory);
		return newCategory;
	}

	public boolean isFileOpened() {
		return confFile != null;
	}

	public boolean isConfigurationOpened() {
		return shop != null;
	}

	public File getOpenedFile() {
		return confFile;
	}

	public void saveToFile(File fileToSave) {
		confFile = fileToSave;
		save();
		
	}

	public void save() {
		OutputStream os = null;
		try {
			os = new FileOutputStream(confFile);
			
			Writer configWriter = new OutputStreamWriter(os); 

			JAXBContext context = JAXBContext.newInstance(new Class[] {Shop.class, Category.class, DataEntry.class, DataField.class, 
					ValueProcessorConfig.class, ValueProcessorType.class, DefaultValueProcessorField.class, 
					DefaultValueProcessors.class, DefaultValueProcessorsPropagation.class});
			Marshaller marshaller = context.createMarshaller();

			marshaller.marshal(shop, configWriter);

		} catch (JAXBException e) {
			throw new IllegalStateException(e);
		} catch (FileNotFoundException e) {
			throw new IllegalStateException(e);
		}
		finally{
			IOUtils.closeQuietly(os);
		}
	}

	public void removeField(int selectedIndex) {
		getDefaultValueProcessorFieldsModel().remove(selectedIndex);
	}

	public void refreshModels() {
		getDefaultValueProcessorFieldsModel().refresh();
	}
	
}
