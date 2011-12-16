package com.gemserk.tools.animationeditor.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.gemserk.commons.gdx.graphics.SpriteUtils;

public class Skin {

	public static class SkinPatch {
		
		Joint joint;
		Sprite sprite;

		public Joint getJoint() {
			return joint;
		}

		public Sprite getSprite() {
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
	ArrayList<SkinPatch> patchList;
	
	public Skin() {
		patches = new HashMap<String, Skin.SkinPatch>();
		patchList = new ArrayList<Skin.SkinPatch>();
	}
	
	public void addPatch(Joint joint, Sprite sprite) {
		SkinPatch patch = new SkinPatch(joint, sprite);
		
		if (patches.containsKey(joint.getId())) {
			SkinPatch previousPatch = patches.get(joint.getId());
			patchList.remove(previousPatch);
		}
		
		patches.put(joint.getId(), patch);
		patchList.add(patch);
	}
	
	public void removePatch(Joint joint) {
		SkinPatch patch = patches.remove(joint.getId());
		patchList.remove(patch);
	}
	
	public void update() {
		Set<String> keySet = patches.keySet();
		for (String jointId : keySet) {
			SkinPatch skinPatch = patches.get(jointId);
			skinPatch.update();
		}
	}
	
	public int patchesCount() {
		return patchList.size();
	}
	
	public SkinPatch getPatch(int index) {
		return patchList.get(index);
	}
	
	public SkinPatch getPatch(String jointId) {
		return patches.get(jointId);
	}
	
}
