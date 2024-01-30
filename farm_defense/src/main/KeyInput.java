package main;

import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class KeyInput extends KeyAdapter implements MouseListener{
	
	private Player player;
	
	public KeyInput(Player player) {
		this.player = player;
	}
	
	public void mouseClicked(MouseEvent e) {
		
	}
	
	public void mouseReleased(MouseEvent e) {
		
	}
	
	public void mousePressed(MouseEvent e) {

	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
			
		if(key == KeyEvent.VK_UP || key == KeyEvent.VK_W) player.setSpeedY(-5);
		if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) player.setSpeedY(5);
		if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) player.setSpeedX(-5);
		if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) player.setSpeedX(5);
			
		if(key == KeyEvent.VK_ESCAPE) {
			System.exit(1);
		}
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if(key == KeyEvent.VK_UP || key == KeyEvent.VK_W) player.setSpeedY(0);
		if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) player.setSpeedY(0);
		if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) player.setSpeedX(0);
		if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) player.setSpeedX(0);
	}

	

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
