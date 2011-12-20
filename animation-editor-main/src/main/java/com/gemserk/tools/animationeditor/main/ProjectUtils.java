package com.gemserk.tools.animationeditor.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gemserk.resources.ResourceManager;
import com.gemserk.tools.animationeditor.core.Joint;
import com.gemserk.tools.animationeditor.core.JointImpl;
import com.gemserk.tools.animationeditor.core.Skeleton;
import com.gemserk.tools.animationeditor.core.SkeletonAnimation;
import com.gemserk.tools.animationeditor.core.Skin;
import com.gemserk.tools.animationeditor.core.Skin.SkinPatch;
import com.gemserk.tools.animationeditor.json.JointJsonDeserializer;
import com.gemserk.tools.animationeditor.json.JointJsonSerializer;
import com.gemserk.tools.animationeditor.json.SkinJsonDeserializer;
import com.gemserk.tools.animationeditor.json.SkinJsonSerializer;
import com.gemserk.tools.animationeditor.json.SkinPatchJsonDeserializer;
import com.gemserk.tools.animationeditor.json.SkinPatchJsonSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class ProjectUtils {
	
	protected static final Logger logger = LoggerFactory.getLogger(ProjectUtils.class);
	
	public static List<SkeletonAnimation> loadAnimations(Project project) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		Type animationsListType = new TypeToken<List<SkeletonAnimation>>() {
		}.getType();
		Gson gson = new GsonBuilder() //
				.setPrettyPrinting() //
				.create();
		return gson.fromJson(new FileReader(project.animationsFile), animationsListType);
	}

	public static Skeleton loadSkeleton(Project project) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		Gson gson = new GsonBuilder() //
				.registerTypeAdapter(Joint.class, new JointJsonDeserializer()) //
				.setPrettyPrinting() //
				.create();
		return gson.fromJson(new FileReader(project.skeletonFile), Skeleton.class);
	}

	public static Skin loadSkin(Project project, Skeleton skeleton, ResourceManager<String> resourceManager) throws JsonSyntaxException, JsonIOException, FileNotFoundException {

		SkinPatchJsonDeserializer skinPatchJsonDeserializer = new SkinPatchJsonDeserializer();

		skinPatchJsonDeserializer.setSkeleton(skeleton);
		skinPatchJsonDeserializer.setResourceManager(resourceManager);

		Gson gson = new GsonBuilder() //
				.registerTypeAdapter(Skin.class, new SkinJsonDeserializer()) //
				.registerTypeAdapter(SkinPatch.class, skinPatchJsonDeserializer) //
				.setPrettyPrinting() //
				.create();

		return gson.fromJson(new FileReader(project.skinFile), Skin.class);
	}
	
	public static void saveProject(Project project) {
		try {
			Gson gson = new GsonBuilder() //
					.setPrettyPrinting() //
					.create();
			FileWriter writer = new FileWriter(new File(project.projectFile));

			gson.toJson(project, writer);

			writer.flush();
			writer.close();

			logger.info("Project file saved to " + project.projectFile);
		} catch (IOException e1) {
			logger.error("Failed to save project file to " + project.projectFile, e1);
		}
	}
	
	public static void saveSkeleton(Project project, Skeleton skeleton) {
		try {
			Gson gson = new GsonBuilder() //
					.registerTypeAdapter(JointImpl.class, new JointJsonSerializer()) //
					.setPrettyPrinting() //
					.create();
			FileWriter writer = new FileWriter(new File(project.skeletonFile));

			gson.toJson(skeleton, writer);

			writer.flush();
			writer.close();

			logger.info("Skeleton saved to " + project.skeletonFile);
		} catch (IOException e1) {
			logger.error("Failed to save skeleton file to " + project.skeletonFile, e1);
		}
	}
	
	public static void saveSkin(Project project, Skin skin) {
		try {
			Gson gson = new GsonBuilder() //
					.registerTypeAdapter(SkinPatch.class, new SkinPatchJsonSerializer()) //
					.registerTypeAdapter(Skin.class, new SkinJsonSerializer()) //
					.setPrettyPrinting() //
					.create();

			FileWriter writer = new FileWriter(new File(project.skinFile));

			gson.toJson(skin, writer);

			writer.flush();
			writer.close();

			logger.info("Skin saved to " + project.skinFile);
		} catch (IOException e1) {
			logger.error("Failed to save skin file to " + project.skinFile, e1);
		}
	}
	
	public static void saveAnimations(Project project, List<SkeletonAnimation> animations) {
		try {
			Gson gson = new GsonBuilder() //
					.setPrettyPrinting() //
					.create();

			FileWriter writer = new FileWriter(new File(project.animationsFile));

			gson.toJson(animations, writer);

			writer.flush();
			writer.close();

			logger.info("Animations saved to " + project.animationsFile);
		} catch (IOException e1) {
			logger.error("Failed to save animations file to " + project.animationsFile, e1);
		}
	}

}
