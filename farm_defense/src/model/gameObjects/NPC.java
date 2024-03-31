package model.gameObjects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import controller.Game;
import controller.objectHandling.ID;
import model.GameObject;
import util.ImageUtil;

public class NPC extends GameObject
{
	private final int BOX_PADDING = Game.WIDTH / 68;
	
	private final Rectangle DIALOGUE_BOX = new Rectangle(Game.WIDTH / 20, Game.HEIGHT - 3 * (Game.HEIGHT / 10), Game.WIDTH - 2 * (Game.WIDTH / 20), 2 * (Game.HEIGHT / 10));
	private final Rectangle OUTER_BOX = new Rectangle(DIALOGUE_BOX.x - BOX_PADDING, DIALOGUE_BOX.y - BOX_PADDING, DIALOGUE_BOX.width + (2 * BOX_PADDING), DIALOGUE_BOX.height + (2 * BOX_PADDING));
	
	private final int TEXT_X = DIALOGUE_BOX.x + DIALOGUE_BOX.height + BOX_PADDING*2;
	private final int TEXT_Y = DIALOGUE_BOX.y + BOX_PADDING*3;
	
	private final Color DARK_BROWN = new Color(127, 72, 0);
	private final Color LIGHT_BROWN = new Color(222, 160, 79);
	
	private boolean talking = false;
	private boolean interaction = false;
	
	private ArrayList<String> currentScript = new ArrayList<String>();
	private String currentScriptName = "";
	private int scriptPointer = 0;
	
	private String dialogue1 = "";
	private String dialogue2 = "";
	private String dialogue3 = "";
	
	private String currDialogue1 = "";
	private String currDialogue2 = "";
	private String currDialogue3 = "";
	
	private int currLetter1 = 0;
	private int currLetter2 = 0;
	private int currLetter3 = 0;
	
	public Image currImage;
	public Image headshotImage;
	
	public NPC(int x, int y, ID id)
	{
		super(x, y, id);
		
		loadScript("resources/npcscripts/script_introduction.txt");
		
		currImage = ImageUtil.addImage(75, 75, "resources/npc/cowboy_front.png");
		headshotImage = ImageUtil.addImage(48, 48, "resources/npc/cowboy_headshot.png");
	}

	public void tick() 
	{
		if (interaction && !talking)
		{
			talking = true;
			interaction = false;
			Game.player.setLocked(true);
		}
		
		if (talking)
		{
			if (!currDialogue1.equals(dialogue1))
			{
				currDialogue1 += dialogue1.charAt(currLetter1);
				currLetter1++;
			}
			else if (!currDialogue2.equals(dialogue2))
			{
				currDialogue2 += dialogue2.charAt(currLetter2);
				currLetter2++;
			}
			else if (!currDialogue3.equals(dialogue3))
			{
				currDialogue3 += dialogue3.charAt(currLetter3);
				currLetter3++;
			}
			else
			{
				if (interaction)
				{
					currLetter1 = 0;
					currLetter2 = 0;
					currLetter3 = 0;
					currDialogue1 = "";
					currDialogue2 = "";
					currDialogue3 = "";
					interaction = false;
					
					if (scriptPointer + 1 == currentScript.size())
					{
						// switch scripts if the player has heard the introduction
						if (currentScriptName.equals("resources/npcscripts/script_introduction.txt"))
							loadScript("resources/npcscripts/script_introduction_standby.txt");
						
						advanceDialogue(-1);
						
						talking = false;
						Game.player.setLocked(false);
					}
					else
					{
						if (currentScript.get(scriptPointer).equals("--"))
						{
							talking = false;
							Game.player.setLocked(false);
						}
						
						advanceDialogue(scriptPointer);
					}		
				}
			}
			
			if (interaction)
			{
				currDialogue1 = dialogue1;
				currDialogue2 = dialogue2;
				currDialogue3 = dialogue3;
				interaction = false;
			}
		}
	}

	public void render(Graphics g) 
	{
		if(worldX > Game.player.getWorldX() - Game.WIDTH &&
				worldX < Game.player.getWorldX() + Game.WIDTH &&
				worldY > Game.player.getWorldY() - Game.HEIGHT &&
				worldY < Game.player.getWorldY() + Game.HEIGHT) 
		{
			setScreenX(worldX - Game.player.getWorldX() + Game.player.getScreenX());
			setScreenY(worldY - Game.player.getWorldY() + Game.player.getScreenY());
			
			g.drawImage(currImage, (int) Math.round(getScreenX()), (int) Math.round(getScreenY()), null);
			
			//show hitbox
			//g.setColor(Color.white); g.drawRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
			
			if (talking)
			{
				// talking box
				g.setColor(DARK_BROWN);
				g.fillRoundRect(OUTER_BOX.x, OUTER_BOX.y, OUTER_BOX.width, OUTER_BOX.height, BOX_PADDING, BOX_PADDING); // outer border
				g.setColor(LIGHT_BROWN);
				g.fillRect(DIALOGUE_BOX.x, DIALOGUE_BOX.y, DIALOGUE_BOX.width, DIALOGUE_BOX.height); // inner text box
				g.setColor(DARK_BROWN);
				g.fillRoundRect(OUTER_BOX.x, OUTER_BOX.y, DIALOGUE_BOX.height + BOX_PADDING*2, DIALOGUE_BOX.height + BOX_PADDING*2, BOX_PADDING, BOX_PADDING);
				g.drawImage(headshotImage, DIALOGUE_BOX.x, DIALOGUE_BOX.y, DIALOGUE_BOX.height, DIALOGUE_BOX.height, null);
				g.setColor(Color.white);
				g.setFont(new Font("TimesRoman", Font.PLAIN, DIALOGUE_BOX.height / 4)); 
				g.drawString(currDialogue1, TEXT_X, TEXT_Y);
				g.drawString(currDialogue2, TEXT_X, TEXT_Y + DIALOGUE_BOX.height / 4);
				g.drawString(currDialogue3, TEXT_X, TEXT_Y + 2*(DIALOGUE_BOX.height / 4));
				g.setFont(null);
			}
		}
	}

	public void setInteraction(boolean i)
	{
		interaction = i;
	}
	
  /**
	 * Loads global ArrayList<String> currentScript with the string of each
	 * line in the script .txt file. 
	 * 
	 * @param script (string) filepath of the script to be loaded
	 */
	public void loadScript(String script)
	{
		try {
			URL url = getClass().getClassLoader().getResource(script);
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			
			currentScript.clear();
			currentScriptName = script;
			
			String line = br.readLine();
			while (line != null)
			{
				currentScript.add(line);
				line = br.readLine();
			}
			
			advanceDialogue(-1);
			
			br.close () ;
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
  /**
	 * Determines what the next dialogue that the NPC will say will be
	 * based on the passed pointer.
	 * 
	 * @param pointer integer represent a custom user-defined pointer to the index of the arraylist
	 * 				  to start calculating the next dialogue. Use private variable scriptPointer
	 * 				  if the NPC should continue within the regular script. Use -1 if resetting to
	 * 				  beginning of the script.
	 */
	public void advanceDialogue(int pointer)
	{
		int currentDialogueSwitching = 1;
		
		for (int i = 1; i <= 4; i++)
		{
			if (currentScript.get(pointer + i).equals("-") || currentScript.get(pointer + i).equals("--"))
			{
				scriptPointer = pointer + i;
				
				switch (currentDialogueSwitching)
				{
					case 2:
						dialogue2 = "";
						dialogue3 = "";
						break;
					case 3:
						dialogue3 = "";
						break;
				}
				
				break; // exit loop
			}
			else
			{
				switch (currentDialogueSwitching)
				{
					case 1:
						dialogue1 = currentScript.get(pointer + i);
						currentDialogueSwitching++;
						break;
					case 2:
						dialogue2 = currentScript.get(pointer + i);
						currentDialogueSwitching++;
						break;
					case 3:
						dialogue3 = currentScript.get(pointer + i);
						currentDialogueSwitching++;
						break;
				}
			}
		}
	}
	
	public void setDialogue(String d1, String d2, String d3)
	{
		dialogue1 = d1;
		dialogue2 = d2;
		dialogue3 = d3;
	}
	
	@Override
	public Rectangle getBounds() 
	{
		return new Rectangle(20 + getScreenX(), 20 + getScreenY(), 34, 30);
	}

	@Override
	public Rectangle getSize() 
	{
		return new Rectangle(10, 10, 50, 60);
	}
}
