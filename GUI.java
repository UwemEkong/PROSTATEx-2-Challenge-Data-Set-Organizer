import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.Color;
import javax.swing.UIManager;

/**
 * The GUI class makes it easier for the user to select the correct data to be
 * processed
 * 
 * @author uweme
 * @version I
 */
public class GUI extends JFrame {

	/**
	 * Main window that each component lives on
	 */
	private JPanel contentPane;
	/**
	 * lets the user know what directory they chose after they have uploaded the data set
	 */
	private JLabel chosenDirectory;
	/**
	 * Tells the user what the first step is to process the data
	 */
	private JLabel dataSetSteps;
	/**
	 * Label that contains the next arrow icon
	 */
	private JLabel nextStep;
	/**
	 * Button that allows the user to select the data set
	 */
	private JButton uploadDataSetBtn;
	/**
	 * Icon label for the folder image
	 */
	private JLabel folderIconLabel;
	/**
	 * Button that goes to the next window after the data set is uploaded
	 */
	private JButton dataSetNextBtn;
	/**
	 * Allows the user to upload the csv file upon being clicked
	 */
	private JButton uploadCSVBtn;
	/**
	 * icon image for the csv file example
	 */
	private JLabel CSVIconLabel;
	/**
	 *  Label that tells the user what csv file they selected
	 */
	private JLabel chosenCSVFile;
	/**
	 * Tells the user how to upload the csv file
	 */
	private JLabel csvFileSteps;
	/**
	 * Calls the folder parser and csv parser to clean the data
	 */
	private JButton organizeDataBtn;
	/**
	 * label for the organize data button
	 */
	private JLabel organizeDataLbl;
	/**
	 * loading screen label
	 */
	private JLabel loading;
	/**
	 * label for the loading screen
	 */
	private JLabel waitingScreenLabel;
	/**
	 * Tells the user when the program is done executing
	 */
	private JLabel finished;
	/**
	 * Image that displays when the program is finished executing
	 */
	private JLabel finishedMessage;
	/**
	 * Path to the "ProstateX-2-Findings-Train" csv file
	 */
	static String csvFilePath;
	/**
	 * Path to the data set 
	 */
	static String dataSetFilePath;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI() {

		setTitle("Data Organizer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 484, 382);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// New Page
		uploadDataSetBtn = new JButton("Click To Upload DataSet");
		uploadDataSetBtn.setBounds(137, 171, 212, 59);
		contentPane.add(uploadDataSetBtn);

		folderIconLabel = new JLabel("");
		folderIconLabel.setBounds(184, 61, 126, 126);
		folderIconLabel.setIcon(new ImageIcon("icon.png"));
		contentPane.add(folderIconLabel);

		dataSetNextBtn = new JButton("");
		dataSetNextBtn.setBounds(379, 249, 68, 50);
		dataSetNextBtn.setIcon(new ImageIcon("nexticon.png"));
		contentPane.add(dataSetNextBtn);

		chosenDirectory = new JLabel("Directory Chosen: ");
		chosenDirectory.setBounds(15, 254, 349, 20);

		dataSetSteps = new JLabel(
				"<html><center>Step 1.) Please Upload The Data Set Directory Labeled <br/><strong> \"PROSTATEx\"</strong></strong></center><html>");
		dataSetSteps.setBounds(73, 0, 351, 94);
		contentPane.add(dataSetSteps);

		nextStep = new JLabel("Next Step");
		nextStep.setFont(new Font("Tahoma", Font.PLAIN, 20));
		nextStep.setBounds(375, 306, 87, 20);
		contentPane.add(nextStep);

		// New Page
		uploadCSVBtn = new JButton("Click To Upload CSV File");
		uploadCSVBtn.setBounds(111, 186, 212, 59);

		CSVIconLabel = new JLabel("");
		CSVIconLabel.setBounds(154, 58, 126, 126);
		CSVIconLabel.setIcon(new ImageIcon("csvicon.png"));

		chosenCSVFile = new JLabel("CSV File Chosen: ");
		chosenCSVFile.setBounds(33, 261, 331, 20);

		csvFileSteps = new JLabel(
				"<html><center>Step 2.) Please Upload The CSV File Labeled <br/><strong> \"ProstateX-2-Findings-Train\"</strong></center><html>");
		csvFileSteps.setBounds(96, 0, 351, 94);

		organizeDataBtn = new JButton("org");
		organizeDataBtn.setForeground(UIManager.getColor("Button.background"));
		organizeDataBtn.setFont(new Font("Tahoma", Font.PLAIN, 5));
		organizeDataBtn.setBounds(379, 249, 68, 50);
		organizeDataBtn.setIcon(new ImageIcon("nexticon.png"));

		organizeDataLbl = new JLabel("Organize Data!");
		organizeDataLbl.setFont(new Font("Tahoma", Font.BOLD, 15));
		organizeDataLbl.setBounds(346, 306, 126, 20);

		// New Page
		loading = new JLabel("New label");
		loading.setBounds(168, 87, 128, 128);
		loading.setIcon(new ImageIcon("loadingScreen.gif"));

		waitingScreenLabel = new JLabel("Organizing Data, Please Wait");
		waitingScreenLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		waitingScreenLabel.setBounds(81, 231, 302, 25);

		// New Page
		finished = new JLabel("");
		finished.setBounds(108, 16, 230, 230);
		finished.setIcon(new ImageIcon("checkicon.png"));

		finishedMessage = new JLabel("Finished Organizing");
		finishedMessage.setFont(new Font("Tahoma", Font.BOLD, 20));
		finishedMessage.setBounds(118, 244, 218, 31);

		ActionListener uploadDataSetListener = new ButtonListener(contentPane, uploadDataSetBtn, chosenDirectory);
		uploadDataSetBtn.addActionListener(uploadDataSetListener);

		ActionListener dataSetNextListener = new ButtonListener(contentPane, dataSetNextBtn, folderIconLabel,
				chosenDirectory, dataSetSteps, uploadCSVBtn, CSVIconLabel, chosenCSVFile, csvFileSteps, organizeDataBtn,
				organizeDataLbl, uploadDataSetBtn, nextStep);
		dataSetNextBtn.addActionListener(dataSetNextListener);

		ActionListener uploadCSVFileListener = new ButtonListener(contentPane, uploadCSVBtn, chosenCSVFile,
				CSVIconLabel);
		uploadCSVBtn.addActionListener(uploadCSVFileListener);

		ActionListener organizeDataListener = new ButtonListener(contentPane, organizeDataBtn, CSVIconLabel,
				chosenCSVFile, csvFileSteps, uploadCSVBtn, organizeDataLbl, loading, waitingScreenLabel, finished,
				finishedMessage);
		organizeDataBtn.addActionListener(organizeDataListener);
		
		

	}
}
