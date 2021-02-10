import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

/**
 * Listens to button click events so that the GUI can update proerly
 * 
 * @author uweme
 * @version I
 */
public class ButtonListener implements ActionListener {
	/**
	 * Main window that each component lives on
	 */
	private JPanel contentPane;

	/**
	 * The specific button that the user pressed
	 */
	private JButton selectedBtn;
	/**
	 * lets the user know what directory they chose after they have uploaded the
	 * data set
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
	private JLabel csvIconLabel;
	/**
	 * Label that tells the user what csv file they selected
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

	public ButtonListener(JPanel contentPane, JButton uploadDataSetBtn, JLabel chosenDirectory) {
		this.selectedBtn = uploadDataSetBtn;
		this.contentPane = contentPane;
		this.chosenDirectory = chosenDirectory;

	}

	public ButtonListener(JPanel contentPane, JButton dataSetNextBtn, JLabel folderIconLabel, JLabel chosenDirectory,
			JLabel dataSetSteps, JButton uploadCSVBtn, JLabel csvIconLabel, JLabel chosenCSVFile, JLabel csvFileSteps,
			JButton organizeDataBtn, JLabel organizeDataLbl, JButton uploadDataSetBtn, JLabel nextStep) {
		this.selectedBtn = dataSetNextBtn;
		this.contentPane = contentPane;
		this.chosenDirectory = chosenDirectory;
		this.folderIconLabel = folderIconLabel;
		this.chosenDirectory = chosenDirectory;
		this.dataSetSteps = dataSetSteps;
		this.uploadCSVBtn = uploadCSVBtn;
		this.csvIconLabel = csvIconLabel;
		this.chosenCSVFile = chosenCSVFile;
		this.csvFileSteps = csvFileSteps;
		this.organizeDataBtn = organizeDataBtn;
		this.organizeDataLbl = organizeDataLbl;
		this.uploadDataSetBtn = uploadDataSetBtn;
		this.nextStep = nextStep;
	}

	public ButtonListener(JPanel contentPane, JButton uploadCSVBtn, JLabel chosenCSVFile, JLabel csvIconLabel) {
		this.contentPane = contentPane;
		this.selectedBtn = uploadCSVBtn;
		this.chosenCSVFile = chosenCSVFile;
		this.csvIconLabel = csvIconLabel;
	}

	public ButtonListener(JPanel contentPane, JButton organizeDataBtn, JLabel csvIconLabel, JLabel chosenCSVFile,
			JLabel csvFileSteps, JButton uploadCSVBtn, JLabel organizeDataLbl, JLabel loading,
			JLabel waitingScreenLabel, JLabel finished, JLabel finishedMessage) {
		this.contentPane = contentPane;
		this.selectedBtn = organizeDataBtn;
		this.csvIconLabel = csvIconLabel;
		this.chosenCSVFile = chosenCSVFile;
		this.csvFileSteps = csvFileSteps;
		this.uploadCSVBtn = uploadCSVBtn;
		this.organizeDataLbl = organizeDataLbl;
		this.loading = loading;
		this.waitingScreenLabel = waitingScreenLabel;
		this.finished = finished;
		this.finishedMessage = finishedMessage;

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (selectedBtn.getText().contains("DataSet")) {
			String dataSetFilePath = getdataSetFilePath();
			GUI.dataSetFilePath = dataSetFilePath;
			chosenDirectory.setText("Directory Chosen: " + dataSetFilePath);
			contentPane.add(chosenDirectory);
			contentPane.repaint();
		} else if (selectedBtn.getText().equals("")) {
			updateContentPaneWithCSVInfo();
		} else if (selectedBtn.getText().contains("CSV")) {
			String csvFilePath = getCSVFilePath();
			GUI.csvFilePath = csvFilePath;
			chosenCSVFile.setText("CSV File Chosen: " + csvFilePath);
			contentPane.add(chosenCSVFile);
			contentPane.repaint();
		} else if (selectedBtn.getText().contains("org")) {
			organizeDataSet();
		}
	}

	/**
	 * USes the JFileChooser class to select the data set folder
	 * 
	 * @return String
	 */
	private String getdataSetFilePath() {
		JFileChooser getDataSet = new JFileChooser();
		getDataSet.setCurrentDirectory(new File("."));
		getDataSet.setDialogTitle("Select Data Set Firectory");
		getDataSet.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		getDataSet.showOpenDialog(null);
		return getDataSet.getSelectedFile().getAbsolutePath();

	}

	/**
	 * Uses the JFileCHooser class to select the correct csv file
	 * 
	 * @return String
	 */
	private String getCSVFilePath() {
		JFileChooser getCSV = new JFileChooser();
		getCSV.setCurrentDirectory(new File("."));
		getCSV.setDialogTitle("Select CSV File");
		getCSV.setFileSelectionMode(JFileChooser.FILES_ONLY);
		getCSV.showOpenDialog(null);
		return getCSV.getSelectedFile().getAbsolutePath();
	}

	/**
	 * Removes the data set info and instructions and replaces it with the csv information
	 */
	private void updateContentPaneWithCSVInfo() {
		contentPane.remove(selectedBtn);
		contentPane.remove(uploadDataSetBtn);
		contentPane.remove(dataSetSteps);
		contentPane.remove(folderIconLabel);
		contentPane.remove(chosenDirectory);
		contentPane.remove(nextStep);

		contentPane.add(uploadCSVBtn);
		contentPane.add(csvIconLabel);
		contentPane.add(csvFileSteps);
		contentPane.add(organizeDataBtn);
		contentPane.add(organizeDataLbl);
		contentPane.repaint();

	}

	/**
	 * Parses the data set so that the data can be cleaned
	 */
	private void organizeDataSet() {
		
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				CSVParser csvParser = new CSVParser(GUI.csvFilePath);
				FolderParser folderParser = new FolderParser(GUI.dataSetFilePath, csvParser.getGroupMap());
				folderParser.cleanData();
				removeLoadingScreen();
				return null;
			}
			
		};
		addLoadingScreen();
		worker.execute();

	}

	/**
	 * Adds the loading screen to the content pane after the program finishes
	 */
	private void addLoadingScreen() {
		contentPane.removeAll();
		contentPane.add(loading);
		contentPane.add(waitingScreenLabel);
		contentPane.repaint();

	}

	/**
	 * Removes the loading screen after the program finishes
	 */
	private void removeLoadingScreen() {
		contentPane.removeAll();
		contentPane.add(finished);
		contentPane.add(finishedMessage);
		contentPane.repaint();

	}

}
