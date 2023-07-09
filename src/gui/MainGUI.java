package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultCaret;

import org.openqa.selenium.WebDriver;

import controllers.DriverCenter;
import controllers.FlowController;
import controllers.MessageCenter;
import entities.GeneralSettings;

public class MainGUI {

	private JFrame frame;
	private JButton btnStart;
	private JButton btnStop;
	private JLabel lbErrors;
	private String profilePath = "";
	private String victimProfileUrl = "";
	private String comments = "";
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
		frame.setBounds(100, 100, 800, 500);
//		frame.pack();
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
		btnStop = new JButton("Stop All Running Threads");
		btnStop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				stopAction();
			}
		});
		panel.add(btnStart);
		panel.add(btnStop);

		// init main panel

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		JPanel profilePathPanel = new JPanel();
		profilePathPanel.setLayout(new FlowLayout());
		mainPanel.add(profilePathPanel);
		JLabel lbProfilePath = new JLabel("Browser Profile Path: ");
		JTextField txtProfilePath = new JTextField();
		txtProfilePath.setPreferredSize(new Dimension(500, 20));
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
				profilePath = txtProfilePath.getText().trim();
			}

		});
		profilePathPanel.add(lbProfilePath);
		profilePathPanel.add(txtProfilePath);
		txtProfilePath.setText("/Users/vietlh/Library/Application Support/Firefox/Profiles/pa0a6807.user1");
		JPanel victimProfileUrlPanel = new JPanel();
		victimProfileUrlPanel.setLayout(new FlowLayout());
		mainPanel.add(victimProfileUrlPanel);
		JLabel lbVictimProfileUrl = new JLabel("Victim Profile Page: ");
		JTextField txtVictimProfileUrl = new JTextField();
		txtVictimProfileUrl.getDocument().addDocumentListener((DocumentListener) new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				updateVictimProfileUrl();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				updateVictimProfileUrl();
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				updateVictimProfileUrl();
			}

			public void updateVictimProfileUrl() {
				victimProfileUrl = txtVictimProfileUrl.getText().trim();
			}

		});
		txtVictimProfileUrl.setPreferredSize(new Dimension(500, 20));
		txtVictimProfileUrl.setText("https://www.facebook.com/tieu.caycay/");
		victimProfileUrlPanel.add(lbVictimProfileUrl);
		victimProfileUrlPanel.add(txtVictimProfileUrl);
		JPanel commentIntervalPanel = new JPanel();
		commentIntervalPanel.setLayout(new FlowLayout());
		mainPanel.add(commentIntervalPanel);
		JLabel lbCommentInterval = new JLabel("Comment Interval: ");
		JTextField txtCommentInterval = new JTextField();
		txtCommentInterval.getDocument().addDocumentListener((DocumentListener) new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				updateCommentInterval();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				updateCommentInterval();
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				updateCommentInterval();
			}

			public void updateCommentInterval() {
				try {
					GeneralSettings.commentInterval = Integer.parseInt(txtVictimProfileUrl.getText().trim());
				} catch (Exception e) {

				}
			}

		});
		txtCommentInterval.setPreferredSize(new Dimension(50, 20));
		txtCommentInterval.setText("" + GeneralSettings.commentInterval);
		commentIntervalPanel.add(lbCommentInterval);
		commentIntervalPanel.add(txtCommentInterval);
		JPanel commentsPanel = new JPanel();
		commentsPanel.setLayout(new FlowLayout());
		mainPanel.add(commentsPanel);
		JLabel lbComments = new JLabel("Possible Comments: \n(use '|' to separate)");
		JTextArea txtComments = new JTextArea(10, 60);
		txtComments.setLineWrap(true);
		txtComments.setEditable(true); // set textArea editable
		JScrollPane jsp = new JScrollPane(txtComments);
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		DefaultCaret caret = (DefaultCaret) txtComments.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		txtComments.getDocument().addDocumentListener((DocumentListener) new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				updateComments();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				updateComments();
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				updateComments();
			}

			public void updateComments() {
				comments = txtComments.getText();
			}

		});
		txtComments.setText(
				"Bạn rất biết lắng nghe. | Bạn thật mạnh mẽ | Công việc bạn đang làm thật tuyệt | Bạn thật truyền cảm hứng! | Bạn là một người bạn tốt! | Bạn có một trái tim ấm áp");
		commentsPanel.add(lbComments);
		commentsPanel.add(jsp);

		// init bottom panel
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new FlowLayout());
		frame.getContentPane().add(infoPanel, BorderLayout.SOUTH);
		JPanel errorsPanel = new JPanel();
		errorsPanel.setLayout(new FlowLayout());
		lbErrors = new JLabel("");
		lbErrors.setForeground(Color.RED);
		errorsPanel.add(lbErrors);
		infoPanel.add(errorsPanel);
		JPanel logPanel = new JPanel();
		JTextArea txtLogs = new JTextArea(10, 60);
		DefaultCaret logCaret = (DefaultCaret) txtLogs.getCaret();
		logCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		txtLogs.setLineWrap(true);
		txtLogs.setEditable(false); // set textArea non-editable
		JScrollPane jspLog = new JScrollPane(txtLogs);
		jspLog.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		logPanel.add(jspLog);
		infoPanel.add(logPanel);
		// link the log text area to the message center
		MessageCenter.setMessageCenterTextArea(txtLogs);
	}

	private void startAction() {
		if (comments.isBlank()) {
			lbErrors.setText("Comments should not be empty");
		} else {
			if (profilePath.isBlank()) {
				lbErrors.setText("Profile Path should not be empty");
			} else {
				if (victimProfileUrl.isBlank()) {
					lbErrors.setText("Victim profile url should not be empty");
				} else {
					lbErrors.setText("");
					if (!GeneralSettings.isStartedRunning) {
						btnStart.setEnabled(false);
						GeneralSettings.isStartedRunning = true;
						try {
							if (profilePath.length() > 0) {
								driver = DriverCenter.getNewFirefoxDriver(profilePath);
							} else {
								driver = DriverCenter.getNewFirefoxDriver();
							}
							FlowController flow = new FlowController(driver, victimProfileUrl, comments);
							Thread thread = new Thread(flow);
							thread.start();
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
		}
	}

	private void stopAction() {
		if (GeneralSettings.isStartedRunning) {
			DriverCenter.terminateAllDrivers();
			GeneralSettings.isStartedRunning = false;
			btnStart.setText("Start");
			btnStart.setEnabled(true);
		}
	}
}
