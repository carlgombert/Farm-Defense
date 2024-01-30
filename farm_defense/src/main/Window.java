package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.LayoutManager;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.UIManager;

public class Window {
	
	public static JButton startButton;
	public static JButton stopButton;
	public static JSlider gravitySlider;
	public static JLabel gravityLabel;
	public static JSlider velocitySlider;
	public static JLabel velocityLabel;
	public static JSlider angleSlider;
	public static JLabel angleLabel;
	
	public Window(int width, int height, String title, Game game)
	{
		JFrame frame = new JFrame(title); 
		
		frame.setLayout(new BorderLayout());
		
		frame.setSize(width, height);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		
		frame.add(game);
		frame.setVisible(true);
		game.start();
	}
}
