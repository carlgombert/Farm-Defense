package model;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * The Sound class serves as an audioplayer for music and different in game events
 */
public class Sound {
	
	private static Clip clip;
	private static URL[] soundFile = new URL[30];
	
	// timers to make sure sounds that repeat very quickly do not play too many times at once. 
	// If a zombie gets hit by 10 bullets at once I don't want 10 death sounds.
	private static long rifleTimerStart = System.currentTimeMillis();
	private static long zombieTimerStart = System.currentTimeMillis();
	
	/*
	 * 0: no music
	 * 1: day music
	 * 2: night music
	 * 3: menu music
	 * */
	private static int currSong = 0;
	
	// stores pointers to the clips that are currently being looped
	// this way I can grab them and stop them later
	private static HashMap<Integer, Clip> loopedClips = new HashMap<>();
	
	private static Queue<Clip>[][] repeatedClips = new LinkedList[30][2];
	
	
	public Sound() {
		
		soundFile[0] = Sound.class.getClassLoader().getResource("resources/sound/effects/rifle.wav");
		soundFile[1] = Sound.class.getClassLoader().getResource("resources/sound/effects/pistol.wav");
		soundFile[2] = Sound.class.getClassLoader().getResource("resources/sound/effects/reload.wav");
		soundFile[3] = Sound.class.getClassLoader().getResource("resources/sound/effects/zombiedeath.wav");
		soundFile[4] = Sound.class.getClassLoader().getResource("resources/sound/effects/burstRifle.wav");
		soundFile[5] = Sound.class.getClassLoader().getResource("resources/sound/effects/storeEntrance.wav");
		soundFile[6] = Sound.class.getClassLoader().getResource("resources/sound/effects/buy.wav");
		soundFile[7] = Sound.class.getClassLoader().getResource("resources/sound/effects/sell.wav");
		soundFile[8] = Sound.class.getClassLoader().getResource("resources/sound/music/Nightfall.wav");
		soundFile[9] = Sound.class.getClassLoader().getResource("resources/sound/music/test_music.wav");
		soundFile[10] = Sound.class.getClassLoader().getResource("resources/sound/music/test_menu.wav");
		soundFile[11] = Sound.class.getClassLoader().getResource("resources/sound/effects/wood_wall.wav");
		soundFile[12] = Sound.class.getClassLoader().getResource("resources/sound/effects/stone_wall.wav");
		
		setFile(8);
		loopedClips.put(2, clip);
		
		setFile(9);
		loopedClips.put(1, clip);
		
		setFile(10);
		loopedClips.put(3, clip);
		
		for(int i = 0; i < 13; i++) {
			repeatedClips[i][0] = new LinkedList<>();
			repeatedClips[i][1] = new LinkedList<>();
			for(int j = 0; j < 10; j++) {
				setFile(i);
				repeatedClips[i][0].add(clip);
			}
		}
	}
	
	public static void setFile(int i) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile[i]);
			clip = AudioSystem.getClip();
			if(!clip.isOpen()) {
				clip.open(ais);
			}
		} catch (LineUnavailableException | IllegalStateException | IOException | OutOfMemoryError | UnsupportedAudioFileException e) {
			System.out.println("clip not played");
		}
	}
	
	public static void play() {
		clip.start();
	}
	
	public static void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public static void play(int index) {
		loopedClips.get(index).stop();
		loopedClips.get(index).setMicrosecondPosition(0);
		loopedClips.get(index).start();
	}
	
	public static void pause(int index) {
		loopedClips.get(index).stop();
	}
	
	public static void resume(int index) {
		loopedClips.get(index).loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public static void restart(int index) {
		loopedClips.get(index).setMicrosecondPosition(0);
	}
	
	public static void stop(int index) {
		loopedClips.get(index).setMicrosecondPosition(0);
		loopedClips.get(index).stop();
	}
	
	public static void rifleSound() {
		if(System.currentTimeMillis() - rifleTimerStart > 20) {
			rifleTimerStart = System.currentTimeMillis();
			while(repeatedClips[0][1].peek() != null && repeatedClips[0][1].peek().isRunning() == false) {
				Clip temp = repeatedClips[0][1].remove();
				temp.setMicrosecondPosition(0);
				repeatedClips[0][0].add(temp);
			}
			if(repeatedClips[0][0].peek() != null) {
				Clip temp = repeatedClips[0][0].remove();
				temp.start();
				repeatedClips[0][1].add(temp);
			}
		}
	}
	
	public static void burstRifleSound() {
		if(System.currentTimeMillis() - rifleTimerStart > 20) {
			rifleTimerStart = System.currentTimeMillis();
			setFile(4);
			play();
		}
	}
	
	public static void pistolSound() {
		while(repeatedClips[1][1].peek() != null && repeatedClips[1][1].peek().isRunning() == false) {
			Clip temp = repeatedClips[1][1].remove();
			temp.setMicrosecondPosition(0);
			repeatedClips[1][0].add(temp);
		}
		if(repeatedClips[1][0].peek() != null) {
			Clip temp = repeatedClips[1][0].remove();
			temp.start();
			repeatedClips[1][1].add(temp);
		}
	}
	
	public static void zombieDeathSound() {
		if(System.currentTimeMillis() - zombieTimerStart > 20) {
			zombieTimerStart = System.currentTimeMillis();
			while(repeatedClips[3][1].peek() != null && repeatedClips[3][1].peek().isRunning() == false) {
				Clip temp = repeatedClips[3][1].remove();
				temp.setMicrosecondPosition(0);
				repeatedClips[3][0].add(temp);
			}
			if(repeatedClips[3][0].peek() != null) {
				Clip temp = repeatedClips[3][0].remove();
				temp.start();
				repeatedClips[3][1].add(temp);
			}
		}
	}
	
	public static void reloadSound() {
		while(repeatedClips[2][1].peek() != null && repeatedClips[2][1].peek().isRunning() == false) {
			Clip temp = repeatedClips[2][1].remove();
			temp.setMicrosecondPosition(0);
			repeatedClips[2][0].add(temp);
		}
		if(repeatedClips[2][0].peek() != null) {
			Clip temp = repeatedClips[2][0].remove();
			temp.start();
			repeatedClips[2][1].add(temp);
		}
	}
	
	public static void enterStoreSound() {
		while(repeatedClips[5][1].peek() != null && repeatedClips[5][1].peek().isRunning() == false) {
			Clip temp = repeatedClips[5][1].remove();
			temp.setMicrosecondPosition(0);
			repeatedClips[5][0].add(temp);
		}
		if(repeatedClips[5][0].peek() != null) {
			Clip temp = repeatedClips[5][0].remove();
			temp.start();
			repeatedClips[5][1].add(temp);
		}
	}
	
	public static void buySound() {
		while(repeatedClips[6][1].peek() != null && repeatedClips[6][1].peek().isRunning() == false) {
			Clip temp = repeatedClips[6][1].remove();
			temp.setMicrosecondPosition(0);
			repeatedClips[6][0].add(temp);
		}
		if(repeatedClips[6][0].peek() != null) {
			Clip temp = repeatedClips[6][0].remove();
			temp.start();
			repeatedClips[6][1].add(temp);
		}
	}
	
	public static void sellSound() {
		while(repeatedClips[7][1].peek() != null && repeatedClips[7][1].peek().isRunning() == false) {
			Clip temp = repeatedClips[7][1].remove();
			temp.setMicrosecondPosition(0);
			repeatedClips[7][0].add(temp);
		}
		if(repeatedClips[7][0].peek() != null) {
			Clip temp = repeatedClips[7][0].remove();
			temp.start();
			repeatedClips[7][1].add(temp);
		}
	}
	
	public static void woodBuildSound() {
		while(repeatedClips[11][1].peek() != null && repeatedClips[11][1].peek().isRunning() == false) {
			Clip temp = repeatedClips[11][1].remove();
			temp.setMicrosecondPosition(0);
			repeatedClips[11][0].add(temp);
		}
		if(repeatedClips[11][0].peek() != null) {
			Clip temp = repeatedClips[11][0].remove();
			temp.start();
			repeatedClips[11][1].add(temp);
		}
	}
	
	public static void stoneBuildSound() {
		while(repeatedClips[12][1].peek() != null && repeatedClips[12][1].peek().isRunning() == false) {
			Clip temp = repeatedClips[12][1].remove();
			temp.setMicrosecondPosition(0);
			repeatedClips[12][0].add(temp);
		}
		if(repeatedClips[12][0].peek() != null) {
			Clip temp = repeatedClips[12][0].remove();
			temp.start();
			repeatedClips[12][1].add(temp);
		}
	}
	
	public static void nightMusic() {
		switch(currSong) {
			case 1:
				stopDayMusic();
				break;
			case 2:
				stopNightMusic();
				break;
			case 3:
				stopMenuMusic();
				break;
		}
		currSong = 2;
		resume(currSong);
	}
	
	public static void stopNightMusic() {
		stop(2);
	}
	
	public static void pauseNightMusic() {
		pause(2);
	}
	
	public static void dayMusic() {
		switch(currSong) {
			case 1:
				stopDayMusic();
				break;
			case 2:
				stopNightMusic();
				break;
			case 3:
				stopMenuMusic();
				break;
		}
		currSong = 1;
		resume(currSong);
	}
	
	public static void pauseDayMusic() {
		pause(1);
	}
	
	public static void stopDayMusic() {
		stop(1);
	}
	
	public static void menuMusic() {
		switch(currSong) {
			case 1:
				stopDayMusic();
				break;
			case 2:
				stopNightMusic();
				break;
			case 3:
				stopMenuMusic();
				break;
		}
		currSong = 3;
		resume(currSong);
	}
	
	public static void stopMenuMusic() {
		stop(3);
	}
	
	public static void noMusic() {
		switch(currSong) {
			case 1:
				stopDayMusic();
				break;
			case 2:
				stopNightMusic();
				break;
			case 3:
				stopMenuMusic();
				break;
		}
		currSong = 0;
	}
	
	
	
}