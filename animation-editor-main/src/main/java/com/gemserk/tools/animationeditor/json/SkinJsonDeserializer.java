package com.gemserk.tools.animationeditor.json;

import java.lang.reflect.Type;
import java.util.Map;

import com.gemserk.tools.animationeditor.core.Skin;
import com.gemserk.tools.animationeditor.core.SkinPatch;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class SkinJsonDeserializer implements JsonDeserializer<Skin> {

	Type patchesType = new TypeToken<Map<String, SkinPatch>>() {
	}.getType();

	@Override
	public Skin deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		Skin skin = new Skin();

		JsonObject jsonObject = json.getAsJsonObject();

		Map<String, SkinPatch> patches = context.deserialize(jsonObject.get("patches"), patchesType);

		for (String jointId : patches.keySet()) {
			SkinPatch skinPatch = patches.get(jointId);
			skin.getPatches().put(jointId, skinPatch);
			skin.getPatchList().add(skinPatch);
		}

		return skin;
	}
}