package com.gemserk.tools.animationeditor.core;

import static org.junit.Assert.*;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

import com.gemserk.commons.files.FileUtils;
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
	
	@Test
	public void testCommonBasePath() {
		String projectFile = "/tmp/coco/pedro.aprj";
		String imageFile = "/tmp/coco/images/image.png";
		String imageFile2 = "/tmp/images/image.png";
		
		String projectFullPath = FilenameUtils.getFullPath(projectFile);
		String imageFullPath = FilenameUtils.getFullPath(imageFile);

		assertTrue(FileUtils.isSubPath(projectFullPath, imageFullPath));
		assertFalse(FileUtils.isSubPath(projectFullPath, FilenameUtils.getFullPath(imageFile2)));
	}
	

	@Test
	public void testRemovePath() {
		String projectFile = "/tmp/coco/pedro.aprj";
		String imageFile = "/tmp/coco/images/image.png";
		
		String projectFullPath = FilenameUtils.getFullPath(projectFile);

		assertEquals("images/image.png", FileUtils.removeSubPath(projectFullPath, imageFile));
	}
	
}
