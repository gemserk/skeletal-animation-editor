package com.gemserk.tools.animationeditor.core.tree;

import com.gemserk.tools.animationeditor.core.Animation;
import com.gemserk.tools.animationeditor.core.AnimationKeyFrame;
import com.gemserk.tools.animationeditor.core.NodeUtils;

public class AnimationEditorImpl implements AnimationEditor {
	
	protected TreeEditor treeEditor;
	
	int index = 0;
	Animation currentAnimation = new Animation();
	AnimationKeyFrame selectedKeyFrame;

	public AnimationEditorImpl(TreeEditor treeEditor) {
		this.treeEditor = treeEditor;
	}

	@Override
	public AnimationKeyFrame addKeyFrame() {
		String name = "keyFrame" + index++;
		AnimationKeyFrame keyFrame = new AnimationKeyFrame(name, 
				NodeUtils.cloneTree(treeEditor.getRoot()));
		currentAnimation.getKeyFrames().add(keyFrame);
		return keyFrame;
	}

	@Override
	public void selectKeyFrame(AnimationKeyFrame keyFrame) {
		selectedKeyFrame = keyFrame;
	}

	@Override
	public void removeKeyFrame() {
		if (selectedKeyFrame != null)
			currentAnimation.getKeyFrames().remove(selectedKeyFrame);
	}

	@Override
	public Animation getCurrentAnimation() {
		return currentAnimation;
	}
	
}
