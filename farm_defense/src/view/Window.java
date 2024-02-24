package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.LayoutManager;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.ImageIcon;

import controller.Game;
import util.ImageUtil;

public class Window {
	
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
