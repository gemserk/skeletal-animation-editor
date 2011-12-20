package com.gemserk.tools.animationeditor.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


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

	public void addPatch(String jointId, String textureId) {
		SkinPatch patch = new SkinPatch(jointId, textureId);

		if (patches.containsKey(jointId)) {
			SkinPatch previousPatch = patches.get(jointId);
			patchList.remove(previousPatch);
		}

		patches.put(jointId, patch);
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
