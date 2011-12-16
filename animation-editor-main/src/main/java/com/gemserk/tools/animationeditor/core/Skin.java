package com.gemserk.tools.animationeditor.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.gemserk.commons.gdx.games.Spatial;
import com.gemserk.commons.gdx.games.SpatialImpl;

public class Skin {

	public static class SkinPatch {

		Joint joint;
		Sprite sprite;

		Spatial spatial;
		Vector2 center;

		public Joint getJoint() {
			return joint;
		}

		public Sprite getSprite() {
			return sprite;
		}

		public Spatial getSpatial() {
			return spatial;
		}

		SkinPatch(Joint joint, Sprite sprite) {
			this.joint = joint;
			this.sprite = sprite;
			this.spatial = new SpatialImpl(0f, 0f, sprite.getWidth(), sprite.getHeight(), 0f);
			this.center = new Vector2(0.5f, 0.5f);
		}

		void update() {

			float angle = spatial.getAngle() + joint.getAngle();
			sprite.setRotation(angle);

			float ox = spatial.getWidth() * center.x;
			float oy = spatial.getHeight() * center.y;

			if (ox != sprite.getOriginX() || oy != sprite.getOriginY())
				sprite.setOrigin(ox, oy);

			if (sprite.getWidth() != spatial.getWidth() || sprite.getHeight() != spatial.getHeight())
				sprite.setSize(spatial.getWidth(), spatial.getHeight());

			float x = spatial.getX() - sprite.getOriginX();
			float y = spatial.getY() - sprite.getOriginY();

			x += joint.getX();
			y += joint.getY();

			if (x != sprite.getX() || y != sprite.getY())
				sprite.setPosition(x, y);

			// SpriteUtils.centerOn(sprite, joint.getX(), joint.getY(), 0.5f, 0.5f);
			// sprite.setRotation(joint.getAngle());

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
