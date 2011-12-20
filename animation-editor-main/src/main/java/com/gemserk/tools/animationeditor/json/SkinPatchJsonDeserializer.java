package com.gemserk.tools.animationeditor.json;

import java.lang.reflect.Type;

import com.gemserk.tools.animationeditor.core.SkinPatch;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class SkinPatchJsonDeserializer implements JsonDeserializer<SkinPatch> {

	@Override
	public SkinPatch deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();

		String jointId = jsonObject.get("jointId").getAsString();
		String textureId = jsonObject.get("textureId").getAsString();

		Float angle = jsonObject.get("angle").getAsFloat();
		Float cx = jsonObject.get("cx").getAsFloat();
		Float cy = jsonObject.get("cy").getAsFloat();

		SkinPatch skinPatch = new SkinPatch(jointId, textureId);

		skinPatch.angle = angle.floatValue();
		skinPatch.center.set(cx.floatValue(), cy.floatValue());

		return skinPatch;
	}
}
