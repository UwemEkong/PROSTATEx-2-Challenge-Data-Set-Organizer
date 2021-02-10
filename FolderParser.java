import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Parses the Prostatex2 MR Gleason Grade Group data set and organizes each
 * image based on view and gleason grade group score
 * 
 * @author Uwem Ekong
 * @version I
 */
public class FolderParser {
	String sourceFolderPath;
	HashMap<String, Integer> gleasonGroupMap;

	public FolderParser(String sourceFolderPath, HashMap<String, Integer> gleasonGroupMap) {
		this.sourceFolderPath = sourceFolderPath;
		this.gleasonGroupMap = gleasonGroupMap;
	}

	/**
	 * Walks through the data set and organizes the data by removing the first
	 * layer, renaming the dicom images, and creating 4 separate folders for each
	 * view
	 */
	public void cleanData() {
		try {
			makeCopy(sourceFolderPath);
			deleteFirstLayer(sourceFolderPath);
			renameViews(sourceFolderPath);
			renameDicom(sourceFolderPath);
			createViewFolders(sourceFolderPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Creates a copy of the unedited data set directory before organizing the data
	 * 
	 * @param sourceFolderPath - Path to main directory
	 * @throws IOException
	 */
	private void makeCopy(String sourceFolderPath) throws IOException {
		Path sourcePath = Paths.get(sourceFolderPath);
		Path destinationPath = Paths.get(sourceFolderPath + " (1)");
		Files.walk(sourcePath).forEach(file -> {
			Path destination = Paths.get(destinationPath.toString(),
					file.toString().substring(sourceFolderPath.length()));

			try {
				Files.copy(sourcePath, destination);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

	}

	/**
	 * Copies all files in the "MR prostaat kanker" folder and removes the "MR
	 * prostaat kanker" layer
	 * 
	 * @param sourceFolder - Path to main directory
	 * @throws IOException
	 */
	private static void deleteFirstLayer(String sourceFolder) throws IOException {

		Files.walk(Paths.get(sourceFolder)).forEach(file -> {
			if (file.getFileName().toString().contains("ProstateX")) {
				String destination = sourceFolder + File.separatorChar + file.getFileName();
				try {
					findSource(destination);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * Obtains the location of the "MR prostaat kanker" folder so that the dicom
	 * images in the folder can be moved to another directory
	 * 
	 * @param destinationFolder - Path to main directory
	 * @throws IOException
	 */
	private static void findSource(String destinationFolder) throws IOException {
		Files.walk(Paths.get(destinationFolder)).forEach(file -> {
			String fileName = file.getFileName().toString();
			if (fileName.length() > 8 && containsDate(fileName.substring(0, 10))) {
				String sourceFolder = destinationFolder + File.separatorChar + file.getFileName();
				try {
					copyFolder(sourceFolder, destinationFolder);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
	}

	/**
	 * Determines if the given file has a date present within the file path. If so,
	 * then this file is the "MR prostaat kanker" layer
	 * 
	 * @param filePath - Path of the selected file
	 * @return boolean
	 */
	private static boolean containsDate(String filePath) {
		int digitCount = 0;
		for (char ch : filePath.toCharArray()) {
			if (Character.isDigit(ch)) {
				digitCount++;
			}
		}
		if (digitCount == 8) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Copies the file located at sourcePath and places it at the destinationPath
	 * 
	 * @param sourcePath      - Source Directory path
	 * @param destinationPath - Destination Directory Path
	 * @throws IOException
	 */
	public static void copyFolder(String sourcePath, String destinationPath) throws IOException {

		Files.walk(Paths.get(sourcePath)).forEach(source -> {
			Path destination = Paths.get(destinationPath, source.toString().substring(sourcePath.length()));
			// System.out.println("Dest:" + destination.getFileName().toString());
			try {
				Files.copy(source, destination);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		deleteFolder(sourcePath);

	}

	/**
	 * Deletes the folder passed in, as well as all of the files within the folder
	 * 
	 * @param currPath - Path to file that gets deleted
	 * @throws IOException
	 */
	private static void deleteFolder(String currPath) throws IOException {
		Files.walk(Paths.get(currPath)).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
	}

	/**
	 * Renames all of the view folders for each patient into a more readable format
	 * 
	 * @param sourceFolder - Main Directory path
	 * @throws IOException
	 */
	private static void renameViews(String sourceFolder) throws IOException {

		Files.walk(Paths.get(sourceFolder)).forEach(file -> {
			if (file.getFileName().toString().contains("ProstateX")) {
				String parentFolder = sourceFolder + File.separatorChar + file.getFileName();

				try {
					findViews(parentFolder);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * Finds the location of each view folder
	 * 
	 * @param sourceFolder - Main Directory path
	 * @throws IOException
	 */
	private static void findViews(String sourceFolder) throws IOException {

		Files.walk(Paths.get(sourceFolder)).forEach(file -> {
			String fileName = file.getFileName().toString();
			renameFile(sourceFolder + File.separatorChar + fileName, fileName);
		});

	}

	/**
	 * Renames each view folder into a shorter format that is easier to read
	 * 
	 * @param filePath - original view folder path name
	 * @param fileName
	 */
	private static void renameFile(String filePath, String fileName) {
		String newName = "";
		Path oldPath = Paths.get(filePath);
		if (fileName.contains("ADC")) {
			newName = "ep2adc";
		} else if (fileName.contains("CALC")) {
			newName = "ep2calc";
		}
		if (fileName.contains("sag")) {
			newName = "t2sag";
		} else if (fileName.contains("setra")) {
			newName = "t2setra";
		}

		changeFileName(oldPath, newName);

	}

	/**
	 * Takes a renamed file and places it into the directory that corresponds to the
	 * new name
	 * 
	 * @param originalPath - unedited path name of the specific file
	 * @param newName      - new name of the file
	 */
	private static void changeFileName(Path originalPath, String newName) {
		try {
			Files.move(originalPath, originalPath.resolveSibling(newName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Locates each patient folder so that the dicom images in each can be renamed
	 * 
	 * @param sourceFolder - Main directory path
	 * @throws IOException
	 */
	private static void renameDicom(String sourceFolder) throws IOException {

		Files.walk(Paths.get(sourceFolder)).forEach(file -> {
			if (file.getFileName().toString().contains("ProstateX")) {
				String selectedFolder = file.toString();
				try {
					renameAllDicom(selectedFolder);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * Traverses the current directory and renames each dicom image into a format
	 * that is easier to work with. The new format includes the patient id
	 * 
	 * @param selectedFolder - Current directory containing unedited dicom files
	 * @throws IOException
	 */
	private static void renameAllDicom(String selectedFolder) throws IOException {
		String patientID = selectedFolder.substring(selectedFolder.length() - 4);
		Files.walk(Paths.get(selectedFolder)).forEach(file -> {
			if (file.getFileName().toString().contains("dcm")) {
				String newName = patientID + file.getFileName().toString().substring(1);
				changeFileName(file, newName);
			}
		});

	}

	/**
	 * Creates 4 separate directories for each view
	 * 
	 * @param sourceFolder - Main directory path
	 * @throws IOException
	 */
	private void createViewFolders(String sourceFolder) throws IOException {
		File t2sagFolder = new File(sourceFolder + File.separatorChar + "t2sag");
		File t2setraFolder = new File(sourceFolder + File.separatorChar + "t2setra");
		File ep2calcFolder = new File(sourceFolder + File.separatorChar + "ep2calc");
		File ep2adcFolder = new File(sourceFolder + File.separatorChar + "ep2adc");

		t2sagFolder.mkdirs();
		t2setraFolder.mkdirs();
		ep2calcFolder.mkdirs();
		ep2adcFolder.mkdirs();

		ArrayList<File> list = new ArrayList();
		list.add(t2sagFolder);
		list.add(t2setraFolder);
		list.add(ep2calcFolder);
		list.add(ep2adcFolder);
		populateViewFolders(list);
		deleteExcessViewFolders(list);
		deletePatientFolders(sourceFolder);

	}

	/**
	 * Iterates through the list of view folder paths so that each dicom image can
	 * be placed in the proper directory
	 * 
	 * @param viewFolders - List of the locations for each of the 4 view folders
	 * @throws IOException
	 */
	private void populateViewFolders(ArrayList<File> viewFolders) throws IOException {
		for (File folder : viewFolders) {
			for (int i = 1; i < 6; i++) {
				String location = folder.getAbsolutePath() + File.separatorChar + i;
				File groupFolder = new File(location);
				groupFolder.mkdirs();
			}
		}

		organizeDicomImages(viewFolders);
	}

	/**
	 * Uses the "ProstateX-2-Findings-Train" csv file to correctly organize each
	 * patients' dicom images
	 * 
	 * @param viewFolders - List of the paths of each of the 4 view folders
	 * @throws IOException
	 */
	public void organizeDicomImages(ArrayList<File> viewFolders) throws IOException {
		Files.walk(Paths.get(sourceFolderPath)).forEach(file -> {
			if (file.getFileName().toString().contains("ProstateX")
					&& gleasonGroupMap.containsKey(file.getFileName().toString())) {
				String id = file.getFileName().toString();
				int group = gleasonGroupMap.get(id);
				try {
					arrangePatientDicom(file.toString(), group, viewFolders);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * Organizes the patient's dicom images by targeting each of the 4 views in the
	 * patients directory and pulling out each dicom image
	 * 
	 * @param patientFilePath - File path to patient's directory
	 * @param group           - Group that the patient's dicom images belong to
	 * @param viewFolders     - list of paths to each of the 4 view folders
	 * @throws IOException
	 */
	public void arrangePatientDicom(String patientFilePath, int group, ArrayList<File> viewFolders) throws IOException {
		Files.walk(Paths.get(patientFilePath)).forEach(file -> {
			if (!file.getFileName().toString().contains("dcm")) {
				try {
					traverseViewFolder(file, group, viewFolders);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * Iterates through the list of view folders and searches for the one that
	 * matches up to the current viewFolder passed in. Te purpose of this is to find
	 * the destination path
	 * 
	 * @param viewFolder  - Specific view folder
	 * @param group       - group that the patient's dicom images belong to
	 * @param viewFolders - List of paths that correspond to each view folder
	 * @throws IOException
	 */
	public void traverseViewFolder(Path viewFolder, int group, ArrayList<File> viewFolders) throws IOException {
		String viewName = viewFolder.getFileName().toString();
		String correspondingView = "";

		for (File folder : viewFolders) {
			if (folder.getName().equals(viewName)) {
				correspondingView = folder.getAbsolutePath();
			}
		}
		Path destViewPath = Paths.get(correspondingView);
		Files.walk(Paths.get(viewFolder.toString())).forEach(dicomImage -> {
			try {
				organizeAccordingToGroup(dicomImage, group, destViewPath);
			} catch (IOException e) {
				e.printStackTrace();
			}

		});

	}

	/**
	 * Writes the dicom image taken from the patient view folder and places it in
	 * the corresponding view folder
	 * 
	 * @param dicomImage   - dicom image that needs to be organized
	 * @param group        - specific group that the dicom image belongs to
	 * @param destViewPath - Specific view that the dicom image is associated with
	 * @throws IOException
	 */
	synchronized private void organizeAccordingToGroup(Path dicomImage, int group, Path destViewPath)
			throws IOException {
		Files.walk(destViewPath).forEach(groupFolder -> {
			if (isValidGroupFolder(groupFolder, group)) {

				Path destination = Paths
						.get(groupFolder.toString() + File.separatorChar + dicomImage.getFileName().toString());
				try {
					FileWriter w;
					try {
						w = new FileWriter("fileData");
						w.write("Origin: " + dicomImage.toString() + "\n");
						w.write("Dest: " + destination.toString() + "\n");
						w.close();
					} catch (IOException e1) {

						e1.printStackTrace();
					}
					Files.copy(dicomImage, destination);

				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		});
	}

	/**
	 * Determines if the passed in groupFolder is indeed a valid groupFolder
	 * 
	 * @param groupFolder - Folder within the view folder that helps identify the
	 *                    group
	 * @param group       - Specific group for dicom image
	 * @return
	 */
	private boolean isValidGroupFolder(Path groupFolder, int group) {
		String name = groupFolder.getFileName().toString();
		if (name.length() == 1 && Integer.parseInt(name) == group) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Deletes the extra view folders that were produced when organizing the dicom
	 * into each respective view older and group folder
	 * 
	 * @param viewFolders - list of each of the four view folders
	 * @throws IOException
	 */
	private void deleteExcessViewFolders(ArrayList<File> viewFolders) throws IOException {
		for (File viewFolder : viewFolders) {
			Files.walk(Paths.get(viewFolder.getAbsolutePath())).forEach(file -> {
				if (isViewFolder(file, viewFolders) && parentIsGroup(file.getParent())) {

					try {
						deleteFolder(file.toString());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			});
		}

	}

	/**
	 * Determines if the given path is a valid view folder that exists
	 * 
	 * @param path        - Candidate view folder
	 * @param viewFolders - list of view folder paths
	 * @return
	 */
	private boolean isViewFolder(Path path, ArrayList<File> viewFolders) {
		String fileName = path.getFileName().toString();
		ArrayList<String> folderNames = new ArrayList<String>();
		for (File f : viewFolders) {
			if (f.getName().equals(fileName)) {
				return true;
			}
		}
		return false;

	}

	/**
	 * Determines if the parent directory is a digit. If so, then that means the
	 * parent directory is a gleason group directory
	 * 
	 * @param parent - Gleason group number
	 * @return
	 */
	private boolean parentIsGroup(Path parent) {
		String name = parent.getFileName().toString();
		for (char ch : name.toCharArray()) {
			if (!Character.isDigit(ch)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Deletes the old patient folders after all of the dicom images have been
	 * organized
	 * 
	 * @param sourceFolder - Main directory path
	 * @throws IOException
	 */
	public void deletePatientFolders(String sourceFolder) throws IOException {
		Files.walk(Paths.get(sourceFolder)).forEach(file -> {
			if (file.getFileName().toString().contains("ProstateX")) {
				String selectedFolder = file.toString();
				try {
					deleteFolder(selectedFolder);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

}
