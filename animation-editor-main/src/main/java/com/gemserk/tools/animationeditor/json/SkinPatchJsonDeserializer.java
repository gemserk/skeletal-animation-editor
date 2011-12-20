package com.gemserk.tools.animationeditor.json;

import java.lang.reflect.Type;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.gemserk.commons.gdx.resources.LibgdxResourceBuilder;
import com.gemserk.resources.Resource;
import com.gemserk.resources.ResourceManager;
import com.gemserk.tools.animationeditor.core.Skeleton;
import com.gemserk.tools.animationeditor.core.SkinPatch;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class SkinPatchJsonDeserializer implements JsonDeserializer<SkinPatch> {

	Skeleton skeleton;
	ResourceManager<String> resourceManager;

	public void setSkeleton(Skeleton skeleton) {
		this.skeleton = skeleton;
	}

	public void setResourceManager(ResourceManager<String> resourceManager) {
		this.resourceManager = resourceManager;
	}

	@Override
	public SkinPatch deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();

		String jointId = jsonObject.get("jointId").getAsString();
		String textureId = jsonObject.get("textureId").getAsString();

		Float angle = jsonObject.get("angle").getAsFloat();
		Float cx = jsonObject.get("cx").getAsFloat();
		Float cy = jsonObject.get("cy").getAsFloat();

		Resource<Sprite> resource = resourceManager.get(textureId);

		if (resource == null) {
			LibgdxResourceBuilder resourceBuilder = new LibgdxResourceBuilder(resourceManager);
			resourceBuilder.resource(textureId + ".Texture", resourceBuilder.texture2(Gdx.files.absolute(textureId)) //
					.minFilter(TextureFilter.Linear) //
					.magFilter(TextureFilter.Linear) //
					);
			resourceBuilder.sprite(textureId, textureId + ".Texture");
			resource = resourceManager.get(textureId);
		}

		SkinPatch skinPatch = new SkinPatch(jointId, textureId);

		skinPatch.angle = angle.floatValue();
		skinPatch.center.set(cx.floatValue(), cy.floatValue());

		return skinPatch;
	}
}
