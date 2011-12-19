package com.gemserk.tools.animationeditor.json;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.gemserk.tools.animationeditor.core.Joint;
import com.gemserk.tools.animationeditor.core.JointImpl;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class JointJsonDeserializer implements JsonDeserializer<Joint> {

	Type childrenType = new TypeToken<ArrayList<Joint>>() {}.getType();

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