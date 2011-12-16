package com.gemserk.tools.animationeditor.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Skin {

	public static class SkinPatch {

		Joint joint;
		Sprite sprite;

		public float angle;
		public Vector2 center;
		
		public Joint getJoint() {
			return joint;
		}

		public Sprite getSprite() {
			return sprite;
		}

		SkinPatch(Joint joint, Sprite sprite) {
			this.joint = joint;
			this.sprite = sprite;
			this.center = new Vector2(0.5f, 0.5f);
		}

		void update() {
			float angle = this.angle + joint.getAngle();

			sprite.setRotation(angle);

			float ox = sprite.getWidth() * center.x;
			float oy = sprite.getHeight() * center.y;

			if (ox != sprite.getOriginX() || oy != sprite.getOriginY())
				sprite.setOrigin(ox, oy);

			float x = -sprite.getOriginX();
			float y = -sprite.getOriginY();

			x += joint.getX();
			y += joint.getY();

			sprite.setPosition(x, y);
		}

		public void project(Vector2 position) {
			float centerX = sprite.getX() + sprite.getOriginX();
			float centerY = sprite.getY() + sprite.getOriginY();

			position.add(-centerX, -centerY);

			position.rotate(-sprite.getRotation());

			float scaleX = 1f;
			float scaleY = 1f;

			position.x *= scaleX;
			position.y *= scaleY;

			position.add( //
					sprite.getWidth() * 0.5f, //
					-sprite.getHeight() * 0.5f //
			);

			position.y *= -1f;
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
