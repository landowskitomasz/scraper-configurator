package com.tennizoom.scraper.configurator;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.Box;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import com.tennizoom.scraper.config.ValueProcessorConfig;
import com.tennizoom.scraper.config.ValueProcessorOption;
import com.tennizoom.scraper.config.ValueProcessorType;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import java.awt.Component;

public class ValueProcessorDetailsDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	
	private Map<String, JTextField> optionInputs = new HashMap<String, JTextField>();
	private Box optionsBox;
	private JComboBox comboBox;
	private final Action action = new OkAction();
	private final Action action_1 = new CancelAction();
	
	private boolean canceled = false;

	private ValueProcessorConfig config;
	
	/**
	 * Create the dialog.
	 * @param model 
	 */
	public ValueProcessorDetailsDialog(ValueProcessorConfig valueProcessor) {
		setModal(true);
		setBounds(100, 100, 430, 183);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			Box verticalBox = Box.createVerticalBox();
			contentPanel.add(verticalBox);
			{
				JPanel panel = new JPanel();
				panel.setBorder(null);
				verticalBox.add(panel);
				panel.setLayout(new GridLayout(0, 2, 0, 0));
				{
					JLabel valueLabel = new JLabel("Typ");
					valueLabel.setHorizontalAlignment(SwingConstants.TRAILING);
					panel.add(valueLabel);
				}
				{
					comboBox = new JComboBox();
					comboBox.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							createOptionsForValueProcessor((ValueProcessorType)(comboBox.getSelectedItem()));
						}
					});
					comboBox.setModel(new DefaultComboBoxModel(ValueProcessorType.values()));
					panel.add(comboBox);
				}
			}{
				optionsBox = Box.createVerticalBox(); 
				optionsBox.setBorder(null);
				verticalBox.add(optionsBox);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setAction(action);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setAction(action_1);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		setInitValue(valueProcessor);
	}

	private void setInitValue(ValueProcessorConfig valueProcessor) {
		if(valueProcessor != null){
			comboBox.setSelectedItem(valueProcessor.getProcessorType());
			for(ValueProcessorOption option : valueProcessor.getOptions()){
				JTextField field = optionInputs.get(option.getName());
				if(field != null){
					field.setText(option.getValue());
				}	
			}
		}
		else{
			comboBox.setSelectedItem(ValueProcessorType.regexClean);
		}
	}
	
	public ValueProcessorConfig getValue(){
		return config;
	}

	protected void createOptionsForValueProcessor(
			ValueProcessorType selectedItem) {
		optionsBox.removeAll();
		optionInputs.clear();
		
		switch (selectedItem) {
		case append:
		{
			JPanel panel = new JPanel();
			panel.setBorder(null);
			optionsBox.add(panel);
			panel.setLayout(new GridLayout(0, 2, 0, 0));
			{
				JLabel lblNazwa = new JLabel("WartoÊç");
				lblNazwa.setHorizontalAlignment(SwingConstants.TRAILING);
				panel.add(lblNazwa);
			}
			{
				final JTextField valueField = new JTextField();
				optionInputs.put("value", valueField);
				panel.add(valueField);
			}
		}
			break;
		case regexClean:
		case validate:
		{
			JPanel panel = new JPanel();
			panel.setBorder(null);
			optionsBox.add(panel);
			panel.setLayout(new GridLayout(0, 2, 0, 0));
			{
				JLabel valueLabel = new JLabel("Wyra˝enie regularne");
				valueLabel.setHorizontalAlignment(SwingConstants.TRAILING);
				panel.add(valueLabel);
			}
			{
				final JTextField valueField = new JTextField();
				optionInputs.put("regex", valueField);
				panel.add(valueField);
			}
		}
		break;
		case replace:
		{
			JPanel panel = new JPanel();
			panel.setBorder(null);
			optionsBox.add(panel);
			panel.setLayout(new GridLayout(0, 2, 0, 0));
			{
				JLabel valueLabel = new JLabel("Wzór");
				valueLabel.setHorizontalAlignment(SwingConstants.TRAILING);
				panel.add(valueLabel);
			}
			{
				final JTextField valueField = new JTextField();
				optionInputs.put("pattern", valueField);
				panel.add(valueField);
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(null);
			optionsBox.add(panel);
			panel.setLayout(new GridLayout(0, 2, 0, 0));
			{
				JLabel valueLabel = new JLabel("Zamiennik");
				valueLabel.setHorizontalAlignment(SwingConstants.TRAILING);
				panel.add(valueLabel);
			}
			{
				final JTextField valueField = new JTextField();
				optionInputs.put("replacement", valueField);
				panel.add(valueField);
			}
		}
			break;
		case htmlUnescape:
		default:
			break;
		}
		contentPanel.updateUI();
	}
	
	

	private class OkAction extends AbstractAction {
		public OkAction() {
			putValue(NAME, "Ok");
			putValue(SHORT_DESCRIPTION, "Close dialog with result.");
		}
		public void actionPerformed(ActionEvent e) {

			config = new ValueProcessorConfig();
			config.setProcessorType((ValueProcessorType)comboBox.getSelectedItem());
			for(String optionName : optionInputs.keySet()){
				ValueProcessorOption option = new ValueProcessorOption();
				option.setName(optionName);
				option.setValue(optionInputs.get(optionName).getText());
				config.getOptions().add(option);
			}
			System.out.println("Config created " + config.getProcessorType());
			
			ValueProcessorDetailsDialog.this.dispose();
		}
	}
	private class CancelAction extends AbstractAction {
		public CancelAction() {
			putValue(NAME, "Cancel");
			putValue(SHORT_DESCRIPTION, "Close dialog without result.");
		}
		public void actionPerformed(ActionEvent e) {
			canceled = true;
			ValueProcessorDetailsDialog.this.dispose();
		}
	}
}
