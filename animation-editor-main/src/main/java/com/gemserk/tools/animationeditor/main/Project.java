package com.gemserk.tools.animationeditor.main;

import org.apache.commons.io.FilenameUtils;
import java.util.HashMap;
import java.util.Map;

public class Project {

	public static final String PROJECT_EXTENSION = "aprj";
	public static final String SKELETON_EXTENSION = "skeleton";
	public static final String SKIN_EXTENSION = "skin";
	public static final String ANIMATIONS_EXTENSION = "animations";

	private String projectFile;
	public Map<String, String> texturePaths = new HashMap<String, String>();

	public Project(String projectPath) {
		setProjectFile(projectPath);
	}

	public String getSkeletonFile() {
		String baseName = FilenameUtils.getBaseName(getProjectFile());
		String basePath = FilenameUtils.getFullPath(getProjectFile());
		return FilenameUtils.concat(basePath, baseName + "." + SKELETON_EXTENSION);
	}

	public String getSkinFile() {
		String baseName = FilenameUtils.getBaseName(getProjectFile());
		String basePath = FilenameUtils.getFullPath(getProjectFile());
		return FilenameUtils.concat(basePath, baseName + "." + SKIN_EXTENSION);
	}

	public String getAnimationsFile() {
		String baseName = FilenameUtils.getBaseName(getProjectFile());
		String basePath = FilenameUtils.getFullPath(getProjectFile());
		return FilenameUtils.concat(basePath, baseName + "." + ANIMATIONS_EXTENSION);
	}

	public void setProjectFile(String projectFile) {
		projectFile = FilenameUtils.normalize(projectFile);

		String baseName = FilenameUtils.getBaseName(projectFile);
		String basePath = FilenameUtils.getFullPath(projectFile);

		this.projectFile = basePath + baseName + "." + PROJECT_EXTENSION;
	}

	public String getProjectFile() {
		return projectFile;
	}
}
