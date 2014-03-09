package com.tennizoom.scraper.configurator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;

import com.tennizoom.scraper.config.ValueProcessorConfig;
import com.tennizoom.scraper.config.ValueProcessorConfig;
import com.tennizoom.scraper.configurator.controller.CategoryWindowController;
import com.tennizoom.scraper.domain.Category;
import com.tennizoom.scraper.domain.DataField;
import com.tennizoom.scraper.domain.Pagination;
import com.tennizoom.scraper.exception.HtmlLoaderException;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;

import java.awt.event.ActionListener;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import java.awt.FlowLayout;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Dimension;
import java.awt.Component;
import java.awt.Color;
import java.util.concurrent.ExecutionException;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import com.tennizoom.scraper.config.DefaultValueProcessorsPropagation;
import javax.swing.JCheckBox;

public class CategoryWindow extends JFrame{
	
	private static final long serialVersionUID = 762433732899687986L;
	
	private JTextField nameInput;
	private JTextField urlImput;
	private JTextField xPathInput;
	private final Action action = new LoadHtmlDocumentAction();

	private JTable resultsTable;
	private JTextField fieldxPathInput;
	private JTable valueProcessorsTable;
	private JList fieldsList; 
	
	private CategoryWindowController controller;
	private final Action action_1 = new AddFieldAction();

	private JLabel documentLoadedLabel;

	private JButton editValueProcessorButton;

	private JButton removeValueProcessorButton;

	private JButton moveUpProcessorButton;

	private JButton moveDownProcessorButton;

	private JButton addValueProcessorButton;

	private JButton addFieldButton;

	private JButton removeFieldButton;
	private final Action action_2 = new RemoveFieldAction();
	private final Action action_3 = new AddValueProcessorAction();
	private final Action action_4 = new EditValueProcessorAction();
	private final Action action_5 = new RemoveValueProcessorAction();
	private final Action action_6 = new MoveUpValueProcessorAction();
	private final Action action_7 = new MoveDownAction();

	private JComboBox comboBox;
	private final Action action_8 = new MoveValueProcessorToDefaultsAction();

	private JButton moveValueProcessorToDefaults;
	private JTable pagesTable;
	private JTextField paginationXPathInput;
	private JTextField paginationRegexInput;

	private JCheckBox hasPaginationCheckbox;
	/**
	 * Create the application.
	 */
	public CategoryWindow(Category category) {
		this.controller = new CategoryWindowController(category); 
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		setBounds(150, 150, 914, 453);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel basicTab = new JPanel();
		tabbedPane.addTab("Dane podstawowe", null, basicTab, null);
		basicTab.setLayout(new BorderLayout(0, 0));
		
		JPanel configPanel = new JPanel();
		basicTab.add(configPanel, BorderLayout.NORTH);
		configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.X_AXIS));
		
		Box verticalBox = Box.createVerticalBox();
		configPanel.add(verticalBox);
		
		JPanel namePanel = new JPanel();
		verticalBox.add(namePanel);
		namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
		
		JLabel nameLabel = new JLabel("Nazwa:");
		namePanel.add(nameLabel);
		
		nameInput = new JTextField();
		nameInput.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				controller.nameChanged(nameInput.getText());
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				controller.nameChanged(nameInput.getText());
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				controller.nameChanged(nameInput.getText());
			}
		});
		namePanel.add(nameInput);
		nameInput.setColumns(10);
		
		JPanel urlPanel = new JPanel();
		verticalBox.add(urlPanel);
		urlPanel.setLayout(new BoxLayout(urlPanel, BoxLayout.X_AXIS));
		
		JLabel urlLabel = new JLabel("Url:");
		urlPanel.add(urlLabel);
		
		urlImput = new JTextField();
		urlImput.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent e) {
				controller.urlChanged(urlImput.getText());
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				controller.urlChanged(urlImput.getText());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				controller.urlChanged(urlImput.getText());
			}	
			
		});
		urlPanel.add(urlImput);
		urlImput.setColumns(10);
		
		JButton loadPageButton = new JButton("Wczytaj stron\u0119");
		loadPageButton.setAction(action);
		urlPanel.add(loadPageButton);
		
		JPanel panel = new JPanel();
		verticalBox.add(panel);
		
		documentLoadedLabel = new JLabel("Dokument html nie zosta\u0142 wczytany do pami\u0119ci, zapytania xPath nie b\u0119d\u0105 wykonywane.");
		documentLoadedLabel.setForeground(Color.RED);
		panel.add(documentLoadedLabel);
		
		JPanel xPathPanel = new JPanel();
		verticalBox.add(xPathPanel);
		xPathPanel.setLayout(new BoxLayout(xPathPanel, BoxLayout.X_AXIS));
		
		JLabel xPathLabel = new JLabel("xPath:");
		xPathPanel.add(xPathLabel);
		
		xPathInput = new JTextField();
		xPathInput.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent e) {
				controller.xPathChanged(xPathInput.getText());
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				controller.xPathChanged(xPathInput.getText());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				controller.xPathChanged(xPathInput.getText());
			}	
		});
		xPathPanel.add(xPathInput);
		xPathInput.setColumns(10);
		
		JPanel resultsPanel = new JPanel();
		basicTab.add(resultsPanel);
		resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.X_AXIS));
		
		JScrollPane scrollPane = new JScrollPane();
		resultsPanel.add(scrollPane);
		
		JList list = new JList();
		list.setValueIsAdjusting(true);
		
		list.setModel(controller.getEntriesListModel());
		scrollPane.setViewportView(list);
		
		JPanel paginatonTab = new JPanel();
		tabbedPane.addTab("Paginacja", null, paginatonTab, null);
		paginatonTab.setLayout(new BorderLayout(0, 0));
		
		JPanel paginationConfigutationPanel = new JPanel();
		paginatonTab.add(paginationConfigutationPanel, BorderLayout.NORTH);
		paginationConfigutationPanel.setLayout(new BorderLayout(0, 0));
		
		Box verticalBox_2 = Box.createVerticalBox();
		paginationConfigutationPanel.add(verticalBox_2);
		
		JPanel panel_1 = new JPanel();
		verticalBox_2.add(panel_1);
		
		hasPaginationCheckbox = new JCheckBox("posiada paginacje");
		hasPaginationCheckbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				refreshButtonsActivity();
			}
		});
		panel_1.add(hasPaginationCheckbox);
		
		JPanel paginationXPathPanel = new JPanel();
		verticalBox_2.add(paginationXPathPanel);
		paginationXPathPanel.setLayout(new BorderLayout(0, 0));
		
		JLabel paginationXPathlabel = new JLabel("xPath");
		paginationXPathPanel.add(paginationXPathlabel, BorderLayout.WEST);
		
		paginationXPathInput = new JTextField();
		paginationXPathPanel.add(paginationXPathInput, BorderLayout.CENTER);
		paginationXPathInput.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				controller.paginationXPathChanged(paginationXPathInput.getText());
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				controller.paginationXPathChanged(paginationXPathInput.getText());
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				controller.paginationXPathChanged(paginationXPathInput.getText());
			}
		});
		paginationXPathInput.setColumns(10);
		
		JPanel pagnationRegexPanel = new JPanel();
		verticalBox_2.add(pagnationRegexPanel);
		pagnationRegexPanel.setLayout(new BorderLayout(0, 0));
		
		JLabel paginationRegexLabel = new JLabel("Wyra\u017Cenie reguralne");
		pagnationRegexPanel.add(paginationRegexLabel, BorderLayout.WEST);
		
		paginationRegexInput = new JTextField();
		pagnationRegexPanel.add(paginationRegexInput, BorderLayout.CENTER);
		paginationRegexInput.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				controller.paginationRegexChanged(paginationRegexInput.getText());
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				controller.paginationRegexChanged(paginationRegexInput.getText());
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				controller.paginationRegexChanged(paginationRegexInput.getText());
			}
		});
		paginationRegexInput.setColumns(10);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		paginatonTab.add(scrollPane_2, BorderLayout.CENTER);
		
		pagesTable = new JTable();
		pagesTable.setModel(controller.getPagesTableModel());
		scrollPane_2.setViewportView(pagesTable);
		pagesTable.getColumnModel().getColumn(0).setMaxWidth(50);
		
		JPanel fieldsTab = new JPanel();
		tabbedPane.addTab("Pola", null, fieldsTab, null);
		fieldsTab.setLayout(new BorderLayout(0, 5));
		
		JPanel resultsTabPanel = new JPanel();
		fieldsTab.add(resultsTabPanel, BorderLayout.CENTER);
		resultsTabPanel.setLayout(new BoxLayout(resultsTabPanel, BoxLayout.X_AXIS));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		resultsTabPanel.add(scrollPane_1);
		
		resultsTable = new JTable();
		resultsTable.setModel(controller.getResultsTableModel());
		scrollPane_1.setViewportView(resultsTable);
		
		JPanel fieldsConfigPanel = new JPanel();
		
		fieldsTab.add(fieldsConfigPanel, BorderLayout.NORTH);
		fieldsConfigPanel.setLayout(new BorderLayout(5, 0));
		
		JPanel fieldsListPanel = new JPanel();
		fieldsConfigPanel.add(fieldsListPanel, BorderLayout.WEST);
		fieldsListPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel fieldsButtonsPanel = new JPanel();
		FlowLayout fl_fieldsButtonsPanel = (FlowLayout) fieldsButtonsPanel.getLayout();
		fl_fieldsButtonsPanel.setAlignOnBaseline(true);
		fieldsListPanel.add(fieldsButtonsPanel, BorderLayout.NORTH);
		
		JLabel lblFields = new JLabel("Pola");
		fieldsButtonsPanel.add(lblFields);
		
		addFieldButton = new JButton("Dodaj");
		addFieldButton.setAction(action_1);
		fieldsButtonsPanel.add(addFieldButton);
		
		removeFieldButton = new JButton("Usu\u0144");
		removeFieldButton.setAction(action_2);
		fieldsButtonsPanel.add(removeFieldButton);
		
		fieldsList = new JList();
		fieldsList.setModel(controller.getDataFieldsModel());
		fieldsList.addListSelectionListener(new ListSelectionListener() {
			
			public void valueChanged(ListSelectionEvent arg0) {
				DataField field = controller.dataFieldSelected(fieldsList.getSelectedIndex());
				fieldxPathInput.setText(field.getxPath());
				comboBox.setSelectedItem(field.getCallDefaultValueProcessors());
				refreshButtonsActivity();
			}
		});
		fieldsListPanel.add(fieldsList, BorderLayout.CENTER);
		
		JPanel fieldDetailsPanel = new JPanel();
		fieldsConfigPanel.add(fieldDetailsPanel);
		fieldDetailsPanel.setLayout(new BorderLayout(0, 0));
		
		Box verticalBox_1 = Box.createVerticalBox();
		fieldDetailsPanel.add(verticalBox_1);
		
		JPanel fieldxPathPanel = new JPanel();
		verticalBox_1.add(fieldxPathPanel);
		fieldxPathPanel.setLayout(new BoxLayout(fieldxPathPanel, BoxLayout.X_AXIS));
		
		JLabel lblXpath = new JLabel("xPath");
		fieldxPathPanel.add(lblXpath);
		
		fieldxPathInput = new JTextField();
		fieldxPathInput.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				controller.currentFieldXPathModified(fieldsList.getSelectedIndex(), fieldxPathInput.getText());
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				controller.currentFieldXPathModified(fieldsList.getSelectedIndex(), fieldxPathInput.getText());
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				controller.currentFieldXPathModified(fieldsList.getSelectedIndex(), fieldxPathInput.getText());	
			}
		});
		fieldxPathPanel.add(fieldxPathInput);
		fieldxPathInput.setColumns(10);
		
		JPanel defaultValueProcessorsPropagationPanel = new JPanel();
		verticalBox_1.add(defaultValueProcessorsPropagationPanel);
		defaultValueProcessorsPropagationPanel.setLayout(new BorderLayout(0, 0));
		JLabel lbldefaultValueProcessorsPropagation = new JLabel("Zastosuj domy\u015Blne walu processory");
		defaultValueProcessorsPropagationPanel.add(lbldefaultValueProcessorsPropagation, BorderLayout.WEST);
		
	    comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				controller.defaultValueProcessorsPropagationChanged(fieldsList.getSelectedIndex(), (DefaultValueProcessorsPropagation)(comboBox.getSelectedItem()));
			}
		});
		comboBox.setModel(new DefaultComboBoxModel(DefaultValueProcessorsPropagation.values()));
		
		defaultValueProcessorsPropagationPanel.add(comboBox, BorderLayout.CENTER);
		
		JPanel processorsLabelPanel = new JPanel();
		FlowLayout fl_processorsLabelPanel = (FlowLayout) processorsLabelPanel.getLayout();
		fl_processorsLabelPanel.setAlignment(FlowLayout.LEFT);
		fl_processorsLabelPanel.setAlignOnBaseline(true);
		verticalBox_1.add(processorsLabelPanel);
		
		JLabel lblValueprocessory = new JLabel("Value processory:");
		lblValueprocessory.setHorizontalAlignment(SwingConstants.LEFT);
		processorsLabelPanel.add(lblValueprocessory);
		
		JPanel processorsTablePanel = new JPanel();
		processorsTablePanel.setMinimumSize(new Dimension(10, 100));
		verticalBox_1.add(processorsTablePanel);
		processorsTablePanel.setLayout(new BorderLayout(0, 0));
		
		Component verticalStrut = Box.createVerticalStrut(100);
		processorsTablePanel.add(verticalStrut, BorderLayout.WEST);
		
		valueProcessorsTable = new JTable();
		valueProcessorsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				if(hasPaginationCheckbox.isSelected()){
					Pagination pagination = controller.createPagination();
					paginationXPathInput.setText(pagination.getxPath());
					paginationRegexInput.setText(pagination.getRegex());
				}
				else{
					controller.removePagination();
					paginationXPathInput.setText("");
					paginationRegexInput.setText("");
				}
				refreshButtonsActivity();	
			}
		});
		valueProcessorsTable.setModel(controller.getValueProcessorsTabModel());
		processorsTablePanel.add(valueProcessorsTable, BorderLayout.CENTER);
		valueProcessorsTable.getColumnModel().getColumn(0).setMaxWidth(50);
		
		JPanel processorsButtonsPanel = new JPanel();
		verticalBox_1.add(processorsButtonsPanel);
		FlowLayout fl_processorsButtonsPanel = new FlowLayout(FlowLayout.CENTER, 5, 5);
		fl_processorsButtonsPanel.setAlignOnBaseline(true);
		processorsButtonsPanel.setLayout(fl_processorsButtonsPanel);
		
		addValueProcessorButton = new JButton("Dodaj");
		addValueProcessorButton.setAction(action_3);
		processorsButtonsPanel.add(addValueProcessorButton);
		
		editValueProcessorButton = new JButton("Edytuj");
		editValueProcessorButton.setAction(action_4);
		processorsButtonsPanel.add(editValueProcessorButton);
		
		removeValueProcessorButton = new JButton("Usu\u0144");
		removeValueProcessorButton.setAction(action_5);
		processorsButtonsPanel.add(removeValueProcessorButton);
		
		moveUpProcessorButton = new JButton("Do g\u00F3ry");
		moveUpProcessorButton.setAction(action_6);
		processorsButtonsPanel.add(moveUpProcessorButton);
		
		moveDownProcessorButton = new JButton("Na d\u00F3\u0142");
		moveDownProcessorButton.setAction(action_7);
		processorsButtonsPanel.add(moveDownProcessorButton);
		
		moveValueProcessorToDefaults = new JButton("Przenie\u015B do domy\u015Blnych");
		moveValueProcessorToDefaults.setAction(action_8);
		processorsButtonsPanel.add(moveValueProcessorToDefaults);
		
		loadInitData();
		refreshButtonsActivity();
	}
	
	private void refreshButtonsActivity(){
		removeFieldButton.setEnabled(fieldsList.getSelectedIndex() > -1);
		addValueProcessorButton.setEnabled(fieldsList.getSelectedIndex() > -1);
		editValueProcessorButton.setEnabled(valueProcessorsTable.getSelectedRow() > -1);
		removeValueProcessorButton.setEnabled(valueProcessorsTable.getSelectedRow() > -1);
		moveDownProcessorButton.setEnabled(valueProcessorsTable.getSelectedRow() > -1);
		moveUpProcessorButton.setEnabled(valueProcessorsTable.getSelectedRow() > -1);
		moveValueProcessorToDefaults.setEnabled(valueProcessorsTable.getSelectedRow() > -1);
		
		paginationRegexInput.setEnabled(hasPaginationCheckbox.isSelected());
		paginationXPathInput.setEnabled(hasPaginationCheckbox.isSelected());
	}

	private void loadInitData() {
		Category category = controller.getCategory();
		nameInput.setText(category.getName());
		urlImput.setText(category.getUrl());
		xPathInput.setText(category.getDataEntries().get(0).getxPath());
		
		Pagination pagination = category.getPagination();
		hasPaginationCheckbox.setSelected(pagination != null);
		if(pagination != null){
			paginationXPathInput.setText(pagination.getxPath());
			paginationRegexInput.setText(pagination.getRegex());
		}
	}

	private class LoadHtmlDocumentAction extends AbstractAction {
		
		private static final long serialVersionUID = -5594119552715982900L;
		
		public LoadHtmlDocumentAction() {
			putValue(NAME, "Wczytaj");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			final ProgressDialog dialog = ProgressDialog.show("Wczytywanie strony www, prosze czekaj..");
			SwingWorker<Boolean, Void > worker = new SwingWorker<Boolean, Void>(){
				@Override
				protected Boolean doInBackground() throws Exception {
					try {
						controller.loadUrl();
						documentLoadedLabel.setText("Dokument html znajduje si\u0119 w pami\u0119ci, zapytania xPath  b\u0119d\u0105 wykonywane.");
						documentLoadedLabel.setForeground(new Color(0, 128, 0));
					} catch (HtmlLoaderException e1) {
						documentLoadedLabel.setText("Dokument html nie zosta\u0142 wczytany do pami\u0119ci, zapytania xPath nie b\u0119d\u0105 wykonywane.");
						documentLoadedLabel.setForeground(Color.RED);
						return false;
					}
					return true;
				}
				
				@Override
				protected void done() {
					try {
						if(!get()){
							JOptionPane.showMessageDialog(CategoryWindow.this, 
									"Nie uda∏o si´ za∏adowaç strony www, sprawdê czy adres www jest poprawny.");
						}
					} catch (InterruptedException e) {
					} catch (ExecutionException e) {
					}
					dialog.dispose();
					super.done();
				}
				
			};
			worker.execute();
			
			
		}
	}
	private class AddFieldAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 7562762986442127686L;
		
		public AddFieldAction() {
			putValue(NAME, "Dodaj");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			String name = JOptionPane.showInputDialog("Podaj nazw´ pola:");
			controller.createNewField(name);
		}
	}
	private class RemoveFieldAction extends AbstractAction {
		public RemoveFieldAction() {
			putValue(NAME, "Usuƒ");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			int index = fieldsList.getSelectedIndex();
			fieldsList.getSelectionModel().clearSelection();
			controller.removeField(index);
			refreshButtonsActivity();
		}
	}
	private class AddValueProcessorAction extends AbstractAction {
		public AddValueProcessorAction() {
			putValue(NAME, "Dodaj");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			ValueProcessorDetailsDialog dialog = new ValueProcessorDetailsDialog(null);
			dialog.setVisible(true);
			ValueProcessorConfig createdValueProcessor = dialog.getValue();
			if(createdValueProcessor != null){
				controller.addValueProcessor(createdValueProcessor);
			}
		}
	}
	private class EditValueProcessorAction extends AbstractAction {
		public EditValueProcessorAction() {
			putValue(NAME, "Edytuj");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			ValueProcessorConfig valueProcessor = controller.getValueProcessorToEdit(valueProcessorsTable.getSelectedRow());
			ValueProcessorDetailsDialog dialog = new ValueProcessorDetailsDialog(valueProcessor);
			dialog.setVisible(true);
			ValueProcessorConfig editedValueProcessor = dialog.getValue();
			if(editedValueProcessor != null){
				controller.applyValueProcessorEdit(valueProcessor, editedValueProcessor);
			}
		}
	}
	private class RemoveValueProcessorAction extends AbstractAction {
		public RemoveValueProcessorAction() {
			putValue(NAME, "Usuƒ");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			int selected = valueProcessorsTable.getSelectedRow();
			valueProcessorsTable.getSelectionModel().clearSelection();
			controller.deleteValueProcessor(selected);	
		}
	}
	private class MoveUpValueProcessorAction extends AbstractAction {
		public MoveUpValueProcessorAction() {
			putValue(NAME, "Do góry");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			controller.moveUpValueProcessor(valueProcessorsTable.getSelectedRow());
		}
	}
	private class MoveDownAction extends AbstractAction {
		public MoveDownAction() {
			putValue(NAME, "Na dó∏");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			controller.moveDownValueProcessor(valueProcessorsTable.getSelectedRow());
		}
	}
	private class MoveValueProcessorToDefaultsAction extends AbstractAction {
		public MoveValueProcessorToDefaultsAction() {
			putValue(NAME, "PrzenieÊ do domyÊlnych");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			int selectedField = fieldsList.getSelectedIndex();
			int selectedProcessor = valueProcessorsTable.getSelectedRow();
			controller.moveValueProcessorToDefault(selectedField, selectedProcessor);
		}
	}
}
