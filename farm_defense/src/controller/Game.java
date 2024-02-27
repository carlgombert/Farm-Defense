package controller;

import java.awt.Canvas;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import controller.objectHandling.Handler;
import controller.objectHandling.ID;
import model.gameObjects.ZombieSpawner;
import model.Sound;
import model.Inventory.Inventory;
import model.gameObjects.NPC;
import model.gameObjects.Player;
import view.HUD;
import view.KeyInput;
import view.MapEditorHelper;
import view.Window;
import view.fullMenu.DeathMenu;
import view.fullMenu.MainMenu;
import view.fullMenu.PauseMenu;
import view.map.LightManager;
import view.map.building.BuildingManager;
import view.map.farming.FarmingManager;
import view.map.tile.TileManager;
import view.sideMenu.TradeMenu;
import view.sideMenu.TurretMenu;

/**
 * The Game class is a manager for the entire Game.
 * This class is the highest level of manager and the startpoint, inililizing 
 * every element of the game and containing the runtime algorithm which handles
 * the rendering of graphics and updating of object data
 */
public class Game extends Canvas implements Runnable{
	
	private static final long serialVersionUID = -5505267217615912489L;
	
	private Thread thread;
	private boolean running = false;
	public static final int WIDTH = 16 * 48, HEIGHT = 12 * 48;
	
	public static Handler handler;
	public static HUD hud;
	
	public static int tileSize = 48;
	
	public static int mapCol = 40;
	public static int mapRow = 40;
	
	public static Player player;
	
	public static TileManager tileManager;
	public static BuildingManager buildingManager;
	public static FarmingManager farmingManager;
	
	public static MapEditorHelper mapHelper;
	
	public static Inventory inventory;
	
	public static Font defaultFont;
	
	public static TradeMenu tm;
	public static TurretMenu turm;
	
	public static boolean night;
	public static int nightTimer = 0;
	
	public static GameState gamestate = GameState.MainMenu;
	
	// the following are the 3 conditions for the player to go bankrupt. 
	// noCoins will be changes by player, noCrops by farming manager and
	// badInventory by inventory.
	public static boolean noCoins = false; // not actually no coins but not enough to buy seeds
	public static boolean badInventory = false; // if the player doesn't have seeds or crops
	public static boolean noCrops = false; // if there aren't crops planted
	
	public enum GameState {
		Paused(),
		Running(),
		MainMenu(),
		Dead()
	}
	
	public Game() {
		handler = new Handler();
		
		player = new Player(ID.Player);
		handler.addObject(player);
		
		hud = new HUD(player);
		inventory = new Inventory();
		
		handler.addObject(new NPC(48*15, 48*20, ID.NPC));
		
		tm = new TradeMenu();
		
		turm = new TurretMenu();
		
		new Sound();
		
		defaultFont = new Font("Lucida Grande", Font.PLAIN, 13);
		
		this.addKeyListener(new KeyInput());
		this.addMouseListener(new KeyInput());
		this.addMouseMotionListener(new KeyInput());
		
		tileManager = new TileManager();
		buildingManager = new BuildingManager();
		farmingManager = new FarmingManager();
		mapHelper = new MapEditorHelper();
		
		handler.addObject(new ZombieSpawner(10, ID.ZombieSpawner));
		
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
		if(gamestate == GameState.Running) {
			nightTimer++;
			if(nightTimer >= 10000) {
				nightTimer = 0;
				night = !night;
				player.setHealth(400);
			}
			if(badInventory && (noCoins && noCrops)) { // check if the player is bankrupt
				gamestate = GameState.Dead;
			}
			handler.tick();
		}
	}
	
	// render method displays in game objects on the screen
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		if(gamestate == GameState.Running) {
			//rendering the tilemanager renders the background map
			tileManager.render(g);
			
			buildingManager.render(g);
			farmingManager.render(g);
			
			// renders the highlight on the tiles when player is building/farming, needs to be rendered last (in terms of map renders)
			mapHelper.render(g);
			
			//tileManager.renderNightFade(g);
			LightManager.render(g);
			//rendering handler renders all gameobjects
			handler.render(g);
			
			
			if(tm.visible) {
				tm.render(g);
			}
			else if(turm.visible) {
				turm.render(g);
			}
			
			if(night) {
				tileManager.renderNightConstant(g);
			}
			
			hud.render(g);
		}
		if(gamestate == GameState.MainMenu) {
			MainMenu.render(g);
		}
		if(gamestate == GameState.Paused) {
			PauseMenu.render(g);
		}
		if(gamestate == GameState.Dead) {
			DeathMenu.render(g);
		}
		g.dispose();
		bs.show();
	}
}
