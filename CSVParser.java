import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Parses the "ProstateX-2-Findings-Train" csv file to create a mapping of each
 * patient's ID to the corresponding gleason grade group
 * 
 * @author uweme
 * @version I
 */
public class CSVParser {

	/**
	 * File path to the "ProstateX-2-Findings-Train" csv file
	 */
	public String csvFilePath;
	/**
	 * Maps the corresponding gleason grade group to the specific patient
	 */
	public HashMap<String, Integer> gleasonGroupMap;
	/**
	 * Handles duplicate data in the csv file by mapping each patient to a list of
	 * all the scores they had within the csv file. After we find duplicates, we
	 * remove them from the dataset
	 */
	public HashMap<String, ArrayList<Integer>> numGroupsPerPatient;

	public CSVParser(String csvFilePath) {
		this.csvFilePath = csvFilePath;
		getNumPatientScores();
		populateGleasonGroupMap();
	}

	/**
	 * Creates a reference to the csv file and populates the numGroupsPerPatient map
	 */
	public void getNumPatientScores() {
		numGroupsPerPatient = new HashMap();

		File csvFile = new File(csvFilePath);
		try {
			Scanner csvParser = new Scanner(csvFile);
			while (csvParser.hasNext()) {
				populateNumScoresMap(csvParser.nextLine());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if the current patient has already been processed. If the patient has
	 * been seen before, the additional score gets added onto their list of scores.
	 * 
	 * @param data - row of data within the csv file
	 */
	private void populateNumScoresMap(String data) {
		String[] dataArr = data.split(",");
		if (!dataArr[dataArr.length - 1].equals("ggg") && !dataArr[dataArr.length - 1].equals("")) {

			String id = dataArr[0];
			int score = score = Integer.parseInt(dataArr[dataArr.length - 1]);
			if (!dataArr[dataArr.length - 1].equals("ggg")) {

			}

			if (numGroupsPerPatient.containsKey(id) && !numGroupsPerPatient.get(id).contains(score)) {
				numGroupsPerPatient.get(id).add(score);
			} else {
				ArrayList<Integer> scores = new ArrayList();
				scores.add(score);
				numGroupsPerPatient.put(id, scores);
			}
		}

	}

	/**
	 * Checks how many groups the current patient maps to. If the patient maps to
	 * more than 1, their data gets discarded
	 */
	private void populateGleasonGroupMap() {
		gleasonGroupMap = new HashMap();
		for (String patient : numGroupsPerPatient.keySet()) {
			if (numGroupsPerPatient.get(patient).size() <= 1) {

				addToNormalGroup(patient);
			}
		}

	}

	/**
	 * Adds the current patient's data to the gleasonGroupMap
	 * 
	 * @param patient - Current patient to be processed
	 */
	private void addToNormalGroup(String patient) {
		int group = numGroupsPerPatient.get(patient).get(0);

		gleasonGroupMap.put(patient, group);

	}

	/**
	 * Retrieves the gleasonGroup map of each patient and their respective scores
	 * 
	 * @return HashMap<String, Integer>
	 */
	public HashMap<String, Integer> getGroupMap() {
		return gleasonGroupMap;
	}

}
