import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

/**
 * 
 * @author Anthony Watanabe
 *
 */


public class Player extends GameObject {

	private RectangularCollider collider;

	public Player(float xCoord, float yCoord, String aName)	{
		location = new Vertex3f(xCoord, yCoord, 0);
		name = aName;
		collider = new RectangularCollider(location, new Vertex3f(100f, 100f, 0));
		addObserver(collider);
	}
	
	public void setX(float newX) {
		location.setX(newX);
		notifyObservers();
	}
	
	public void setY(float newY) {
		location.setY(newY);
		notifyObservers();
	}

	public float getX() {
		return location.getX();
	}
	
	public float getY() {
		return location.getY();
	}
	
	public Set<GameObject> getIntersectingObjects() {
		Set<GameObject> result = new HashSet<GameObject>();
		
		return result;
	}
}
