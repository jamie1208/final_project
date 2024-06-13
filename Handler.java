/*
 * how to use LinkedList
 * https://www.runoob.com/java/java-linkedlist.html
 * java.util.ConcurrentModificationException
 * use CopyOnWriteArrayList
 * https://www.digitalocean.com/community/tutorials/java-util-concurrentmodificationexception
 */
package com.martio.game;

import java.awt.Graphics;

import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

//collection的add,remove盡量用(List)Iterator
public class Handler {
	
	private int size = 45;
	public List<Entity> entities = new CopyOnWriteArrayList<Entity>();
	public List<Tile> tiles = new CopyOnWriteArrayList<Tile>();
	public Iterator<Entity>enIterator;
	public Iterator<Tile>tilIterator;
	
	
	Handler() {
		//creatLevel();//handler被創建時,會創建水平面
	}
	
	public void draw(Graphics g) {
		for(Entity en : entities) {
			if(en.visible) {
				en.draw(g);	
			}
		}
		
		for(Tile tl : tiles) {
			if(GamePanel.goThroughPipe)tl.draw(g);
			else if(GamePanel.getVisibleArea() != null && tl.getBound().intersects(GamePanel.getVisibleArea()))
			tl.draw(g);
		}
	}
	
	public void update() {
		for(Entity en : entities) {
			if(GamePanel.goThroughPipe)en.update();
			else if(GamePanel.getVisibleArea() != null && en.getBound().intersects(GamePanel.getVisibleArea()))
			en.update();
		}
		
		for(Tile tl : tiles) {
			if(GamePanel.goThroughPipe)tl.updata();
			else if(GamePanel.getVisibleArea() != null && tl.getBound().intersects(GamePanel.getVisibleArea()))
			tl.updata();
		}
	}
	
	public void addEntity(Entity en) {
		entities.add(en);
	}
	
	public void removeEntity(Entity en) {
		enIterator = entities.iterator();
		while(enIterator.hasNext()) {
			Entity entity = enIterator.next();
			if(entity.equals(en)) {
				//System.out.println("equal : "+entity.getId());
				entities.remove(en);
			}
		}
	}
	
	public void addTile(Tile ti) {
		tiles.add(ti);
	}
	
	public void removeTile(Tile tl) {
		tilIterator = tiles.iterator();
		while(tilIterator.hasNext()) {
			Tile tile = tilIterator.next();
			if(tile.equals(tl)) {
				tiles.remove(tl);
			}
		}
	}
	
	//根據location_image的位置創造component,每塊都是64*64
	public void  creatLevel(BufferedImage location_image) {
		int width = location_image.getWidth();
		int height = location_image.getHeight();
		for(int y=0;y<height;y++) {
			for(int x=0;x<width;x++) {
				//System.out.printf("x,y = %d,%d\n",x,y);
				int pixel = location_image.getRGB(x, y);//取得指定格數的rgb
				
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				if(x == 12 && y == 20) {
					System.out.printf("%d,%d,%d\n",red,green,blue);
				}
				//黑色 0/0/0 w-middle
				if(red== 0 &&green== 0 &&blue== 0) 
					addTile(new Wood(x*size, y*size, size, size, true, Id.wood, this, "wm"));
				
				//深青色 0/120/120 w-front
				else if(red== 0 &&green== 120 &&blue== 120) 
					addTile(new Wood(x*size, y*size, size, size, true, Id.wood, this, "wf"));
				
				//深綠色 0/120/0 w-back
				else if(red== 0 &&green== 120 &&blue== 0) 
					addTile(new Wood(x*size, y*size, size, size, true, Id.wood, this, "wb"));
				
				//金色 120/120/0 w-all
				else if(red== 120 &&green== 120 &&blue== 0) 
					addTile(new Wood(x*size, y*size, size, size, true, Id.wood, this, "wa"));
				
				//深紅 100/0/0 h-front
				else if(red== 100 &&green== 0 &&blue== 0) 
					addTile(new Wood(x*size, y*size, size, size, true, Id.wood, this, "hf"));
				
				
				//灰色 100/100/100 h-middle
				else if(red== 100 &&green== 100 &&blue== 100) 
					addTile(new Wood(x*size, y*size, size, size, true, Id.wood, this, "hm"));
				
				//深綠 0/50/0 h-back
				else if(red== 0 &&green== 50 &&blue== 0) 
					addTile(new Wood(x*size, y*size, size, size, true, Id.wood, this, "hb"));
				
				//灰色 100/100/100 h-middle
				else if(red== 100 &&green== 100 &&blue== 100) 
					addTile(new Wood(x*size, y*size, size, size, true, Id.wood, this, "hm"));
				
				//深紅 50/0/0 h-all
				else if(red== 50 &&green== 0 &&blue== 0) 
					addTile(new Wood(x*size, y*size, size, size, true, Id.wood, this, "ha"));
				
				//藍色 - player 0/0/255
				else if(red== 0 &&green== 0 &&blue== 255) {
					//System.out.println("addPlayer");
					addEntity(new Player(x*size, y*size, size, size, true, Id.player, this));
				}
				//紅色 - pinecone 255/0/0 
				else if(red == 255 &&green== 0 &&blue== 0) {
					//System.out.println("addMushroom");
					addEntity(new Pinecone(x*size, y*size, size, size, true, Id.pinecone, this));
				}
				//橘色 - goomba
				else if(red == 255 && green==100 && blue==0) {
					//System.out.println("addGoomba");
					//System.out.printf("init x,y = %d,%d\n",x*64, y*64);
					addEntity(new Enemy(x*size, y*size, size, size, true, Id.enemy, this));
				}
				//綠色 - 0,120,0
				else if(red == 0 && green == 255 && blue == 0) {
					//System.out.println("addPowerUp");
					addTile(new PowerUpBlock(x*size, y*size, size, size, true, Id.powerUp, this));
				}
				//深紫色 - 往下pipe 60,0,60
				else if(red == 60 && green == 0 && blue == 60) {
					//System.out.println("addPowerUp");
					addTile(new Pipe(x*size-15, y*size, 2*size+30, size*19, true, Id.pipe, this ,1, 9));
				}
				//深紫色 - 往上pipe 40,0,40
				else if(red == 40 && green == 0 && blue == 40) {
					//System.out.println("addPowerUp");
					addTile(new Pipe(x*size-15, y*size, 2*size+30, size*19, true, Id.pipe, this ,0, 9));
				}
				//黃色 - waffle 255,220,60
				else if(red == 255 && green == 220 && blue == 60) {
					//System.out.println("addPowerUp");
					addTile(new Waffle(x*size, y*size, size/3*2 , size/3*2, true, Id.waffle, this));
				}
				//藍綠色 - egg 100/150/150 
				else if(red == 100 && green == 150 && blue == 150) {
					//System.out.println("addPowerUp");
					addTile(new Egg(x*size, y*size-size, 2*size , 2*size, true, Id.egg, this));
				}
			}
		}
		
	}
}
