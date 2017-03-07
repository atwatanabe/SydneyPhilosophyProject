
/**
 * 
 * @author Anthony Watanabe
 *
 */


public class Wall extends GameObject {

	private Vertex3f endpoint0;
	private Vertex3f endpoint1;
	
	
	public Vertex3f getEndpoint0() {
		return endpoint0;
	}

	public void setEndpoint0(Vertex3f endpoint0) {
		this.endpoint0 = endpoint0;
	}

	public Vertex3f getEndpoint1() {
		return endpoint1;
	}

	public void setEndpoint1(Vertex3f endpoint1) {
		this.endpoint1 = endpoint1;
	}

	public Wall(Vertex3f ep0, Vertex3f ep1) {
		endpoint0 = ep0;
		endpoint1 = ep1;
		collider = new RectangularCollider(new Vertex3f((ep0.getX() + ep1.getX()) / 2, (ep0.getY() + ep1.getY()) / 2, 0f), 
				   new Vertex3f(Math.abs(ep1.getX() - ep0.getX()), Math.abs(ep1.getY() - ep0.getY()), 0));
	}
	
	@Override
	public void render() {

	}

}
