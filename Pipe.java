package com.martio.game;

import java.awt.Graphics;

public class Pipe extends Tile{
	 
	private int height_pixels; //高度有多少格
	private int onePixels_height; //一格的高度
	
	public Pipe(int x, int y, int width, int height, boolean solid, Id id, Handler handler,int facing, int pixels) {
		super(x, y, width, height, solid, id, handler);
		this.facing = facing;
		this.height_pixels = pixels; 
		this.onePixels_height = height/pixels;
	}

	@Override
	public void draw(Graphics g) {
		//頭
		g.drawImage(GamePanel.pipe_head.getBufferedImage(), x, y, width, onePixels_height, null);	
		//身 (長度 = total-頭尾)
		g.drawImage(GamePanel.pipe_body.getBufferedImage(), x, y+onePixels_height, width, onePixels_height*(height_pixels-2), null);
		//尾
		g.drawImage(GamePanel.pipe_foot.getBufferedImage(), x, y+(height_pixels-1)*onePixels_height, width, onePixels_height, null);
		
	}

	@Override
	public void updata() {
		// TODO Auto-generated method stub
		
	}

}
