package com.martio.game;

import java.awt.Graphics;
import java.util.Random;

public class Enemy extends Entity{
	
	private Random random = new Random();
	
	private int frame = 0;
	private int frame_delay = 0;//更新速度(player animation 更新)
	private boolean animate = false; //是否要動

	public Enemy(int x, int y, int width, int height, boolean solid, Id id, Handler handler) {
		super(x, y, width, height, solid, id, handler);
		
		int direction = random.nextInt(2);//初始往哪個方向跑 0-left 1-right
		switch (direction) {
		case 0: 
			setxVelocity(-2);
			facing = 0;
			break;
		case 1:
			setxVelocity(2);
			facing = 1;
			break;
		}
	}

	@Override
	public void draw(Graphics g) {
		if(facing == 1) { 
			g.drawImage(GamePanel.enemy_sprites[frame].getBufferedImage(), x, y, width, height, null);	
		}
		else if(facing == 0) {
			g.drawImage(GamePanel.enemy_sprites[frame+4].getBufferedImage(), x, y, width, height, null);
		}
		
	}

	@Override
	public void update() {
		move();
		checkCollision();
		falling();
		if(xVelocity!=0) animate = true;
		else animate = false;
		if(animate) {
			animation();
		}
		
	}

	@Override
	public void move() {
		x += xVelocity;
		y += yVelocity;
		
	}
	
	public void checkCollision() {
		boolean hasb_collect = false; //在所有的tile中,只要有一個collent bottom,則falling=false;
		for(int i=0;i<handler.tiles.size();i++) {
			Tile t = handler.tiles.get(i);
			//System.out.println("bottomBound:"+getBottomBound());
			if(t.isSolid()) {
				if(getBottomBound().intersects(t.getBound())&& t.getId()!= Id.waffle) {
					//System.out.println("enemy BottomBound!");
					setyVelocity(0); //只要在地上,yVelocit永遠都0
					if(falling) {
						falling = false;
					}
					hasb_collect = true;
					//break;
				} 
				else if(!falling && !hasb_collect){
						gravity = 0.8;
						falling = true;
				}
				//player left撞到 tile
				if(getLeftBound().intersects(t.getBound())&& t.getId()!= Id.waffle) {
					//System.out.println("leftBound,enemy!");
					facing = 1;
					setxVelocity(2); //player 撞到後不動,mushroom撞到後自動反向移動
				}
				//player right撞到 tile
				if(getRightBound().intersects(t.getBound()) && t.getId()!= Id.waffle) {
					//System.out.println("rightBound,enemy!");
					facing = 0;
					setxVelocity(-2); 
				}
			}
		}
			//bottom撞到 tile
	}
	
	public void falling() {
		//System.out.println("enemy falling "+falling);
		if(falling) {
			gravity+=0.1;
			setyVelocity((int)gravity);
		}
	}
	
	public void animation() {
		frame_delay++; 
		if(frame_delay>=10) { //螢幕每更新n次,變換一次動作(更改此處的值,可更改animation轉換時間)
			frame++; //變換第幾個動作
			if(frame>=GamePanel.enemy_sprites.length/2) {
				frame = 0;
			}
			frame_delay = 0;
		}
	}

}
