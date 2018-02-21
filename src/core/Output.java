package core;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import debug.Debug;

public class Output {

	public String filename;
	
	public Output(String filename) {
		this.filename = filename;
	}
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void writeToFile(String text) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(filename));
		}
		catch (Exception e) {
			Debug.printException(e);
		}
		
		try {
			out.write(text);
		}
		catch (Exception e) {
			Debug.printException(e);
		}
		finally {
			try {
				out.close();
			} catch (IOException e) {
				Debug.printException(e);
			}
		}
	}
	
}
