package com.martio.game;

import java.awt.Graphics;
import java.util.Random;

public class Player extends Entity{
	private Random random = new Random();
	
	private PlayerState state;
	private int frame = 0;
	private int frame_delay = 0;//更新速度(player animation 更新)
	private int pixelsTravelled = 0;
	private int index = 0;
	private boolean animate = false; //是否要動
	private int onFireTime = 0;
	private Sprite[] playerSprites = new Sprite[10];
	
	public static Thread gotWaffleThread;
	public static Thread gotPineconeThread;
	public static Thread jumpThread;
	public static Thread gotMushroomThread;
	public static Thread looseThread;
	public static Thread stepEnemyThread;
	public static Thread attackThread;
	public static Thread winThread;
	public static Thread getAttackThread;
	
	
	public final static String GOTWAFFLESOUND = "res/gotwaffle.wav";
	public final static String GOTPINECONESOUND = "res/gotPinecone.wav";
	public final static String JUMPSOUND = "res/jump.wav";
	public final static String GOTMUSHROOMSOUND = "res/gotMushroom.wav";
	public final static String LOOSE = "res/loose.wav";
	public final static String WIN = "res/win.wav";
	public final static String STEPENEMYSOUND = "res/stepEnemy.wav";
	public final static String ATTACKSOUND = "res/attack.wav";
	public final static String GETATTACKSOUND = "res/getAttack.wav";
	
	
	public Player(int x, int y, int width, int height, boolean solid, Id id, Handler handler) {
		super(x, y, width, height, solid, id, handler);
		state = PlayerState.SMALL;
		int direction = random.nextInt(2);//初始往哪個方向跑 0-left 1-right
		switch (direction) {
		case 0: 
			setxVelocity(-2);
			facing = 0;
			break;
		case 1:
			setxVelocity(2);
			facing = 1;
			break;
		}
	}
	
	//player的image
	public void draw(Graphics g) {
		if(onFire == false) playerSprites = GamePanel.players_sprite;
		else if(onFire) playerSprites = GamePanel.players_sprite_onfire;
		
		if(jumping && state == PlayerState.DIE) {
			g.drawImage(playerSprites[8].getBufferedImage(), x, y, width, height, null);
		}
		if(falling && state == PlayerState.DIE) {
			g.drawImage(playerSprites[9].getBufferedImage(), x, y, width, height, null);
		}
		else {
			if(facing == 0) index = frame+4;
			else index = frame;
			g.drawImage(playerSprites[index].getBufferedImage(), x, y, width, height, null);	
		}
	}

	public void update() {
		move();
		//if goThroughPipe -> collision and jumpOrFall不會進行
		if(state != PlayerState.DIE) {
			checkCollision(); //確認是否有撞擊	
		}
		jumpOrfall(); 
		if(xVelocity!=0 && state != PlayerState.DIE) animate = true;
		else animate = false;
		if(animate) {
			animation();	
		}
		if(goThroughPipe) {
			System.out.println("go:"+goThroughPipe);
			goingThroughPipe();
		}
		if(onFire) {
			onFireTime ++;
			System.out.println("onFireTime = "+onFireTime);
			if(onFireTime >= 600){//持續十秒
				onFire = false;
				onFireTime = 0;
			}
		}
	}
	
	public void move() {
		x += xVelocity; //先移動
		y += yVelocity;
	}
	
	public void checkCollision() {
		boolean hasb_collect = false; //在所有的tile中,只要有一個collent bottom,則falling=false;
		//player and tile collision (wood,waffle)
		for(int i=0;i<handler.tiles.size();i++) {
			Tile t = handler.tiles.get(i);
			if(t.isSolid() && !goThroughPipe) {
				if(getTopBound().intersects(t.getBound()) && t.getId() != Id.waffle) {
					setyVelocity(0); //之後不會繼續往上
					if(jumping) {
						jumping = false;
						twice_jumping = false;
						gravity = 0.8; //一定要設定代表到最高點了,否則會在上面停留至先前jump的gravity(+)減至0為止,設為0.8是為了必免降落延遲
						falling = true;
					}
					//往上撞到powerup
					if(t.getId() == Id.powerUp) {
						t.activated = true;
					}
				}
				//player bottom撞到 tile
				if(getBottomBound().intersects(t.getBound()) && t.getId() != Id.waffle) {
					//System.out.println("BottomBound!");
					setyVelocity(0); //只要在地上,velocity永遠都0
					if(falling) falling = false;
					hasb_collect = true;
				}
				else if(!falling&&!jumping&& !hasb_collect && t.getId() != Id.waffle){ //if底下無東西
					gravity = 0.8;
					falling = true;
				}
				//player left撞到 tile
				if(getLeftBound().intersects(t.getBound()) && t.getId() != Id.waffle) {
					//System.out.println("LeftBound!");
					setxVelocity(0);
					x = t.x+t.width;
				}
				//player right撞到 tile
				if(getRightBound().intersects(t.getBound()) && t.getId() != Id.waffle) {
					//System.out.println("RightBound!");
					setxVelocity(0); 
					x = t.x-width;
				}
				//player 撞到鬆餅
				if(getBound().intersects(t.getBound()) && t.getId() == Id.waffle) {
					GamePanel.get_waffle += 1;
					GamePanel.playSound(GOTWAFFLESOUND, gotWaffleThread);
					System.out.println("waffle die");
					t.die();
				}
			}
		}
		
		for(int i=0;i<handler.entities.size();i++) {
			Entity en = handler.entities.get(i);
			//player and pinecone collision
			if(en.getId() == Id.pinecone) { 
				if(getBound().intersects(en.getBound())) { //只能長大一次
					if(state == PlayerState.SMALL) {
						state = PlayerState.BIG;
						int tpx = getX();
						int tpy = getY();
						width*=2; //改變長寬會使xy隨機transport
						height*=2;
						setX(tpx-width/2);
						setY(tpy-height/2);
						GamePanel.playSound(GOTPINECONESOUND, gotPineconeThread);
						en.die();
					}
					else{
						en.die();
					}
					System.out.println("pinecone die!");
					break;
				}
			}
			//player and mushroom collision
			else if(en.getId() == Id.mushroom) {
				System.out.println("onfire= "+onFire);
				if(getBound().intersects(en.getBound())) {
					onFire = true;
					onFireTime = 0;
					System.out.println("mushroom die!");
					GamePanel.playSound(GOTMUSHROOMSOUND, gotMushroomThread);
					en.die();
					break;
				}
			}
			//player and enemy
			else if (en.getId() == Id.enemy) {
				//上面踩 - enemy die
				if(getBottomBound().intersects(en.getBound())) {
					en.die();
					GamePanel.killed_enemy += 1;
					System.out.println("ennmy die!");
					GamePanel.playSound(STEPENEMYSOUND, stepEnemyThread);
					if(GamePanel.level == GamePanel.killed_enemy) {
						GamePanel.playSound(WIN, winThread);
						//GamePanel.success = true;
					}
					break;
				}
				else if(getBound().intersects(en.getBound())) { //other - player die
					GamePanel.playSound(GETATTACKSOUND, getAttackThread);
					//沒有長大 -> 消失
					if(state == PlayerState.SMALL) {
						//die();
						en.die();
						System.out.println("entity and enemy die!");
						GamePanel.lives -= 1;
						GamePanel.now_live_image = GamePanel.lives_image[GamePanel.lives];
						if(GamePanel.lives == 0) {
							state = PlayerState.DIE;
							gravity = 5;
							jumping = true;
							twice_jumping = false;
							GamePanel.playSound(LOOSE, looseThread);
							System.out.println("Garvity = "+gravity);
						}
						System.out.println(GamePanel.lives);
						break;
					}
					//長大 -> 變小
					else if(state == PlayerState.BIG) {
						System.out.println("PlayerState"+state);
						int tpx = getX();
						int tpy = getY();
						width/=2; //改變長寬會使xy隨機transport
						height/=2;
						setX(tpx-width/2);
						setY(tpy-height/2);
						System.out.println("enemy die!");
						state = PlayerState.SMALL;
						en.die();
						break;
					}
				}
			}
		}
	}
	
	public void jumpOrfall() {
		if(jumping  && !goThroughPipe) {
			if(gravity<=0.0) { //跳到最高點
				jumping = false;
				twice_jumping = false;
				falling = true;
			}	
			gravity -= 0.2; //越跳加速度越慢
			setyVelocity(-(int)gravity); //向上		
		}
		if(falling && !goThroughPipe) {
			gravity += 0.2; //加速度越來越快
			setyVelocity((int)gravity); //向下
			if(state == PlayerState.DIE &&  gravity >= 20) {
				GamePanel.running = false;
			}
		}
	}
	
	public void animation() {
		frame_delay++; 
		if(frame_delay>=10) { //螢幕每更新n次,變換一次動作(更改此處的值,可更改animation轉換時間)
			frame++; //變換第幾個動作
			if(frame>=GamePanel.players_sprite.length/2-2) {
				frame = 0;
			}
			frame_delay = 0;
		}
	}
	
	public void goingThroughPipe() {
		for(int i=0;i<handler.tiles.size();i++){
			Tile tile = handler.tiles.get(i);
			if(tile.getId() == Id.pipe && tile.activated) {
				//下降到完全進入pipe,visible = false
				if(pixelsTravelled>height) {
					visible = false;
				}
				
				//通過pipe (body長度加上2*head長度),visible = true
				if(pixelsTravelled>tile.height+height) {
					visible = true;
					System.out.printf("tile height = %d\n",tile.height);
					System.out.println("high = "+pixelsTravelled);
					System.out.println("finish");
					goThroughPipe = false;
					GamePanel.goThroughPipe = false;
					pixelsTravelled = 0;
					tile.activated = false;
					if(tile.facing == 0) {
						gravity = 0;
					}
					System.out.println("goThroughPipe = "+goThroughPipe);
					System.out.println("pipe close :"+tile.facing);
				}
				//看往上or下
				switch(tile.facing) {
				case 0: //上
					setyVelocity(-5);
					setxVelocity(0);
					pixelsTravelled -= yVelocity;
					break;
				case 1: //下
					setyVelocity(5);
					setxVelocity(0);
					pixelsTravelled += (yVelocity);
					break;
				}
			}
		}
	}
}
