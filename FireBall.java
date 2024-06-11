package com.martio.game;

import java.awt.Graphics;

public class FireBall extends Entity{
	
	public final static String KILLENEMYSOUND = "res/stepEnemy.wav";
	public static Thread killEnemyThread;
	private int facing;

	public FireBall(int x, int y, int width, int height, boolean solid, Id id, Handler handler,int facing) {
		super(x, y, width, height, solid, id, handler);
		this.facing = facing;
		switch(facing) {
		case 0: //左
			setxVelocity(-8);
			break;
		
		case 1:
			setxVelocity(8);
			break;
		}	
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(GamePanel.fireball.getBufferedImage(),x,y,width,height,null);
		
	}

	@Override
	public void update() {
		checkCollision();
		move();
		jumpOrfall();
		
	}

	@Override
	public void move() {
		x += xVelocity;
		y += yVelocity;
		
	}
	
	public void checkCollision() {
		//fireball and tile collision
		for(Tile tl : handler.tiles) {
			if(tl.solid) {
				//System.out.println("tile: "+tl.getId());
				if(getLeftBound().intersects(tl.getBound()) || getRightBound().intersects(tl.getBound())) {
					if(tl.getId() != Id.waffle)
					die();
				}	
				if(getBottomBound().intersects(tl.getBound()) && tl.getId() != Id.waffle) {
					//System.out.println("getBottomBound!");
					jumping = true;
					falling = false;
					gravity = 4;
				}
				else {
					if(!falling && !jumping) {
						jumping = false;
						falling = true;
						gravity = 1.0;
					}
				}
			}
		}
		//fireball and enemy collision
		for(Entity entity : handler.entities) {
			if(entity.getId() == Id.enemy) {
				if(getBound().intersects(entity.getBound())) {
					entity.die();
					GamePanel.killed_enemy += 1;
					GamePanel.playSound(KILLENEMYSOUND, killEnemyThread);
					die();
				}
			}
		}
	}
	
	public void jumpOrfall() {
		if(jumping) {
			gravity -= 0.4;
			setyVelocity(-(int)gravity);
			if(gravity <= 0.5) {
				jumping = false;
				falling = true;
			}
		}
		if(falling) {
			gravity += 0.4;
			setyVelocity((int)gravity);
		}
	}

}
