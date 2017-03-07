import org.newdawn.slick.opengl.Texture;
import java.util.Observable;

/**
 * 
 * @author Anthony Watanabe
 *
 */

public abstract class GameObject extends Observable {
	protected Texture texture;
	protected String name;
	protected Vertex3f location;
	protected Vertex3f dimensions;
	protected RectangularCollider collider;
	
	public RectangularCollider getCollider() {
		return collider;
	}

	public GameObject(){
		
	}
	
	public GameObject(Vertex3f loc, Vertex3f dim){
		location = loc;
		dimensions = dim;
		collider = new RectangularCollider(location, dimensions);
	}
	
	public Vertex3f getLocation() {
		return location;
	}

	public void setLocation(Vertex3f loc) {
		location.setX(loc.getX());
		location.setY(loc.getY());
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Vertex3f getDimensions() {
		return dimensions;
	}

	public void setDimensions(Vertex3f dimensions) {
		this.dimensions = dimensions;
	}

	public abstract void render();
	
}
