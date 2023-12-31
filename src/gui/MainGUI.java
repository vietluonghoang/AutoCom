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
	private String pathToImagesFolder = "";
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
					MessageCenter.appendMessageToCenterLog(e.getMessage());
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
		frame.setBounds(50, 50, 800, 800);
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
		profilePathPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
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
		victimProfileUrlPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
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
		txtVictimProfileUrl.setText("https://www.facebook.com/anne.melson.37");
//		txtVictimProfileUrl.setText("https://www.facebook.com/khanh.chap.71");
		victimProfileUrlPanel.add(lbVictimProfileUrl);
		victimProfileUrlPanel.add(txtVictimProfileUrl);
		JPanel pathToImagesFolderPanel = new JPanel();
		pathToImagesFolderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		mainPanel.add(pathToImagesFolderPanel);
		JLabel lbPathToImagesFolder = new JLabel("Path To Images Folder: ");
		JTextField txtPathToImagesFolder = new JTextField();
		txtPathToImagesFolder.getDocument().addDocumentListener((DocumentListener) new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				updatePathToImagesFolder();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				updatePathToImagesFolder();
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				updatePathToImagesFolder();
			}

			public void updatePathToImagesFolder() {
				pathToImagesFolder = txtPathToImagesFolder.getText().trim();
				GeneralSettings.pathToImagesFolder = pathToImagesFolder;
				MessageCenter.appendMessageToCenterLog("--- Updated path to images folder!");
			}

		});
		txtPathToImagesFolder.setPreferredSize(new Dimension(500, 20));
		txtPathToImagesFolder.setText("/Users/k33/Downloads/images");
		pathToImagesFolderPanel.add(lbPathToImagesFolder);
		pathToImagesFolderPanel.add(txtPathToImagesFolder);
		JPanel commentIntervalPanel = new JPanel();
		commentIntervalPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
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
					MessageCenter.appendMessageToCenterLog("--- Checking comment interval...");
					GeneralSettings.commentInterval = Integer.parseInt(txtCommentInterval.getText().trim());
					MessageCenter.appendMessageToCenterLog("--- Updated comment interval!");
				} catch (Exception e) {
					MessageCenter.appendMessageToCenterLog(e.getMessage());
					MessageCenter.appendMessageToCenterLog("+++ Failed to check comment interval!");
				}
			}

		});
		txtCommentInterval.setPreferredSize(new Dimension(50, 20));
		txtCommentInterval.setText("" + GeneralSettings.commentInterval);
		commentIntervalPanel.add(lbCommentInterval);
		commentIntervalPanel.add(txtCommentInterval);
		JPanel maxCommentsPanel = new JPanel();
		maxCommentsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		mainPanel.add(maxCommentsPanel);
		JLabel lbMaxComment = new JLabel("Max comment per profile: ");
		JTextField txtMaxComment = new JTextField();
		txtMaxComment.getDocument().addDocumentListener((DocumentListener) new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				updateMaxComment();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				updateMaxComment();
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				updateMaxComment();
			}

			public void updateMaxComment() {
				try {
					MessageCenter.appendMessageToCenterLog("--- Checking max comment...");
					GeneralSettings.maxComment = Integer.parseInt(txtMaxComment.getText().trim());
					MessageCenter.appendMessageToCenterLog("--- Updated max comment!");
				} catch (Exception e) {
					MessageCenter.appendMessageToCenterLog(e.getMessage());
					MessageCenter.appendMessageToCenterLog("+++ Failed to update max comment!");
				}
			}

		});
		txtMaxComment.setPreferredSize(new Dimension(50, 20));
		txtMaxComment.setText("" + GeneralSettings.maxComment);
		maxCommentsPanel.add(lbMaxComment);
		maxCommentsPanel.add(txtMaxComment);
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
		txtComments.setText("Thế lực nào đang bảo kê, chống lưng cho tội ác?\n"
				+ "Phạm Tuấn Khanh-Khanh Chập, trùm đâm thuê chém mướn núp dưới vỏ bọc doanh nhân,kẻ đứng sau chỉ đạo hàng loạt các vụ đâm thuê chém mướn,cố ý gây thương tích,gây rối trật tự, bắt giữ người trái phép, sử dụng vũ khí quân dụng uy hiếp, đe doạ tính mạng người dân,sau bao tội ác gây ra vẫn nhởn nhơ ngoài vòng pháp luật. Đằng sau vẻ bề ngoài hiền lành, lịch sự là 1 kẻ khát máu và tàn bạo.|Thế lực nào đang bảo kê, chống lưng cho tội ác?\n"
				+ "Khanh chập có mối quan hệ cực kỳ thân thiết và phức tạp với các trùm giang hồ cộm cán ở khắp các tỉnh thành đồng thời Y còn là chiến tướng, cánh tay phải đắc lực của trùm giang hồ khét tiếng Nam ngọ, ông trùm đường dây đánh bạc nghìn tỷ. Năm 2014 khanh chập tổ chức cầm đầu băng nhóm xã hội đen gồm hàng trăm đối tượng giang hồ nguy hiểm mang cái tên nhóm truy tìm khách nợ. Khanh chập trực tiếp chỉ đạo băng nhóm gây ra một loạt các vụ đâm chém, gây rối trật tự, dùng súng quân dụng thanh toán đối thủ,đòi nợ thuê và đâm thuê chém mướn gây nhức nhối trong dư luận tại thời điểm đó.|Thế lực nào đang bảo kê, chống lưng cho tội ác?\n"
				+ "Năm 2018 sau khi đường dây đánh bạc 1600 tỷ của ông trùm Nam Ngọ bị bộ công an triệt phá, Nam Ngọ bị bắt và phải đền tội, Khanh chập lẩn trốn vào sài gòn, ở đây với bản tính ngông cuồng, hung hãn và máu lạnh, hắn lại tiếp tục tổ chức và chỉ đạo băng ổ nhóm tín dụng đen hoạt động với quy mô lớn , cho vay nặng lãi và cưỡng đoạt tài sản, reo rắc kinh hoàng cho người dân ở khắp các quận huyện Thành Phố Hồ Chí Minh và các Tỉnh Miền Tây.|Thế lực nào đang bảo kê, chống lưng cho tội ác?\n"
				+ "Máu lạnh, hung hãn và ngông cuồng là những lời nhận xét về trùm đâm thuê chém mướn Khanh Chập. Ngoài đuổi cùng giết tận các đối thủ, hắn thậm chí còn sẵn sàng xuống tay một cách tàn bạo với các đàn em nếu ko nghe theo sự chỉ đạo của hắn. Với rất nhiều tội ác gây ra trong nhiều năm ở khắp các tỉnh thành từ Bắc vào Nam trong vai trò tổ chức và cầm đầu băng nhóm nhưng cứ khi cơ quan chức năng chuẩn bị vào cuộc là Khanh chập lại đột nhiên biến mất. Có hay không một thế lực ngầm đang chống lưng cho đối tượng đặc biệt nguy hiểm tác oai tác quái, ngang nghiên coi thường pháp luật.");
//		txtComments.setText(
//				"Bạn rất biết lắng nghe. | Bạn thật mạnh mẽ | Công việc bạn đang làm thật tuyệt | Bạn thật truyền cảm hứng! | Bạn là một người bạn tốt! | Bạn có một trái tim ấm áp");
		commentsPanel.add(lbComments);
		commentsPanel.add(jsp);

		// init bottom panel
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		frame.getContentPane().add(infoPanel, BorderLayout.SOUTH);
		JPanel errorsPanel = new JPanel();
		errorsPanel.setLayout(new FlowLayout());
		lbErrors = new JLabel("");
		lbErrors.setForeground(Color.RED);
		errorsPanel.add(lbErrors);
		infoPanel.add(errorsPanel);
		JPanel logTitlePanel = new JPanel();
		logTitlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblogTitle = new JLabel("Logs: ");
		logTitlePanel.add(lblogTitle);
		infoPanel.add(logTitlePanel);
		JPanel logPanel = new JPanel();
		JTextArea txtLogs = new JTextArea(20, 60);
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
							MessageCenter.appendMessageToCenterLog(e.getMessage());
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
