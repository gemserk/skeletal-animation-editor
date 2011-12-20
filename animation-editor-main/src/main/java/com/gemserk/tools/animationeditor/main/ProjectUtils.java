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

import com.gemserk.tools.animationeditor.core.Joint;
import com.gemserk.tools.animationeditor.core.JointImpl;
import com.gemserk.tools.animationeditor.core.Skeleton;
import com.gemserk.tools.animationeditor.core.SkeletonAnimation;
import com.gemserk.tools.animationeditor.core.Skin;
import com.gemserk.tools.animationeditor.core.SkinPatch;
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
		String animationsFile = project.getAnimationsFile();
		Type animationsListType = new TypeToken<List<SkeletonAnimation>>() {
		}.getType();
		Gson gson = new GsonBuilder() //
				.setPrettyPrinting() //
				.create();
		return gson.fromJson(new FileReader(animationsFile), animationsListType);
	}

	public static Skeleton loadSkeleton(Project project) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		String skeletonFile = project.getSkeletonFile();
		Gson gson = new GsonBuilder() //
				.registerTypeAdapter(Joint.class, new JointJsonDeserializer()) //
				.setPrettyPrinting() //
				.create();
		return gson.fromJson(new FileReader(skeletonFile), Skeleton.class);
	}

	public static Skin loadSkin(Project project) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		String skinFile = project.getSkinFile();
		Gson gson = new GsonBuilder() //
				.registerTypeAdapter(Skin.class, new SkinJsonDeserializer()) //
				.registerTypeAdapter(SkinPatch.class, new SkinPatchJsonDeserializer()) //
				.setPrettyPrinting() //
				.create();
		return gson.fromJson(new FileReader(skinFile), Skin.class);
	}

	public static void saveProject(Project project) {
		try {
			Gson gson = new GsonBuilder() //
					.setPrettyPrinting() //
					.create();
			FileWriter writer = new FileWriter(new File(project.getProjectFile()));

			gson.toJson(project, writer);

			writer.flush();
			writer.close();

			logger.info("Project file saved to " + project.getProjectFile());
		} catch (IOException e1) {
			logger.error("Failed to save project file to " + project.getProjectFile(), e1);
		}
	}

	public static void saveSkeleton(Project project, Skeleton skeleton) {
		String skeletonFile = project.getSkeletonFile();
		try {
			Gson gson = new GsonBuilder() //
					.registerTypeAdapter(JointImpl.class, new JointJsonSerializer()) //
					.setPrettyPrinting() //
					.create();
			FileWriter writer = new FileWriter(new File(skeletonFile));

			gson.toJson(skeleton, writer);

			writer.flush();
			writer.close();

			logger.info("Skeleton saved to " + skeletonFile);
		} catch (IOException e1) {
			logger.error("Failed to save skeleton file to " + skeletonFile, e1);
		}
	}

	public static void saveSkin(Project project, Skin skin) {
		String skinFile = project.getSkinFile();
		try {
			Gson gson = new GsonBuilder() //
					.registerTypeAdapter(SkinPatch.class, new SkinPatchJsonSerializer()) //
					.registerTypeAdapter(Skin.class, new SkinJsonSerializer()) //
					.setPrettyPrinting() //
					.create();

			FileWriter writer = new FileWriter(new File(skinFile));

			gson.toJson(skin, writer);

			writer.flush();
			writer.close();

			logger.info("Skin saved to " + skinFile);
		} catch (IOException e1) {
			logger.error("Failed to save skin file to " + skinFile, e1);
		}
	}

	public static void saveAnimations(Project project, List<SkeletonAnimation> animations) {
		String animationsFile = project.getAnimationsFile();
		try {
			Gson gson = new GsonBuilder() //
					.setPrettyPrinting() //
					.create();

			FileWriter writer = new FileWriter(new File(animationsFile));

			gson.toJson(animations, writer);

			writer.flush();
			writer.close();

			logger.info("Animations saved to " + animationsFile);
		} catch (IOException e1) {
			logger.error("Failed to save animations file to " + animationsFile, e1);
		}
	}

}
