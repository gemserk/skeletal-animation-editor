package com.gemserk.commons.files;

import java.io.File;

public class FileUtils {

	/**
	 * Returns true if subPath contains path.
	 */
	public static boolean isSubPath(String path, String subPath) {
		if (subPath.length() < path.length())
			return false;
		String subPathSubString = subPath.substring(0, path.length());
		return subPathSubString.equals(path);
	}
	
	public static boolean isAbsolutePath(String path) {
		return new File(path).isAbsolute();
	}

	/**
	 * Removes path from subpath and returns that.
	 */
	public static String removeSubPath(String path, String subPath) {
		return subPath.replace(path, "");
	}

}
