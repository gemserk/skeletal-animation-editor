package com.gemserk.tools.animationeditor.core.tree;

import com.gemserk.tools.animationeditor.core.Joint;

public interface SkeletonEditor {
	
	void select(Joint joint);
	
	void remove(Joint joint);
	
	void add(Joint joint);
	
	Joint getRoot();
	
//	void setRoot(Joint root);
	
	Joint getNearestNode(float x, float y);
	
	boolean isSelectedNode(Joint joint);
	
	Joint getSelectedNode();

	void moveSelected(float dx, float dy);
	
	void rotateSelected(float angle);
	
}
