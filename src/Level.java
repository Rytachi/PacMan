import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Level {

	public int width;
	public int height;
	
	public Tile[][] tiles;
	
	public List<Apple> apples;
	
	public List<Enemy>enemies;
	
	public List<PowerFruit>fruits;
	
	BufferedImage map;
	
	private boolean fruitBlink = true;
		
	public Level(String path) {
		
		apples = new ArrayList<>();
		enemies = new ArrayList<>();
		fruits = new ArrayList<>();
		
		int nr = 0;
		
		for(int i = 0; i < 4; i++) {
			enemies.add(new Enemy(i));
		}
		
		try {
			
		map = ImageIO.read(getClass().getResource(path));
		
		this.width = map.getWidth();
		
		this.height = map.getHeight();
		
		int[] pixels = new int[width*height];
		
		tiles = new Tile[width][height];
		
		map.getRGB(0, 0, width, height, pixels, 0, width);
		
		int g = 0;
		
		for(int xx = 0; xx < width; xx++) {
			for(int yy = 0; yy < height; yy++) {

				int val = pixels[xx + (yy*width)];
				if(val == 0xFF000000) {
					tiles[xx][yy] = new Tile(xx*32, yy*32);
				}
				else if(val == 0xFFFFD800) {
					fruits.add(new PowerFruit(xx*32, yy*32));
				}
				else if(val == 0xFF0000FF) {
					Game.player.x = xx*32;
					Game.player.y = yy*32;
					Game.player.xx = xx*32;
					Game.player.yy = yy*32;
					apples.add(new Apple(xx*32, yy*32));
				}
				else if(val == 0xFFFF0000) {
					enemies.get(nr).addGhost(xx*32, yy*32);
					nr++;
				}
				else if(val == 0xFF4CFF00) {
					Game.player.addGate(xx, yy, g);
					int a = 0;
					for(int i = 0; i < enemies.size(); i++) {
						a++;
						enemies.get(i).addGate(xx, yy, g);
						System.out.println(a);
				}
					g++;
				}
				else {
					apples.add(new Apple(xx*32, yy*32));
				}
			}
		}
		
		}catch(IOException e){
			e.printStackTrace();	
		}
	}
	
	public void tick() {
		for(int i = 0; i < enemies.size();i++) {
			enemies.get(i).tick();
		}
		
	}
	
	public void render(Graphics g) {
		g.drawImage(Texture.backGround, 0, 0, 640, 480, null);
		
		for(int i = 0; i < apples.size(); i++) {
			apples.get(i).render(g);
		}
		if(fruitBlink)
			for(int i = 0; i < fruits.size();i++) {
				fruits.get(i).render(g);
			}
		for(int i = 0; i < enemies.size();i++) {
			enemies.get(i).render(g);
		}
		
		fruitBlink = ((int) (System.nanoTime() * 0.0000000035) % 3) > 0;	
	}
	
	public void renderLevel(Graphics g) {
		g.drawImage(Texture.backGround, 0, 0, 640, 480, null);	
		
		for(int i = 0; i < apples.size(); i++) {
			apples.get(i).render(g);
		}
		for(int i = 0; i < fruits.size();i++) {
			fruits.get(i).render(g);
		}
	}
	
	public void renderLevelCleared(Graphics g) {
		if(fruitBlink)
			g.drawImage(Texture.backGround, 0, 0, 640, 480, null);
		else
			g.drawImage(Texture.backGroundWhite, 0, 0, 640, 480, null);
		fruitBlink = ((int) (System.nanoTime() * 0.0000000045) % 3) > 0;
	}
	
	public void resetActors() {
		
		for(int i = 0; i < enemies.size(); i++)
			enemies.get(i).resetGhost();
		
		Game.player.resetPlayer();
	}
	
	public void renderOneGhost(int i) {
		enemies.get(i).resetGhost();
	}
	
	public void increaseDifficulty() {
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).increaseEnemyDifficulty();
		}
	}

}
