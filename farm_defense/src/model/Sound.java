package model;

import java.io.IOException;
import java.net.URL;

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
	
	
	public Sound() {
		
		soundFile[0] = Sound.class.getClassLoader().getResource("resources/sound/effects/rifle.wav");
		soundFile[1] = Sound.class.getClassLoader().getResource("resources/sound/effects/pistol.wav");
		soundFile[2] = Sound.class.getClassLoader().getResource("resources/sound/effects/reload.wav");
		soundFile[3] = Sound.class.getClassLoader().getResource("resources/sound/effects/zombiedeath.wav");
		soundFile[4] = Sound.class.getClassLoader().getResource("resources/sound/effects/burstRifle.wav");
		soundFile[5] = Sound.class.getClassLoader().getResource("resources/sound/effects/storeEntrance.wav");
		System.out.println(soundFile[0]);
	}
	
	public static void setFile(int i) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile[i]);
			clip = AudioSystem.getClip();
			if(!clip.isOpen()) {
				clip.open(ais);
			}
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {}
	}
	
	public static void play() {
		clip.start();
	}
	
	public static void rifleSound() {
		if(System.currentTimeMillis() - rifleTimerStart > 20) {
			rifleTimerStart = System.currentTimeMillis();
			setFile(0);
			play();
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
		setFile(1);
		play();
	}
	
	public static void zombieDeathSound() {
		if(System.currentTimeMillis() - zombieTimerStart > 20) {
			zombieTimerStart = System.currentTimeMillis();
			setFile(3);
			play();
		}
	}
	
	public static void reloadSound() {
		setFile(2);
		play();
	}
	
	public static void enterStoreSound() {
		setFile(5);
		play();
	}
	
}