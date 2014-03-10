package com.tennizoom.scraper.configurator;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Component;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import com.tennizoom.scraper.config.ValueProcessorConfig;
import com.tennizoom.scraper.configurator.controller.MainWindowController;
import com.tennizoom.scraper.domain.Category;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JList;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import javax.swing.UIManager.*;

public class MainWindow {

	private JFrame frame;
	private JPanel buttonsPanel;
	private JButton addButton;
	private JButton copyButton;
	private JButton openButton;
	private JTable table;
	private JTabbedPane tabbedPane;
	private JPanel mainTab;
	private JPanel valueProcessorsTab;

	private MainWindowController controller = new MainWindowController();
	private JPanel defaultValueProcessorsFieldDetails;
	private JPanel defaultValueProcessorsButtonsPanel;
	private JButton addValueProcessorButton;
	private JButton editValueProcessorButton;
	private JButton moveUpValueProcessorButton;
	private JButton moveDownValueProcessorButton;
	private JTable defultValueProcessorsTable;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmOpen;
	private JMenuItem mntmSave;
	private JMenuItem mntmSaveAs;
	private JMenuItem mntmZamknij;
	private JMenuItem mntmNew;
	private JSeparator separator;
	private final Action newConfigAction = new NewConfigurationAction();
	private final Action openConfigAction = new OpenShopConfigFileAction();
	private final Action closeAction = new CloseAction();
	private final Action addValueProcessorAction = new AddValueProcessorAction();
	private final Action editValueProcessorAction = new EditValueProcessorAction();
	private final Action moveUpValueProcessorAction = new MoveUpValueProcessorAction();
	private final Action moveDownValueProcessorAction = new MoveDownValueProcessorAction();
	private final Action deleteValueProcessorAction = new DeleteDefaultValueProcessorAction();
	private final Action addFieldAction = new AddFieldAction();
	private final Action addCategoryAction = new AddCategoryAction();
	private final Action openCategoryAction = new OpenCategoryAction();
	private final Action copyCategoryAction = new CopyCategoryAction();
	private final Action action = new SaveAsAction();
	private final Action action_1 = new SaveAction();
	private JPanel panel;
	private JPanel panel_1;
	private JLabel label;
	private JButton addFieldButton;
	private JList list;
	private JButton removeFieldButton;
	private JButton removeValueProcessorButton;
	private final Action action_2 = new RemoveFieldAction();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
									

					try {
						for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
							if ("Nimbus".equals(info.getName())) {
								UIManager.setLookAndFeel(info.getClassName());
								break;
							}
						}
					} catch (Exception e) {
						// If Nimbus is not available, you can set the GUI to another look and feel.
					}
				
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 757, 455);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		mainTab = new JPanel();
		tabbedPane.addTab("Kategorie", null, mainTab, null);
		mainTab.setLayout(new BorderLayout(0, 0));
		
		table = new JTable();
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				refreshButtonsActivity();
			}
		});
		table.setEnabled(false);
		table.setBackground(new Color(240,240,240));
		table.setModel(controller.getCategoriesTabModel());
		table.getColumnModel().getColumn(0).setMaxWidth(50);
		
		buttonsPanel = new JPanel();
		mainTab.add(buttonsPanel, BorderLayout.SOUTH);
		FlowLayout fl_buttonsPanel = (FlowLayout) buttonsPanel.getLayout();
		fl_buttonsPanel.setAlignment(FlowLayout.RIGHT);
		
		openButton = new JButton("Otw\u00F3rz");
		openButton.setEnabled(false);
		openButton.setAction(openCategoryAction);
		buttonsPanel.add(openButton);
		
		copyButton = new JButton("Kopiuj");
		copyButton.setEnabled(false);
		copyButton.setAction(copyCategoryAction);
		buttonsPanel.add(copyButton);
		
		addButton = new JButton("Dodaj");
		addButton.setEnabled(false);
		addButton.setAction(addCategoryAction);
		buttonsPanel.add(addButton);
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(table);
		mainTab.add(scrollPane, BorderLayout.CENTER);
		
		valueProcessorsTab = new JPanel();
		tabbedPane.addTab("Domy\u015blne value processory", null, valueProcessorsTab, null);
		valueProcessorsTab.setLayout(new BorderLayout(0, 0));
		
		defaultValueProcessorsFieldDetails = new JPanel();
		valueProcessorsTab.add(defaultValueProcessorsFieldDetails, BorderLayout.CENTER);
		defaultValueProcessorsFieldDetails.setLayout(new BorderLayout(0, 0));
		
		defaultValueProcessorsButtonsPanel = new JPanel();
		defaultValueProcessorsFieldDetails.add(defaultValueProcessorsButtonsPanel, BorderLayout.SOUTH);
		defaultValueProcessorsButtonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		addValueProcessorButton = new JButton("Dodaj");
		addValueProcessorButton.setAction(addValueProcessorAction);
		defaultValueProcessorsButtonsPanel.add(addValueProcessorButton);
		
		removeValueProcessorButton = new JButton("Usu\u0144");
		removeValueProcessorButton.setEnabled(false);
		removeValueProcessorButton.setAction(deleteValueProcessorAction);
		defaultValueProcessorsButtonsPanel.add(removeValueProcessorButton);
		
		editValueProcessorButton = new JButton("Edytuj");
		editValueProcessorButton.setAction(editValueProcessorAction);
		defaultValueProcessorsButtonsPanel.add(editValueProcessorButton);
		
		moveUpValueProcessorButton = new JButton("Do g\u00F3ry");
		moveUpValueProcessorButton.setAction(moveUpValueProcessorAction);
		defaultValueProcessorsButtonsPanel.add(moveUpValueProcessorButton);
		
		moveDownValueProcessorButton = new JButton("Na d\u00F3\u0142");
		moveDownValueProcessorButton.setEnabled(false);
		moveDownValueProcessorButton.setAction(moveDownValueProcessorAction);
		defaultValueProcessorsButtonsPanel.add(moveDownValueProcessorButton);
		
		defultValueProcessorsTable = new JTable();
		defultValueProcessorsTable.setModel(controller.getDefaultValueProcessorsTabModel());
		defultValueProcessorsTable.setFillsViewportHeight(true);
		defultValueProcessorsTable.setBackground(new Color(240,240,240));
		defultValueProcessorsTable.getColumnModel().getColumn(0).setMaxWidth(50);
		defultValueProcessorsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				refreshButtonsActivity();
			}
		});
		defaultValueProcessorsFieldDetails.add(new JScrollPane(defultValueProcessorsTable), BorderLayout.CENTER);
		
		panel = new JPanel();
		valueProcessorsTab.add(panel, BorderLayout.WEST);
		panel.setLayout(new BorderLayout(0, 0));
		
		panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.NORTH);
		
		label = new JLabel("Pola");
		panel_1.add(label);
		
		addFieldButton = new JButton("Dodaj");
		addFieldButton.setAction(addFieldAction);
		panel_1.add(addFieldButton);
		
		removeFieldButton = new JButton("Usu\u0144");
		removeFieldButton.setAction(action_2);
		panel_1.add(removeFieldButton);
		
		list = new JList();
		list.setModel(controller.getDefaultValueProcessorFieldsModel());
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				controller.defaultValueProcessorFieldSelected(list.getSelectedIndex());
				refreshButtonsActivity();
				defultValueProcessorsTable.getSelectionModel().clearSelection();
			}
		});
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panel.add(list, BorderLayout.CENTER);
		
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		mnFile = new JMenu("Plik");
		menuBar.add(mnFile);
		
		mntmNew = new JMenuItem("Nowy");
		mntmNew.setAction(newConfigAction);
		mnFile.add(mntmNew);
		
		mntmOpen = new JMenuItem("Otw\u00F3rz");
		mntmOpen.setAction(openConfigAction);
		mnFile.add(mntmOpen);
		
		mntmSave = new JMenuItem("Zapisz");
		mntmSave.setAction(action_1);
		mntmSave.setEnabled(false);
		mnFile.add(mntmSave);
		
		mntmSaveAs = new JMenuItem("Zapisz jako...");
		mntmSaveAs.setAction(action);
		mntmSaveAs.setEnabled(false);
		mnFile.add(mntmSaveAs);
		
		separator = new JSeparator();
		mnFile.add(separator);
		
		mntmZamknij = new JMenuItem("Zamknij");
		mntmZamknij.setAction(closeAction);
		mnFile.add(mntmZamknij);
		
		refreshButtonsActivity();
	}
	
	private void refreshButtonsActivity(){
		mntmSave.setEnabled(controller.isFileOpened());
		mntmSaveAs.setEnabled(controller.isConfigurationOpened());
		table.setEnabled(controller.isConfigurationOpened());
		openButton.setEnabled(table.getSelectedRow() > -1);
		copyButton.setEnabled(table.getSelectedRow() > -1);
		addButton.setEnabled(controller.isConfigurationOpened());
		
		addFieldButton.setEnabled(controller.isConfigurationOpened());
		removeFieldButton.setEnabled(list.getSelectedIndex() > -1);
		
		addValueProcessorButton.setEnabled(list.getSelectedIndex() > -1);
		removeValueProcessorButton.setEnabled(defultValueProcessorsTable.getSelectedRow() > -1);
		editValueProcessorButton.setEnabled(defultValueProcessorsTable.getSelectedRow() > -1);
		moveUpValueProcessorButton.setEnabled(defultValueProcessorsTable.getSelectedRow() > -1);
		moveDownValueProcessorButton.setEnabled(defultValueProcessorsTable.getSelectedRow() > -1);
		
	}

	private class OpenShopConfigFileAction extends AbstractAction {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -2610770388153973698L;
		
		private JFileChooser fileChooser = new JFileChooser();
		
		public OpenShopConfigFileAction() {
			putValue(NAME, "Otw\u00F3rz");
			putValue(SHORT_DESCRIPTION, "Wybierz plik konfiguracyjny sklepu aby rozpocz\u0105\u0107 jego edycj\u0119.");
			
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Xml file","xml");
			fileChooser.setFileFilter(filter);
		}
		
		public void actionPerformed(ActionEvent e) {
			int result = fileChooser.showOpenDialog((Component) e.getSource());
			if(result == JFileChooser.APPROVE_OPTION){
				File selectedFile = fileChooser.getSelectedFile();
				try{
					controller.openShopConfiguration(selectedFile);
					frame.setTitle(selectedFile.getName());
				}
				catch(IllegalStateException ex){
					JOptionPane.showMessageDialog(frame, "Otwieranie nie powi\u00F3d\u0142o si\u0119 z powodu: "+ ex.getMessage(), "B\u0142\u0105d", JOptionPane.ERROR_MESSAGE);
				}
				
			}

			refreshButtonsActivity();
		}
	}
	
	private class AddCategoryAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = -9139981987597190576L;
		public AddCategoryAction() {
			putValue(NAME, "Dodaj");
			putValue(SHORT_DESCRIPTION, "Dodaj now\u0105 kategori\u0119 do konfiguracji sklepu.");
		}
		public void actionPerformed(ActionEvent e) {
			Category category = controller.createNewCategory();
			CategoryWindow window = new CategoryWindow(category);
			window.setVisible(true);
		}
	}
	private class AddValueProcessorAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 4638218233668919198L;
		public AddValueProcessorAction() {
			putValue(NAME, "Dodaj");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			
			ValueProcessorDetailsDialog dialog = new ValueProcessorDetailsDialog(null);
			dialog.setVisible(true);
			ValueProcessorConfig createdValueProcessor = dialog.getValue();
			if(createdValueProcessor != null){
				controller.addDefaultValueProcessor(createdValueProcessor);
			}
		}
	}
	private class EditValueProcessorAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 8968079775260457001L;
		public EditValueProcessorAction() {
			putValue(NAME, "Edytuj");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			ValueProcessorConfig valueProcessor = controller.getValueProcessorToEdit(defultValueProcessorsTable.getSelectedRow());
			ValueProcessorDetailsDialog dialog = new ValueProcessorDetailsDialog(valueProcessor);
			dialog.setVisible(true);
			ValueProcessorConfig editedValueProcessor = dialog.getValue();
			if(editedValueProcessor != null){
				controller.applyValueProcessorEdit(valueProcessor, editedValueProcessor);
			}
		}
	}
	private class MoveUpValueProcessorAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3855498065978689113L;
		public MoveUpValueProcessorAction() {
			putValue(NAME, "Do g\u00F3ry");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			controller.moveUpDefaultValueProcessor(defultValueProcessorsTable.getSelectedRow());
		}
	}
	private class MoveDownValueProcessorAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = -7831085163437968827L;
		public MoveDownValueProcessorAction() {
			putValue(NAME, "Na d\u00F3\u0142");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			controller.moveDownDefaultValueProcessor(defultValueProcessorsTable.getSelectedRow());
		}
	}
	private class DeleteDefaultValueProcessorAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = -3733255515151973669L;
		public DeleteDefaultValueProcessorAction() {
			putValue(NAME, "Usu\u0144");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			int selected = defultValueProcessorsTable.getSelectedRow();
			defultValueProcessorsTable.getSelectionModel().clearSelection();
			controller.deleteDefaultValueProcessor(selected);
		}
	}
	private class AddFieldAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = -99298535245145669L;
		public AddFieldAction() {
			putValue(NAME, "Dodaj");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			String name = JOptionPane.showInputDialog("Podaj nazw\u0119 pola:");//dialog.getValue();
			controller.createNewDefaultValueProcessorsField(name);
		}
	}
	private class OpenCategoryAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 2933482274406024392L;
		public OpenCategoryAction() {
			putValue(NAME, "Otw\u00F3rz");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			Category category = controller.getCategoryToOpen(table.getSelectedRow());
			CategoryWindow window = new CategoryWindow(category);
			window.setVisible(true);
		}
	}
	private class CopyCategoryAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 9192843165620351861L;
		public CopyCategoryAction() {
			putValue(NAME, "Kopiuj");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			Category category = controller.copyCategory(table.getSelectedRow());
			CategoryWindow window = new CategoryWindow(category);
			window.setVisible(true);
		}
	}
	private class NewConfigurationAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = -609478284148798663L;
		public NewConfigurationAction() {
			putValue(NAME, "Nowy");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			controller.newShopConfiguration();
			frame.setTitle("Nowa, nie zapisana konfiguracja ...");
			refreshButtonsActivity();
			list.getSelectionModel().clearSelection();
		}
	}
	private class CloseAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 7329307285215582450L;
		public CloseAction() {
			putValue(NAME, "Zamknij");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			// TODO: Pytanie o zapis
			frame.dispose();
		}
	}
	private class SaveAsAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = -4680685061139305917L;
		private JFileChooser fileChooser = new JFileChooser();
		
		public SaveAsAction() {
			putValue(NAME, "Zapisz jako...");
			putValue(SHORT_DESCRIPTION, "Some short description");

			FileNameExtensionFilter filter = new FileNameExtensionFilter("Xml file","xml");
			fileChooser.setFileFilter(filter);
		}
		public void actionPerformed(ActionEvent e) {
			fileChooser.setSelectedFile(controller.getOpenedFile());
			int result = fileChooser.showSaveDialog(frame);
			if(result == JFileChooser.APPROVE_OPTION){
				File fileToSave = fileChooser.getSelectedFile();
				try{
					controller.saveToFile(fileToSave);
					frame.setTitle(fileToSave.getName());	
					JOptionPane.showMessageDialog(frame, "Zapis zako\u0144czony powodzeniem.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
				}
				catch(IllegalStateException ex){
					JOptionPane.showMessageDialog(frame, "Zapis si\u0119 nie powi\u00F3d\u0142 z powodu: "+ ex.getMessage(), "B\u0142\u0105d", JOptionPane.ERROR_MESSAGE);
				}
			}
			refreshButtonsActivity();
		}
	}
	
	private class SaveAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = -294973691143153254L;
		public SaveAction() {
			putValue(NAME, "Zapisz");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			try{
				controller.save();
				JOptionPane.showMessageDialog(frame, "Zapis zako\u0144czony powodzeniem.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
			}
			catch(IllegalStateException ex){
				JOptionPane.showMessageDialog(frame, "Zapis si\u0119 nie powi\u00F3d\u0142 z powodu: "+ ex.getMessage(), "B\u0142\u0105d", JOptionPane.ERROR_MESSAGE);
			}
			
			refreshButtonsActivity();
		}
	}
	private class RemoveFieldAction extends AbstractAction {
		public RemoveFieldAction() {
			putValue(NAME, "Usu\u0144");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			int index = list.getSelectedIndex();
			list.getSelectionModel().clearSelection();
			controller.removeField(index);
			refreshButtonsActivity();
		}
	}
}
