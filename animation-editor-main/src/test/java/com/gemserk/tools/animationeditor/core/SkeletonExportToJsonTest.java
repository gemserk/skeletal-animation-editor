package com.gemserk.tools.animationeditor.core;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

public class SkeletonExportToJsonTest {

	static class JointDeserializer implements JsonDeserializer<Joint> {

		Type childrenType = new TypeToken<ArrayList<Joint>>() {
		}.getType();

		@Override
		public Joint deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JointImpl joint = new JointImpl();
			JsonObject jsonObject = json.getAsJsonObject();
			joint.setId(jsonObject.get("id").getAsString());
			joint.setPosition(jsonObject.get("x").getAsFloat(), jsonObject.get("y").getAsFloat());
			joint.setAngle(jsonObject.get("angle").getAsFloat());

			ArrayList<Joint> children = context.deserialize(jsonObject.get("children").getAsJsonArray(), childrenType);

			for (Joint child : children) 
				child.setParent(joint);

			return joint;
		}

	}

	static class JointSerializer implements JsonSerializer<JointImpl> {
		@Override
		public JsonElement serialize(JointImpl joint, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("id", joint.getId());
			jsonObject.addProperty("x", joint.getX());
			jsonObject.addProperty("y", joint.getY());
			jsonObject.addProperty("angle", joint.getAngle());
			jsonObject.add("children", context.serialize(joint.getChildren()));
			return jsonObject;
		}
	}

	@Test
	public void exportSkeletonTest() {

		Skeleton skeleton = new Skeleton();
		skeleton.setRoot(new JointImpl("root", 50f, 75f, 10f));

		JointImpl child1 = new JointImpl("toto1", 75f, 40f, 0f);
		child1.setParent(skeleton.getRoot());

		Gson gson = new GsonBuilder() //
				.registerTypeAdapter(JointImpl.class, new JointSerializer()) //
				.registerTypeAdapter(Joint.class, new JointDeserializer()) //
				.setPrettyPrinting() //
				.create();

		// System.out.println(skeleton.getRoot().getChildren());

		String json = gson.toJson(skeleton);

		System.out.println(json);

		Skeleton newSkeleton = gson.fromJson(json, Skeleton.class);

		//
		Joint newRoot = newSkeleton.getRoot();
		//
		sysout(newRoot);
		sysout(newRoot.getChildren().get(0));
		// sysout(newRoot.getChildren().get(1));
	}

	private void sysout(Joint joint) {
		System.out.println(joint.getId());
		if (!"".equals(joint.getParent().getId()))
			System.out.println(joint.getParent().getId());
		System.out.println(joint.getX());
		System.out.println(joint.getY());
		System.out.println(joint.getAngle());
		System.out.println(joint.getChildren().size());
	}
}
