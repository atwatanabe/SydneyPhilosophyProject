
public class WarpZone extends GameObject {

	private Level destination;
	
	public Level getDestination() {
		return destination;
	}
	
	public WarpZone(Level dest){
		destination = dest;
	}
	
	public WarpZone(Vertex3f loc, Vertex3f dim, Level dest) {
		super(loc, dim);
		destination = dest;
		collider = new RectangularCollider(location, dimensions);
	}
	
	@Override
	public void render() {

	}

	
}
