package com.gemserk.tools.animationeditor.json;

import java.lang.reflect.Type;

import com.gemserk.tools.animationeditor.core.JointImpl;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class JointJsonSerializer implements JsonSerializer<JointImpl> {

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