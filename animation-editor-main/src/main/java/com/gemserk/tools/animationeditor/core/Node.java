package com.gemserk.tools.animationeditor.core;

import java.util.ArrayList;

public interface Node {
	
	float getX();
	
	float getY();
	
	float getAngle();
	
	void setPosition(float x, float y);
	
	void setParent(Node node);
	
	Node getParent();
	
	ArrayList<Node> getChildren();

}
