package com.gemserk.tools.animationeditor.core.tree;

import com.gemserk.tools.animationeditor.core.AnimationKeyFrame;
import com.gemserk.tools.animationeditor.core.Node;

public interface TreeEditor {
	
	void select(Node node);
	
	void remove(Node node);
	
	void add(Node node);
	
	Node getRoot();
	
	Node getNearestNode(float x, float y);
	
	boolean isSelectedNode(Node node);
	
	Node getSelectedNode();

	void moveSelected(float dx, float dy);
	
	void rotateSelected(float angle);
	
	AnimationKeyFrame addKeyFrame();
	
}
