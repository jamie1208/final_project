package com.martio.game;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class EndFrame extends JFrame{
	private static final String TITLE_STRING = "End";
	EndPanel endPanel;
	
	public EndFrame(EndPanel endPanel) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		
		//設定視窗顯示在螢幕畫面中間位置
        int x = (int) ((dimension.getWidth() - getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - getHeight()) / 2);
        setLocation(x, y);
        this.endPanel = endPanel;
	}
	public void setFrame() {
		this.add(endPanel);
		this.setTitle(TITLE_STRING);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
