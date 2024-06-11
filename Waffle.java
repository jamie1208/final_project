package com.martio.game;

import java.awt.Graphics;

public class Waffle extends Tile{

	public Waffle(int x, int y, int width, int height, boolean solid, Id id, Handler handler) {
		super(x, y, width, height, solid, id, handler);
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(GamePanel.waffle.getBufferedImage(), x, y, width,height,null);
		
	}

	@Override
	public void updata() {
		// TODO Auto-generated method stub
		
	}

}
