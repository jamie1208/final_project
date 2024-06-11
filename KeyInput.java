/*
 * static 用法
 * https://yubin551.gitbook.io/java-note/basic_java_programming/reservedwordstatic/staticvariables
 */
package com.martio.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.event.MenuKeyEvent;


public class KeyInput implements KeyListener{
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		for(Entity en : GamePanel.handler.entities) {
			if(en.getId() == Id.player) {
				if(en.goThroughPipe)return; //正在經過,按任何鍵都無用
				switch(key){
				case KeyEvent.VK_W:
					for(int i=0;i<GamePanel.handler.tiles.size();i++) {
						Tile tile = GamePanel.handler.tiles.get(i); 
						if(tile.getId() == Id.pipe && tile.facing == 0) { //選中水管
							if(en.getTopBound().intersects(tile.getBound())){ //頭上是水管
								en.goThroughPipe  = true;	//通道開啟
								GamePanel.goThroughPipe = true;
								tile.activated = true;
								System.out.println("up pipe open!");
							}
						}
					}
					if(!en.jumping) {
						en.jumping = true; //按w往上跳
						en.gravity = 10;//加速度最大 //改變此值可更改最高高度
						GamePanel.playSound(Player.JUMPSOUND,Player.jumpThread);
						
					}
					break;
				//在水管上時，可以往下
				case KeyEvent.VK_S:
					for(int i=0;i<GamePanel.handler.tiles.size();i++) {
						Tile tile = GamePanel.handler.tiles.get(i); 
						if(tile.getId() == Id.pipe && tile.facing == 1) { //選中水管
							if(en.getBottomBound().intersects(tile.getBound())){ //腳下是水管
								en.goThroughPipe  = true;	//通道開啟
								GamePanel.goThroughPipe = true;
								tile.activated = true;
								System.out.println("down pipe open!");
							}
						}
					}
				case KeyEvent.VK_E:
					if(!en.twice_jumping) {
						//en.jumping = true; //按w往上跳
						en.twice_jumping = true;
						en.gravity = 10;//加速度最大 //改變此值可更改最高高度
						GamePanel.playSound(Player.JUMPSOUND,Player.jumpThread);
					}
					break;
				case KeyEvent.VK_A:
					en.setxVelocity(-5);
					en.facing = 0;
					break;
				case KeyEvent.VK_D:
					en.setxVelocity(5);
					en.facing = 1;
					break;
				case KeyEvent.VK_SPACE:
					if(en.onFire) {
						GamePanel.playSound(Player.ATTACKSOUND,Player.attackThread);
						Entity entity = new FireBall(en.getX()+en.width/2,en.getY()+en.height/2,30,30,true,Id.fireball,GamePanel.handler,en.facing);
						GamePanel.handler.addEntity(entity);	
					}
				}	
			}
		}	
			
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		for(Entity en : GamePanel.handler.entities) {
			if(en.getId() == Id.player) {
				switch(key){
				case KeyEvent.VK_W:
					en.setyVelocity(0);
					break;
				case KeyEvent.VK_A:
					en.setxVelocity(0);
					break;
				case KeyEvent.VK_D:
					en.setxVelocity(0);
					break;
				}	
			}
		}	
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
