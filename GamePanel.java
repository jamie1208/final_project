package com.martio.game;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.SwingUtilities;

//Canvas 畫布 Graphics 畫筆
//creatbufferStrategy -> 畫筆dipose()後,緩衝區show,就呈現在frames上了
//creatImage -> 建立兩個畫筆,先畫在緩衝區,再將緩衝區畫在panel
public class GamePanel extends Canvas implements Runnable{
	
	//set panel size
	public static final int GAME_WIDTH = 320;
	public static final int GAME_HEIGHT = 180;
	public static final int SCALE = 4; //規模 1280*720
	//Dimension 封裝了width,height
	Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH*SCALE,GAME_HEIGHT*SCALE);
	public static boolean success;
	public int success_delay = 0;
	public static boolean running;
	public static boolean goThroughPipe;
	public static int get_waffle;
	public static int lives;
	public static int killed_enemy;
	
	private static BufferedImage background;
	public static BufferedImage location_image;
	public static BufferedImage[] lives_image = new BufferedImage[4];
	public static BufferedImage now_live_image;
	public static SpriteSheet sheet;
	public static Handler handler; //static 代表此handler為所有gamePanel class 共有
	public static Camera camera;
	public static EnemyLives enemyLives;
	
	
	public static Sprite w_front_wood;
	public static Sprite w_back_wood;
	public static Sprite w_middle_wood;
	public static Sprite w_all_wood;
	public static Sprite h_front_wood;
	public static Sprite h_back_wood;
	public static Sprite h_middle_wood;
	public static Sprite h_all_wood;
	public static Sprite pinecone;
	public static Sprite powerUp;
	public static Sprite usedPowerUp;
	public static Sprite pipe_head;
	public static Sprite pipe_body;
	public static Sprite pipe_foot;
	public static Sprite waffle;
	public static Sprite fireball;
	public static Sprite mushroom;
	public static Sprite egg;
	public static Sprite usedEgg;
	public static Sprite[] players_sprite;//for animation
	public static Sprite[] players_sprite_onfire;//for animation
	public static Sprite[] enemy_sprites;
	
	public Random random;
	
	
	//設定panel 的初始畫面
	public GamePanel() {
		System.out.println("game1Panel construcroer!");
		this.setPreferredSize(SCREEN_SIZE);
		goThroughPipe = false;
		killed_enemy = 0;
		get_waffle = 0;
		lives = 3;
		running = true;
		success = false;
	}
	
	//初始設定
	public void init() {
		random = new Random();
		handler = new Handler(); //遊戲開始時,創建handler
		sheet = new SpriteSheet("/SpriteSheet.png");
		camera = new Camera();
		try {
			background = ImageIO.read(getClass().getResource("/background.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		addKeyListener(new KeyInput());
		requestFocus();
		
		//橫向
		w_front_wood = new Sprite(sheet, 1, 2);
		w_middle_wood = new Sprite(sheet, 2, 2);
		w_back_wood = new Sprite(sheet, 3, 2);
		w_all_wood = new Sprite(sheet, 4, 2);
		//直向
		h_front_wood = new Sprite(sheet, 1, 3);
		h_middle_wood = new Sprite(sheet, 1, 4);
		h_back_wood = new Sprite(sheet, 1, 5);
		h_all_wood = new Sprite(sheet, 1, 6);
		
		pinecone = new Sprite(sheet, 6, 1);
		powerUp = new Sprite(sheet, 4, 1);
		usedPowerUp = new Sprite(sheet, 5, 1);
		pipe_head = new Sprite(sheet, 7, 1);
		pipe_body = new Sprite(sheet, 7, 2);
		pipe_foot = new Sprite(sheet, 7, 3);
		waffle = new Sprite(sheet, 8, 1);
		fireball = new Sprite(sheet, 9, 1);
		mushroom = new Sprite(sheet, 3, 1);
		egg = new Sprite(sheet, 10, 1);
		usedEgg = new Sprite(sheet, 11, 1);
		
		players_sprite = new Sprite[10];
		players_sprite_onfire = new Sprite[10];
		enemy_sprites = new Sprite[8];
		
		//player
		for(int i=0;i<players_sprite.length;i++) {
			players_sprite[i] = new Sprite(sheet, i+1, 14);	
		}
		for(int i=0;i<players_sprite_onfire.length;i++) {
			players_sprite_onfire[i] = new Sprite(sheet, i+1, 13);	
		}
		//enemy
		int e_sprite = 7+random.nextInt(1); //有幾種enemy,nextInt放幾
		for(int i=0;i<enemy_sprites.length;i++) {
			enemy_sprites[i] = new Sprite(sheet, i+1, e_sprite);	
		}
		//lives
		try {
			lives_image[0] = ImageIO.read(getClass().getResource("/love0.png"));
			lives_image[1] = ImageIO.read(getClass().getResource("/love1.png"));
			lives_image[2] = ImageIO.read(getClass().getResource("/love2.png"));
			lives_image[3] = ImageIO.read(getClass().getResource("/love3.png"));
			now_live_image = lives_image[3];
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			location_image = ImageIO.read(getClass().getResource("/location.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//handler.addEntity(new Player(400, 400, 64, 64, true, Id.player, handler));
		handler.creatLevel(location_image);
	}
	
	public static int getGameWidth() {
		return GAME_WIDTH*SCALE;
	}
	
	public static int getGameHeight() {
		return GAME_HEIGHT*SCALE;
	}
	
	public static Rectangle getVisibleArea() {
		for(int i=0;i<handler.entities.size();i++) {
			Entity entity = handler.entities.get(i);
			if(entity.getId() == Id.player)return new Rectangle(entity.getX()-getGameWidth()/2+5,entity.getY()-getGameHeight()/2+5,getGameWidth()+100,getGameHeight()+100);
		}
		return null;
	}
	
	//為了建立單獨的thread
    public static void playSound(String SOUND,Thread sound){
        try{
            sound = new SoundHandler(SOUND);
            Thread thread = new Thread(sound);
            thread.start();
        }catch(Exception e){System.out.println("ERROR SOUND!");}
    }
    
	//建立緩衝區並顯示在frames
	public void render() {
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
	
	
	//繪製於緩衝區
	public void draw(Graphics g) {
		g.drawImage(background, 0,0,getGameWidth(),getGameHeight(),null);
		g.translate(camera.getX(), camera.getY());
		handler.draw(g);
		g.drawImage(waffle.getBufferedImage(),20-camera.getX(),100+20-camera.getY(),70,70,null);
		g.setColor(Color.BLACK);
		g.setFont(new Font("Courier",Font.BOLD,50));
		g.drawString("x"+get_waffle,100-camera.getX(),180-camera.getY());
		g.drawImage(now_live_image, 20-camera.getX(),20-camera.getY(),210,70,null);
		enemyLives = new EnemyLives(20-camera.getX(), getGameHeight()-40-camera.getY(), getGameWidth()-40, 20);
		enemyLives.draw(g);
		
	}
	
	//呼叫時update
	public void update(){
		handler.update();
		
		for(Entity en:handler.entities) {
			if(en.getId() == Id.player) {
				//通過水管時畫面不動 
				//camera.update(en);
				if(!en.goThroughPipe)camera.update(en); //以player為基準移動camera
			}
		}
		enemyLives.update();
	}
	
	public void run() {
		init();
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();//毫秒
		double delta = 0.0;
		double ns = 1000000000/60.0;
		int frames = 0; //螢幕更新
		int ticks = 0; //畫布更新
		while(running) {
			Long now = System.nanoTime();
			delta += (now-lastTime)/ns;
			if(Game.level == killed_enemy) {
				success = true;
				render();
				render();
				try {
					Game.gameThread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Window window = SwingUtilities.getWindowAncestor(GamePanel.this);
            	if(window != null) {
            		window.dispose();
            	}
				return;
			}
			lastTime = now;
			while(delta>1) {//大於1/60秒(60赫茲)
				update();
				ticks ++;
				delta --;
			}
			render(); //相當於repaint
			frames ++;	
			if(System.currentTimeMillis()-timer>1000) {//大於1秒(1000毫秒)
				timer += 1000;
				//System.out.println(frames+" Frames Per Second "+ticks+" Updates Per Second");
				ticks = 0;
			}
		}
		Game.frame.remove(this);
		try {
			Game.endPanel = new EndPanel();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Game.endThread = new Thread(Game.endPanel);	
        Game.endThread.start();
        Game.frame.add(Game.endPanel);
        Game.frame.setFrame();
	}
}
