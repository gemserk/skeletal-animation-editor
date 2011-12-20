package com.gemserk.tools.animationeditor.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.gemserk.resources.Resource;

public class Skin {

	Map<String, SkinPatch> patches;
	ArrayList<SkinPatch> patchList;

	public Map<String, SkinPatch> getPatches() {
		return patches;
	}

	public ArrayList<SkinPatch> getPatchList() {
		return patchList;
	}

	public Skin() {
		patches = new HashMap<String, SkinPatch>();
		patchList = new ArrayList<SkinPatch>();
	}

	public void addPatch(Joint joint, Resource<Sprite> sprite, String textureId) {
		SkinPatch patch = new SkinPatch(joint, sprite, textureId);

		if (patches.containsKey(joint.getId())) {
			SkinPatch previousPatch = patches.get(joint.getId());
			patchList.remove(previousPatch);
		}

		patches.put(joint.getId(), patch);
		patchList.add(patch);
	}

	public void removePatch(Joint joint) {
		ArrayList<Joint> joints = JointUtils.toArrayList(joint);
		for (int i = 0; i < joints.size(); i++) {
			Joint j = joints.get(i);
			SkinPatch patch = patches.remove(j.getId());
			patchList.remove(patch);
		}
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

	public SkinPatch getPatch(Joint joint) {
		return getPatch(joint.getId());
	}
}
