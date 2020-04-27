package com.sggev.game.components;

public class ComponentManager {
public enum ComponentType{AABB, Circle};

public static Component getComponent(ComponentType ct)
{
	switch(ct)
	{
	case AABB:
		return new AABBComponent();
	case Circle:
		return new CircleComponent();
	default:
		return new AABBComponent();
	}
}
}
