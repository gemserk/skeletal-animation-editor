package com.gemserk.tools.animationeditor.core;

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
		Node childNode = new NodeImpl("BBB");
		
		childNode.setParent(parentNode1);
		
		childNode.setPosition(70f, 50f);
		childNode.setAngle(20f);
		
		childNode.setParent(parentNode2);
		
		assertThat(childNode.getX(), IsEqual.equalTo(70f));
		assertThat(childNode.getY(), IsEqual.equalTo(50f));
		assertThat(childNode.getAngle(), IsEqual.equalTo(20f));
		assertThat(childNode.getParent(), IsSame.sameInstance(parentNode2));
	}
	
}
