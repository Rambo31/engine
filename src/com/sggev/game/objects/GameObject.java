package com.sggev.game.objects;

import java.util.ArrayList;

import com.sggev.engine.GameContainer;
import com.sggev.engine.Renderer;
import com.sggev.game.GameManager;
import com.sggev.game.components.Component;

public abstract class GameObject {

	protected String tag;
	protected double posX, posY;
	protected int width, height;
	protected int radius;
	protected int padding, paddingTop;
	protected boolean dead = false;
	
	protected ArrayList<Component> components = new ArrayList<Component>(); 
	
	public abstract void update(GameContainer gc, GameManager gm, float dt);
	public abstract void render(GameContainer gc, Renderer r);
	public abstract void collision(GameObject other);
	
	public GameObject(Component c)
	{
		c.setParent(this);
		this.addComponent(c);
	}
	
	
	public void updateComponents(GameContainer gc, GameManager gm, float dt)
	{
		for(Component c: components)
		{
			c.update(gc, gm, dt);
		}
	}
	
	public void renderComponents(GameContainer gc, Renderer r)
	{
		for(Component c: components)
		{
			c.render(gc, r);
		}
	}
	
	public void addComponent(Component c)
	{
		components.add(c);
	}
	
	public void removeComponent(String tag)
	{
		for(int i =0; i < components.size(); i++)
		{
			if(components.get(i).getTag().equalsIgnoreCase(tag))
			{
				components.remove(i);
			}
		}
	}
	
	
	public Component findComponent(String tag)
	{
		for(int i =0; i < components.size(); i++)
		{
			if(components.get(i).getTag().equalsIgnoreCase(tag))
			{
				return components.get(i);
			}
		}
		
		return null;
	}
	
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public double getPosX() {
		return posX;
	}
	public void setPosX(double d) {
		this.posX = d;
	}
	public double getPosY() {
		return posY;
	}
	public void setPosY(double d) {
		this.posY = d;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public boolean isDead() {
		return dead;
	}
	public void setDead(boolean dead) {
		this.dead = dead;
	}
	public int getPadding() {
		return padding;
	}
	public void setPadding(int padding) {
		this.padding = padding;
	}
	public int getPaddingTop() {
		return paddingTop;
	}
	public void setPaddingTop(int paddingTop) {
		this.paddingTop = paddingTop;
	}
	public int getRadius() {
		return radius;
	}
	public void setRadius(int radius) {
		this.radius = radius;
	}
}
