package com.sggev.game.objects;

import com.sggev.engine.GameContainer;
import com.sggev.engine.Renderer;
import com.sggev.game.GameManager;
import com.sggev.game.components.AABBComponent;
import com.sggev.game.components.Component;

public class Platform extends GameObject {

	
	private int color = (int) (Math.random() * Integer.MAX_VALUE);
	
	public Platform(Component c)
	{
		super(c);
		
		this.tag = "platform";
		this.width = 130;
		this.height = 32;
		this.padding = 0;
		this.paddingTop = 0;
		
		this.posX = 160;
		this.posY = 144;
		
		//this.addComponent(new AABBComponent(this));
	}
	

	
	
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {


		
		this.updateComponents(gc, gm, dt);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {

		r.drawFillRect((int)posX, (int)posY, width, height, color);
		
		this.renderComponents(gc, r);
	}

	@Override
	public void collision(GameObject other) {

		color = (int) (Math.random() * Integer.MAX_VALUE);
	}

}
