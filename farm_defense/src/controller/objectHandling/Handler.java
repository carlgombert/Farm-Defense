package controller.objectHandling;

import java.awt.Graphics;
import java.util.LinkedList;

import model.GameObject;
import model.gameObjects.Zombie;

/**
 * the Handler class handles adding, removing, rendering and updating all in game objects. 
 * this becomes important when many in game objects are constantly being added,
 * removed and moving at once.
 */
public class Handler {
	
	private LinkedList<GameObject> object = new LinkedList<GameObject>();
	private int zombiesKilled;
	
	public void tick() {
		for(int i = 0; i < getObjects().size(); i++) {
			GameObject tempObject = getObjects().get(i);
			
			tempObject.tick();
		}
	}
	
	public void render(Graphics g) 
	{
		for(int i = 1; i < getObjects().size(); i++)
		{
			GameObject tempObject = getObjects().get(i);
			tempObject.render(g);
		}
		
		getObjects().get(0).render(g); // always renders the player last to draw them above all other objects
	}
	
	public void addObject(GameObject addedObject) 
	{
		this.getObjects().add(addedObject);
	}
	
	public void removeObject(GameObject removedObject) {
		if(removedObject.getId() == ID.Zombie) {
			zombiesKilled++;
		}
		this.getObjects().remove(removedObject);
	}

	public LinkedList<GameObject> getObjects() {
		return object;
	}

	public void setObject(LinkedList<GameObject> object) {
		this.object = object;
	}

	public int getZombiesKilled() {
		return zombiesKilled;
	}

	public void setZombiesKilled(int zombiesKilled) {
		this.zombiesKilled = zombiesKilled;
	}
}
