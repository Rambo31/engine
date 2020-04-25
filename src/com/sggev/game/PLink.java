package com.sggev.game;

import com.sggev.game.objects.PBParticle;

public class PLink {

	private double defLen;
	private double stiffness;
	
	
	private PBParticle p1;
	private PBParticle p2;
	
	
	public PLink(PBParticle p1, PBParticle p2, double defLen, double stiffness)
	{
		this.p1 = p1;
		this.p2 = p2;
		this.defLen = defLen;
		this.stiffness = stiffness;
	}
	
	public void solve()
	{
		
		//длина вектора
		double dlen = Math.pow(p2.getPosX() - p1.getPosX(), 2) + Math.pow(p2.getPosY() - p1.getPosY(), 2); 
		double len = Math.pow(dlen, 0.5);
		
		
		
		
		double normX, normY;
		
		normX = ( p2.getPosX() - p1.getPosX() ) / len;
		normY = ( p2.getPosY() - p1.getPosY() ) / len;
		
		
		double goal1X, goal1Y;
		double goal2X, goal2Y;
		
		
		goal1X = (p1.getPosX() + p2.getPosX()) * 0.5 - normX * defLen * 0.5;
		goal1Y = (p1.getPosY() + p2.getPosY()) * 0.5 - normY * defLen * 0.5;
		
		
		goal2X = (p1.getPosX() + p2.getPosX()) * 0.5 + normX * defLen * 0.5;
		goal2Y = (p1.getPosY() + p2.getPosY()) * 0.5 + normY * defLen * 0.5;
		
		/*p1.setPosX(p1.getPosX() + (goal1X - p1.getPosX()) * this.stiffness);
		p1.setPosY(p1.getPosY() + (goal1Y - p1.getPosY()) * this.stiffness);
		
		
		p2.setPosX(p2.getPosX() + (goal2X - p2.getPosX()) * this.stiffness);
		p2.setPosY(p2.getPosY() + (goal2Y - p2.getPosY()) * this.stiffness);*/
		
		
		
		
		p1.push((goal1X - p1.getPosX()) * this.stiffness, (goal1Y - p1.getPosY()) * this.stiffness);
		
		p2.push((goal2X - p2.getPosX()) * this.stiffness, (goal2Y - p2.getPosY()) * this.stiffness);
		
	}
	
	
}
