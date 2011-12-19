package com.gemserk.tools.animationeditor.main;

public class Project {

	public static final String PROJECT_EXTENSION = "aprj";
	public static final String SKELETON_EXTENSION = "skeleton";
	public static final String SKIN_EXTENSION = "skin";

	public String skeletonFile;
	public String skinFile;
	public String projectFile;

	public Project(String projectName) {
		projectFile = projectName + "." + PROJECT_EXTENSION;
		skeletonFile = projectName + "." + SKELETON_EXTENSION;
		skinFile = projectName + "." + SKIN_EXTENSION;
	}

}
