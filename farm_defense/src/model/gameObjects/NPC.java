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
	
	private int dialogueBoxX = Game.WIDTH / 20;
	private int dialogueBoxY = Game.HEIGHT - 3 * (Game.HEIGHT / 10);
	private int dialogueBoxWidth = Game.WIDTH - 2 * (Game.WIDTH / 20);
	private int dialogueBoxHeight = 2 * (Game.HEIGHT / 10);
	
	private int boxPadding = Game.WIDTH / 68;
	
	private int outerBoxX = dialogueBoxX - boxPadding;
	private int outerBoxY = dialogueBoxY - boxPadding;
	private int outerBoxWidth = dialogueBoxWidth + (2 * boxPadding);
	private int outerBoxHeight = dialogueBoxHeight + (2 * boxPadding);
	
	private int textX = dialogueBoxX + dialogueBoxHeight + boxPadding*2;
	private int textY = dialogueBoxY + boxPadding*3;
	
	private Color darkBrown = new Color(127, 72, 0);
	private Color lightBrown = new Color(222, 160, 79);
	
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
				g.setColor(darkBrown);
				g.fillRoundRect(outerBoxX, outerBoxY, outerBoxWidth, outerBoxHeight, boxPadding, boxPadding); // outer border
				g.setColor(lightBrown);
				g.fillRect(dialogueBoxX, dialogueBoxY, dialogueBoxWidth, dialogueBoxHeight); // inner text box
				g.setColor(darkBrown);
				g.fillRoundRect(outerBoxX, outerBoxY, dialogueBoxHeight + boxPadding*2, dialogueBoxHeight + boxPadding*2, boxPadding, boxPadding);
				g.drawImage(headshotImage, dialogueBoxX, dialogueBoxY, dialogueBoxHeight, dialogueBoxHeight, null);
				g.setColor(Color.white);
				g.setFont(new Font("TimesRoman", Font.PLAIN, dialogueBoxHeight / 4)); 
				g.drawString(currDialogue1, textX, textY);
				g.drawString(currDialogue2, textX, textY + dialogueBoxHeight / 4);
				g.drawString(currDialogue3, textX, textY + 2*(dialogueBoxHeight / 4));
				g.setFont(null);
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
