package com.gemserk.tools.animationeditor.core;

import java.util.ArrayList;

/**
 * Concept of spatial node with location and angle.
 */
public interface Node {
	
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

	Node getParent();

	void setPosition(float x, float y);

	void setAngle(float angle);

	void setParent(Node node);

	ArrayList<Node> getChildren();

	/**
	 * Returns the x coordinate of the specified position in local to the Node coordinates.
	 */
	float getLocalX(float x, float y);

	/**
	 * Returns the y coordinate of the specified position in local to the Node coordinates.
	 */
	float getLocalY(float x, float y);
	
}
