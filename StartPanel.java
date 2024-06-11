package com.martio.game;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

public class StartPanel extends JPanel implements Runnable{
    //set panel size
    static final int START_WIDTH = 400;
    static final int START_HEIGHT = 200;
    //Dimension 封裝了width,heigjt
    static final Dimension SCREEN_SIZE = new Dimension(START_WIDTH,START_HEIGHT);
    //set label size
    // static final int LABEL_WIDTH = 200;
    // static final int LABEL_HEIGJT = 80;
    static final String LABEL_SOUND = "res/label.wav";
    static final String LABEL_NAME = "/start.png";
    static final String DESCRIPTION = "click to start !";

    SoundHandler label_sound;
    JLabel startLabel;
    //Image image;
    //Graphics graphics;
    boolean start = false;
    ImageIcon label_img;

    
    StartPanel() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        setLabel();
        label_sound = new SoundHandler(LABEL_SOUND);
        this.setBackground(Color.black.darker().darker());
        this.setPreferredSize(SCREEN_SIZE);
        this.setLayout(new BorderLayout());
        this.add(startLabel,BorderLayout.CENTER);

    }

    public void setLabel(){
        //創建start label
        label_img = createImgIcon(LABEL_NAME);
        System.out.println(label_img.getIconWidth());
        System.out.println(label_img.getIconHeight());
        startLabel = new JLabel(DESCRIPTION, label_img, JLabel.CENTER);
        startLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
        startLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        startLabel.setFont(new Font("ComicSansMS",Font.PLAIN,20));
        startLabel.setForeground(Color.white);
        startLabel.setFocusable(true);
        startLabel.addMouseListener(new ML());

    }

    
    public ImageIcon createImgIcon(String path){
        java.net.URL imgURL = getClass().getResource(path);
        if(imgURL != null){
            return new ImageIcon(imgURL);
        }
        else{
            System.out.println("No imageIcon file");
            return null;
        }
    }   
    //確認是否按下start
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
                if(checkStart()){
                    break;
                }
            }
        }   
    }
    public class ML extends MouseAdapter{
        public void mousePressed(MouseEvent e){
            System.out.println("start!");
            label_sound.start();
            start = true;
        }
    }
}
