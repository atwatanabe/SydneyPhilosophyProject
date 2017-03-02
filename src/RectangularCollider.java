import java.util.Observable;
import java.util.Observer;



public class RectangularCollider implements Observer {
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

	public boolean intersects(RectangularCollider other) {
		double xDist = Math.abs(location.getX() - other.getLocation().getX())
					- 0.5 * (dimensions.getX() + other.getDimensions().getX());
		double yDist = Math.abs(location.getY() - other.getLocation().getY())
					- 0.5 * (dimensions.getY() + other.getDimensions().getY());
		return xDist <= 0 && yDist <= 0;
	}
	
	public void update(Observable o, Object arg){
		GameObject vehicle = (GameObject) o;
		location.setX(vehicle.getLocation().getX());
		location.setY(vehicle.getLocation().getY());
	}
}
