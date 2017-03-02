import org.newdawn.slick.opengl.Texture;
import java.util.Observable;

/**
 * 
 * @author Anthony
 *
 */

public class GameObject extends Observable {
	protected Texture texture;
	protected String name;
	protected Vertex3f location;
	
	public Vertex3f getLocation() {
		return location;
	}

	public void setLocation(Vertex3f location) {
		this.location = location;
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
}
