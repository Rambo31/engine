package com.sggev.game.objects;

import com.sggev.engine.GameContainer;
import com.sggev.engine.Renderer;
import com.sggev.game.GameManager;
import com.sggev.game.components.AABBComponent;
import com.sggev.game.components.CircleComponent;
import com.sggev.game.components.Component;

public class PBParticle extends GameObject {
	//private double lastPosY, lastPosX;
	private double accelerationX, accelerationY;
	private double wallBounce = 4;
	
	public PBParticle(float startPosX, float startPosY, float accelerationX, float accelerationY, int radius, Component c)
	{
		super(c);
		//инициализировать все что нужно для компонента
		
		this.tag = "pbparticle";
		this.width = 0;
		this.height = 0;
		this.padding = 0;
		this.paddingTop = 0;
		
		
		this.deltaPosX = 0;
		this.deltaPosY = 0;
		
		this.posX  = startPosX;
		this.posY  = startPosY;
		
		//this.posX = this.lastPosX = startPosX;
		//this.posY = this.lastPosY = startPosY;
		
		
		this.accelerationX = accelerationX;
		this.accelerationY = accelerationY;
		this.radius = radius;
		
		
		//TODO: aabb component
		//this.addComponent(new CircleComponent(this));
	}
	
	
	
	
	private void move(float dt)
	{
		/*double deltaX;
		double deltaY;
		
		deltaX = posX - lastPosX;
		deltaY = posY - lastPosY;
		
		lastPosX = posX;
		lastPosY = posY;
		
		
		posX += deltaX + accelerationX * dt * dt;
		posY += deltaY + accelerationY * dt * dt;*/
		
		
		deltaPosX += accelerationX * dt * dt;
		deltaPosY += accelerationY * dt * dt;
		
		
		posX += deltaPosX;
		posY += deltaPosY;
		
		
	}
	
	
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {

		//TODO: update comp
		
		
		this.updateComponents(gc, gm, dt);
		
		this.move(dt);
		
		//мусор
		
		
		
		if(this.posY + this.radius > gm.getLevelH() * GameManager.TS)
		{
			//отскок
			
			push(0, -this.wallBounce);
		}
		else if(this.posY - this.radius < 0)
		{
			push(0, this.wallBounce);
		}
		
		
		
		if(this.posX - this.radius < 0)
		{
			push(this.wallBounce, 0);
		}
		else if(this.posX + this.radius > gm.getLevelW() * GameManager.TS)
		{
			push(-this.wallBounce, 0);
		}
	}

	@Override
	public void render(GameContainer gc, Renderer r) {

		r.drawFillCircle((int)posX, (int)posY, (int) radius, 0xff2a2a2a);
		
		
		//TODO: render comp
		
		this.renderComponents(gc, r);
	}

	@Override
	public void collision(GameObject other) {
		//event on collision
		
		//проверка по тегу
		if(other.getTag().equalsIgnoreCase("pbparticle"))
		{

			
			CircleComponent myC = (CircleComponent) this.findComponent("circle");
			CircleComponent otherC = (CircleComponent) other.findComponent("circle");
			
			double penDepth;
			double penDirX, penDirY;
			double dlen = Math.pow(myC.getPosX() - otherC.getPosX(), 2) + Math.pow(myC.getPosY() - otherC.getPosY(), 2); 
			double len = Math.pow(dlen, 0.5);
			
			penDirX =  (otherC.getPosX() - myC.getPosX()) / len;
			penDirY =  (otherC.getPosY() - myC.getPosY()) / len;
			
			
			penDepth = myC.getRadius() + otherC.getRadius() - len;
			
			
			this.push(- penDirX * penDepth * 0.5, - penDirY * penDepth * 0.5);
			
			
			((PBParticle)other).push(penDirX * penDepth * 0.5, penDirY * penDepth * 0.5);
			
			double bounce = 0;
			
			
			double relVelX, relVelY;
			
			relVelX = this.getDeltaPosX() - ((PBParticle)other).getDeltaPosX();
			relVelY = this.getDeltaPosY() - ((PBParticle)other).getDeltaPosY();
			
			
			double exVel;
			exVel = (1 + bounce)  * (relVelX * penDirX + relVelY * penDirY);
			
			
			if(exVel > 0)
			{
				this.setDeltaPosX(this.getDeltaPosX() + penDirX * exVel * 0.5);
				this.setDeltaPosY(this.getDeltaPosY() + penDirY * exVel * 0.5);
				
				
				
				((PBParticle)other).setDeltaPosX(((PBParticle)other).getDeltaPosX() - penDirX * exVel * 0.5);
				((PBParticle)other).setDeltaPosY(((PBParticle)other).getDeltaPosY() - penDirY * exVel * 0.5);
			}
			
		}

		
		
		
		
	}




	public int getRadius() {
		return radius;
	}




	public void setRadius(int radius) {
		this.radius = radius;
	}





	public double getAccelerationX() {
		return accelerationX;
	}




	public void setAccelerationX(double accelerationX) {
		this.accelerationX = accelerationX;
	}




	public double getAccelerationY() {
		return accelerationY;
	}




	public void setAccelerationY(double accelerationY) {
		this.accelerationY = accelerationY;
	}




	/*public double getLastPosX() {
		return lastPosX;
	}




	public void setLastPosX(double lastPosX) {
		this.lastPosX = lastPosX;
	}




	public double getLastPosY() {
		return lastPosY;
	}




	public void setLastPosY(double lastPosY) {
		this.lastPosY = lastPosY;
	}*/
}
