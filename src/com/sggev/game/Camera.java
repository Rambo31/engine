package com.sggev.game;

import com.sggev.engine.GameContainer;
import com.sggev.engine.Renderer;
import com.sggev.game.objects.GameObject;

public class Camera {

	private float offX, offY;
	
	
	private String targetTag;
	private GameObject target = null;
	
	
	public Camera(String tag)
	{
		this.targetTag = tag;
	}
	
	public void update(GameContainer gc, GameManager gm, float dt)
	{
		if(target == null)
		{
			target = gm.getObject(targetTag);
		}
		
		
		
		if(target == null)
		{
			return;
		}
		
		
		float targetX = (float) ((target.getPosX() + target.getWidth() / 2) - gc.getWidth() / 2);
		float targetY = (float) ((target.getPosY() + target.getHeight() / 2 )- gc.getHeight() / 2);
		
		
		
		offX = targetX;
		offY = targetY;
		//offX -= dt  * (int)(offX - targetX) * 20;
		//offY -= dt  * (int)(offY - targetY) * 20;

		
		if(offX < 0) offX = 0;
		if(offY < 0) offY = 0;
		if(offX + gc.getWidth() > gm.getLevelW() * GameManager.TS) offX = gm.getLevelW() * GameManager.TS - gc.getWidth();
		if(offY + gc.getHeight() > gm.getLevelH() * GameManager.TS) offY = gm.getLevelH() * GameManager.TS - gc.getHeight();
		
	}

	public void render(Renderer r)
	{
		r.setCamX((int)offX);
		r.setCamY((int)offY);
		
	}
	
	public float getOffX() {
		return offX;
	}

	public void setOffX(float offX) {
		this.offX = offX;
	}

	public float getOffY() {
		return offY;
	}

	public void setOffY(float offY) {
		this.offY = offY;
	}

	public String getTargetTag() {
		return targetTag;
	}

	public void setTargetTag(String targetTag) {
		this.targetTag = targetTag;
	}

	public GameObject getTarget() {
		return target;
	}

	public void setTarget(GameObject target) {
		this.target = target;
	}
}
