package controller.objectHandling;

import java.awt.Graphics;
import java.util.LinkedList;

import model.GameObject;

// handler handles adding, removing, rendering and updating all in game objects. this becomes important
// when many in game objects are constantly being added, removed and moving at once.
public class Handler {
	
	public LinkedList<GameObject> object = new LinkedList<GameObject>();
	
	public void tick() {
		for(int i = 0; i < object.size(); i++) {
			GameObject tempObject = object.get(i);
			
			tempObject.tick();
		}
	}
	
	public void render(Graphics g) 
	{
		for(int i = 1; i < object.size(); i++)
		{
			GameObject tempObject = object.get(i);
			tempObject.render(g);
		}
		
		object.get(0).render(g); // always renders the player last to draw them above all other objects
	}
	
	public void addObject(GameObject addedObject) 
	{
		this.object.add(addedObject);
	}
	
	public void removeObject(GameObject removedObject) {
		this.object.remove(removedObject);
	}
}
