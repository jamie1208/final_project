package com.martio.game;

import java.awt.Graphics;
import java.util.Random;

public class PowerUpBlock extends Tile{
	
	public Sprite powerUpSprite; //要出現的sprite
	
	public boolean poppedUp = false;//sprite是否彈出（慢慢往上升,直到完全跑出來後彈出）
	
	private int spriteY = getY();//撞擊後sprite慢慢往上升
	
	private int random_num;

	

	public PowerUpBlock(int x, int y, int width, int height, boolean solid, Id id, Handler handler) {
		super(x, y, width, height, solid, id, handler);
		Random random= new Random();
		random_num = random.nextInt(5);
		System.out.println("random_num = "+random_num);
		if(random_num > 2) {
			powerUpSprite = GamePanel.pinecone;
		}
		else {
			System.out.println("mushroom");
			powerUpSprite = GamePanel.mushroom;
		}
	}

	@Override
	public void draw(Graphics g) {
		if(!poppedUp) g.drawImage(powerUpSprite.getBufferedImage(), x, spriteY, width, height,null);
		if(!activated) g.drawImage(GamePanel.powerUp.getBufferedImage(), x, y, width, height,null);
		else g.drawImage(GamePanel.usedPowerUp.getBufferedImage(), x, y, width, height,null);
		
	}

	@Override
	public void updata() {
		//被撞擊到且未彈出
		if(activated && !poppedUp) {
			spriteY --; //慢慢上升
			if(spriteY == y-height) {
				//在上升的最高點建立一個pinecone(會自動falldown);	
				if(random_num > 2) {
					handler.addEntity(new Pinecone(x, spriteY, width, height, true, Id.pinecone, handler));	
				}
				else {
					System.out.println("addMushroom");
					handler.addEntity(new Mushroom(x, spriteY, width, height, true, Id.mushroom, handler));
				}
				poppedUp = true;
			}
		}
		
	}

}
