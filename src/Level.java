import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 
 * @author Anthony Watanabe
 *
 */

public class Level extends GameObject {
	private Set<GameObject> objects;
	private Vertex3f spawnPoint;
	
	public Level(Vertex3f loc, Vertex3f dim) {
		super(loc, dim);
		objects = new HashSet<GameObject>();
		spawnPoint = new Vertex3f(loc.getX(), loc.getY(), loc.getX());
	}
	
	public Set<GameObject> getIntersectingObjects(GameObject target) {
		Set<GameObject> result = new HashSet<GameObject>();
		
		Iterator<GameObject> iter = objects.iterator();
		while (iter.hasNext()){
			GameObject temp = iter.next();
			if (target.getCollider().intersects(temp.getCollider()))
				result.add(temp);
		}
		
		return result;
	}
	
	public Set<GameObject> getObjectsInDirection(GameObject target, Direction dir) {
		Set<GameObject> result = new HashSet<GameObject>();
		Iterator<GameObject> iter = objects.iterator();
		while (iter.hasNext()) {
			GameObject temp = iter.next();
			switch (dir) {
				case LEFT: {
					if (temp.getLocation().getX() < target.getLocation().getX())
						result.add(temp);
					break;
				} case RIGHT: {
					if (temp.getLocation().getX() > target.getLocation().getX())
						result.add(temp);
					break;
				} case UP: {
					if (temp.getLocation().getY() > target.getLocation().getY())
						result.add(temp);
					break;
				} case DOWN: {
					if (temp.getLocation().getY() < target.getLocation().getY())
						result.add(temp);
					break;
				}
			}
		}
		return result;
	}
	
	public boolean canMove(GameObject target, Direction dir) {
		Set<GameObject> objs = getIntersectingObjects(target);
		Set<GameObject> left = new HashSet<GameObject>();
		Set<GameObject> right = new HashSet<GameObject>();
		Set<GameObject> up = new HashSet<GameObject>();
		Set<GameObject> down = new HashSet<GameObject>();
		
		Iterator<GameObject> iter = objs.iterator();
		while (iter.hasNext()) {
			GameObject temp = iter.next();
			if (temp.getLocation().getX() <= target.getLocation().getX())
				left.add(temp);
			else
				right.add(temp);
			if (temp.getLocation().getY() <= target.getLocation().getY())
				down.add(temp);
			else
				up.add(temp);
		}
		
		switch (dir) {
			case LEFT: {
				iter = left.iterator();
				while (iter.hasNext()) {
					GameObject temp = iter.next();
					if (target.getCollider().getYDist(temp.getCollider()) < 0)
						return false;
				}
				break;
			} case RIGHT: {
				iter = right.iterator();
				while (iter.hasNext()) {
					GameObject temp = iter.next();
					if (target.getCollider().getYDist(temp.getCollider()) < 0)
						return false;
				}
				break;
			} case UP: {
				iter = up.iterator();
				while (iter.hasNext()) {
					GameObject temp = iter.next();
					if (target.getCollider().getXDist(temp.getCollider()) < 0)
						return false;
				}
				break;
			} case DOWN: {
				iter = down.iterator();
				while (iter.hasNext()) {
					GameObject temp = iter.next();
					if (target.getCollider().getXDist(temp.getCollider()) < 0)
						return false;
				}
				break;
			}
		}
		return true;
	}
	
	public Set<GameObject> getObjects() {
		return objects;
	}

	public void setObjects(Set<GameObject> objects) {
		this.objects = objects;
	}

	public boolean addObject(GameObject toAdd) {
		return objects.add(toAdd);
	}
	
	public boolean removeObject(GameObject toRemove) {
		return objects.remove(toRemove);
	}
	
	public Vertex3f getSpawnPoint() {
		return spawnPoint;
	}

	public void setSpawnPoint(Vertex3f spawnPoint) {
		this.spawnPoint = spawnPoint;
	}

	public void render(){
		float left = 0f, right = 0.8f, up = 0f, down = 0.6f;
		float xadj = dimensions.getX() / 2, yadj = dimensions.getY() / 2;
		texture.bind();
		glBegin(GL_QUADS);
			glTexCoord2f(left, up);
			glVertex2f(0, 0);
			glTexCoord2f(right, up);
			glVertex2f(dimensions.getX(), 0);
			glTexCoord2f(right, down);
			glVertex2f(dimensions.getX(), dimensions.getY());
			glTexCoord2f(left, down);
			glVertex2f(0, dimensions.getY());
		glEnd();
		
		Iterator<GameObject> iter = objects.iterator();
		while (iter.hasNext()){
			GameObject temp = iter.next();
			temp.render();
		}
	}
}
