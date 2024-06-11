package com.martio.game;

import java.awt.Canvas;
import java.awt.Dimension;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Game{
	public static GameFrame frame;
	//遊戲開始
	public static Thread startThread;
	private static StartPanel startPanel;
	//遊戲進行設置
	public static Thread gameThread;
	public static GamePanel gamePanel;
	//遊戲結束
	public static Thread endThread;
	public static EndPanel endPanel;
	
	public double enemyCp;
	public int level;
	public double playerCp;
	
	
	public Game(double enemyCp,double playerCp,GameFrame gameFrame) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		//計算enemy level(最少5)
		this.enemyCp = enemyCp;
		System.out.println("(enemyCp/playerCp)= "+(enemyCp/playerCp));
		level = (int)((enemyCp/playerCp)*10);
		if(level < 5) {
			level = 5;
		}
		//level = 1;
		System.out.println("level = "+level);
		
		Game.frame = gameFrame;      
		startPanel = new StartPanel(enemyCp,playerCp,level);
		//gameFrame = new GameFrame(gamePanel);
		startThread = new Thread(startPanel);
		System.out.println("START GAME!");
		frame.add(startPanel);
		frame.setFrame();
		startThread.start();
        System.out.println("GameENd!");      
	}
}
