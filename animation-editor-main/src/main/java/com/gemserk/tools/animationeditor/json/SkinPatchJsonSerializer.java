package com.gemserk.tools.animationeditor.json;

import java.lang.reflect.Type;

import com.gemserk.tools.animationeditor.core.Skin.SkinPatch;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class SkinPatchJsonSerializer implements JsonSerializer<SkinPatch> {
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