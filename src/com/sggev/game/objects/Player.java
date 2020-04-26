package com.sggev.game.objects;

import java.awt.event.KeyEvent;

import com.sggev.engine.GameContainer;
import com.sggev.engine.Renderer;
import com.sggev.engine.gfx.ImageTile;
import com.sggev.game.GameManager;
import com.sggev.game.components.AABBComponent;
import com.sggev.game.components.Component;

public class Player extends GameObject {

	
	private ImageTile playerImage  = new ImageTile("/sumoHulk_spriteSheet.png", 16, 16);

	
	
	
	private boolean isLeftDir = false;
	private float anim = 0;
	private float animSpeed = 12f;
	private int tileX, tileY;
	private float offX, offY;
	
	private float speed = 150f;
	
	
	private float fallSpeed = 10f;
	private float jump = -4f;
	private float fallDistance = 0;
	private boolean ground = false;
	private boolean groundLast = false;
	private boolean colliding = false;
	
	public Player(int posX, int posY, Component c)
	{
		super(c);
		
		this.tag = "player";
		this.tileX = posX;
		this.tileY = posY;
		this.offX = 0;
		this.offY = 0;
		this.posX = posX * GameManager.TS;
		this.posY = posY * GameManager.TS;
		this.width = GameManager.TS;
		this.height = GameManager.TS;
		
		this.padding = 4;
		this.paddingTop = 2;
		
		
		//this.addComponent(new AABBComponent(this));
	}
	
	@Override
	public void update(GameContainer gc, GameManager gm,  float dt) {
		//left and right
		if(gc.getInput().isKey(KeyEvent.VK_D))
		{
			
			if(gm.getCollision(tileX + 1, tileY) || gm.getCollision(tileX + 1, tileY + (int)Math.signum((int)offY)))
			{
				offX += dt * speed; 
					
				if(offX > padding)
				{
					offX = padding;
				}

			}
			else
			{
				offX += dt * speed;
			}
			
		}
		
		
		if(gc.getInput().isKey(KeyEvent.VK_A))
		{
			if(gm.getCollision(tileX - 1, tileY) || gm.getCollision(tileX - 1, tileY + (int)Math.signum((int)offY)))
			{
				offX -= dt * speed;
					
				if(offX < -padding)
				{
					offX = -padding;
				}
			}
			else
			{
				offX -= dt * speed;
			}
		}
		//end of left and right
		
		
		
		
		//beginning of jump and gravaity
		fallDistance += dt * fallSpeed;
		

		
		if(fallDistance < 0)
		{
			if((gm.getCollision(tileX, tileY - 1) || gm.getCollision(tileX + (int)Math.signum((int) Math.abs(offX) > padding ? offX : 0), tileY - 1)) && offY < -paddingTop)
			{
				fallDistance = 0;	
				offY = -paddingTop;
				
			}
		}
		if(fallDistance > 0)
		{
			if((gm.getCollision(tileX, tileY + 1) || gm.getCollision(tileX + (int)Math.signum((int)Math.abs(offX) > padding ? offX : 0), tileY + 1)) && offY > 0)
			{
				fallDistance = 0;	
				offY = 0;
				
				ground = true;
			}
		}
		//end of jump and gravity
		
		if(gc.getInput().isKeyDown(KeyEvent.VK_W) && ground)
		{
			fallDistance = jump;
			
			ground = false;
		}
		
		
		offY += fallDistance;
		
		
		//final position
		if(offY > GameManager.TS / 2)
		{
			tileY++;
			offY -= GameManager.TS;
			
		}
		if(offY < -GameManager.TS / 2)
		{
			tileY--;
			offY += GameManager.TS;
			
		}
		
		
		if(offX > GameManager.TS / 2)
		{
			tileX++;
			offX -= GameManager.TS;
			
		}
		if(offX < -GameManager.TS / 2)
		{
			tileX--;
			offX += GameManager.TS;
			
		}

		
		posX = tileX * GameManager.TS + offX;
		posY = tileY * GameManager.TS + offY;
		
		
		//shooting
		if(gc.getInput().isKeyDown(KeyEvent.VK_UP))
		{
			gm.addObject(new Bullet(tileX, tileY, offX + width / 2, offY + height / 2, 0, new AABBComponent()));
		}
		if(gc.getInput().isKeyDown(KeyEvent.VK_RIGHT))
		{
			gm.addObject(new Bullet(tileX, tileY, offX + width / 2, offY + height / 2, 1, new AABBComponent()));
		}
		if(gc.getInput().isKeyDown(KeyEvent.VK_DOWN))
		{
			gm.addObject(new Bullet(tileX, tileY, offX + width / 2, offY + height / 2,  2, new AABBComponent()));
		}
		if(gc.getInput().isKeyDown(KeyEvent.VK_LEFT))
		{
			gm.addObject(new Bullet(tileX, tileY, offX + width / 2, offY + height / 2, 3, new AABBComponent()));
		}
		
		
		
		if(gc.getInput().isKey(KeyEvent.VK_D))
		{
			isLeftDir = false;
			
			anim += dt * animSpeed;
			
			if(anim >= 6)
			{
				anim = 0;
			}
		}
		else if(gc.getInput().isKey(KeyEvent.VK_A))
		{
			isLeftDir = true;
			
			anim += dt * animSpeed;
			
			if(anim >= 6)
			{
				anim = 0;
			}
		}
		else
		{
			anim = 0;
		}
		
		
		
		if((int)fallDistance != 0)
		{
			anim = 0;
			ground = false;
		}

		if(ground && !groundLast)
		{
			anim = 2;
		}
		
		groundLast = ground;
		
		
	
		
		this.updateComponents(gc, gm, dt);
		
	}

	@Override
	public void render(GameContainer gc, Renderer r) {

		
		
		if(isLeftDir)
		{
			r.drawReverseImageTile(playerImage, (int)posX, (int)posY, (int)anim, 1);
		}
		else
		{
			r.drawImageTile(playerImage, (int)posX, (int)posY, (int)anim, 1);
		}

		
		
		this.renderComponents(gc, r);
	}

	@Override
	public void collision(GameObject other) {

		
		
		
		if(other.getTag().equalsIgnoreCase("platform"))
		{
			AABBComponent myC = (AABBComponent) this.findComponent("aabb");
			AABBComponent otherC = (AABBComponent) other.findComponent("aabb");
			
			if(Math.abs(myC.getLastCenterX() - otherC.getLastCenterX()) < (myC.getHalfWidth() + otherC.getHalfWidth()))
			{
				if(myC.getCenterY() < otherC.getCenterY())
				{
					int distance = (myC.getHalfHeight() + otherC.getHalfHeight()) - (otherC.getCenterY() - myC.getCenterY());
					offY -= distance;
					posY -= distance;
					
					
					myC.setCenterY(myC.getCenterY() - distance);
					
					fallDistance = 0;
					ground = true;
				}
				
				if(myC.getCenterY() > otherC.getCenterY())
				{
					int distance = (myC.getHalfHeight() + otherC.getHalfHeight()) - (myC.getCenterY() - otherC.getCenterY());
					offY += distance;
					posY += distance;
					
					
					myC.setCenterY(myC.getCenterY() + distance);
					
					fallDistance = 0;
	
				}
			}
			else
			{
				if(myC.getCenterX() < otherC.getCenterX())
				{
					int distance = (myC.getHalfWidth() + otherC.getHalfWidth()) - (otherC.getCenterX() - myC.getCenterX());
					offX -= distance;
					posX -= distance;
					
					
					myC.setCenterX(myC.getCenterX() - distance);
					

				}
				
				if(myC.getCenterX() > otherC.getCenterX())
				{
					int distance = (myC.getHalfWidth() + otherC.getHalfWidth()) - (myC.getCenterX() - otherC.getCenterX());
					offX += distance;
					posX += distance;
					
					
					myC.setCenterX(myC.getCenterX() + distance);

	
				}
			}
		}
	}

	
	
	
	
	
}
