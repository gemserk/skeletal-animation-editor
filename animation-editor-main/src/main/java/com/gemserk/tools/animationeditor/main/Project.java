package com.gemserk.tools.animationeditor.main;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;

import com.gemserk.commons.files.FileUtils;

public class Project {

	public static final String PROJECT_EXTENSION = "aprj";
	public static final String SKELETON_EXTENSION = "skeleton";
	public static final String SKIN_EXTENSION = "skin";
	public static final String ANIMATIONS_EXTENSION = "animations";

	private String projectFile;
	private Map<String, String> texturePaths = new HashMap<String, String>();

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
	
	public Map<String, String> getAbsoluteTexturePaths() {
		HashMap<String, String> textureAbsolutePaths = new HashMap<String, String>();
		
		Set<String> keySet = texturePaths.keySet();
		
		String basePath = FilenameUtils.getFullPath(projectFile);
		
		for (String textureId : keySet) {
			String texturePath = texturePaths.get(textureId);
			if (!FileUtils.isAbsolutePath(texturePath))
				texturePath = basePath + texturePath;
			textureAbsolutePaths.put(textureId, texturePath);
		}
		
		return textureAbsolutePaths;
	}

	public void setTextureAbsolutePaths(Map<String, String> texturePaths) {
		this.texturePaths.clear();
		Set<String> keySet = texturePaths.keySet();
		String basePath = FilenameUtils.getFullPath(projectFile);
		for (String textureId : keySet) {
			String textureAbsolutePath = texturePaths.get(textureId);
			if (FileUtils.isSubPath(basePath, textureAbsolutePath)) {
				textureAbsolutePath = FileUtils.removeSubPath(basePath, textureAbsolutePath);
			}
			this.texturePaths.put(textureId, textureAbsolutePath);
		}
	}
}
