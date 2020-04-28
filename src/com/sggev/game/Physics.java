package com.sggev.game;

import java.util.ArrayList;
import java.util.Arrays;

import com.sggev.game.components.AABBComponent;
import com.sggev.game.components.CircleComponent;
import com.sggev.game.objects.PBParticle;

public class Physics {

	
	private static ArrayList<AABBComponent> aabbList = new ArrayList<AABBComponent>();
	private static ArrayList<CircleComponent> circleList = new ArrayList<CircleComponent>();
	private static int circleListPrevSize = 0;
	
	
	private static PLink[] linkList;
	
	public enum Direction{UP, DOWN, LEFT, RIGHT};
	
	public static void addAABBComponent(AABBComponent aabb)
	{
		aabbList.add(aabb);
	}
	
	
	public static void addCircleComponent(CircleComponent circle)
	{
		circleList.add(circle);
	}
	

	
	public static void update()
	{
		for(int i = 0; i < aabbList.size(); i++)
		{
			for(int j = i + 1; j < aabbList.size(); j++)
			{
				AABBComponent c0 = aabbList.get(i);
				AABBComponent c1 = aabbList.get(j);
				

				
				if(Math.abs(c0.getCenterX() - c1.getCenterX()) < (c0.getHalfWidth() + c1.getHalfWidth()))
				{
					if(Math.abs(c0.getCenterY() - c1.getCenterY()) < (c0.getHalfHeight() + c1.getHalfHeight()))
					{
						
						c0.getParent().collision(c1.getParent());
						c1.getParent().collision(c0.getParent());
					}
				}
			}
		}
		

		
		for(int i = 0; i < circleList.size(); i++)
		{
			for(int j = i + 1; j < circleList.size(); j++)
			{
				CircleComponent c0 = circleList.get(i);
				CircleComponent c1 = circleList.get(j);
				
				//Collision detection code
				double dlen = Math.pow(c0.getPosX() - c1.getPosX(), 2) + Math.pow(c0.getPosY() - c1.getPosY(), 2); 
				
				if(dlen < Math.pow(c0.getRadius() + c1.getRadius(), 2))
				{	
					c0.getParent().collision(c1.getParent());
				}
				
			}
			
		}
		//что значит нескольких раздельных жидкостей??
		//проблема в том, что если появляются новые частицы, то для их связей нет места в таблице связей
		
		
		
		//если потребуется создание нескольких раздельных жидкостей
		//надо модифицировать этот код
		
		{
			if(circleListPrevSize == 0)
			{
				linkList = new PLink[circleList.size() * circleList.size()];
				
				System.out.println("massive init");
				
			}
			else if(circleListPrevSize - circleList.size() != 0)
			{
				linkList = Arrays.copyOf(linkList, circleList.size()*circleList.size());
				
				
				System.out.println("massive reinit: " + Math.abs(circleListPrevSize - circleList.size()));
			}
			
			
			circleListPrevSize = circleList.size();
		}

		
		
		
		
		for(int i = 0; i < circleList.size(); i++)
		{
			for(int j = i + 1; j < circleList.size(); j++)
			{
				//вычисление расстояния между частицами
				CircleComponent c0 = circleList.get(i);
				CircleComponent c1 = circleList.get(j);
				
				double dlen = Math.pow(c0.getPosX() - c1.getPosX(), 2) + Math.pow(c0.getPosY() - c1.getPosY(), 2); 
				
				double len = Math.pow(dlen, 0.5);
				
				double alfaLen = 10;
				double betaLen = 20;
				double defLen = alfaLen + (betaLen - alfaLen) / 2;
				
				//если расстояние больше определенного порога
				if(len > betaLen)
				{
					if(linkList[i + j * circleList.size()] != null)
					{
						
						//System.out.println("remove link");
						
						linkList[i + j * circleList.size()] = null;
						
					}

				
				
					//если есть связь, то разрываем
				}
				//если расстояние меньше определенного порога
				else if(len < alfaLen)
				{
					//если связи нет , то создаем
					if(linkList[i + j * circleList.size()] == null)
					{
						System.out.println("no link");
					
						//добавляем связь в массив связей
						
						
						linkList[i + j * circleList.size()] = new PLink((PBParticle)c0.getParent(), (PBParticle)c1.getParent(), defLen, 0.01);
						
					}
					else 
					{
						System.out.println("link exists");
					}
					
					
				}
				
				
				
			}
		}
		
		//check for collision ball and box
		for(int i = 0; i < aabbList.size(); i++)
		{
			for(int j = 0; j < circleList.size(); j++)
			{
				
				//get AABB and Circle components
				
				AABBComponent a0 = aabbList.get(i);
				CircleComponent c1 = circleList.get(j);
				
				
				//get difference vector between circle and aabb centers
				double difX, difY;
				
				difX = c1.getPosX() - a0.getCenterX();
				difY = c1.getPosY() - a0.getCenterY();
				
				//get the closest to circle point on aabb
				double clampedX = 0, clampedY = 0;
				
				if(Math.abs(difX) <= a0.getHalfWidth() && Math.abs(difY) <= a0.getHalfHeight())
				{
					System.out.println("in in in");
					
					double minDistToSideX = 0, minDistToSideY = 0;
					
					if(difY >= 0)
					{
						minDistToSideY = a0.getHalfHeight() - difY + 0.1;
					}else if(difY < 0)
					{
						minDistToSideY = -0.1 - difY - a0.getHalfHeight();
					}
					
					
					
					if(difX >= 0)
					{
						minDistToSideX = a0.getHalfWidth() - difX + 0.1;
					}else if(difX < 0)
					{
						minDistToSideX = -0.1 - difX - a0.getHalfWidth();
					}
					
					
					//в зависимости какое расстояние меньше мы выбираем точку на стороне (есть 2 точки, а мы выбираем одну)
					if(Math.abs(minDistToSideX) < Math.abs(minDistToSideY))
					{
						clampedX = difX + minDistToSideX;
						clampedY = difY;
					}else
					{
						clampedX = difX;
						clampedY = difY + minDistToSideY;
					}
					
					double clX = a0.getCenterX() + clampedX;
					double clY = a0.getCenterY() + clampedY;
					
					//System.out.println("closestX: " + clX);
					//System.out.println("closestY: " + clY);
					
					//переместить позицию на ближайшую точку за пределами
					//сместить центр немного за границу чтобы результат подходил под стандартный случай коллизии
					c1.setPosX(clX);
					c1.setPosY(clY);
					
					//стоит ли изменить ластпос?
					
					
					//пересчитать dif
					
					difX = c1.getPosX() - a0.getCenterX();
					difY = c1.getPosY() - a0.getCenterY();
				}
				
				
				
				clampedX = Math.min(Math.max(difX, -a0.getHalfWidth()), a0.getHalfWidth());
				clampedY = Math.min(Math.max(difY, -a0.getHalfHeight()), a0.getHalfHeight());

				
				
				
				double closestX, closestY;
				
				closestX = a0.getCenterX() + clampedX;
				closestY = a0.getCenterY() + clampedY;
				
				
				//get vector between circle center and closest point on aabb
				difX = closestX - c1.getPosX();
				difY = closestY - c1.getPosY();
				
				
				
				double dlen = Math.pow(difX, 2) + Math.pow(difY, 2); 
				double len = Math.pow(dlen, 0.5);
				
				//collision resolution condition
				if(len < c1.getRadius() )
				{
					c1.getParent().collision(a0.getParent(), difX, difY);
					a0.getParent().collision(c1.getParent(), difX, difY);					
				}
			}
		}
		
		
		
		
		aabbList.clear();
		circleList.clear();
		
		
		
		for(PLink l: linkList)
		{
			if(l != null)
				l.solve();
		}
	}
}
