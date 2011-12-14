package com.gemserk.tools.animationeditor.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsSame;
import org.junit.Test;

public class NodeImplTest {

	@Test
	public void cloneTest() {

		Joint parentNode = new JointImpl("AAA", 100f, 200f, 45f);
		Joint childNode = new JointImpl("BBB");

		childNode.setParent(parentNode);

		childNode.setPosition(70f, 50f);
		childNode.setAngle(20f);

		Joint clonedNode = new JointImpl(childNode);

		assertThat(clonedNode.getX(), IsEqual.equalTo(childNode.getX()));
		assertThat(clonedNode.getY(), IsEqual.equalTo(childNode.getY()));
		assertThat(clonedNode.getAngle(), IsEqual.equalTo(childNode.getAngle()));
		assertThat(clonedNode.getParent(), IsSame.sameInstance(childNode.getParent()));

	}

	@Test
	public void testGetPositionAndAngle() {
		Joint parentNode = new JointImpl("AAA", 100f, 200f, 45f);
		Joint childNode = new JointImpl("BBB");

		childNode.setParent(parentNode);

		childNode.setPosition(70f, 50f);
		childNode.setAngle(20f);

		assertThat(childNode.getX(), IsEqual.equalTo(70f));
		assertThat(childNode.getY(), IsEqual.equalTo(50f));
		assertThat(childNode.getAngle(), IsEqual.equalTo(20f));
		assertThat(childNode.getParent(), IsSame.sameInstance(parentNode));
	}

	@Test
	public void shouldConverToLocalToParentOnParentChanged() {
		Joint parentNode1 = new JointImpl("AAA", 100f, 200f, 45f);
		Joint parentNode2 = new JointImpl("CCC", 50f, 25f, 0f);
		JointImpl childNode = new JointImpl("BBB");

		childNode.setParent(parentNode1);

		childNode.setPosition(70f, 50f);
		childNode.setAngle(20f);

		childNode.setParent(parentNode2);

		assertThat(childNode.getX(), IsEqual.equalTo(70f));
		assertThat(childNode.getY(), IsEqual.equalTo(50f));
		assertThat(childNode.getAngle(), IsEqual.equalTo(20f));
		assertThat(childNode.getParent(), IsSame.sameInstance(parentNode2));

		assertThat(childNode.localPosition.x, IsEqual.equalTo(20f));
	}

	@Test
	public void testGetLocalValues() {
		Joint parentNode1 = new JointImpl("AAA", 100f, 200f, 45f);
		Joint childNode = new JointImpl("BBB", 150, 250, 80f);

		childNode.setParent(parentNode1);

		float absoluteX = childNode.getX();
		float absoluteY = childNode.getY();
		float absoluteAngle = childNode.getAngle();

		assertThat(absoluteX, IsEqual.equalTo(150f));
		assertThat(absoluteY, IsEqual.equalTo(250f));
		assertThat(absoluteAngle, IsEqual.equalTo(80f));

		assertThat(childNode.projectX(absoluteX, absoluteY), IsEqual.equalTo(0f));
		assertThat(childNode.projectY(absoluteX, absoluteY), IsEqual.equalTo(0f));

		// assertThat(childNode.getLocalAngle(0f), IsEqual.equalTo(-80f));
		// assertThat(childNode.getLocalX(0f, 0f), IsEqual.equalTo(-150f));
		// assertThat(childNode.getLocalY(0f, 0f), IsEqual.equalTo(-250f));

		// assertThat(absoluteAngle, IsEqual.equalTo(0f));

	}

	@Test
	public void testGetXWhenRotateParent() {
		Joint parentNode1 = new JointImpl("AAA", 0f, 0f, 0f);
		Joint childNode = new JointImpl("BBB", 100, 0, 0f);
		childNode.setParent(parentNode1);
		assertThat(childNode.getX(), IsEqual.equalTo(100f));
		parentNode1.setAngle(180f);
		assertThat(childNode.getX(), IsEqual.equalTo(-100f));
	}

	@Test
	public void testGetYWhenRotateParent() {
		Joint parentNode1 = new JointImpl("AAA", 0f, 0f, 0f);
		Joint childNode = new JointImpl("BBB", 0, 100f, 0f);
		childNode.setParent(parentNode1);
		assertThat(childNode.getY(), IsEqual.equalTo(100f));
		parentNode1.setAngle(180f);
		assertThat(childNode.getY(), IsEqual.equalTo(-100f));
	}

	@Test
	public void testGetXYWhenRotateParentNotCentered() {
		Joint parentNode1 = new JointImpl("AAA", 50f, 50f, 0f);
		Joint childNode = new JointImpl("BBB", 100f, 100f, 0f);
		childNode.setParent(parentNode1);
		assertThat(childNode.getX(), IsEqual.equalTo(100f));
		assertThat(childNode.getY(), IsEqual.equalTo(100f));
		parentNode1.setAngle(180f);
		assertEquals(0f, childNode.getX(), 0.001f);
		assertEquals(0f, childNode.getY(), 0.001f);
	}

	@Test
	public void testGetXYWhenRotateParentNotCentered2() {
		Joint parentNode1 = new JointImpl("AAA", 50f, 50f, 180f);
		Joint childNode = new JointImpl("BBB", 100f, 100f, 0f);
		childNode.setParent(parentNode1);
		assertThat(childNode.getX(), IsEqual.equalTo(100f));
		assertThat(childNode.getY(), IsEqual.equalTo(100f));
		parentNode1.setAngle(0f);
		assertEquals(0f, childNode.getX(), 0.001f);
		assertEquals(0f, childNode.getY(), 0.001f);
	}

	@Test
	public void testLocalTransformedWhenParentTransformed() {
		Joint parentNode1 = new JointImpl("AAA", 200f, 200f, 0f);
		Joint parentNode2 = new JointImpl("CCC", 100f, 100f, 0f);
		JointImpl childNode = new JointImpl("BBB");

		childNode.setParent(parentNode1);

		childNode.setPosition(150f, 150f);
		childNode.setAngle(0f);

		assertThat(childNode.getLocalX(), IsEqual.equalTo(-50f));
		assertThat(childNode.getLocalY(), IsEqual.equalTo(-50f));
		assertThat(childNode.getLocalAngle(), IsEqual.equalTo(0f));

		childNode.setParent(parentNode2);

		assertThat(childNode.getLocalX(), IsEqual.equalTo(50f));
		assertThat(childNode.getLocalY(), IsEqual.equalTo(50f));
		assertThat(childNode.getLocalAngle(), IsEqual.equalTo(0f));
	}
	
	@Test
	public void testLocalTransformedWhenParentTransformed2() {
		Joint parentNode1 = new JointImpl("AAA", 200f, 200f, 0f);
		Joint parentNode2 = new JointImpl("CCC", 100f, 100f, 180f);
		JointImpl childNode = new JointImpl("BBB");

		childNode.setParent(parentNode1);

		childNode.setPosition(150f, 125f);
		childNode.setAngle(0f);

		assertThat(childNode.getLocalX(), IsEqual.equalTo(-50f));
		assertThat(childNode.getLocalY(), IsEqual.equalTo(-75f));
		assertThat(childNode.getLocalAngle(), IsEqual.equalTo(0f));

		childNode.setParent(parentNode2);

		assertEquals(-50f, childNode.getLocalX(), 0.01f);
		assertEquals(-25f, childNode.getLocalY(), 0.01f);
		assertEquals(-180f, childNode.getLocalAngle(), 0.01f);
	}

	// @Test
	// public void test() {
	//
	// Node root = new NodeImpl("root");
	// root.setPosition(Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() * 0.5f);
	//
	// Node node1 = new NodeImpl("nodo1", Gdx.graphics.getWidth() * 0.25f, Gdx.graphics.getHeight() * 0.75f, 0f);
	// Node node2 = new NodeImpl("nodo2", Gdx.graphics.getWidth() * 0.25f, Gdx.graphics.getHeight() * 0.65f, 0f);
	//
	// node1.setParent(root);
	// node2.setParent(node1);
	//
	// root.setAngle(180f);
	// node1.setAngle(90f);
	//
	// TimelineValueMutableObjectImpl<Node> timelineValue1 = new TimelineValueMutableObjectImpl<Node>(root, NodeConverter.instance);
	// TimelineValueMutableObjectImpl<Node> timelineValue2 = new TimelineValueMutableObjectImpl<Node>(node1, NodeConverter.instance);
	// TimelineValueMutableObjectImpl<Node> timelineValue3 = new TimelineValueMutableObjectImpl<Node>(node2, NodeConverter.instance);
	//
	// timelineValue1.addKeyFrame(new KeyFrame(0f, new float[] { 400f, 300f, 0f }));
	// timelineValue1.addKeyFrame(new KeyFrame(1f, new float[] { 400f, 300f, 360f }));
	//
	// timelineValue2.addKeyFrame(new KeyFrame(0f, new float[] { 200f, 200f, 0f }));
	// timelineValue3.addKeyFrame(new KeyFrame(0f, new float[] { 200f, 250f, 0f }));
	//
	// ArrayList<TimelineValue> values = new ArrayList<TimelineValue>();
	//
	// values.add(timelineValue1);
	// values.add(timelineValue2);
	// values.add(timelineValue3);
	//
	// Timeline timeline = new Timeline(values);
	//
	//
	//
	// }

	// @Test
	// public void testSetLocalValues() {
	// Node parentNode1 = new NodeImpl("AAA", 100f, 200f, 45f);
	// Node childNode = new NodeImpl("BBB", 150, 250, 80f);
	//
	// childNode.setParent(parentNode1);
	//
	// float localX = childNode.getLocalX(0f, 0f);
	// float localY = childNode.getLocalY(0f, 0f);
	// float localAngle = childNode.getLocalAngle(0f);
	//
	// childNode.setLocalPosition(localX, localY);
	// childNode.setLocalAngle(localAngle);
	//
	// float absoluteX = childNode.getX();
	// float absoluteY = childNode.getY();
	// float absoluteAngle = childNode.getAngle();
	//
	// assertThat(absoluteX, IsEqual.equalTo(150f));
	// assertThat(absoluteY, IsEqual.equalTo(250f));
	// assertThat(absoluteAngle, IsEqual.equalTo(80f));
	// }

}
