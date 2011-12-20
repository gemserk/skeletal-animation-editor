package com.gemserk.tools.animationeditor.core;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

import com.gemserk.tools.animationeditor.main.Project;

public class FileNameTest {

	@Test
	public void test() {
		
		String projectFile = "/tmp/coco/pedro.aprj";
		
		String baseName = FilenameUtils.getBaseName(projectFile);
		String basePath = FilenameUtils.getFullPath(projectFile);
		
		System.out.println(baseName);
		System.out.println(basePath);
		
		String skeletonFile = FilenameUtils.concat(basePath, baseName + "." + Project.SKELETON_EXTENSION);
		
		System.out.println(skeletonFile);

	}

}
