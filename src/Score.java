import java.awt.*;

public class Score {
	
	public String scoreZeros = "00000";
	
	private String score = "00000";
	
	private String highScore = "00000";
	
	private int targetScore = 100;
	
	public int lives = 3;
	
	public int whLife = 140;
	
	private boolean ready = true;
	
	public void render(Graphics g) {
		Font fnt = new Font("Serif", Font.PLAIN, 20);
		g.setFont(fnt);
		g.setColor(Color.WHITE);
		
		g.drawString("SCORE", 0, Game.HEIGHT-45);
		g.drawString(score, 0, Game.HEIGHT-20);
		g.drawString("HIGH SCORE", 150, Game.HEIGHT-45);
		g.drawString(highScore, 150, Game.HEIGHT-20);
		g.drawString("LIVES", Game.WIDTH - 200, Game.HEIGHT-45);
		
		if(lives == 2) {
		g.drawImage(Texture.playerRL[3], Game.WIDTH - 200, Game.HEIGHT - 45 , 32, 32, null);
		g.drawImage(Texture.playerRL[3], Game.WIDTH - 170, Game.HEIGHT - 45 , 32, 32, null);
		
		}
		else if(lives == 1) {
			g.drawImage(Texture.playerRL[3], Game.WIDTH - 200, Game.HEIGHT - 45 , 32, 32, null);
			
		}
		else if(lives == 0) {}
		else {
			g.drawImage(Texture.playerRL[3], Game.WIDTH - 200, Game.HEIGHT - 45 , 32, 32, null);
			g.drawImage(Texture.playerRL[3], Game.WIDTH - 170, Game.HEIGHT - 45 , 32, 32, null);
			g.drawImage(Texture.playerRL[3], Game.WIDTH - 140, Game.HEIGHT - 45 , 32, 32, null);
			
		}
		
		
	}
	public void addScore(int score) {
		String zeros;
		
		int tempScore;
		
		tempScore = Integer.parseInt(this.score) + score;
		
		if(tempScore == targetScore) {
			targetScore *= 10;
			String newstr = scoreZeros.substring(0, 0) + scoreZeros.substring(0 + 1);
			scoreZeros = newstr;
		}
		
		zeros = scoreZeros + Integer.toString(tempScore);
		
		if(Integer.parseInt(zeros) >= Integer.parseInt(this.highScore))
			this.highScore = zeros;
		
		this.score = zeros;
	}
	
	public void renderReady(Graphics g) {
		
		if(ready)
			g.drawImage(Texture.ready, Game.WIDTH/2 - 35, Game.HEIGHT/2 + 10, 75, 25, null);
		
		ready = ((int) (System.nanoTime() * 0.0000000035) % 3) > 0;
		
	}
	
	public void renderGameOver(Graphics g) {
		
		if(ready)
			g.drawImage(Texture.game_over, Game.WIDTH/2 - 35, Game.HEIGHT/2 + 10, 75, 25, null);
		
		ready = ((int) (System.nanoTime() * 0.0000000075) % 3) > 0;
		
	}
	
	public void renderGhostEaten(Graphics g, int x, int y) {
		Font fnt = new Font("Serif", Font.PLAIN, 9);
		g.setFont(fnt);
		g.setColor(Color.WHITE);
		g.drawString("200", x, y);
		
	}
	
	public void removeLife() {
		whLife += 30;
		lives--;
	}
	
	public void resetScore() {
		this.score = "00000";
		lives = 3;
	}
	
}
