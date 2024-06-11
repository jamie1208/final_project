package com.martio.game;

import java.awt.*;
import java.util.Iterator;

public abstract class Entity{  //每個entity都是一個thread避免update時干擾

	public int x,y;
	public int width, height;
	public int facing = 0; //left for 0 ,right for 1
	
	public boolean solid;
	public boolean jumping = false;
	public boolean twice_jumping = false;
	public boolean falling = true;
	public boolean goThroughPipe = false;
	public boolean visible = true;
	public boolean onFire = false;
	
	public int xVelocity = 0;
	public int yVelocity = 0;
	
	public double gravity = 0.0;
	
	public Id id;
	
	private Thread thread;
	public Handler handler;
	
	public Entity(int x,int y,int width,int height,boolean solid ,Id id,Handler handler){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.solid = solid;
		this.id = id;
		this.handler = handler;
	}
	
	public abstract void draw(Graphics g);
	
	public abstract void update();
	
	public abstract void move();

	public boolean isSolid() {
		return solid;
	}
	
	public Id getId() {
		return id;
	}
	
	public void die() {
		System.out.println("handler remove -> "+this.getId());
		handler.removeEntity(this);
	}

	public void setxVelocity(int xVelocity) {
		this.xVelocity = xVelocity;
	}

	public void setyVelocity(int yVelocity) {
		this.yVelocity = yVelocity;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	//用來判斷collision
	//全部
	public Rectangle getBound() {
		return new Rectangle(x,y,width,height);
	}
	
	//上半部
	public Rectangle getTopBound() {
		return new Rectangle(x+10,y,width-20,height/4);
	}
	
	//下半部
	public Rectangle getBottomBound() {
		return new Rectangle(x+10,y+height-height/4,width-20,height/4+1);
	}
	
	//左半部
	public Rectangle getLeftBound() {
		return new Rectangle(x,y+10,width/2,height-20);
	}
	
	//右半部
	public Rectangle getRightBound() {
		return new Rectangle(x+width-width/2,y+10,width/2,height-20);
	}
	
	
}
