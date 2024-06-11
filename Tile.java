package com.martio.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Tile{

	public int x,y;
	public int width, height;
	
	public boolean solid = false;
	public boolean activated = false; //是否被觸發
	
	public int xVelocity;
	public int yVelocity;
	
	public int facing = 0;
	
	public Id id;
	
	public Handler handler;
	
	public Tile(int x,int y,int width,int height,boolean solid,Id id,Handler handler){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.solid = solid;
		this.id = id;
		this.handler = handler;
	}
	
	public abstract void draw(Graphics g);
	
	public abstract void updata();

	public boolean isSolid() {
		return solid;
	}
	
	public Id getId() {
		return id;
	}
	
	public void die() {
		handler.removeTile(this);
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

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
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
		return new Rectangle(x+10,y,width-20,5);
	}
	
	//下半部
	public Rectangle getBottomBound() {
		return new Rectangle(x+10,y+height-5,width-20,5);
	}
	
	//左半部
	public Rectangle getLeftBound() {
		return new Rectangle(x,y+10,5,height-20);
	}
	
	//右半部
	public Rectangle getRightBound() {
		return new Rectangle(x+width-5,y+10,5,height-20);
	}
	
}
