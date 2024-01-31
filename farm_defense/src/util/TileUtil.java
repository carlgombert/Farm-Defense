package util;

import view.map.TileManager;
import controller.Game;
import model.GameObject;

public class TileUtil {
	
	public static void checkTileCollision(GameObject object) {
        int objectLeftWorldX = object.getWorldX() + object.getSize().x;
        int objectRightWorldX = object.getWorldX() + object.getSize().x + object.getSize().width;
        int objectTopWorldY = object.getWorldY() + object.getSize().y;
        int objectBottomWorldY = object.getWorldY() + object.getSize().y + object.getSize().height;

        int objectLeftCol = objectLeftWorldX / Game.tileSize;
        int objectRightCol = objectRightWorldX / Game.tileSize;
        int objectTopRow = objectTopWorldY / Game.tileSize;
        int objectBottomRow = objectBottomWorldY / Game.tileSize;

        int tileNum1, tileNum2;

        
        if(object.getSpeedY() == -5) {
            objectTopRow = (objectTopWorldY + object.getSpeedY()) / Game.tileSize;

            tileNum1 = TileManager.mapTileNum[objectLeftCol][objectTopRow];
            tileNum2 = TileManager.mapTileNum[objectRightCol][objectTopRow];

            if (TileManager.tile[tileNum1].isCollision() || TileManager.tile[tileNum2].isCollision()) {
                object.setTileCollision(true);
                
                
            }
        }
        
        if(object.getSpeedY() == 5) {
            objectBottomRow = (objectBottomWorldY + object.getSpeedY()) / Game.tileSize;

            tileNum1 = TileManager.mapTileNum[objectLeftCol][objectBottomRow];
            tileNum2 = TileManager.mapTileNum[objectRightCol][objectBottomRow];

            if (TileManager.tile[tileNum1].isCollision() || TileManager.tile[tileNum2].isCollision()) {
                object.setTileCollision(true);
                
                
            }
        }
        
        if(object.getSpeedX() == -5) {
            objectLeftCol = (objectLeftWorldX + object.getSpeedX()) / Game.tileSize;

            tileNum1 = TileManager.mapTileNum[objectLeftCol][objectTopRow];
            tileNum2 = TileManager.mapTileNum[objectLeftCol][objectBottomRow];

            if (TileManager.tile[tileNum1].isCollision() || TileManager.tile[tileNum2].isCollision()) {
                object.setTileCollision(true);
                
                
            }
        }
        
        if(object.getSpeedX() == 5) {
            objectRightCol = (objectRightWorldX + object.getSpeedX()) / Game.tileSize;

            tileNum1 = TileManager.mapTileNum[objectRightCol][objectTopRow];
            tileNum2 = TileManager.mapTileNum[objectRightCol][objectBottomRow];

            if (TileManager.tile[tileNum1].isCollision() || TileManager.tile[tileNum2].isCollision()) {
                object.setTileCollision(true);
                
                
            }
        }
    }
}
