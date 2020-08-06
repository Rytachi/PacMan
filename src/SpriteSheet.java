import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SpriteSheet {

	private BufferedImage sheet;
	
	public SpriteSheet(String path) {
		try {
			sheet = ImageIO.read(getClass().getResource(path));
		}catch(IOException e) {
			System.out.println("failed to load image");
		}
		
	}
	
	public BufferedImage getSprite(int xx, int yy) {
		
		return sheet.getSubimage(xx, yy, 16, 16);
	}
	
}
