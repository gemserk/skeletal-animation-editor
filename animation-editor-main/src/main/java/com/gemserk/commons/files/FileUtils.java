package com.gemserk.commons.files;

public class FileUtils {
	
	public static String getFileNameWithoutExtension(String file) {
		int lastIndexOf = file.lastIndexOf(".");
		if (lastIndexOf == -1)
			return file;
		return file.substring(0, lastIndexOf);
	}

}
