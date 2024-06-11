package com.martio.game;

//儲存的image是用來代表所有物件的圖像
//gamePanel裡的bufferImage是用來代表位置的圖像
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {
	
	private BufferedImage sheet;
	
	//創建網格
	public SpriteSheet(String path) {
		try {
			sheet = ImageIO.read(getClass().getResource(path)); //在指定檔案中尋找path名稱的檔案,取得資訊並讀檔
			System.out.println("sheet = "+sheet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//取得指定格數的image
	public BufferedImage getSprite(int x,int y) {
		 return sheet.getSubimage((x-1)*32, (y-1)*32, 32, 32);
	}
}
