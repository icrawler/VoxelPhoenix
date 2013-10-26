package com.phoenix.voxel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;


/**
 * VoxelCamInputController Class
 * 
 * @author Phoenix Enero
 */
public class VoxelCamInputController extends InputAdapter {
	/** The button for rotating the camera. */ 
	public int rotateButton = Buttons.RIGHT;
	/** The angle to rotate when moved the full width or height of the screen. */
	public float rotateAngle = 360f;
	/** The button for translating the camera along the up/right plane */
	//public int translateButton = Buttons.RIGHT;
	/** The units to translate the camera when moved the full width or height of the screen. */
	public float translateUnits = 10f; // FIXME auto calculate this based on the target
	/** The button for translating the camera along the direction axis */
	public int forwardButton = Buttons.MIDDLE;
	/** The key which must be pressed to activate rotate, translate and forward or 0 to always activate. */ 
	public int activateKey = 0;
	/** Indicates if the activateKey is currently being pressed. */
	protected boolean activatePressed;
	/** Whether scrolling requires the activeKey to be pressed (false) or always allow scrolling (true). */
	public boolean alwaysScroll = true;
	/** The weight for each scrolled amount. */
	public float scrollFactor = -0.1f;
	/** Whether to update the camera after it has been changed. */
	public boolean autoUpdate = true;
	/** The target to rotate around. */
	public Vector3 target = new Vector3();
	/** Whether to update the target on translation */ 
	public boolean translateTarget = true;
	/** Whether to update the target on forward */
	public boolean forwardTarget = true;
	/** Whether to update the target on scroll */
	public boolean scrollTarget = false;
	public int forwardKey = Keys.W;
	protected boolean forwardPressed;
	public int backwardKey = Keys.S;
	protected boolean backwardPressed;
	public int leftKey = Keys.A;
	protected boolean leftKeyPressed;
	public int rightKey = Keys.D;
	protected boolean rightKeyPressed;
	/** The camera. */
	public Camera camera;
	/** The current (first) button being pressed. */
	protected int button = -1;
	
	private float startX, startY;
	private final Vector3 tmpV1 = new Vector3();
	private final Vector3 tmpV2 = new Vector3();
	
	private Boolean fpm = false;
	private int farDistance = 512;
	
	public VoxelCamInputController(final Camera camera, int farDistance) {
		this.camera = camera;
		this.farDistance = farDistance;
	}
	
	public void update() {
		if (leftKeyPressed || rightKeyPressed || forwardPressed || backwardPressed) {
			final float delta = Gdx.graphics.getDeltaTime();
			if (leftKeyPressed) {
				camera.translate(tmpV1.set(camera.direction.x, 0, camera.direction.z)
					  .rotate(90, 0, 1, 0)
					  .nor()
					  .scl(delta * translateUnits));
				target.add(tmpV1);
			}
			if (rightKeyPressed) {
				camera.translate(tmpV1.set(camera.direction.x, 0, camera.direction.z)
					  .rotate(-90, 0, 1, 0)
					  .nor()
					  .scl(delta * translateUnits));
				target.add(tmpV1);
			}
			if (forwardPressed) {
				camera.translate(tmpV1.set(camera.direction).scl(delta * translateUnits));
				if (forwardTarget)
					target.add(tmpV1);
			}
			if (backwardPressed) {
				camera.translate(tmpV1.set(camera.direction).scl(-delta * translateUnits));
				if (forwardTarget)
					target.add(tmpV1);
			}
			limitViewDistance();
			if (autoUpdate)
				camera.update();
		}
	}
	
	private void limitViewDistance() {
		float dist = tmpV1.set(camera.position).sub(target).len();
		if (dist > farDistance) {
			tmpV1.set(target).sub(camera.position).nor().scl(farDistance);
			camera.position.set(target).sub(tmpV1);
		}
	}
	
	@Override
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		if (this.button < 0 && (activateKey == 0 || activatePressed)) {
			startX = screenX;
			startY = screenY;
			this.button = button;
		}
		return activatePressed;
	}
	
	@Override
	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		if (button == this.button)
			this.button = -1;
		return activatePressed;
	}
	
	protected boolean process(float deltaX, float deltaY, int button) {
		if (button == rotateButton) {
			tmpV1.set(camera.direction).crs(camera.up).y = 0f;
			camera.rotateAround(target, tmpV1.nor(), deltaY * rotateAngle);
			camera.rotateAround(target, Vector3.Y, deltaX * -rotateAngle);
		}/* else if (button == translateButton) {
			camera.translate(tmpV1.set(camera.direction).crs(camera.up).nor().scl(-deltaX * translateUnits));
			camera.translate(tmpV2.set(camera.up).scl(-deltaY * translateUnits));
			if (translateTarget)
				target.add(tmpV1).add(tmpV2);				
		}*/ else if (button == forwardButton) {
			camera.translate(tmpV1.set(camera.direction).scl(deltaY * translateUnits));
			if (forwardTarget)
				target.add(tmpV1);
		}
		if (autoUpdate)
			camera.update();
		return true;
	}
	
	@Override
	public boolean touchDragged (int screenX, int screenY, int pointer) {
		if (this.button < 0)
			return false;
		final float deltaX = (screenX - startX) / Gdx.graphics.getWidth();
		final float deltaY = (startY - screenY) / Gdx.graphics.getHeight();
		startX = screenX;
		startY = screenY;
		return process(deltaX, deltaY, button);
	}
	
	@Override
	public boolean scrolled (int amount) {
		if (!alwaysScroll && activateKey != 0 && !activatePressed)
			return false;
		tmpV1.set(camera.direction)
		.scl(amount * scrollFactor * translateUnits)
		.add(camera.position)
		.sub(target);
		if (tmpV1.dot(tmpV2.set(camera.position).sub(target)) > 0 || fpm == true) {
			if (fpm == true && amount != 1) {
				tmpV1.set(0, 0, 0);
			}
			else
				tmpV1.set(camera.direction)
				 .scl(amount * scrollFactor * translateUnits);
			camera.translate(tmpV1);
			if (scrollTarget)
				target.add(tmpV1);
			fpm = false;
		}
		else
		{
			camera.position.set(target);
			fpm = true;
		}
		limitViewDistance();
		if (autoUpdate)
			camera.update();
		return true;
	}
	
	/**
	 * Forces first person mode
	 */
	public void forceFPM() {
		camera.position.set(target);
		fpm = true;
	}
	
	@Override
	public boolean keyDown (int keycode) {
		if (keycode == activateKey)
			activatePressed = true;
		if (keycode == forwardKey)
			forwardPressed = true;
		else if (keycode == backwardKey)
			backwardPressed = true;
		else if (keycode == leftKey)
			leftKeyPressed = true;
		else if (keycode == rightKey)
			rightKeyPressed = true;
		return false;
	}
	
	@Override
	public boolean keyUp (int keycode) {
		if (keycode == activateKey) {
			activatePressed = false;
			button = -1;
		}
		if (keycode == forwardKey)
			forwardPressed = false;
		else if (keycode == backwardKey)
			backwardPressed = false;
		else if (keycode == leftKey)
			leftKeyPressed = false;
		else if (keycode == rightKey)
			rightKeyPressed = false;
		return false;
	}
}