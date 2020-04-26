package com.sggev.game;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.sggev.engine.AbstractGame;
import com.sggev.engine.GameContainer;
import com.sggev.engine.Renderer;
import com.sggev.engine.audio.SoundClip;
import com.sggev.engine.gfx.Image;
import com.sggev.engine.gfx.ImageTile;
import com.sggev.engine.gfx.Light;
import com.sggev.game.components.AABBComponent;
import com.sggev.game.components.CircleComponent;
import com.sggev.game.objects.GameObject;
import com.sggev.game.objects.PBParticle;
import com.sggev.game.objects.Platform;
import com.sggev.game.objects.Player;

public class GameManager extends AbstractGame {

	public static final int TS = 16;

	private ArrayList<GameObject> objects = new ArrayList<GameObject>();

	
	
	private Camera camera;

	
	private Image lImage = new Image("/levelImage4.png");
	private Image sImage = new Image("/skies.png");
	
	private boolean[] collision;
	private int levelW, levelH;
	
	//мусор 
	private int numOfPart;
	/*private ArrayList<PLink> hlinks = new ArrayList<PLink>();
	private ArrayList<PLink> vlinks = new ArrayList<PLink>();
	private ArrayList<PLink> mdlinks = new ArrayList<PLink>();
	private ArrayList<PLink> sdlinks = new ArrayList<PLink>();*/

	//PBParticle p1;
	
	
	public GameManager() {
		objects.add(new Player(4, 4, new AABBComponent(), ProjectileManager.ProjectileType.Bullet));
		
	
		
		objects.add(new Platform(new AABBComponent()));
		
		loadLevel("/levelImage2_0.png");
		
		//мусор
		
		//p1 = null;
		
		
		PBParticle p1 = null;
		PBParticle p2 = null;
		
		PLink l1 = null;
		
		numOfPart = 48;
		
		int colNum = numOfPart / 3;
		
		
		for(int i = 0; i < numOfPart; i ++)
		{
			
			
			p2 = p1; 
			
			p1 = new PBParticle( (float)((4 + i % colNum) * TS),(float) ((4 + i / colNum) * TS), (float)(Math.random()*5), (float)(Math.random()*5),  1, new CircleComponent());
			
			
			//связи диагональ левый верхний - правый нижний
			/*if(i / colNum > 0 && i % colNum < colNum - 1)//не первая строка и не последняя чатсица в строке
			{
				l1 = new PLink(p1, (PBParticle)objects.get(objects.size() - colNum + 1), 20, this.stiffness);
				
				
				sdlinks.add(l1);
			}
			
			//связи диагональ правый верхний - левый нижний
			
			if(i / colNum > 0 && i % colNum > 0) 
			{
				
				
				l1 = new PLink(p1, (PBParticle)objects.get(objects.size() - colNum - 1), 20, this.stiffness);
				
				
				mdlinks.add(l1);
			}
			
			
			
			
			
			
			//горизонтальные связи
			
			//первая итерация для создания связи не подходит
			
			//также для создания горизонтальной связи не подходит итерация
			//когда p1 на новой строке, а p2 а старой
			
			
			if(p2 != null && (p1.getPosY() - p2.getPosY()) == 0)
			{
				l1 = new PLink(p1, p2, 20, this.stiffness);
				
				hlinks.add(l1);
			}
			
			//вертикальные связи
			
			//проверить не ноль ли при делении i на количество столбцов
			//если не ноль то я должен выбрать из массива ссылок элемент 
			//порядковый номер которого 
			if(i / colNum != 0) //не первая строка
			{
				
				
				l1 = new PLink(p1, (PBParticle)objects.get(objects.size() - colNum ), 20, this.stiffness);
				
				
				vlinks.add(l1);
			}*/
			
			
			objects.add(p1);
		}
		

		
		
		//p1 = new PBParticle( 12 * TS, 1 * TS, 0, 1,  1);
		//objects.add(p1);
		
		
		
		
		
		
		camera = new Camera("player");
	}

	@Override
	public void init(GameContainer gc) {

		gc.getRenderer().setAmbientColor(-1);

	}

	@Override
	public void update(GameContainer gc, float dt) {

		for (int i = 0; i < objects.size(); i++) {
			objects.get(i).update(gc, this, dt);
			if (objects.get(i).isDead()) {
				objects.remove(i);
				i--;
			}
		}
		

		

		
		//мусор
		//разрешение горизонтальныз связей
		/*for(PLink l1 : hlinks)
		{
			l1.solve();
		}
		
		//разрешение вертикальных связей
		for(PLink l1 : vlinks)
		{
			l1.solve();
		}
		
		for(PLink l1 : mdlinks)
		{
			l1.solve();
		}
		
		for(PLink l1 : sdlinks)
		{
			l1.solve();
		}*/

		
		Physics.update();
		
		

		
		camera.update(gc, this, dt);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {

		camera.render(r);
		
		
		r.drawImage(sImage, 0, 0);
		r.drawImage(lImage, 0, 0);
		
		/*for (int y = 0; y < levelH; y++) {
			for (int x = 0; x < levelW; x++) {
				if (collision[x + y * levelW]) {
					r.drawFillRect(x * TS, y * TS, TS, TS, 0xff0f0f0f);
				} else {
					r.drawFillRect(x * TS, y * TS, TS, TS, 0xfff9f9f9);
				}
			}

		}*/
		
		

		for (GameObject obj : objects) {
			obj.render(gc, r);
		}

	}

	public void loadLevel(String path) {
		Image levelImage = new Image(path);

		levelW = levelImage.getW();
		levelH = levelImage.getH();
		collision = new boolean[levelW * levelH];

		for (int y = 0; y < levelImage.getH(); y++) {
			for (int x = 0; x < levelImage.getW(); x++) {
				if (levelImage.getP()[x + y * levelImage.getW()] == 0xff000000) {
					collision[x + y * levelImage.getW()] = true;
				} else {
					collision[x + y * levelImage.getW()] = false;
				}
			}
		}
	}

	public void addObject(GameObject object)
	{
		objects.add(object);
	}
	
	
	public GameObject getObject(String tag)
	{
		for(int i = 0; i < objects.size(); i++)
		{
			if(objects.get(i).getTag().equals(tag))
			{
				return objects.get(i);
			}
		}
		
		return null;
	}
	
	public boolean getCollision(int x, int y) {
		if (x < 0 || x >= levelW || y < 0 || y >= levelH)
			return true;

		return collision[x + y * levelW];
	}

	public static void main(String args[]) {
		GameContainer gc = new GameContainer(new GameManager());

		gc.setWidth(320);
		gc.setHeight(240);
		gc.setScale(3f);

		gc.start();
	}

	public int getLevelW() {
		return levelW;
	}

	public void setLevelW(int levelW) {
		this.levelW = levelW;
	}

	public int getLevelH() {
		return levelH;
	}

	public void setLevelH(int levelH) {
		this.levelH = levelH;
	}

	

	
	
}
