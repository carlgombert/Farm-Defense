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
		for(int i = 0; i < getObject().size(); i++) {
			GameObject tempObject = getObject().get(i);
			
			tempObject.tick();
		}
	}
	
	public void render(Graphics g) 
	{
		for(int i = 1; i < getObject().size(); i++)
		{
			GameObject tempObject = getObject().get(i);
			tempObject.render(g);
		}
		
		getObject().get(0).render(g); // always renders the player last to draw them above all other objects
	}
	
	public void addObject(GameObject addedObject) 
	{
		this.getObject().add(addedObject);
	}
	
	public void removeObject(GameObject removedObject) {
		if(removedObject.getId() == ID.Zombie) {
			zombiesKilled++;
		}
		this.getObject().remove(removedObject);
	}

	public LinkedList<GameObject> getObject() {
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
