import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Texture {
	
	private static int pics = 16;
	public static BufferedImage[] playerRL;
	public static BufferedImage[] playerUD;
	
	public static BufferedImage[][] ghosts;
	
	public static BufferedImage title, food, ready, game_over, powerFruit;

	public static BufferedImage backGround;
	
	public static BufferedImage backGroundWhite;
	
	public Texture(String path1, String path2) {
		playerRL = new BufferedImage[4];
		playerUD = new BufferedImage[4];
		ghosts = new BufferedImage[17][4];
		
		for(int i = 0; i < 4; i++) {
			
			playerRL[i] = Game.spritesheet.getSprite(i*pics, 0);
			playerUD[i] = Game.spritesheet.getSprite(i*pics, 16);
			
		}
		
		for(int i = 0; i < 17; i++) {
			for(int j = 0; j < 4; j++) {
				
				ghosts[i][j] = Game.spritesheet.getSprite(j * pics, pics * 2 + pics * i);	
			}
		}
		
		try {
			
			title = ImageIO.read(getClass().getResource("resources/title.png"));
			ready = ImageIO.read(getClass().getResource("resources/ready.png"));
			game_over = ImageIO.read(getClass().getResource("resources/gameover.png"));
			food = ImageIO.read(getClass().getResource("resources/food.png"));
			powerFruit = ImageIO.read(getClass().getResource("resources/powerBall.png"));
			
			backGround = ImageIO.read(getClass().getResource(path1));
			backGroundWhite = ImageIO.read(getClass().getResource(path2));
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
