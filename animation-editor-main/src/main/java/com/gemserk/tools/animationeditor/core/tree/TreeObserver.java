package com.gemserk.tools.animationeditor.core.tree;

import com.gemserk.tools.animationeditor.core.Node;

public interface TreeObserver {
	
	void select(Node node);
	
	void remove(Node node);
	
	void add(Node node);

}
