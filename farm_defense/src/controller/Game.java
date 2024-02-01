package controller;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;

import javax.imageio.ImageIO;

import controller.objectHandling.Handler;
import controller.objectHandling.ID;
import model.gameObjects.Zombie;
import model.gameObjects.Player;
import model.gameObjects.Turret;
import view.Window;
import view.map.TileManager;

public class Game extends Canvas implements Runnable{
	
	private static final long serialVersionUID = -5505267217615912489L;
	
	private Thread thread;
	private boolean running = false;
	public static final int WIDTH = 16 * 48, HEIGHT = 12 * 48;
	
	public static Handler handler;
	
	public static int tileSize = 48;
	
	public static int mapCol = 40;
	public static int mapRow = 40;
	
	public static Player player;
	
	public static TileManager tileManager;
	
	public Game() {
		handler = new Handler();
		
		player = new Player(ID.Player);
		handler.addObject(player);
		
		
		
		Zombie zombie = new Zombie(24*48, 24*48, ID.Zombie);
		handler.addObject(zombie);
		handler.addObject(new Turret(48*15, 48*20, ID.Turret));
		
		this.addKeyListener(new KeyInput(player));
		this.addMouseListener(new KeyInput(player));
		
		tileManager = new TileManager();
		
		new Window(WIDTH, HEIGHT, "Zombie Valley", this);
	}
	
	public static void main(String[] args) 
	{
		new Game();
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		this.requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				tick();
				delta--;
			}
			if(running)
				render();
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000) {
				System.out.println("FPS: " + frames);
				timer += 1000;
				frames = 0;
			}
			
		}
		stop();
	}
	
	// tick method updates data of in game objects
	private void tick() {
		handler.tick();
	}
	
	// render method displays in game objects on the screen
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		//rendering the tilemanager renders the background map
		tileManager.render(g);
		
		//tileManager.renderNightFade(g);
		//tileManager.renderNightConstant(g);
		
		//rendering handler renders all gameobjects
		handler.render(g);
		
		g.dispose();
		bs.show();
	}
}
