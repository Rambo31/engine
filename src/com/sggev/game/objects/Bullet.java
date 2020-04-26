package com.sggev.game.objects;

import com.sggev.engine.GameContainer;
import com.sggev.engine.Renderer;
import com.sggev.game.GameManager;
import com.sggev.game.components.Component;

public class Bullet extends GameObject {

	
	private int tileX, tileY;
	private float offX, offY;
	
	private int direction;
	private float speed = 200f;
	//изменить конструктор под pos
	public Bullet(int tileX, int tileY, float offX, float offY, int direction, Component c)
	{
		super(c);
		
		this.direction = direction;
		this.tileX = tileX;
		this.tileY = tileY;
		this.offX = offX;
		this.offY = offY;
		
		posX = tileX * GameManager.TS + offX;
		posY = tileY * GameManager.TS + offY;
		
		this.padding = 0;
		this.paddingTop = 0;
		this.width = 4;
		this.height = 4;
	}
	public Bullet(double posX, double posY, int direction, Component c)
	{
		super(c);
		
		this.posX = posX;
		this.posY = posY;
		
		this.direction = direction;
		this.tileX = (int) (posX / GameManager.TS);
		this.tileY = (int) (posY / GameManager.TS);
		
		
		this.offX = (float) (this.posX - this.tileX * GameManager.TS);
		this.offY = (float) (this.posY - this.tileY * GameManager.TS);
		

		
		this.padding = 0;
		this.paddingTop = 0;
		this.width = 4;
		this.height = 4;
	}
	
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		
		switch(direction)
		{
		case 0: offY -= speed * dt; break;
		case 1: offX += speed * dt; break;
		case 2: offY += speed * dt; break;
		case 3: offX -= speed * dt; break;
			
		}
		
		//final position
		if(offY > GameManager.TS / 2)
		{
			tileY++;
			offY -= GameManager.TS;
			
		}
		if(offY < 0)
		{
			tileY--;
			offY += GameManager.TS;
			
		}
		
		
		if(offX > GameManager.TS / 2)
		{
			tileX++;
			offX -= GameManager.TS;
			
		}
		if(offX < 0)
		{
			tileX--;
			offX += GameManager.TS;
			
		}
		
		
		if(gm.getCollision(tileX, tileY))
		{
			this.dead = true;
		}
		
		posX = tileX * GameManager.TS + offX;
		posY = tileY * GameManager.TS + offY;
		
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		r.drawFillRect((int)posX, (int)posY, width, height, 0xffff0000);
		
	}


	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub
		
	}

}
