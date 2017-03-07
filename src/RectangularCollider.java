import java.util.Observable;
import java.util.Observer;

/**
 * 
 * @author Anthony Watanabe
 *
 */

public class RectangularCollider implements Observer{
	private Vertex3f location;
	private Vertex3f dimensions;
	
	public RectangularCollider(Vertex3f loc, Vertex3f dim) {
		location = loc;
		dimensions = dim;
	}
	
	public Vertex3f getLocation() {
		return location;
	}
	
	public Vertex3f getDimensions() {
		return dimensions;
	}
	
	public void setLocation(Vertex3f location) {
		this.location = location;
	}

	public void setDimensions(Vertex3f dimensions) {
		this.dimensions = dimensions;
	}

	/*
	 * checks if this collider intersects with @other collider
	 * only works with rectangular colliders that have horizontal/vertical sides
	 */
	public boolean intersects(RectangularCollider other) {
		return getXDist(other) <= 0 && getYDist(other) <= 0;
	}

	public double getYDist(RectangularCollider other) {
		return Math.abs(location.getY() - other.getLocation().getY())
					- 0.5 * (dimensions.getY() + other.getDimensions().getY());
	}

	public double getXDist(RectangularCollider other) {
		return Math.abs(location.getX() - other.getLocation().getX())
					- 0.5 * (dimensions.getX() + other.getDimensions().getX());
	}
	
	public void update(Observable o, Object arg){
		GameObject vehicle = (GameObject) o;
		location.setX(vehicle.getLocation().getX());
		location.setY(vehicle.getLocation().getY());
	}

}
