package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;

import javax.imageio.ImageIO;

public class Game extends Canvas implements Runnable{
	
	private static final long serialVersionUID = -5505267217615912489L;
	
	private Thread thread;
	private boolean running = false;
	public static final int WIDTH = 16 * 48, HEIGHT = 12 * 48;
	
	private Handler handler;
	
	public static Player player;
	
	public Game() {
		handler = new Handler();
		
		player = new Player(ID.Player);
		handler.addObject(player);
		
		this.addKeyListener(new KeyInput(player));
		this.addMouseListener(new KeyInput(player));
		
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
		
		handler.render(g);
		
		g.dispose();
		bs.show();
	}
	
	public static BufferedImage addImage(int width, int height, String file) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return image;
	}
}
