package com.sggev.game;

import java.util.ArrayList;

import com.sggev.game.components.AABBComponent;
import com.sggev.game.components.CircleComponent;
import com.sggev.game.objects.PBParticle;

public class Physics {

	
	private static ArrayList<AABBComponent> aabbList = new ArrayList<AABBComponent>();
	private static ArrayList<CircleComponent> circleList = new ArrayList<CircleComponent>();
	
	
	
	private static PLink[] linkList;
	
	enum Direction{UP, DOWN, LEFT, RIGHT};
	
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
		
		
		//���� ����������� �������� ���������� ���������� ���������
		//���� �������������� ���� ���
		
		if(linkList == null)
		{
			linkList = new PLink[circleList.size() * circleList.size()];
			for(PLink l: linkList)
			{
				l = null;
			} 
		}

		
		for(int i = 0; i < circleList.size(); i++)
		{
			for(int j = i + 1; j < circleList.size(); j++)
			{
				//���������� ���������� ����� ���������
				CircleComponent c0 = circleList.get(i);
				CircleComponent c1 = circleList.get(j);
				
				double dlen = Math.pow(c0.getPosX() - c1.getPosX(), 2) + Math.pow(c0.getPosY() - c1.getPosY(), 2); 
				
				double len = Math.pow(dlen, 0.5);
				
				double alfaLen = 10;
				double betaLen = 20;
				double defLen = alfaLen + (betaLen - alfaLen) / 2;
				
				//���� ���������� ������ ������������� ������
				if(len > betaLen)
				{
					if(linkList[i + j * circleList.size()] != null)
					{
						
						//System.out.println("remove link");
						
						linkList[i + j * circleList.size()] = null;
						
					}

				
				
					//���� ���� �����, �� ���������
				}
				//���� ���������� ������ ������������� ������
				else if(len < alfaLen)
				{
					//���� ����� ��� , �� �������
					if(linkList[i + j * circleList.size()] == null)
					{
						//System.out.println("no link");
					
						//��������� ����� � ������ ������
						
						
						linkList[i + j * circleList.size()] = new PLink((PBParticle)c0.getParent(), (PBParticle)c1.getParent(), defLen, 0.01);
						
					}
					else 
					{
						//System.out.println("link exists");
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
					
					
					//� ����������� ����� ���������� ������ �� �������� ����� �� ������� (���� 2 �����, � �� �������� ����)
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
					
					//����������� ������� �� ��������� ����� �� ���������
					//�������� ����� ������� �� ������� ����� ��������� �������� ��� ����������� ������ ��������
					c1.setPosX(clX);
					c1.setPosY(clY);
					
					//����� �� �������� �������?
					
					
					//����������� dif
					
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
					System.out.println("Circle AABB collision");
					System.out.println("difX: " + difX + " difY: " + difY);
					
					
					
					//extract direction
					
					Direction dir;
					
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