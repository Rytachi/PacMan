import java.awt.*;


public class Apple extends Rectangle{

	private static final long serialVersionUID = 1L;

	public Apple(int x, int y) {
		setBounds(x + 10, y + 10, 4, 4);

	}
	
	public void render(Graphics g) {
		g.drawImage(Texture.food, x + 5, y + 5, width, height, null);
		
	}
}
