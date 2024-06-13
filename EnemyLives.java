package com.martio.game;

import java.awt.*;

public class EnemyLives extends Rectangle{
	
	private int interval = 0; //等級越高interval越多
	private Graphics2D graphics2d;
	public static int end_width = 0;
	
	public EnemyLives(int x,int y,int width,int height) {
		super(x,y,width,height);
		interval = (width-90)/Game.level;
		//System.out.println("level in enemyLives = "+Game.level);
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.red.darker());
		if(GamePanel.success){
			end_width = width-90;
		}
		g.fillRoundRect(x+90, y, width-90-end_width, height,10,10);

		graphics2d = (Graphics2D)g;
		graphics2d.setStroke(new BasicStroke(3.0f));
		graphics2d.setColor(Color.RED.darker().darker().darker());
		graphics2d.drawRoundRect(x+90, y, width-90, height,10,10);
		graphics2d.setColor(Color.RED.darker().darker().darker());
		for(int i = 0;i<Game.level-1;i++) {
			graphics2d.drawLine(x+90+interval*(i+1), y, x+90+interval*(i+1), y+height);
		}
		
		g.drawImage(GamePanel.enemy_sprites[0].getBufferedImage(), x+5, y-50, 100,100,null);
		
	}
	
	public void update() {
		if(end_width < GamePanel.killed_enemy*interval && end_width<width-90) {
			end_width += 2;
			System.out.println("end_width = "+end_width);
		}
	}
}
