package com.gemserk.tools.animationeditor.core;

import java.lang.reflect.Type;

import org.junit.Test;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.gemserk.tools.animationeditor.core.Skin.SkinPatch;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class SkinToJsonTest {

	public static class SkinPatchJsonSerializer implements JsonSerializer<SkinPatch> {
		@Override
		public JsonElement serialize(SkinPatch skinPatch, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject jsonObject = new JsonObject();
			
			jsonObject.addProperty("jointId", skinPatch.getJoint().getId());
			jsonObject.addProperty("textureId", skinPatch.filePath);
			
			jsonObject.addProperty("angle", skinPatch.angle);
			jsonObject.addProperty("cx", skinPatch.center.x);
			jsonObject.addProperty("cy", skinPatch.center.y);
			
			return jsonObject;
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

	static class MockSprite extends Sprite {

	}

	@Test
	public void exportSkeletonTest() {

		Skin skin = new Skin();
		skin.addPatch(new JointImpl("root", 30f, 50f, 0f), new MockSprite(), "/tmp/body.png");
		skin.addPatch(new JointImpl("leg", 30f, 50f, 0f), new MockSprite(), "/tmp/leg.png");

		Gson gson = new GsonBuilder() //
				.registerTypeAdapter(SkinPatch.class, new SkinPatchJsonSerializer()) //
				.registerTypeAdapter(Skin.class, new SkinJsonSerializer()) //
				.setPrettyPrinting() //
				.create();
		
		String json = gson.toJson(skin);
		
		System.out.println(json);
	}

}
