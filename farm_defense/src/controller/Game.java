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
import model.gameObjects.Zombie;
import view.HUD;
import view.MapEditorHelper;
import view.Window;
import view.fullMenu.BankruptMenu;
import view.fullMenu.DeathMenu;
import view.fullMenu.MainMenu;
import view.fullMenu.PauseMenu;
import view.fullMenu.StoreMenu;
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
	//hello
	private Thread thread;
	private boolean running = false;
	
	public static final int WIDTH = 16 * 48, HEIGHT = 12 * 48;
	
	public static final int TILE_SIZE = 48;
	
	public static final int MAP_COL = 40;
	public static final int MAP_ROW = 40;
	
	public static final Font DEFAULT_FONT = new Font("Lucida Grande", Font.PLAIN, 13);
	
	public static Handler handler;
	public static HUD hud;
	
	public static Player player;
	
	public static TileManager tileManager;
	public static BuildingManager buildingManager;
	public static FarmingManager farmingManager;
	
	public static MapEditorHelper mapHelper;
	
	public static Inventory inventory;
	
	public static TradeMenu tm;
	
	public static boolean night;
	public static int nightTimer = 0;
	public static double nightCount = 0;
	
	public static GameState gamestate = GameState.MainMenu;
	
	// options for the current state of the game
	public enum GameState {
		Paused(),
		Running(),
		MainMenu(),
		Dead(),
		Bankrupt(),
		Store()
	}
	
	public Game() {
		handler = new Handler();
		
		player = new Player(ID.Player);
		handler.addObject(player);
		
		hud = new HUD(player);
		inventory = new Inventory();
		
		handler.addObject(new NPC(48*16, (int) (48*5.5), ID.NPC));
		
		tm = new TradeMenu();
		
		new Sound();
		
		this.addKeyListener(new KeyInput());
		this.addMouseListener(new KeyInput());
		this.addMouseMotionListener(new KeyInput());
		
		tileManager = new TileManager();
		buildingManager = new BuildingManager();
		farmingManager = new FarmingManager();
		mapHelper = new MapEditorHelper();
		
		handler.addObject(new ZombieSpawner(2, ID.ZombieSpawner));
		
		new Window(WIDTH, HEIGHT, "Zombie Valley", this);
	}
	

	public static void main(String[] args) 
	{
		new Game();
	}
	
	/**
     * Starts the game loop by creating and starting a new thread.
     */
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	/**
     * Stops the game loop by joining the thread.
     */
	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
     * The main game loop responsible for updating and rendering the game.
     */
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
	
	 /**
     * Updates in-game objects' data
     * Manages day/night cycle
     */
	private void tick() {
		if(gamestate == GameState.Running) {
			nightTimer++;
			if(nightTimer >= 10000) {
				nightTimer = 0;
				night = !night;
				nightCount += 0.5;
				player.setHealth(400);
				farmingManager.advanceAllStages(3);
			}
			handler.tick();
		}
	}
	
	/**
     * Renders in-game objects on the screen during the game.
     * Manages different rendering states based on the game state.
     */
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
			
			LightManager.render(g);
			
			handler.render(g);
			
			if(night) {
				tileManager.renderNight(g);
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
		if(gamestate == GameState.Bankrupt) {
			BankruptMenu.render(g);
		}
		if(gamestate == GameState.Store) {
			StoreMenu.render(g);
		}
		g.dispose();
		bs.show();
	}
}