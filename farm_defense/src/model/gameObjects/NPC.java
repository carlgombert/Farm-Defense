package model.gameObjects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import controller.Game;
import controller.objectHandling.ID;
import model.GameObject;
import util.ImageUtil;

// !!!README!!! all of the code in this file is temporary and very poorly organized
// i will probably make a better system for this in the future, but for now this will work
// im not gonna waste time commenting this file, so if u want to understand it ask me
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
		
		dialogue1 = "Hello I'm an NPC";
		dialogue2 = "Test Test Test";
		dialogue3 = "get the fuck away from me!";
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
						talking = false;
						currLetter1 = 0;
						currLetter2 = 0;
						currLetter3 = 0;
						currDialogue1 = "";
						currDialogue2 = "";
						currDialogue3 = "";
						interaction = false;
						Game.player.setLocked(false);
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
		if(!Game.night) {
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
	}

	public void setInteraction(boolean i)
	{
		interaction = i;
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
