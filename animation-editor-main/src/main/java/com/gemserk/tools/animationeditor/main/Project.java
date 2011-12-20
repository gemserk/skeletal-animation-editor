package com.gemserk.tools.animationeditor.main;

import java.util.HashMap;
import java.util.Map;

public class Project {

	public static final String PROJECT_EXTENSION = "aprj";
	public static final String SKELETON_EXTENSION = "skeleton";
	public static final String SKIN_EXTENSION = "skin";
	public static final String ANIMATIONS_EXTENSION = "animations";

	public String skeletonFile;
	public String skinFile;
	public String projectFile;
	public String animationsFile;
	
	public Map<String, String> texturePaths = new HashMap<String, String>();

	public Project(String projectName) {
		projectFile = projectName + "." + PROJECT_EXTENSION;
		skeletonFile = projectName + "." + SKELETON_EXTENSION;
		skinFile = projectName + "." + SKIN_EXTENSION;
		animationsFile = projectName + "." + ANIMATIONS_EXTENSION;
	}

}
