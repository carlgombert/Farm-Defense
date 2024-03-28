package model.items;

import java.awt.image.BufferedImage;

import util.ImageUtil;

public class ItemManager {
	private static Item[] items = new Item[60];
	
	public static void loadItems() {
		BufferedImage[] itemImages = new BufferedImage[60];
		String[] itemNames = new String[60];
		
		// item id 0 is the hoe
		itemImages[0] = ImageUtil.addImage(16, 16, "resources/inventory/inventory_hoe.png");
		
		// ids 1-9 will be guns
		itemImages[1] = ImageUtil.addImage(16, 16, "resources/inventory/inventory_gun.png");
		
		// ids in the 10s will be melee weapons
		itemImages[10] = ImageUtil.addImage(16, 16, "resources/inventory/inventory_sword.png");
		
		// ids in the 20s will be walls
		itemImages[20] = ImageUtil.addImage(16, 16, "resources/inventory/walls/inventory_wall_wood.png");
		itemImages[21] = ImageUtil.addImage(16, 16, "resources/inventory/walls/inventory_wall_stone.png");
		
		// ids in the 30s will be seeds
		itemImages[30] = ImageUtil.addImage(16, 16, "resources/inventory/seeds/inventory_seeds_carrot.png");
		itemImages[31] = ImageUtil.addImage(16, 16, "resources/inventory/seeds/inventory_seeds_corn.png");
		
		// ids in the 40s will be grown crops
		itemImages[40] = ImageUtil.addImage(16, 16, "resources/inventory/inventory_carrot.png");
		
		// id 50 will be turret
		itemImages[50] = ImageUtil.addImage(16, 16, "resources/inventory/inventory_turret.png");
		
		// id 51 will be torch
		itemImages[51] = ImageUtil.addImage(16, 16, "resources/tiles/torch.png");
		
		
		itemNames[0] = "Hoe";
		
		itemNames[1] = "Pistol";
		
		itemNames[10] = "Sword";
		
		itemNames[20] = "Wooden Wall";
		itemNames[21] = "Stone Wall";
		
		itemNames[30] = "Carrot Seeds";
		itemNames[31] = "Corn Seeds";
		
		itemNames[40] = "Carrot";
		
		itemNames[50] = "Turret";
		itemNames[51] = "Torch";
		
		for(int i = 0; i < 60; i++) {
			if(itemImages[i] != null) {
				items[i] = new Item(i, itemImages[i], itemNames[i]);
			}
		}
	}
	
	public static Item getItem(int id) {
		return items[id];
	}
}
