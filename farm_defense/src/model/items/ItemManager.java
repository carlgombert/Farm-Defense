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
		itemImages[32] = ImageUtil.addImage(16, 16, "resources/inventory/seeds/inventory_seeds_cauliflower.png");
		itemImages[33] = ImageUtil.addImage(16, 16, "resources/inventory/seeds/inventory_seeds_potato.png");
		itemImages[34] = ImageUtil.addImage(16, 16, "resources/inventory/seeds/inventory_seeds_pepper.png");
		itemImages[35] = ImageUtil.addImage(16, 16, "resources/inventory/seeds/inventory_seeds_watermelon.png");
		itemImages[36] = ImageUtil.addImage(16, 16, "resources/inventory/seeds/inventory_seeds_sunflower.png");
		
		// ids in the 40s will be grown crops
		itemImages[40] = ImageUtil.addImage(16, 16, "resources/inventory/inventory_carrot.png");
		itemImages[41] = ImageUtil.addImage(16, 16, "resources/inventory/inventory_corn.png");
		itemImages[42] = ImageUtil.addImage(16, 16, "resources/inventory/inventory_cauliflower.png");
		itemImages[43] = ImageUtil.addImage(16, 16, "resources/inventory/inventory_potato.png");
		itemImages[44] = ImageUtil.addImage(16, 16, "resources/inventory/inventory_pepper.png");
		itemImages[45] = ImageUtil.addImage(16, 16, "resources/inventory/inventory_watermelon.png");
		itemImages[46] = ImageUtil.addImage(16, 16, "resources/inventory/inventory_sunflower.png");
		
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
		itemNames[32] = "Cauliflower Seeds";
		itemNames[33] = "Potato Seeds";
		itemNames[34] = "Pepper Seeds";
		itemNames[35] = "Watermelon Seeds";
		itemNames[36] = "Sunflower Seeds";
		
		itemNames[40] = "Carrot";
		itemNames[41] = "Corn";
		itemNames[42] = "Cauliflower";
		itemNames[43] = "Potato";
		itemNames[44] = "Pepper";
		itemNames[45] = "Watermelon";
		itemNames[46] = "Sunflower";
		
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
