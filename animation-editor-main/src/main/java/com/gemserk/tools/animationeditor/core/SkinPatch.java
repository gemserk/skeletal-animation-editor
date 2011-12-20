package com.gemserk.tools.animationeditor.core;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.gemserk.resources.Resource;

public class SkinPatch {

	Joint joint;
	// Sprite sprite;

	Resource<Sprite> sprite;

	public float angle;
	public Vector2 center = new Vector2(0.5f, 0.5f);

	// path to the texture file
	public String textureId;

	public Joint getJoint() {
		return joint;
	}

	public Sprite getSprite() {
		return sprite.get();
	}

	public SkinPatch(Joint joint, Resource<Sprite> sprite, String textureId) {
		this.joint = joint;
		this.sprite = sprite;
		this.textureId = textureId;
	}

	public void update() {
		float angle = this.angle + joint.getAngle();
		
		Sprite sprite = this.sprite.get();

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
		Sprite sprite = this.sprite.get();
		
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

	@Override
	public String toString() {
		return "SkinPatch [id:" + joint.getId() + ", textureId:" + textureId + ", center:" + center.toString() + ", angle:" + angle + "]";
	}

}