package com.gemserk.tools.animationeditor.core;

import java.util.ArrayList;

public interface Node {

	float getX();

	float getY();

	float getAngle();

	Node getParent();

	void setPosition(float x, float y);

	void setAngle(float angle);

	void setParent(Node node);

	ArrayList<Node> getChildren();

	float getLocalX(float x, float y);

	float getLocalY(float x, float y);
	
}
