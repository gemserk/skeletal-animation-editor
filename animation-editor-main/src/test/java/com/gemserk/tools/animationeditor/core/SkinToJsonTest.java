package com.gemserk.tools.animationeditor.core;

import java.lang.reflect.Type;
import java.util.Map;

import org.junit.Test;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.gemserk.tools.animationeditor.core.Skin.SkinPatch;
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

public class SkinToJsonTest {

	public static class SkinPatchJsonSerializer implements JsonSerializer<SkinPatch> {
		@Override
		public JsonElement serialize(SkinPatch skinPatch, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject jsonObject = new JsonObject();

			jsonObject.addProperty("jointId", skinPatch.getJoint().getId());
			jsonObject.addProperty("textureId", skinPatch.textureId);

			jsonObject.addProperty("angle", skinPatch.angle);
			jsonObject.addProperty("cx", skinPatch.center.x);
			jsonObject.addProperty("cy", skinPatch.center.y);

			return jsonObject;
		}
	}

	public static class SkinPatchJsonDeserializer implements JsonDeserializer<SkinPatch> {

		Skeleton skeleton;

		public SkinPatchJsonDeserializer(Skeleton skeleton) {
			this.skeleton = skeleton;
		}

		@Override
		public SkinPatch deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject jsonObject = json.getAsJsonObject();

			String jointId = jsonObject.get("jointId").getAsString();
			String textureId = jsonObject.get("textureId").getAsString();

			Float angle = jsonObject.get("angle").getAsFloat();
			Float cx = jsonObject.get("cx").getAsFloat();
			Float cy = jsonObject.get("cy").getAsFloat();

			SkinPatch skinPatch = new SkinPatch(skeleton.getRoot().find(jointId), null, textureId);

			skinPatch.angle = angle.floatValue();
			skinPatch.center.set(cx.floatValue(), cy.floatValue());

			return skinPatch;
		}
	}

	public static class SkinJsonSerializer implements JsonSerializer<Skin> {
		@Override
		public JsonElement serialize(Skin skin, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.add("patches", context.serialize(skin.patches));
			return jsonObject;
		}
	}

	public static class SkinJsonDeserializer implements JsonDeserializer<Skin> {

		Type patchesType = new TypeToken<Map<String, SkinPatch>>() {
		}.getType();

		@Override
		public Skin deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			Skin skin = new Skin();

			JsonObject jsonObject = json.getAsJsonObject();

			Map<String, SkinPatch> patches = context.deserialize(jsonObject.get("patches"), patchesType);

			for (String jointId : patches.keySet()) {
				SkinPatch skinPatch = patches.get(jointId);

				skin.patches.put(jointId, skinPatch);
				skin.patchList.add(skinPatch);
			}

			return skin;
		}
	}

	static class MockSprite extends Sprite {

	}

	@Test
	public void exportSkeletonTest() {
		Skeleton skeleton = new Skeleton();

		JointImpl root = new JointImpl("root", 30f, 50f, 0f);
		JointImpl child = new JointImpl("leg", 30f, 50f, 0f);

		child.setParent(root);

		skeleton.setRoot(root);

		Skin skin = new Skin();
		skin.addPatch(root, new MockSprite(), "/tmp/body.png");
		skin.addPatch(child, new MockSprite(), "/tmp/leg.png");

		Gson gson = new GsonBuilder() //
				.registerTypeAdapter(SkinPatch.class, new SkinPatchJsonSerializer()) //
				.registerTypeAdapter(Skin.class, new SkinJsonSerializer()) //
				.registerTypeAdapter(Skin.class, new SkinJsonDeserializer()) //
				.registerTypeAdapter(SkinPatch.class, new SkinPatchJsonDeserializer(skeleton)) //
				.setPrettyPrinting() //
				.create();

		String json = gson.toJson(skin);

		System.out.println(json);

		Skin newSkin = gson.fromJson(json, Skin.class);

		System.out.println("=======================");
		
		System.out.println(newSkin.patches.toString());
		

	}

}
