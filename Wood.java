package com.martio.game;

import java.awt.Color;
import java.awt.Graphics;

public class Wood extends Tile{
	
	String direction;

	public Wood(int x, int y, int width, int height, boolean solid, Id id, Handler handler ,String direction) {
		super(x, y, width, height, solid, id, handler);
		this.direction = direction;
		
	}

	@Override
	public void draw(Graphics g) {
		switch(direction) {
		case "wf":
			g.drawImage(GamePanel.w_front_wood.getBufferedImage(), x, y, width, height, null);	
			break;
		
		case "wm":
			g.drawImage(GamePanel.w_middle_wood.getBufferedImage(), x, y, width, height, null);	
			break;
			
		case "wb":
			g.drawImage(GamePanel.w_back_wood.getBufferedImage(), x, y, width, height, null);	
			break;
			
		case "wa":
			g.drawImage(GamePanel.w_all_wood.getBufferedImage(), x, y, width, height, null);	
			break;
		
		case "hf":
			g.drawImage(GamePanel.h_front_wood.getBufferedImage(), x, y, width, height, null);	
			break;
		
		case "hm":
			g.drawImage(GamePanel.h_middle_wood.getBufferedImage(), x, y, width, height, null);	
			break;
			
		case "hb":
			g.drawImage(GamePanel.h_back_wood.getBufferedImage(), x, y, width, height, null);	
			break;
			
		case "ha":
			g.drawImage(GamePanel.h_all_wood.getBufferedImage(), x, y, width, height, null);	
			break;
		};
		
	}

	@Override
	public void updata() {}
	

}
