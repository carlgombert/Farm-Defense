package model.skin;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class SkinManager {
	
	private HashMap<String, Skin> skins = new HashMap<String, Skin>();
	
	public SkinManager() {
		skins.put("old", new Skin("resources/player"));
		skins.put("carl", new Skin("resources/skins/carl"));
	}
	
	public Skin getSkin(String name) {
		return skins.getOrDefault(name, null);
	}
}
