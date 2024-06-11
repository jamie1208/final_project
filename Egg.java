package com.martio.game;

import java.awt.Graphics;
import java.util.Random;

public class Egg extends Tile{
	
	public Sprite eggSprite; //要出現的sprite
	
	public boolean poppedUp = false;//sprite是否彈出（慢慢往上升,直到完全跑出來後彈出）
	
	private int spriteY = getY();//撞擊後sprite慢慢往上升
	private int delay = 0;
	private int delayTime = 0;

	

	public Egg(int x, int y, int width, int height, boolean solid, Id id, Handler handler) {
		super(x, y, width, height, solid, id, handler);
		Random random= new Random();
		delayTime = (5+random.nextInt(10))*60;
		eggSprite = GamePanel.enemy_sprites[0];
	}

	@Override
	public void draw(Graphics g) {
		if(!poppedUp) g.drawImage(eggSprite.getBufferedImage(), x+width/4, spriteY, width/2, height/2,null);
		if(!activated) g.drawImage(GamePanel.egg.getBufferedImage(), x, y, width, height,null);
		else g.drawImage(GamePanel.usedEgg.getBufferedImage(), x, y, width, height,null);
		
	}

	@Override
	public void updata() {
		delay ++ ;
		//時間未到
		if(delay >= delayTime && !activated) {
			activated = true;
			poppedUp = false;
		}
		//時間到到且未彈出
		if(activated && !poppedUp) {
			spriteY -= 0.5; //慢慢上升
			if(spriteY == y-height/2) {	
				System.out.println("addMushroom");
				handler.addEntity(new Enemy(x+width/4, spriteY, width/2, height/2, true, Id.enemy, handler));
				poppedUp = true;
				delay = 0;
				activated = false;
				spriteY = getY();
			}
		}
		
	}

}
