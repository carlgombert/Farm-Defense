package view;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import model.gameObjects.trader.Trader;
import util.SwingUtil;

public class TradeMenu {
	public Trader trader;
	
	public JFrame frame;
	
	public JPanel mainPanel;
	
	public int unit = 48;
	public int width = unit * 15;
	public int height = unit * 11;
	
	public int tradePosition = 0;
	
	public static JPanel tradeItemPanel;
	public static JPanel tradePanel;
	
	public Color burlyWood = new Color(188,123,25);
	public Color lightBurlyWood = new Color(169,111,23);
	public Color fireBrick = new Color(178,34,34);
	public Color gold = new Color(212,175,55);
	
	public static ArrayList<JPanel> tradePanels;
	
	public static JButton tradeExitButton;
	public static JButton tradeBackButton;
	public static JButton tradeNextButton;
	
	public TradeMenu(Trader trader) {
		this.trader = trader;
		
		tradePanel = SwingUtil.createPanel(tradePanel, null, 0, 0, width, height, burlyWood);
        tradeItemPanel = SwingUtil.createPanel(tradeItemPanel, null, unit/2, unit*3, unit * 14, (int) (unit * 6.5), Color.blue);
        tradeExitButton = SwingUtil.createButton(tradeExitButton, (unit*14+unit/10), (unit*10+unit/10), (unit-unit/10), (unit/2-unit/10), "EXIT", true, fireBrick, gold, null);
        
        tradeNextButton = SwingUtil.createButton(tradeNextButton, (int)(unit * 7.5), (int)(unit*9.75), unit, unit/2, "NEXT", true, lightBurlyWood, Color.black, null);
        tradeNextButton.addActionListener(e -> addTrade(tradePosition+1));
        
        tradeBackButton = SwingUtil.createButton(tradeBackButton, (int)(unit * 6.5), (int)(unit*9.75), unit, unit/2, "BACK", true, lightBurlyWood, Color.black, null);
        tradeBackButton.addActionListener(e -> addTrade(tradePosition-1));
        
		frame = new JFrame("Trade Menu");
		frame.setLayout(new GridLayout());
		frame.setResizable(false);
		frame.setSize(width, height);
		
		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.setBackground(Color.BLUE);
		
		frame.add(mainPanel);
		
		try {
  			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
  		} catch(Exception e){
  			  e.printStackTrace(); 
  		}
        
        mainPanel.removeAll();
		mainPanel.repaint();
        
        addTrade(0);
    	mainPanel.add(tradePanel);
    	
    	frame.setVisible(true);
        
	}
	
	public void addTrade(int position) {
		//sets the global trade position to the new position
		tradePosition = position;
		//creates all the items in the trade and adds them to an arraylist of different
		//trade slides
		tradePanels = createTradeMenu();
		tradeItemPanel.removeAll();
		tradeItemPanel.repaint();
		//checking if the current trade page is the last page. If so remove the next panel
		//button
		if(position == tradePanels.size() - 1) {
			tradeNextButton.setVisible(false);
		}
		else{
			tradeNextButton.setVisible(true);
		}
		//checking if the current trade page is the first page. If so remove the back button
		if(position == 0) {
			tradeBackButton.setVisible(false);
		}
		else{
			tradeBackButton.setVisible(true);
		}
		//adding in the current trade panel
		tradeItemPanel.add(tradePanels.get(position));
	}
	
	public ArrayList<JPanel> createTradeMenu(){
		
		//list of trade slides to be returned
		ArrayList<JPanel> returnList = new ArrayList<JPanel>();
		
		//set up borders and spacing for item panels
		int verticalBorder = unit / 4;
		int horizontalBorder = (unit / 2) / 12;
		
		int n;
		
		//if there arent any consumables, create trade slide for weapons to use
		if(trader.items.size() == 0) {
			JPanel panel = SwingUtil.createPanel(new JPanel(), null, 0, 0, unit * 14, (int) (unit * 6.5), lightBurlyWood);
			returnList.add(panel);
		}
		
		//adding consumables with buttons to an trade slide and creating a new trade slide 
		//everytime the current trade slide fills up (each trade slide fills up once it has 6
		//items)
		for(int i = 0; i < trader.items.size(); i++) {
			
			//creates an invertory page for items
			JPanel panel = SwingUtil.createPanel(new JPanel(), null, 0, 0, unit * 14, (int) (unit * 6.5), lightBurlyWood);
			
			//for n in range of the size of one page, create an item panel and add it to the 
			//trade slide
			for(n = 0; (i + n) < trader.items.size() && n < 6; n++) {
				int index = i+n;
				JPanel itemPanel = SwingUtil.createPanel(new JPanel(), null, verticalBorder, horizontalBorder + (2 * horizontalBorder * n) + (unit * n), (int) (unit * 14 - (verticalBorder*2)), (unit), burlyWood);
				JLabel label = SwingUtil.createLabel(new JLabel(), trader.items.get(index).getName() +  " COST: " + trader.items.get(index).getCost(), verticalBorder, 0, unit*5, unit, Color.BLACK, null);
				
				JButton button = SwingUtil.createButton(new JButton(), unit*12, horizontalBorder, unit, unit-(horizontalBorder*2), "BUY", true, fireBrick, gold, null);
				//TODO
				//button.addActionListener(e -> buy method);
				
				itemPanel.add(label);
				itemPanel.add(button);
				
				panel.add(itemPanel);
			}
			i+=n - 1;
			
			returnList.add(panel);
		}
		return returnList;
	}
	
	
}
