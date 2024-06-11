package com.martio.game;

import java.awt.Graphics;
import java.util.Random;

public class Mushroom extends Entity{
	
	private Random random = new Random();
	
	public Mushroom(int x, int y, int width, int height, boolean solid, Id id, Handler handler) {
		super(x, y, width, height, solid, id, handler);
		System.out.printf("init x,y = %d,%d\n",x, y);
		System.out.printf("init width,h = %d,%d\n", width,height);
		
		int direction = random.nextInt(2);//初始往哪個方向跑 0-left 1-right
		switch (direction) {
		case 0: 
			setxVelocity(-2);
			break;
		case 1:
			setxVelocity(2);
			break;
		}
	}
	
	@Override
	public void draw(Graphics g) {
		g.drawImage(GamePanel.mushroom.getBufferedImage(), x, y, width, height, null);	
		
	}

	@Override
	public void update() {
		//System.out.println("mushroom update!");
		checkCollision();
		move();
		falling();
		
	}

	@Override
	public void move() {
		//System.out.println("bx = "+x);
		x+=xVelocity;
		y+=yVelocity;
		//System.out.println("x = "+x);
		
	}
	
	public void checkCollision() {
		boolean hasb_collect = false; //在所有的tile中,只要有一個collent bottom,則falling=false;
		for(int i=0;i<handler.tiles.size();i++) {
			//System.out.println("i = "+i);
			Tile t = handler.tiles.get(i);
			if(!t.solid) {
				//System.out.println("not solid");
				break;
			}
			//player bottom撞到 tile
			if(getBottomBound().intersects(t.getBound())) {
				//System.out.println("Mushroom BottomBound!");
				setyVelocity(0); //只要在地上,yVelocit永遠都0
				if(falling) {
					falling = false;
					hasb_collect = true;
				}
				//break;
			} 
			else if(!falling && !hasb_collect){
					//System.out.println("Pinecone not  BottomBound!");
					gravity = 0.8;
					falling = true;
			}
			
			//player left撞到 tile
			if(getLeftBound().intersects(t.getBound())) {
				//System.out.println("leftBound,pinecone!");
				setxVelocity(2); //player 撞到後不動,mushroom撞到後自動反向移動
			}
			//player right撞到 tile
			if(getRightBound().intersects(t.getBound())) {
				//System.out.println("rightBound,pinecone!");
				setxVelocity(-2); 
			}
		}
	}
	
	public void falling() {
		//System.out.println("MushRoom falling "+falling);
		if(falling) {
			gravity+=0.1;
			setyVelocity((int)gravity);
		}
	}
}
