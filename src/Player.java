import java.awt.*;

public class Player extends Rectangle{


	private static final long serialVersionUID = 1L;
	
	public boolean right, left, up, down;
	
	private int time = 0;
	
	private int targetTime = 10;
	
	private int index = 0;
	
	public int speed = 2; 
		
	private int lastDir = 1;
	
	public int playerLevel = 1;
	
	public boolean fruitEaten = false;
	
	public int xx = 0, yy = 0;
	
	private int ghostIndex = 0;
	
	private int gateX[], gateY[];
	
	public Player(int x, int y) {
		setBounds(x, y, 32, 32);
		xx = x;
		yy = y;
		gateX = new int[2];
		gateY = new int[2];
	}
	
	public void addGate(int x1,int y1, int i) {
		gateX[i] = x1;
		gateY[i] = y1;
	
	}
	
	public void tick() {
		if(right && canMove(x + speed, y)) {
			x+=speed;
			lastDir = 1;
		}
		if(left && canMove(x - speed, y)) {
			x-=speed;
			lastDir = -1;
		}
		if(up && canMove(x, y - speed)) {
			y-=speed;
			lastDir = 2;
		}
		if(down && canMove(x, y + speed)) {
			y+=speed;
			lastDir = -2;
		}
		
		Level level = Game.level;
		
		for(int i = 0; i < level.apples.size(); i++) {
			if(this.intersects(level.apples.get(i))) {
				level.apples.remove(i);
				Game.score.addScore(10);
				break;
			}
			
		}
		
		if(level.apples.size() == 0) {
			if(playerLevel == 1)
				Game.state = Game.STATE.LEVEL2;
			else
				Game.state = Game.STATE.LEVEL3;
			playerLevel++;
			return;
		}
		
		for(int i = 0; i < level.enemies.size(); i++) {
			if(Math.abs(level.enemies.get(i).x - x) <= 10 && Math.abs(level.enemies.get(i).y - y) <= 10) {
				
				if(fruitEaten && level.enemies.get(i).vulnerable) {
					ghostIndex = i;

					Game.score.addScore(200);
					Game.state = Game.STATE.GHOSTCOUGHT;
				}
				else {
					Game.score.removeLife();
					if(Game.score.lives == 0) {
						Game.state = Game.STATE.GAMEOVER;
						return;
					}
					Game.state = Game.STATE.INTERSECTS;
				}
			}
		}
			
		for(int i = 0; i < level.fruits.size(); i++) {
			if(this.intersects(level.fruits.get(i))) {
				fruitEaten = true;
				Game.fruitTime = 0;
				level.fruits.remove(i);
				Game.state = Game.STATE.FRUITEATEN;
				for(int j = 0; j < level.enemies.size(); j++) {
					level.enemies.get(j).vulnerable = true;
					level.enemies.get(j).spd = 1;
				}
			}
		}		
		
		time++;
		
		if(time == targetTime) {
			time = 0;
			index++;
		}
		
		
	}
	
	public boolean canMove(int nextx, int nexty) {
		
		Rectangle bounds = new Rectangle(nextx, nexty, width, height);
		Level level = Game.level;
		
		for(int xx = 0; xx < level.tiles.length; xx++)
			for(int yy = 0; yy < level.tiles[0].length; yy++) {
				if(level.tiles[xx][yy] != null) {
					if(bounds.intersects(level.tiles[xx][yy])) {
						return false;
					}
				}
				else if(xx == gateX[0] && yy == gateY[0] || xx == gateX[1] && yy == gateY[1]) {
					
					Rectangle rec = new Rectangle(xx*32, yy*32, width, height);
					if(bounds.intersects(rec)) {
						return false;
					}
					
				}
				
			}
		
		return true;
	}
	
	public void render(Graphics g) {
		if(lastDir == 1)g.drawImage(Texture.playerRL[index%4], x, y, 32, 32, null);
		else if(lastDir == -1) g.drawImage(Texture.playerRL[index%4], x + 32, y, -32, 32, null);
		else if(lastDir == 2) g.drawImage(Texture.playerUD[index%4], x, y, 32, 32, null);
		else g.drawImage(Texture.playerUD[index%4], x, y + 32, 32, -32, null);
	}
	
	public int returnGhostIndex() {
		return ghostIndex;
	}
	
	public void resetPlayer() {
		x = xx;
		y = yy;
	}
	
}
