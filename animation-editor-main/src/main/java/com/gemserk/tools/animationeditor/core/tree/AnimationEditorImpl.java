package com.gemserk.tools.animationeditor.core.tree;

import com.gemserk.tools.animationeditor.core.Animation;
import com.gemserk.tools.animationeditor.core.AnimationKeyFrame;
import com.gemserk.tools.animationeditor.core.Node;
import com.gemserk.tools.animationeditor.core.NodeUtils;

public class AnimationEditorImpl implements AnimationEditor {
	
	protected TreeEditor treeEditor;
	
	int index = 0;
	Animation currentAnimation = new Animation();
	AnimationKeyFrame selectedKeyFrame;
	
	float duration = 0f;

	public AnimationEditorImpl(TreeEditor treeEditor) {
		this.treeEditor = treeEditor;
	}

	@Override
	public AnimationKeyFrame addKeyFrame() {
		String name = "keyFrame" + index++;
		AnimationKeyFrame keyFrame = new AnimationKeyFrame(name, 
				NodeUtils.cloneTree(treeEditor.getRoot()), duration);
		currentAnimation.getKeyFrames().add(keyFrame);
		duration += 1f;
		return keyFrame;
	}

	@Override
	public void selectKeyFrame(AnimationKeyFrame keyFrame) {
		selectedKeyFrame = keyFrame;
		
		Node root = keyFrame.getRoot();
		
		treeEditor.setRoot(root);
		treeEditor.select(root);
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
	
	boolean playing = false;

	@Override
	public boolean isPlayingAnimation() {
		return playing;
	}

	@Override
	public void playAnimation() {
		playing = true;
	}
	
	@Override
	public void stopAnimation() {
		playing = false;
	}

	
}
