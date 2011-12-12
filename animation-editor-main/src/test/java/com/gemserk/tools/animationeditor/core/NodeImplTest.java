package com.gemserk.tools.animationeditor.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsSame;
import org.junit.Test;

public class NodeImplTest {

	@Test
	public void cloneTest() {

		Node parentNode = new NodeImpl("AAA", 100f, 200f, 45f);
		Node childNode = new NodeImpl("BBB");

		childNode.setParent(parentNode);

		childNode.setPosition(70f, 50f);
		childNode.setAngle(20f);

		Node clonedNode = new NodeImpl(childNode);

		assertThat(clonedNode.getX(), IsEqual.equalTo(childNode.getX()));
		assertThat(clonedNode.getY(), IsEqual.equalTo(childNode.getY()));
		assertThat(clonedNode.getAngle(), IsEqual.equalTo(childNode.getAngle()));
		assertThat(clonedNode.getParent(), IsSame.sameInstance(childNode.getParent()));

	}

	@Test
	public void testGetPositionAndAngle() {
		Node parentNode = new NodeImpl("AAA", 100f, 200f, 45f);
		Node childNode = new NodeImpl("BBB");

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
		Node parentNode1 = new NodeImpl("AAA", 100f, 200f, 45f);
		Node parentNode2 = new NodeImpl("CCC", 50f, 25f, 0f);
		NodeImpl childNode = new NodeImpl("BBB");

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
		Node parentNode1 = new NodeImpl("AAA", 100f, 200f, 45f);
		Node childNode = new NodeImpl("BBB", 150, 250, 80f);

		childNode.setParent(parentNode1);

		float absoluteX = childNode.getX();
		float absoluteY = childNode.getY();
		float absoluteAngle = childNode.getAngle();

		assertThat(absoluteX, IsEqual.equalTo(150f));
		assertThat(absoluteY, IsEqual.equalTo(250f));
		assertThat(absoluteAngle, IsEqual.equalTo(80f));

		assertThat(childNode.getLocalAngle(absoluteAngle), IsEqual.equalTo(0f));
		assertThat(childNode.getLocalX(absoluteX, absoluteY), IsEqual.equalTo(0f));
		assertThat(childNode.getLocalY(absoluteX, absoluteY), IsEqual.equalTo(0f));
		
		// assertThat(childNode.getLocalAngle(0f), IsEqual.equalTo(-80f));
		// assertThat(childNode.getLocalX(0f, 0f), IsEqual.equalTo(-150f));
		// assertThat(childNode.getLocalY(0f, 0f), IsEqual.equalTo(-250f));

		// assertThat(absoluteAngle, IsEqual.equalTo(0f));

	}
	
	@Test
	public void testGetXWhenRotateParent() {
		Node parentNode1 = new NodeImpl("AAA", 0f, 0f, 0f);
		Node childNode = new NodeImpl("BBB", 100, 0, 0f);
		childNode.setParent(parentNode1);
		assertThat(childNode.getX(), IsEqual.equalTo(100f));
		parentNode1.setAngle(180f);
		assertThat(childNode.getX(), IsEqual.equalTo(-100f));
	}
	
	@Test
	public void testGetYWhenRotateParent() {
		Node parentNode1 = new NodeImpl("AAA", 0f, 0f, 0f);
		Node childNode = new NodeImpl("BBB", 0, 100f, 0f);
		childNode.setParent(parentNode1);
		assertThat(childNode.getY(), IsEqual.equalTo(100f));
		parentNode1.setAngle(180f);
		assertThat(childNode.getY(), IsEqual.equalTo(-100f));
	}
	
	@Test
	public void testGetXYWhenRotateParentNotCentered() {
		Node parentNode1 = new NodeImpl("AAA", 50f, 50f, 0f);
		Node childNode = new NodeImpl("BBB", 100f, 100f, 0f);
		childNode.setParent(parentNode1);
		assertThat(childNode.getX(), IsEqual.equalTo(100f));
		assertThat(childNode.getY(), IsEqual.equalTo(100f));
		parentNode1.setAngle(180f);
		assertEquals(0f, childNode.getX(), 0.001f);
		assertEquals(0f, childNode.getY(), 0.001f);
	}
	
	@Test
	public void testGetXYWhenRotateParentNotCentered2() {
		Node parentNode1 = new NodeImpl("AAA", 50f, 50f, 180f);
		Node childNode = new NodeImpl("BBB", 100f, 100f, 0f);
		childNode.setParent(parentNode1);
		assertThat(childNode.getX(), IsEqual.equalTo(100f));
		assertThat(childNode.getY(), IsEqual.equalTo(100f));
		parentNode1.setAngle(0f);
		assertEquals(0f, childNode.getX(), 0.001f);
		assertEquals(0f, childNode.getY(), 0.001f);
	}
	
//	@Test
//	public void testSetLocalValues() {
//		Node parentNode1 = new NodeImpl("AAA", 100f, 200f, 45f);
//		Node childNode = new NodeImpl("BBB", 150, 250, 80f);
//
//		childNode.setParent(parentNode1);
//
//		float localX = childNode.getLocalX(0f, 0f);
//		float localY = childNode.getLocalY(0f, 0f);
//		float localAngle = childNode.getLocalAngle(0f);
//
//		childNode.setLocalPosition(localX, localY);
//		childNode.setLocalAngle(localAngle);
//
//		float absoluteX = childNode.getX();
//		float absoluteY = childNode.getY();
//		float absoluteAngle = childNode.getAngle();
//
//		assertThat(absoluteX, IsEqual.equalTo(150f));
//		assertThat(absoluteY, IsEqual.equalTo(250f));
//		assertThat(absoluteAngle, IsEqual.equalTo(80f));
//	}

}
