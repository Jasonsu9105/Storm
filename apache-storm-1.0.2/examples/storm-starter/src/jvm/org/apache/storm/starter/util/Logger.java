package org.apache.storm.starter.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Logger {

	public static void log(String fileName, String input) {
		File file = new File(fileName);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter writer = new FileWriter(file, true); 
			writer.write(input + "\n"); 
			//writer.write(new Date().getTime() + "\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
