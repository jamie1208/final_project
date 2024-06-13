package com.martio.game;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

import game.GameFrame;
import game.TimeFeeling;
import game2.src.Game2.Game2;

public class EndPanel extends Canvas implements Runnable{

    //set panel size
    static final int END_WIDHTH = 1280;
    static final int END_HEIGHT = 720;
    //Dimension 封裝了width,heigjt
    static final Dimension SCREEN_SIZE = new Dimension(END_WIDHTH,END_HEIGHT);

    static final int WL_WIDTH = 500;
    static final int WL_HEIGHT = 300;
    static final int WAFFLE_HEIGHT = 100;
    static final int WAFFLE_WIDTH = 100;

    //all image
    static final String WIN1_NAME = "res/win1.png";
    static final String WIN2_NAME = "res/win2.png";
    static final String WIN3_NAME = "res/win3.png";
    static final String LOOSE1_NAME = "res/loose1.png";
    static final String LOOSE2_NAME = "res/loose2.png";
    static final String LOOSE3_NAME = "res/loose3.png";
    static final String WAFFLE_NAME = "res/waffle.png";
    //all sound
    static final String WIN_SOUND = "res/win.wav";
    static final String LOOSE_SOUND = "res/endloose.wav";

    //結束後cp結算
    SoundHandler sound;
    int img_num;
    boolean success;
    Image image;
    Graphics graphics;
    BufferedImage win1_img,win2_img,win3_img,loose1_img,loose2_img,loose3_img,waffle_img,img;
    Random random;
    boolean close;

    EndPanel() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
    	random = new Random();
        close = false;
        setCP();
        setAllImg();
        setSound();
        this.setPreferredSize(SCREEN_SIZE);
    }

    //設定成功 or 失敗img && waffle img
    public void setAllImg(){
        try{
            win1_img = ImageIO.read(new File(WIN1_NAME));
            win2_img = ImageIO.read(new File(WIN2_NAME));
            win3_img = ImageIO.read(new File(WIN3_NAME));
            loose1_img = ImageIO.read(new File(LOOSE1_NAME));
            loose2_img = ImageIO.read(new File(LOOSE2_NAME));
            loose3_img = ImageIO.read(new File(LOOSE3_NAME));
            //waffle_img = ImageIO.read(new File(WAFFLE_NAME));
        }catch(IOException e){System.out.println("not get img!");}
    }

    //設定total cp(+-10,四捨五入 , 鬆餅是怪獸1%)
    public void setCP(){
        Game.total_CP = (int)Math.round(Game.enemyCp *0.1);
        System.out.println("totalCP = "+Game.total_CP);
        if(success){
            Game.total_CP += GamePanel.get_waffle;
        }
        else{
            Game.total_CP = Math.abs(Game.total_CP-GamePanel.get_waffle);
        }
    }

    //選img
    public void chooseImg(){
        if(success){
            if(img_num == 1) img = win1_img;
            else if(img_num == 2) img = win2_img;
            else if(img_num == 3) img = win3_img;
        }
        else{
            if(img_num == 1) img = loose1_img;
            else if(img_num == 2) img = loose2_img;
            else if(img_num == 3) img = loose3_img;
        }
    }

    //設定成功or失敗sound
    public void setSound() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        if(success){
            sound = new SoundHandler(WIN_SOUND);
        }
        else{
            sound = new SoundHandler(LOOSE_SOUND);
        }
    }
    
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
        g.fillRect(0, 0, END_WIDHTH, END_HEIGHT);
        g.drawImage(img, END_WIDHTH/2-WL_WIDTH/2, 20, WL_WIDTH,WL_HEIGHT,null);
        g.drawImage(GamePanel.waffle.getBufferedImage(), END_WIDHTH/2 - WAFFLE_WIDTH-15, WL_HEIGHT+90, WAFFLE_WIDTH, WAFFLE_HEIGHT, null);

        g.setColor(Color.white);
        g.setFont(new Font("ComicSansMS",Font.PLAIN,100));
        g.drawString("+"+String.valueOf(GamePanel.get_waffle), END_WIDHTH/2,60+WAFFLE_HEIGHT+WL_HEIGHT);
        if(success)
        g.drawString("CP +"+String.valueOf(Game.total_CP),END_WIDHTH/2 - WAFFLE_WIDTH-50,250+WAFFLE_HEIGHT+WL_HEIGHT);
        else{
            g.drawString("CP -"+String.valueOf(Game.total_CP),END_WIDHTH/2 - WAFFLE_WIDTH-50,250+WAFFLE_HEIGHT+WL_HEIGHT);
        }
    }

    public void run(){
        //sound.start();
        long lastTime = System.nanoTime();//虛擬機開啟時,會隨機生成一個long值,nanotTime()返回long值到現在的奈秒數
        double amountofTicks = 10.0; // 控制循環時間
        double ns = 1000000000/amountofTicks;
        double delta = 0;
        double all_delta= 0;
        while(true){
            if(close){break;}
            long now = System.nanoTime();
            delta += (now-lastTime)/ns; // 單位：秒/60
            all_delta += (now-lastTime)/ns;
            lastTime = now;
            if(delta>1){
                img_num = random.nextInt(1,3);
                chooseImg();
                render();
                delta --;
            }
            if(all_delta >20) {
            	break;
            }
        }
        GameFrame.addCp = Game.total_CP;
        if(!success) {
        	GameFrame.addCp *= (-1);
        }
        Window window = SwingUtilities.getWindowAncestor(EndPanel.this);
        if (window != null) {
            window.dispose();
        }
        System.out.println("finish");
    } 
}
