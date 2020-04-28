package com.sggev.game.objects;

import com.sggev.engine.GameContainer;
import com.sggev.engine.Renderer;
import com.sggev.game.GameManager;
import com.sggev.game.Physics;
import com.sggev.game.Physics.Direction;
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
	public void collision(GameObject other, double ... morevals) {
		//event on collision
		
		//проверка по тегу
		if(other.getTag().equalsIgnoreCase("pbparticle"))
		{

			//определение компонент
			//CircleComponent myC = (CircleComponent) this.findComponent("circle");
			//CircleComponent otherC = (CircleComponent) other.findComponent("circle");
			
			Component myC = this.getFComponent();
			Component otherC = other.getFComponent();
			
			
			double penDepth;
			double penDirX, penDirY;
			double dlen = Math.pow(this.getPosX() - other.getPosX(), 2) + Math.pow(this.getPosY() - other.getPosY(), 2); 
			double len = Math.pow(dlen, 0.5);
			
			
			//определение направления проникновения
			penDirX =  (other.getPosX() - this.getPosX()) / len;
			penDirY =  (other.getPosY() - this.getPosY()) / len;
			
			int R1, R2;
			
			R1 = (int) (this.getRadius() > 0 ? this.getRadius() : Math.sqrt(Math.pow(this.getHeight(), 2) + Math.pow(this.getWidth(), 2)));
			R2 = (int) (other.getRadius() > 0 ? other.getRadius() : Math.sqrt(Math.pow(other.getHeight(), 2) + Math.pow(other.getWidth(), 2)));

			
			//определение глубины проникновения
			penDepth = R1 + R2 - len;
			
			
			this.push(- penDirX * penDepth * 0.5, - penDirY * penDepth * 0.5);
			
			
			other.push(penDirX * penDepth * 0.5, penDirY * penDepth * 0.5);
			
			double bounce = 0;
			
			
			double relVelX, relVelY;
			
			relVelX = this.getDeltaPosX() - other.getDeltaPosX();
			relVelY = this.getDeltaPosY() - other.getDeltaPosY();
			
			
			double exVel;
			exVel = (1 + bounce)  * (relVelX * penDirX + relVelY * penDirY);
			
			
			if(exVel > 0)
			{
				this.setDeltaPosX(this.getDeltaPosX() + penDirX * exVel * 0.5);
				this.setDeltaPosY(this.getDeltaPosY() + penDirY * exVel * 0.5);
				
				
				
				other.setDeltaPosX(other.getDeltaPosX() - penDirX * exVel * 0.5);
				other.setDeltaPosY(other.getDeltaPosY() - penDirY * exVel * 0.5);
			}
			
		}
		else if(other.getTag().equalsIgnoreCase("platform") || other.getTag().equalsIgnoreCase("player"))
		{
			
			//collision resolve beginning
			//what needs to be given to resolve collision
			//difX, difY
			
			double difX = morevals[0];
			double difY = morevals[1];
					
			CircleComponent c1 = (CircleComponent) this.findComponent("circle");
			
			System.out.println("Circle AABB collision");
			System.out.println("difX: " + difX + " difY: " + difY);
			
			
			
			//extract direction
			
			Physics.Direction dir;
			
			if(Math.abs(difX) > Math.abs(difY))
			{
				if(Math.signum(difX) > 0)
				{
					dir = Physics.Direction.RIGHT;
					System.out.println("RIGHT");
				}
				else
				{
					dir = Physics.Direction.LEFT;
					System.out.println("LEFT");
				}
			}
			else
			{
				if(Math.signum(difY) > 0)
				{
					dir = Physics.Direction.DOWN;
					System.out.println("DOWN");
				}
				else
				{
					dir = Physics.Direction.UP;
					
					System.out.println("UP");
				}
			}
			
			
			
			double penDepth, penDirX, penDirY;
			
			
			
			//test for horizontal collision
			
			
			if(dir == Physics.Direction.LEFT || dir == Physics.Direction.RIGHT)
			{
				
				//relocate
						
				penDepth = c1.getRadius() - Math.abs(difX);
				
				if(dir == Physics.Direction.LEFT)
				{
					
					//move to the right
					
					((PBParticle)c1.getParent()).setPosX(c1.getPosX() + penDepth);
				}
				else
				{
					
					//move to the left
					
					((PBParticle)c1.getParent()).setPosX(c1.getPosX() - penDepth);
				}
				
				
				//reverse velocity and acceleration
				
				
				((PBParticle)c1.getParent()).setDeltaPosX(-((PBParticle)c1.getParent()).getDeltaPosX());
				
				
				((PBParticle)c1.getParent()).setAccelerationX(-((PBParticle)c1.getParent()).getAccelerationX());
				
				

			}else  //test for verticle collision
			{
				//relocate
				
				penDepth = c1.getRadius() - Math.abs(difY);
				
				if(dir == Physics.Direction.DOWN)
				{
					
					//move up
					
					((PBParticle)c1.getParent()).setPosY(c1.getPosY() - penDepth);
				}
				else
				{
					
					//move down
					
					((PBParticle)c1.getParent()).setPosY(c1.getPosY() + penDepth);
				}
				
				
				
				//reverse velocity and acceleration
				
				
				((PBParticle)c1.getParent()).setDeltaPosY(-((PBParticle)c1.getParent()).getDeltaPosY());
				
				
				((PBParticle)c1.getParent()).setAccelerationY(-((PBParticle)c1.getParent()).getAccelerationY());
				
				
			}
			//collision resolve ending
			
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
