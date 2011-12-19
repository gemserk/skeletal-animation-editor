package com.gemserk.tools.animationeditor.core;


import org.junit.Test;

import com.gemserk.tools.animationeditor.json.JointJsonDeserializer;
import com.gemserk.tools.animationeditor.json.JointJsonSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SkeletonExportToJsonTest {

	@Test
	public void exportSkeletonTest() {

		Skeleton skeleton = new Skeleton();
		skeleton.setRoot(new JointImpl("root", 50f, 75f, 0f));

		JointImpl child1 = new JointImpl("toto1", 75f, 40f, 0f);
		child1.setParent(skeleton.getRoot());

		Gson gson = new GsonBuilder() //
				.registerTypeAdapter(JointImpl.class, new JointJsonSerializer()) //
				.registerTypeAdapter(Joint.class, new JointJsonDeserializer()) //
				.setPrettyPrinting() //
				.create();

		// System.out.println(skeleton.getRoot().getChildren());

		String json = gson.toJson(skeleton);

		System.out.println(json);

		Skeleton newSkeleton = gson.fromJson(json, Skeleton.class);

		//
		Joint newRoot = newSkeleton.getRoot();
		//
		sysout(newRoot);
		sysout(newRoot.getChildren().get(0));
		// sysout(newRoot.getChildren().get(1));
	}

	private void sysout(Joint joint) {
		System.out.println(joint.getId());
		if (!"".equals(joint.getParent().getId()))
			System.out.println(joint.getParent().getId());
		System.out.println(joint.getX());
		System.out.println(joint.getY());
		System.out.println(joint.getAngle());
		System.out.println(joint.getChildren().size());
		
		System.out.println(joint.getLocalX());
		System.out.println(joint.getLocalY());
		System.out.println(joint.getLocalAngle());

	}
}
