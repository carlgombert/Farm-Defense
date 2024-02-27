package util;

import controller.Game;
import model.GameObject;
import model.gameObjects.Player;
import view.map.tile.TileManager;

/**
 * Utility functions tiles. mainly used to check object-tile collisions
 */
public class TileUtil {
	
	public static void checkTileCollision(GameObject object) {
        
		// updated the below commented code to use getbounds instead of getsize, so we can use collision with their hitboxes
		int objectLeftWorldX = object.getWorldX() + object.getBounds().x - object.getScreenX();
		int objectRightWorldX = object.getWorldX() + object.getBounds().x + object.getBounds().width - object.getScreenX();
		int objectTopWorldY = object.getWorldY() + object.getBounds().y - object.getScreenY();
		int objectBottomWorldY = object.getWorldY() + object.getBounds().y + object.getBounds().height - object.getScreenY();
		
		//int objectLeftWorldX = object.getWorldX() + object.getSize().x;
        //int objectRightWorldX = object.getWorldX() + object.getSize().x + object.getSize().width;
        //int objectTopWorldY = object.getWorldY() + object.getSize().y;
        //int objectBottomWorldY = object.getWorldY() + object.getSize().y + object.getSize().height;

        int objectLeftCol = objectLeftWorldX / Game.TILE_SIZE;
        int objectRightCol = objectRightWorldX / Game.TILE_SIZE;
        int objectTopRow = objectTopWorldY / Game.TILE_SIZE;
        int objectBottomRow = objectBottomWorldY / Game.TILE_SIZE;

        int tileNum1, tileNum2;

        if(object.getSpeedY() < 0) {
            objectTopRow = (objectTopWorldY + object.getSpeedY()) / Game.TILE_SIZE;

            tileNum1 = TileManager.mapTileNum[objectLeftCol][objectTopRow];
            tileNum2 = TileManager.mapTileNum[objectRightCol][objectTopRow];

            if (TileManager.tile[tileNum1].isCollision() || TileManager.tile[tileNum2].isCollision()) 
            {
                object.setYTileCollision(true);
            }
            else {
            	object.setYTileCollision(false);
            }
        }
        
        if(object.getSpeedY() > 0) {
            objectBottomRow = (objectBottomWorldY + object.getSpeedY()) / Game.TILE_SIZE;

            tileNum1 = TileManager.mapTileNum[objectLeftCol][objectBottomRow];
            tileNum2 = TileManager.mapTileNum[objectRightCol][objectBottomRow];

            if (TileManager.tile[tileNum1].isCollision() || TileManager.tile[tileNum2].isCollision()) 
            {
                object.setYTileCollision(true);
            }
            else object.setYTileCollision(false);
        }
        
        objectTopRow = objectTopWorldY / Game.TILE_SIZE; // resets these for when checking x tile collision
        objectBottomRow = objectBottomWorldY / Game.TILE_SIZE;
        
        if(object.getSpeedX() < 0) {
            objectLeftCol = (objectLeftWorldX + object.getSpeedX()) / Game.TILE_SIZE;

            tileNum1 = TileManager.mapTileNum[objectLeftCol][objectTopRow];
            tileNum2 = TileManager.mapTileNum[objectLeftCol][objectBottomRow];

            if (TileManager.tile[tileNum1].isCollision() || TileManager.tile[tileNum2].isCollision()) 
            {
                object.setXTileCollision(true);
            }
            else object.setXTileCollision(false);
        }
        
        if(object.getSpeedX() > 0) {
            objectRightCol = (objectRightWorldX + object.getSpeedX()) / Game.TILE_SIZE;

            tileNum1 = TileManager.mapTileNum[objectRightCol][objectTopRow];
            tileNum2 = TileManager.mapTileNum[objectRightCol][objectBottomRow];

            if (TileManager.tile[tileNum1].isCollision() || TileManager.tile[tileNum2].isCollision()) 
            {
                object.setXTileCollision(true);
            }
            else object.setXTileCollision(false);
        }
        
        if(object instanceof Player) { //check if player is infront of the store
        	objectTopRow = (objectTopWorldY - 20) / Game.TILE_SIZE;

            tileNum1 = TileManager.mapTileNum[objectLeftCol][objectTopRow];
            tileNum2 = TileManager.mapTileNum[objectRightCol][objectTopRow];
            
        	if (TileManager.tile[tileNum1].isStore() || TileManager.tile[tileNum2].isStore()) 
            {
                Game.tm.setVisible(true);
            }
            else {
            	Game.tm.setVisible(false);
            }
        }
    }
}
