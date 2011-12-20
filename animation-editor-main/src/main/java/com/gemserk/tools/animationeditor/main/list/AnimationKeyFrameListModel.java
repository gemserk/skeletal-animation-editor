package com.gemserk.tools.animationeditor.main.list;

import java.util.ArrayList;

import javax.swing.AbstractListModel;

import com.gemserk.tools.animationeditor.core.SkeletonAnimationKeyFrame;

public class AnimationKeyFrameListModel extends AbstractListModel {

	private static final long serialVersionUID = 7156920082037501961L;

	public ArrayList<SkeletonAnimationKeyFrame> values;
	
	public AnimationKeyFrameListModel() {
		values = new ArrayList<SkeletonAnimationKeyFrame>();
	}

	public AnimationKeyFrameListModel(ArrayList<SkeletonAnimationKeyFrame> values) {
		this.values = new ArrayList<SkeletonAnimationKeyFrame>(values);
	}

	public int getSize() {
		return values.size();
	}

	public Object getElementAt(int index) {
		return values.get(index).getName();
	}

}