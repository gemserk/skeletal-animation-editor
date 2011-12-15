package com.gemserk.tools.animationeditor.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.gemserk.commons.gdx.graphics.SpriteUtils;

public class Skin {

	static class SkinPatch {
		
		Joint joint;
		Sprite sprite;

		Joint getJoint() {
			return joint;
		}

		Sprite getSprite() {
			return sprite;
		}

		SkinPatch(Joint joint, Sprite sprite) {
			this.joint = joint;
			this.sprite = sprite;
		}
		
		void update() {
			SpriteUtils.centerOn(sprite, joint.getX(), joint.getY(), 0.5f, 0.5f);
			sprite.setRotation(joint.getAngle());
		}
		
	}
	
	Map<String, SkinPatch> patches;
	
	public Skin() {
		patches = new HashMap<String, Skin.SkinPatch>();
	}
	
	public void addPatch(Joint joint, Sprite sprite) {
		patches.put(joint.getId(), new SkinPatch(joint, sprite));
	}
	
	public void removePatch(Joint joint) {
		patches.remove(joint.getId());
	}
	
	public void update() {
		Set<String> keySet = patches.keySet();
		for (String jointId : keySet) {
			SkinPatch skinPatch = patches.get(jointId);
			skinPatch.update();
		}
	}

}
