import java.awt.*;


public class Menu {

	private boolean pushStartVisible = true;

	public void render(Graphics g) {
		Font fnt = new Font("Serif", Font.PLAIN, 24);
		g.setFont(fnt);
		g.setColor(Color.WHITE);
		
		if(pushStartVisible)
			g.drawString("Push space to Start", Game.WIDTH / 2 - 100, Game.HEIGHT - 200);
		
		pushStartVisible = ((int) (System.nanoTime() * 0.0000000075) % 3) > 0;
	
		g.drawImage(Texture.title, Game.WIDTH/2 - 220, Game.HEIGHT/2-150, 450, 115, null);
	}
	
}
