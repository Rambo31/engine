package com.sggev.game;

import com.sggev.game.components.Component;
import com.sggev.game.objects.Bullet;
import com.sggev.game.objects.GameObject;

public class ProjectileManager {
public enum ProjectileType{Bullet};
public static GameObject getProjectile(ProjectileType type, double posX, double posY, int direction, Component c)
{
	switch(type)
	{
	case Bullet:
		return new Bullet(posX, posY, direction, c);
	default:
		return new Bullet(posX, posY, direction, c);
	}
}
}
