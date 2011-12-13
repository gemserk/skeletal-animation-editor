package com.gemserk.tools.animationeditor.core.tree;

import com.gemserk.tools.animationeditor.core.Animation;
import com.gemserk.tools.animationeditor.core.AnimationKeyFrame;

public interface AnimationEditor {
	
	Animation getCurrentAnimation();
	
	AnimationKeyFrame addKeyFrame();
	
	void selectKeyFrame(AnimationKeyFrame keyFrame);
	
	void removeKeyFrame();

}
