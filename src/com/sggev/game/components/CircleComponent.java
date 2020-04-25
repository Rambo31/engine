package com.sggev.game.components;

import com.sggev.engine.GameContainer;
import com.sggev.engine.Renderer;
import com.sggev.game.GameManager;
import com.sggev.game.Physics;
import com.sggev.game.objects.GameObject;

public class CircleComponent extends Component{

	
	private GameObject parent;
	private double posY, posX;
	private double lastPosY, lastPosX;
	private double radius;
	
	
	public CircleComponent(GameObject parent)
	{
		this.parent = parent;
		this.tag = "circle";
	}
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		//update pos
		lastPosX = posX;
		lastPosY = posY;
		
		posX = this.parent.getPosX();
		posY = this.parent.getPosY();
		
		radius = this.parent.getRadius();
		
		//add physics
		Physics.addCircleComponent(this);
	}
	@Override
	public void render(GameContainer gc, Renderer r) {
		
		//render comp
		//тут можно нарисовать форму но это теряет смысл когда мы ресуем полную форму в GameObject
	}

	public GameObject getParent() {
		return parent;
	}

	public void setParent(GameObject parent) {
		this.parent = parent;
	}

	public double getPosY() {
		return posY;
	}

	public void setPosY(double posY) {
		this.posY = posY;
	}

	public double getPosX() {
		return posX;
	}

	public void setPosX(double posX) {
		this.posX = posX;
	}

	public double getLastPosY() {
		return lastPosY;
	}

	public void setLastPosY(double lastPosY) {
		this.lastPosY = lastPosY;
	}

	public double getLastPosX() {
		return lastPosX;
	}

	public void setLastPosX(double lastPosX) {
		this.lastPosX = lastPosX;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}
}
