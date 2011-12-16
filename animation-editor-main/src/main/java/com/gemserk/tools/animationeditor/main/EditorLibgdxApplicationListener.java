package com.gemserk.tools.animationeditor.main;

import java.io.File;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gemserk.animation4j.timeline.TimelineAnimation;
import com.gemserk.commons.gdx.games.Spatial;
import com.gemserk.commons.gdx.graphics.ImmediateModeRendererUtils;
import com.gemserk.componentsengine.input.InputDevicesMonitorImpl;
import com.gemserk.componentsengine.input.LibgdxInputMappingBuilder;
import com.gemserk.tools.animationeditor.core.Animation;
import com.gemserk.tools.animationeditor.core.AnimationKeyFrame;
import com.gemserk.tools.animationeditor.core.Joint;
import com.gemserk.tools.animationeditor.core.JointImpl;
import com.gemserk.tools.animationeditor.core.JointUtils;
import com.gemserk.tools.animationeditor.core.Skeleton;
import com.gemserk.tools.animationeditor.core.Skin;
import com.gemserk.tools.animationeditor.core.Skin.SkinPatch;
import com.gemserk.tools.animationeditor.core.tree.AnimationEditor;
import com.gemserk.tools.animationeditor.core.tree.SkeletonEditor;

public class EditorLibgdxApplicationListener extends Game {

	protected static final Logger logger = LoggerFactory.getLogger(EditorLibgdxApplicationListener.class);

	private static class Colors {

		public static final Color lineColor = new Color(1f, 1f, 1f, 1f);
		public static final Color nodeColor = new Color(1f, 1f, 1f, 1f);
		public static final Color selectedNodeColor = new Color(0f, 0f, 1f, 1f);
		public static final Color nearNodeColor = new Color(1f, 0f, 0f, 1f);

		public static final Color spatialColor = new Color(0f, 1f, 0f, 1f);

	}

	private static class Actions {

		public static final String LeftMouseButton = "leftMouseButton";
		public static final String RightMouseButton = "rightMouseButton";
		public static final String DeleteNodeButton = "deleteNodeButton";
		public static final String RotateButton = "rotateButton";

		public static final String ModifySkinPatchButton = "modifySkinPatchButton";
		public static final String CancelStateButton = "cancelStateButton";

		public static final String SecondActionButton = "secondActionButton";

	}

	SpriteBatch spriteBatch;

	Joint nearNode;

	float nodeSize = 2f;
	float backgroundNodeSize = 4f;

	float selectDistance = 4f;

	Vector2 position = new Vector2();

	SkeletonEditor skeletonEditor;
	AnimationEditor animationEditor;

	private InputDevicesMonitorImpl<String> inputMonitor;

	public void setTreeEditor(SkeletonEditor skeletonEditor) {
		this.skeletonEditor = skeletonEditor;
	}

	public void setAnimationEditor(AnimationEditor animationEditor) {
		this.animationEditor = animationEditor;
	}

	interface EditorState {

		void update();

		void render();

	}

	class PlayingAnimationState implements EditorState {

		private TimelineAnimation timelineAnimation;
		private Skeleton skeleton;

		public PlayingAnimationState() {
			Animation currentAnimation = animationEditor.getCurrentAnimation();

			ArrayList<AnimationKeyFrame> keyFrames = currentAnimation.getKeyFrames();

			skeleton = skeletonEditor.getSkeleton();

			timelineAnimation = new TimelineAnimation(JointUtils.getTimeline(skeleton.getRoot(), keyFrames), (float) keyFrames.size() - 1);
			timelineAnimation.setSpeed(1f);
			timelineAnimation.setDelay(0f);
			timelineAnimation.start(0);
		}

		@Override
		public void update() {

			timelineAnimation.update(Gdx.graphics.getDeltaTime());

			if (!animationEditor.isPlayingAnimation()) {
				currentState = new NormalEditorState();
				return;
			}

		}

		@Override
		public void render() {
			renderSkeleton(skeleton);
		}

	}

	class NormalEditorState implements EditorState {

		public NormalEditorState() {
			logger.debug("Current state: none");
		}

		public void update() {

			int x = Gdx.input.getX();
			int y = Gdx.graphics.getHeight() - Gdx.input.getY();

			position.set(nearNode.getX(), nearNode.getY());

			if (animationEditor.isPlayingAnimation()) {
				currentState = new PlayingAnimationState();
				return;
			}

			if (inputMonitor.getButton(Actions.RightMouseButton).isReleased())
				skeletonEditor.select(nearNode);

			if (inputMonitor.getButton(Actions.ModifySkinPatchButton).isReleased()) {
				currentState = new ModifyingSkinPatchState();
				return;
			}

			if (inputMonitor.getButton(Actions.DeleteNodeButton).isReleased()) {
				if (!skeletonEditor.isSelectedNode(skeletonEditor.getSkeleton().getRoot())) {
					Joint selectedJoint = skeletonEditor.getSelectedNode();
					if (selectedJoint != null) {
						skeletonEditor.remove(selectedJoint);
						skin.removePatch(selectedJoint);
					}
				}
			}

			if (inputMonitor.getButton(Actions.RotateButton).isPressed()) {
				currentState = new RotatingJointState();
			}

			if (inputMonitor.getButton(Actions.LeftMouseButton).isPressed()) {
				if (position.dst(x, y) < selectDistance) {
					skeletonEditor.select(nearNode);
					currentState = new DraggingJointState();
					return;
				}
			}

			if (inputMonitor.getButton(Actions.LeftMouseButton).isReleased()) {
				Joint newNode = new JointImpl("node-" + MathUtils.random(111, 999));
				skeletonEditor.add(newNode);
				newNode.setPosition(x, y);

				skeletonEditor.select(newNode.getParent());

				Texture texture = new Texture(Gdx.files.internal("data/bone.png"));
				texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				skin.addPatch(newNode, new Sprite(texture));
			}

		}

		@Override
		public void render() {
			renderSkeleton(skeletonEditor.getSkeleton());
		}

	}

	class DraggingJointState implements EditorState {

		private int x0;
		private int y0;

		public DraggingJointState() {
			x0 = Gdx.input.getX();
			y0 = Gdx.graphics.getHeight() - Gdx.input.getY();
			logger.debug("Current state: dragging joint");
		}

		@Override
		public void update() {
			int x1 = Gdx.input.getX();
			int y1 = Gdx.graphics.getHeight() - Gdx.input.getY();

			if (inputMonitor.getButton(Actions.LeftMouseButton).isReleased()) {
				currentState = new NormalEditorState();
				return;
			}

			skeletonEditor.moveSelected(x1 - x0, y1 - y0);

			x0 = x1;
			y0 = y1;
		}

		@Override
		public void render() {
			renderSkeleton(skeletonEditor.getSkeleton());
		}

	}

	class RotatingJointState implements EditorState {

		private int currentY;
		float rotationSpeed = 1f;

		public RotatingJointState() {
			currentY = Gdx.graphics.getHeight() - Gdx.input.getY();
			logger.debug("Current state: rotating joint");
		}

		@Override
		public void update() {

			int y = Gdx.graphics.getHeight() - Gdx.input.getY();

			if (inputMonitor.getButton(Actions.RotateButton).isReleased()) {
				currentState = new NormalEditorState();
				return;
			}

			float rotation = (float) (currentY - y) * rotationSpeed;
			skeletonEditor.rotateSelected(rotation);

			currentY = y;
		}

		@Override
		public void render() {
			renderSkeleton(skeletonEditor.getSkeleton());
		}

	}

	class ModifyingSkinPatchState implements EditorState {

		Vector2 position = new Vector2();
		float rotationSpeed = 1f;

		public ModifyingSkinPatchState() {
			position.x = Gdx.input.getX();
			position.y = Gdx.graphics.getHeight() - Gdx.input.getY();
			logger.debug("Current state: modifying skin patch");
		}

		@Override
		public void update() {

			if (inputMonitor.getButton(Actions.CancelStateButton).isReleased()) {
				currentState = new NormalEditorState();
				return;
			}

			if (inputMonitor.getButton(Actions.RotateButton).isHolded()) {
				SkinPatch skinPatch = skin.getPatch(skeletonEditor.getSelectedNode());
				int currentY = Gdx.graphics.getHeight() - Gdx.input.getY();

				Spatial spatial = skinPatch.getSpatial();
				float rotation = (float) (currentY - position.y) * rotationSpeed;
				spatial.setAngle(spatial.getAngle() + rotation);
			} else if (inputMonitor.getButton(Actions.LeftMouseButton).isHolded()) {

				if (inputMonitor.getButton(Actions.SecondActionButton).isHolded()) {

					// move center

					SkinPatch skinPatch = skin.getPatch(skeletonEditor.getSelectedNode());

					int currentX = Gdx.input.getX();
					int currentY = Gdx.graphics.getHeight() - Gdx.input.getY();

					Spatial spatial = skinPatch.getSpatial();

					skinPatch.center.x += (currentX - position.x) / spatial.getWidth();
					skinPatch.center.y += (currentY - position.y) / spatial.getHeight();

					// spatial.setPosition(newX, newY);

				} else {

					// move spatial

					SkinPatch skinPatch = skin.getPatch(skeletonEditor.getSelectedNode());

					int currentX = Gdx.input.getX();
					int currentY = Gdx.graphics.getHeight() - Gdx.input.getY();

					Spatial spatial = skinPatch.getSpatial();

					float newX = spatial.getX() + currentX - position.x;
					float newY = spatial.getY() + currentY - position.y;

					spatial.setPosition(newX, newY);

				}
			}

			position.x = Gdx.input.getX();
			position.y = Gdx.graphics.getHeight() - Gdx.input.getY();
		}

		@Override
		public void render() {
			renderSkeleton(skeletonEditor.getSkeleton());

			Joint selectedJoint = skeletonEditor.getSelectedNode();

			SkinPatch skinPatch = skin.getPatch(selectedJoint);
			Spatial spatial = skinPatch.getSpatial();

			// renderPoint(spatial.getX() + selectedJoint.getX(), spatial.getY() + selectedJoint.getY());

			renderPoint(spatial.getX() + selectedJoint.getX() + skinPatch.center.x * spatial.getWidth(), spatial.getY() + selectedJoint.getY() + skinPatch.center.y * spatial.getHeight());
		}

	}

	EditorState currentState;
	Skin skin;

	public void setCurrentSkin(final File file) {

		Gdx.app.postRunnable(new Runnable() {

			@Override
			public void run() {
				Texture texture = new Texture(Gdx.files.absolute(file.getAbsolutePath()));
				texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

				Joint selectedNode = skeletonEditor.getSelectedNode();

				if (selectedNode == null)
					return;

				skin.addPatch(selectedNode, new Sprite(texture));
			}
		});

	}

	@Override
	public void create() {
		Gdx.graphics.setVSync(true);

		Gdx.graphics.getGL10().glClearColor(0.25f, 0.25f, 0.25f, 1f);

		Texture.setEnforcePotImages(false);

		currentState = new NormalEditorState();

		spriteBatch = new SpriteBatch();

		inputMonitor = new InputDevicesMonitorImpl<String>();

		new LibgdxInputMappingBuilder<String>(inputMonitor, Gdx.input) {
			{
				monitorMouseLeftButton(Actions.LeftMouseButton);
				monitorMouseRightButton(Actions.RightMouseButton);
				monitorKeys(Actions.DeleteNodeButton, Keys.DEL, Keys.BACKSPACE);

				monitorKey(Actions.RotateButton, Keys.CONTROL_LEFT);

				monitorKey(Actions.ModifySkinPatchButton, Keys.R);
				monitorKey(Actions.CancelStateButton, Keys.ESCAPE);

				monitorKey(Actions.SecondActionButton, Keys.SHIFT_LEFT);
			}
		};

		Joint root = new JointImpl("root");

		root.setPosition(Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() * 0.5f);
		root.setAngle(0f);

		skeletonEditor.setSkeleton(new Skeleton(root));
		skeletonEditor.select(root);

		Texture texture = new Texture(Gdx.files.internal("data/bone.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		skin = new Skin();
		skin.addPatch(root, new Sprite(texture));

	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		System.out.println("game.resize " + width + "x" + height);
	}

	@Override
	public void render() {
		realUpdate();
		realRender();
	}

	private void realUpdate() {
		inputMonitor.update();

		int x = Gdx.input.getX();
		int y = Gdx.graphics.getHeight() - Gdx.input.getY();

		nearNode = skeletonEditor.getNearestNode(x, y);

		currentState.update();

		skin.update();
	}

	private void realRender() {
		Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);

		spriteBatch.begin();
		for (int i = 0; i < skin.patchesCount(); i++) {
			SkinPatch patch = skin.getPatch(i);
			patch.getSprite().draw(spriteBatch);
		}
		spriteBatch.end();

		currentState.render();
	}

	private void renderSkeleton(Skeleton skeleton) {
		renderNodeTree(skeleton.getRoot());
	}

	private void renderNodeTree(Joint joint) {
		renderNodeOnly(joint);
		ArrayList<Joint> children = joint.getChildren();
		for (int i = 0; i < children.size(); i++) {
			Joint child = children.get(i);
			ImmediateModeRendererUtils.drawLine(joint.getX(), joint.getY(), child.getX(), child.getY(), Colors.lineColor);
			renderNodeTree(child);
		}
	}

	private void renderNodeOnly(Joint joint) {
		if (joint == nearNode) {
			ImmediateModeRendererUtils.fillRectangle(joint.getX() - backgroundNodeSize, //
					joint.getY() - backgroundNodeSize, //
					joint.getX() + backgroundNodeSize, //
					joint.getY() + backgroundNodeSize, //
					Colors.nearNodeColor);
		}

		if (skeletonEditor.isSelectedNode(joint)) {
			float backgroundNodeSize = 4f;
			ImmediateModeRendererUtils.fillRectangle(joint.getX() - backgroundNodeSize, //
					joint.getY() - backgroundNodeSize, //
					joint.getX() + backgroundNodeSize, //
					joint.getY() + backgroundNodeSize, //
					Colors.selectedNodeColor);
		}

		ImmediateModeRendererUtils.fillRectangle(joint.getX() - nodeSize, joint.getY() - nodeSize, joint.getX() + nodeSize, joint.getY() + nodeSize, //
				Colors.nodeColor);
	}

	private void renderPoint(float x, float y) {
		ImmediateModeRendererUtils.fillRectangle(x - nodeSize, y - nodeSize, x + nodeSize, y + nodeSize, //
				Colors.spatialColor);
	}

}