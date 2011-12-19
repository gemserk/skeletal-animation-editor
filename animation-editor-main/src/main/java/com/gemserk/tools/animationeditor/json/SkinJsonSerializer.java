package com.gemserk.tools.animationeditor.json;

import java.lang.reflect.Type;

import com.gemserk.tools.animationeditor.core.Skin;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class SkinJsonSerializer implements JsonSerializer<Skin> {
	@Override
	public JsonElement serialize(Skin skin, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.add("patches", context.serialize(skin.getPatches()));
		return jsonObject;
	}
}