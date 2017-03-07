import static org.lwjgl.opengl.GL11.*;

/**
 * 
 * @author Anthony Watanabe
 *
 */


public class Player extends GameObject {

	private static Player instance;
	private Direction direction;
	
//	public Player(float xCoord, float yCoord, String aName)	{
//		location = new Vertex3f(xCoord, yCoord, 0);
//		name = aName;
//		collider = new RectangularCollider(location, new Vertex3f(100f, 100f, 0));
//		addObserver(collider);
//	}
	
	private Player() {
		location = new Vertex3f(0f, 0f, 0f);
		dimensions = new Vertex3f(60f, 80f, 0f);
		name = "player";
		collider = new RectangularCollider(location, new Vertex3f(60f, 80f, 0));
		direction = Direction.DOWN;
	}
	
	public static Player getInstance() {
		if (instance == null) {
			instance = new Player();
		}
		return instance;
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
	
	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public void render(){		
		//float left = 215 / 4032, right = 915 / 4032, up = 550 / 3024, down = 2010 / 3024;
		float left, right, up, down;
		texture.bind();
		switch (direction) {
			case LEFT: {
				left = 0.65f;
				right = 0.9f;
				up = 0.08f;
				down = 0.5f;
				applyTexture(right, left, up, down);
				break;
			} case DOWN: {
				left = 0.045f;
				right = 0.23f;
				up = 0.1275f;
				down = 0.51f;
				applyTexture(left, right, up, down);
				break;
			} case RIGHT: {
				left = 0.65f;
				right = 0.9f;
				up = 0.08f;
				down = 0.5f;
				applyTexture(left, right, up, down);
				break;
			} case UP: {
				left = 0.35f;
				right = 0.55f;
				up = 0.1f;
				down = 0.51f;
				applyTexture(left, right, up, down);
				break;
			}		
		}
		
	}
	
	private void applyTexture(float left, float right, float up, float down) {
		float xadj = dimensions.getX() / 2, yadj = dimensions.getY() / 2;
		glBegin(GL_QUADS);
			glTexCoord2f(left, up);
			glVertex3f(location.getX() - xadj, location.getY() - yadj, 0.2f);
			glTexCoord2f(right, up);
			glVertex3f(location.getX() + xadj, location.getY() - yadj, 0.2f);
			glTexCoord2f(right, down);
			glVertex3f(location.getX() + xadj, location.getY() + yadj, 0.2f);
			glTexCoord2f(left, down);
			glVertex3f(location.getX() - xadj, location.getY() + yadj, 0.2f);
		glEnd();
	}
}
