package com.sggev.game.components;

import com.sggev.engine.GameContainer;
import com.sggev.engine.Renderer;
import com.sggev.game.GameManager;
import com.sggev.game.objects.GameObject;

public abstract class Component {
	protected GameObject parent;
	protected String tag;
	public abstract void update(GameContainer gc, GameManager gm, float dt);
	public abstract void render(GameContainer gc, Renderer r);
	
	
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public GameObject getParent() {
		return parent;
	}
	public void setParent(GameObject parent) {
		this.parent = parent;
	}

}
