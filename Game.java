package com.martio.game;

import java.awt.Canvas;
import java.awt.Dimension;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Game{
	//遊戲開始
	public static Thread startThread;
	private static StartPanel startPanel;
	private static StartFrame startFrame;
	//遊戲進行設置
	public static Thread gameThread;
	private static GamePanel gamePanel;
	private static GameFrame gameFrame;
	//遊戲結束
	public static Thread endThread;
	private static EndPanel endPanel;
	private static EndFrame endFrame;
	
	public static double enemyCp;
	public static int level;
	public static double playerCp;
	
	
	public Game(double enemyCp,double playerCp) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		//計算enemy level(最少5)
		this.enemyCp = enemyCp;
		System.out.println("(enemyCp/playerCp)= "+(enemyCp/playerCp));
		level = (int)((enemyCp/playerCp)*10);
		if(level < 5) {
			level = 5;
		}
		//level = 1;
		System.out.println("level = "+level);
		
		startPanel = new StartPanel();
		startFrame = new StartFrame(startPanel);
		startThread = new Thread(startPanel);
		startFrame.setFrame();
		startThread.start();
        try{
        	startThread.join();
        }catch(InterruptedException e){
            System.out.println("START ERROR");
        }
		
		gamePanel = new GamePanel();
		gameFrame = new GameFrame(gamePanel);
		gameThread = new Thread(gamePanel);
		System.out.println("START GAME!");
		gameThread.start();
		gameFrame.setFrame();
        try{
        	gameThread.join();
        }catch(InterruptedException e){
            System.out.println("GAME ERROR");
        }
        System.out.println("GameENd!");
		
		endPanel = new EndPanel(GamePanel.success);
		endFrame = new EndFrame(endPanel);
		endThread = new Thread(endPanel);	
        endThread.start();
		endFrame.setFrame();
        try{
        	endThread.join();
        }catch(InterruptedException e){
            System.out.println("END ERROR");
        }
        
        System.out.println("GAME OVER!");
        
	}
}
