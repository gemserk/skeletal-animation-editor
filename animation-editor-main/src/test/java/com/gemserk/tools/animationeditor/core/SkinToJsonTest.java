package com.gemserk.tools.animationeditor.core;

import java.lang.reflect.Type;

import org.junit.Test;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.gemserk.tools.animationeditor.core.Skin.SkinPatch;
import com.gemserk.tools.animationeditor.json.SkinJsonDeserializer;
import com.gemserk.tools.animationeditor.json.SkinJsonSerializer;
import com.gemserk.tools.animationeditor.json.SkinPatchJsonSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class SkinToJsonTest {

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

		SkinPatchJsonDeserializer skinPatchDeserializer = new SkinPatchJsonDeserializer(skeleton);

		Gson gson = new GsonBuilder() //
				.registerTypeAdapter(SkinPatch.class, new SkinPatchJsonSerializer()) //
				.registerTypeAdapter(Skin.class, new SkinJsonSerializer()) //
				.registerTypeAdapter(Skin.class, new SkinJsonDeserializer()) //
				.registerTypeAdapter(SkinPatch.class, skinPatchDeserializer) //
				.setPrettyPrinting() //
				.create();

		String json = gson.toJson(skin);

		System.out.println(json);

		Skin newSkin = gson.fromJson(json, Skin.class);

		System.out.println("=======================");

		System.out.println(newSkin.patches.toString());
	}

}
