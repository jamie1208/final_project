package com.martio.game;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class GameFrame extends JFrame{
	
	private static final String TITLE_STRING = "Mario Game";
	GamePanel gamePanel;
	
	//設定frame的初始畫面
	public GameFrame(GamePanel gamePanel) {
		// TODO Auto-generated constructor stub
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		
		//設定視窗顯示在螢幕畫面中間位置
        int x = (int) ((dimension.getWidth() - getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - getHeight()) / 2);
        setLocation(x, y);
        this.gamePanel = gamePanel;
	}
	
	public void setFrame() {
		this.add(gamePanel);
		this.setTitle(TITLE_STRING);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
