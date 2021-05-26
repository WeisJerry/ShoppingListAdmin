package shoppinglist.testutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileUtils {
	
	/**
	 * Retrieve contents of a specified file, optionally
	 * preserving the newline character
	 * @param name
	 * @param includeReturns
	 * @return
	 */
	public static String getFile(String name, boolean includeReturns) {
		String newLine = System.lineSeparator();
		StringBuilder buffer = new StringBuilder();
		
		//verify file exists
		File file = new File(name);
		if (file.exists()) {
			
			BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader(name));
				String line = reader.readLine();
				while (line != null) {
					buffer.append(line); 
					if (includeReturns) {
						buffer.append(newLine);
					}
					line = reader.readLine();
				}
				reader.close();
			}
			catch(IOException ex) {
				
				buffer.delete(0, buffer.length());
			}
		}
		return buffer.toString();
	}

}
