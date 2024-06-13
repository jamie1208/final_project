package com.martio.game;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

public class StartPanel extends Canvas implements Runnable{
	
	//set panel size
    static final int START_WIDTH = 400;
    static final int START_HEIGHT = 200;
    //Dimension 封裝了width,heigjt
    static final Dimension SCREEN_SIZE = new Dimension(START_WIDTH,START_HEIGHT);
    //set label size
     static final int LABEL_WIDTH = 200;
     static final int LABEL_HEIGJT = 80;
    static final String LABEL_SOUND = "res/label.wav";
    static final String LABEL_NAME = "res/start.png";
    static final String DESCRIPTION = "click to start !";
    BufferedImage img;

    SoundHandler label_sound;
    JLabel startLabel;
    //Image image;s
    //Graphics graphics;
    boolean start = false;

	public StartPanel(){
		GamePanel.goThroughPipe = false;
		GamePanel.killed_enemy = 0;
		GamePanel.get_waffle = 0;
		GamePanel.lives = 3;
		GamePanel.running = true;
		EnemyLives.end_width = 0;
        try {
			label_sound = new SoundHandler(LABEL_SOUND);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try{
            img = ImageIO.read(new File(LABEL_NAME));
            //waffle_img = ImageIO.read(new File(WAFFLE_NAME));
        }catch(IOException e){System.out.println("not get img!");}
        this.setBackground(Color.black.darker().darker());
        this.setPreferredSize(SCREEN_SIZE);
        this.setFocusable(true);
        this.addMouseListener(new ML());
        //this.setLayout(new BorderLayout());
        //this.add(startLabel,BorderLayout.CENTER);
    	
    }
    //確認是否按下start
	
	public void render(){
		//getbufferStrategy返回此元件使用的緩衝區策略
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			//建立緩衝區
			System.out.println("creat bufferStrategy!");
			createBufferStrategy(3);
			return;//not建立完馬上畫,等到下次才畫
		}
		Graphics g = bs.getDrawGraphics();//取得畫筆
		draw(g);
		g.dispose();
		bs.show();//顯示在frames
    }

    public void draw(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, START_WIDTH, START_HEIGHT);
        g.drawImage(img, 50,20,300,150,null);

        g.setColor(Color.white);
        g.setFont(new Font("ComicSansMS",Font.PLAIN,20));
        g.drawString(DESCRIPTION, 130,190);
    }
    
    public boolean checkStart(){
        if(start){
            return true;
        }
        return false;
    }

    public void run(){
        //nanoTime()用來計算時間差,判斷 (現在時間-上次移動時間)/ns > 1,才會動
		long lastTime = System.nanoTime();//虛擬機開啟時,會隨機生成一個long值,nanotTime()返回long值到現在的奈秒數
        double amountofTicks = 60.0; // 控制循環時間
        double ns = 1000000000/amountofTicks;
        double delta = 0;
        while(true){
            long now = System.nanoTime();
            delta += (now-lastTime)/ns; // 單位：秒/60
            //delta_pcone += (now-lastTime)/ns;
            lastTime = now;
            if(delta>=1){
            	render();
            	delta --;
                if(checkStart()){
                	System.out.println("start checked");
                    break;
                }
            }
        }
        Game.frame.remove(this);
        Game.gamePanel = new GamePanel();
		Game.gameThread = new Thread(Game.gamePanel);	
        Game.gameThread.start();
        Game.frame.add(Game.gamePanel);
        Game.frame.setFrame();
    }
    public class ML extends MouseAdapter{
        public void mousePressed(MouseEvent e){
            System.out.println("start!");
            label_sound.start();
            start = true;
        }
    }

}
