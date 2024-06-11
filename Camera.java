package com.martio.game;

public class Camera {
	
	public int x,y;
	
	public void update(Entity player) {
		//System.out.printf("x,y = %d,%d\n",x,y);
		x = -player.getX()-player.width+ GamePanel.getGameWidth()/2;
		y = -player.getY()+GamePanel.getGameHeight()/2;
		setX(x);
		setY(y);
		//System.out.printf("x,y = %d,%d\n",x,y);
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
	
}
