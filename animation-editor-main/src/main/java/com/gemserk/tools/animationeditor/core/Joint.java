package com.gemserk.tools.animationeditor.core;

import java.util.ArrayList;

/**
 * Concept of spatial node with location and angle.
 */
public interface Joint {

	String getId();

	void setId(String id);

	/**
	 * Returns the x coordinate of the center of the Node.
	 */
	float getX();

	/**
	 * Returns the y coordinate of the center of the Node.
	 */
	float getY();

	float getAngle();

	Joint getParent();

	void setPosition(float x, float y);

	void setLocalPosition(float x, float y);

	void setAngle(float angle);

	void setLocalAngle(float angle);

	void setParent(Joint joint);

	ArrayList<Joint> getChildren();

	/**
	 * Returns the x coordinate of the specified position in local to the Node coordinates.
	 */
	float projectX(float x, float y);

	/**
	 * Returns the y coordinate of the specified position in local to the Node coordinates.
	 */
	float projectY(float x, float y);

	float getLocalX();

	float getLocalY();

	float getLocalAngle();
	
	Joint getChild(String id);

}
