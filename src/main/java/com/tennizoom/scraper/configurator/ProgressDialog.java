package com.tennizoom.scraper.configurator;

import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JProgressBar;

public class ProgressDialog extends JDialog {

	private static final long serialVersionUID = -1027166686689031277L;

	private final JPanel contentPanel = new JPanel();
	

	public static ProgressDialog show(String message){
		ProgressDialog dialog = new ProgressDialog(message);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.setVisible(true);
		return dialog;
	}
	

	/**
	 * Create the dialog.
	 */
	private ProgressDialog(String message) {
		
		setBounds(200, 200, 450, 99);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JLabel lblMessage = new JLabel(message);
			lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblMessage, BorderLayout.NORTH);
		}
		{
			JProgressBar progressBar = new JProgressBar();
			progressBar.setIndeterminate(true);
			contentPanel.add(progressBar, BorderLayout.CENTER);
		}
		
	}

}
