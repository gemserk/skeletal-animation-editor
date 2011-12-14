package com.gemserk.tools.animationeditor.main.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import com.gemserk.tools.animationeditor.core.Joint;

public class TreeNodeForJointImpl extends DefaultMutableTreeNode {
	
	private static final long serialVersionUID = -5346981181496467679L;
	
	Joint joint;
	
	public Joint getNode() {
		return joint;
	}

	public TreeNodeForJointImpl(Joint joint) {
		super(joint);
		this.joint = joint;
	}
	
	@Override
	public void setUserObject(Object userObject) {
		super.setUserObject(userObject);
		if (userObject instanceof String) 
			joint.setId((String) userObject);
	}
	
	@Override
	public Object getUserObject() {
		return super.getUserObject();
	}

}
