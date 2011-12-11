package com.gemserk.tools.animationeditor.main;

import javax.swing.tree.DefaultMutableTreeNode;

import com.gemserk.tools.animationeditor.core.Node;

public class TreeNodeEditorImpl extends DefaultMutableTreeNode {
	
	private static final long serialVersionUID = -5346981181496467679L;
	
	Node node;
	
	public Node getNode() {
		return node;
	}

	public TreeNodeEditorImpl(Node node) {
		super(node);
		this.node = node;
	}
	
	@Override
	public void setUserObject(Object userObject) {
		super.setUserObject(userObject);
		if (userObject instanceof String) 
			node.setId((String) userObject);
	}
	
	@Override
	public Object getUserObject() {
		return super.getUserObject();
	}

}
