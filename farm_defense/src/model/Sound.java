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

import util.MathUtil;

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
		soundFile[13] = Sound.class.getClassLoader().getResource("resources/sound/effects/zombie/zombie_1.wav");
		soundFile[14] = Sound.class.getClassLoader().getResource("resources/sound/effects/zombie/zombie_2.wav");
		soundFile[15] = Sound.class.getClassLoader().getResource("resources/sound/effects/zombie/zombie_3.wav");
		soundFile[16] = Sound.class.getClassLoader().getResource("resources/sound/effects/zombie/zombie_4.wav");
		soundFile[17] = Sound.class.getClassLoader().getResource("resources/sound/effects/zombie/zombie_5.wav");
		soundFile[18] = Sound.class.getClassLoader().getResource("resources/sound/effects/zombie/zombie_6.wav");
		soundFile[19] = Sound.class.getClassLoader().getResource("resources/sound/effects/zombie/zombie_7.wav");
		soundFile[20] = Sound.class.getClassLoader().getResource("resources/sound/effects/zombie/zombie_8.wav");
		soundFile[21] = Sound.class.getClassLoader().getResource("resources/sound/effects/hoe_dirt.wav");
		soundFile[22] = Sound.class.getClassLoader().getResource("resources/sound/effects/empty.wav");
		soundFile[23] = Sound.class.getClassLoader().getResource("resources/sound/effects/footstep.wav");
		
		setFile(8);
		loopedClips.put(2, clip);
		
		setFile(9);
		loopedClips.put(1, clip);
		
		setFile(10);
		loopedClips.put(3, clip);
		
		setFile(23);
		loopedClips.put(4, clip);
		
		for(int i = 0; i <= 23; i++) {
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
		
		// move all clips that ended from the playing queue back to the playable queue
		while(repeatedClips[index][1].peek() != null && repeatedClips[index][1].peek().isRunning() == false) {
			Clip temp = repeatedClips[index][1].remove();
			temp.setMicrosecondPosition(0);
			repeatedClips[index][0].add(temp);
		}
		
		// play the top of the playable queue
		if(repeatedClips[index][0].peek() != null) {
			Clip temp = repeatedClips[index][0].remove();
			temp.start();
			repeatedClips[index][1].add(temp);
		}
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
			play(0);
		}
	}
	
	public static void burstRifleSound() {
		if(System.currentTimeMillis() - rifleTimerStart > 20) {
			rifleTimerStart = System.currentTimeMillis();
			play(4);
		}
	}
	
	public static void pistolSound() {
		play(1);
	}
	
	public static void emptySound() {
		play(22);
	}
	
	public static void zombieSound() {
		int increment = MathUtil.randomNumber(0, 7);
		play(13+increment);
	}
	
	public static void zombieDeathSound() {
		if(System.currentTimeMillis() - zombieTimerStart > 20) {
			zombieTimerStart = System.currentTimeMillis();
			play(3);
		}
	}
	
	public static void reloadSound() {
		play(2);
	}
	
	public static void enterStoreSound() {
		play(5);
	}
	
	public static void buySound() {
		play(6);
	}
	
	public static void sellSound() {
		play(7);
	}
	
	public static void woodBuildSound() {
		play(11);
	}
	
	public static void stoneBuildSound() {
		play(12);
	}
	
	public static void hoeSound() {
		play(21);
	}
	
	public static void stepSound() {
		play(23);
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