import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;


public class ImageQue {
	private LinkedList<String> imageQue;
	
	public ImageQue(){
		this.imageQue = new LinkedList<String>();
	}
	
	/**
	 * puts all the files into the que that are in the folder specified by path
	 * and returns the next file in the que
	 * @param the folder that you want to get images from
	 * @return the filename of the next image in images left to look at
	 */
	public String getNextFile(String path) {
		boolean foundFile = false;
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String file = listOfFiles[i].getName();
				if (file.endsWith(".jpg") || file.endsWith(".JPG")) {
					if (imageQue.size() == 0) {
						imageQue.add(file);
						this.imageQue.add(file);
						return path + "/" + file;
					} else {
						foundFile = false;
						for (int j = 0; j < imageQue.size(); j++) {
							if (imageQue.get(j).equals(file)) {
								foundFile = true;
							}
						}
						if (!foundFile) {
							this.imageQue.add(file);
							this.waitTillFileIsReady(path + "/" + file);
							return path + "/" + file;
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * if a file has just been copied into the folder this will wait until
	 * its fully copied before returning it as a file that can be checked
	 * @param the name of the file to be waited on
	 * @return returns true if the file is ready
	 */
	private boolean waitTillFileIsReady(String filename) {
		File fileToCopy = new File(filename);
		boolean fileReady = false;
		int sleepTime = 500; // Sleep 1 second
		while (!fileReady) {
			// Cannot write to file, windows still working on it
			// Sleep(sleepTime);
			try {
				Thread.sleep(sleepTime);
				FileReader test = new FileReader(fileToCopy);
				test.close();
				fileReady = true;
			} catch (Exception e) {
				System.out.println(e);
				waitTillFileIsReady(filename);
			}
		}
		return true;
	}

}
