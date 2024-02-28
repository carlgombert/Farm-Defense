package view;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import controller.Game;

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
