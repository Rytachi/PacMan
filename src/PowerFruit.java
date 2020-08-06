import java.awt.*;


public class PowerFruit extends Rectangle{

	private static final long serialVersionUID = 1L;
	
	public PowerFruit(int x, int y) {
		setBounds(x + 10, y + 10, 10, 10);

	}
	
	public void render(Graphics g) {
		g.drawImage(Texture.powerFruit,x, y, width, height, null);
		
	}
	
}
