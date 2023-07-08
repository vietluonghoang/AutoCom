package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.openqa.selenium.WebDriver;

import controllers.DriverCenter;
import entities.GeneralSettings;

public class MainGUI {

	private JFrame frame;
	private JButton btnStart;
	private String profilePath = "";
	private WebDriver driver;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI window = new MainGUI();
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
	public MainGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// init top panel
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.NORTH);

		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				startAction();
			}
		});
		panel.add(btnStart);

		// init main panel

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new FlowLayout());
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		JPanel profilePathPanel = new JPanel();
		profilePathPanel.setLayout(new FlowLayout());
		mainPanel.add(profilePathPanel);
		JLabel lbProfilePath = new JLabel("Profile Path");
		JTextField txtProfilePath = new JTextField();
		txtProfilePath.setPreferredSize(new Dimension(300, 20));
		txtProfilePath.getDocument().addDocumentListener((DocumentListener) new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				updateProfilePath();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				updateProfilePath();
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				updateProfilePath();
			}

			public void updateProfilePath() {
				profilePath = txtProfilePath.getText();
			}

		});
		profilePathPanel.add(lbProfilePath);
		profilePathPanel.add(txtProfilePath);
		JPanel commentContentPanel = new JPanel();
		commentContentPanel.setLayout(new FlowLayout());
		mainPanel.add(commentContentPanel);
		JLabel lbCommentContent = new JLabel("Comment");
		JTextField txtCommentContent = new JTextField();
		txtCommentContent.setPreferredSize(new Dimension(300, 20));
		commentContentPanel.add(lbCommentContent);
		commentContentPanel.add(txtCommentContent);
	}

	private void startAction() {
		if (!GeneralSettings.isStartedRunning) {
			btnStart.setEnabled(false);
			GeneralSettings.isStartedRunning = true;
			try {
				if (profilePath.length() > 0) {
					driver = DriverCenter.getNewFirefoxDriver(profilePath);
				} else {
					driver = DriverCenter.getNewFirefoxDriver();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			btnStart.setText("Stop");
			btnStart.setEnabled(true);
		} else {
			DriverCenter.terminateAllDrivers();
			GeneralSettings.isStartedRunning = false;
			btnStart.setText("Start");
		}

	}

}
