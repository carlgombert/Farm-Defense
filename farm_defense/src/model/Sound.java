package model;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
	
	static Clip clip;
	static File[] soundFile = new File[30];
	
	// timers to make sure sounds that repeat very quickly do not play too many times at once. 
	// If a zombie gets hit by 10 bullets at once I don't want 10 death sounds.
	private static long rifleTimerStart = System.currentTimeMillis();
	private static long zombieTimerStart = System.currentTimeMillis();
	
	
	public Sound() {
		
		soundFile[0] = new File("resources/sound/effects/rifle.wav").getAbsoluteFile();
		soundFile[1] = new File("resources/sound/effects/pistol.wav").getAbsoluteFile();
		soundFile[2] = new File("resources/sound/effects/reload.wav").getAbsoluteFile();
		soundFile[3] = new File("resources/sound/effects/zombiedeath.wav").getAbsoluteFile();
	}
	
	public static void setFile(int i) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile[i]);
			clip = AudioSystem.getClip();
			clip.open(ais);
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
	
}
