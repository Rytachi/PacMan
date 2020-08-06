import java.awt.*;
import java.util.Random;

public class Enemy extends Rectangle{
	
	private static final long serialVersionUID = 1L;

	private int random = 0, smart = 1, find_path = 2;
	
	private int state = random;
	
	private int right = 0, left = 1, up = 2, down = 3;
	
	private int dir = -1, time = 0, targetTime = 60*4, smartTime = 60*3;

	public int spd = 2, lastDir = -1, index = 0, time1 = 0;
	
	private int targetIndex = 10,nr = 0, xx = 0, yy = 0;

	public boolean vulnerable = false, almostVEnd = false;
	
	private int gateX[], gateY[];
	
	public boolean pastTheGate = false;
	
	public Random randomGen;
	
	public Enemy(int nr) {
		randomGen = new Random();
		dir = randomGen.nextInt(4);
		gateX = new int [2];
		gateY = new int [2];
		this.nr = nr;
	}
	
	public void addGhost(int x, int y) {
		setBounds(x, y, 32, 32);
		xx = x;
		yy = y;
	}
	
	public void addGate(int x1,int y1, int i) {
		gateX[i] = x1;
		gateY[i] = y1;
		
	}
	
	public void increaseEnemyDifficulty() {
		this.smartTime += 60;
		if(targetTime < 0) 
			targetTime = 0;
		else 
			this.targetTime -= 60;

	}
	private boolean canMove(int nextx, int nexty) {
		
		Rectangle bounds = new Rectangle(nextx, nexty, width, height);
		Level level = Game.level;
		
		for(int xx = 0; xx < level.tiles.length; xx++)
			for(int yy = 0; yy < level.tiles[0].length; yy++) {
				if(level.tiles[xx][yy] != null) {
					if(bounds.intersects(level.tiles[xx][yy])) {
						return false;
					}
				}
				else if(xx == gateX[0] && yy == gateY[0] && pastTheGate|| xx == gateX[1] && yy == gateY[1] && pastTheGate) {		
					Rectangle rec = new Rectangle(xx*32, yy*32, width, height);
					if(bounds.intersects(rec)) {
						return false;
					}
				
				}
			}
		return true;
	}

	
	public void tick() {
		if(state == random) {
			
			if(dir == right) {
				if(canMove(x + spd, y)) {
					x += spd;
					
				}else {
					dir = randomGen.nextInt(4);
				}
			}
			else if(dir == left) {
				if(canMove(x - spd, y)) {
					x -= spd;
					
				}else {
					dir = randomGen.nextInt(4);
				}
			}
			else if(dir == up) {
				if(canMove(x, y - spd)) {
					y -= spd;
					
				}else {
					dir = randomGen.nextInt(4);
				}
				
			}
			else if(dir == down) {
				if(canMove(x, y + spd)) {
					y += spd;
					
				}else {
					dir = randomGen.nextInt(4);
				}
			}
			
			
			time++;
			
			if(time > targetTime) {
				time = 0;
				state = smart;
			}
		}
		else if(state == smart) {
			
			boolean move = false;

			if(x < Game.player.x) {
				if(canMove(x+spd, y)) {
					x += spd;
					move = true;
					lastDir = right;
					dir = right;
				}
			}
			if(x > Game.player.x) {
				if(canMove(x - spd, y)) {
					x -= spd;
					move = true;
					lastDir = left;
					dir = left;
				}
			}
			if(y < Game.player.y) {
				if(canMove(x, y + spd)) {
					y += spd;
					move = true;
					lastDir = down;
					dir = down;
				}
			}
			if(y > Game.player.y) {
				if(canMove(x, y - spd)) {
					y -= spd;
					move = true;
					lastDir = up;
					dir = up;
				}
			}
			
			if(x == Game.player.x && y == Game.player.y)move = true;
			
			if(!move) {
				state = find_path;
			}
			time++;
			
			if(time > smartTime) {
				time = 0;
				state = random;
			}
			
		}
		
		else if(state == find_path) {
			boolean move1 = false;
			
			if(lastDir == right) {
				if(y < Game.player.y) {
					if(canMove(x, y + spd)) {
						y += spd;
						state = smart;
						dir = down;
						move1 = true;
					}
				}
				else {
					if(canMove(x, y - spd)) {
						y -= spd;
						state = smart;
						dir = up;
						move1 = true;
					}
				}
				
				if(canMove(x + spd, y)) {
					x += spd;
					dir = right;
					move1 = true;
				}
			}
			else if(lastDir == left) {
				if(y < Game.player.y) {
					if(canMove(x, y + spd)) {
						y += spd;
						state = smart;
						dir = down;
						move1 = true;
					}
				}
				else {
					if(canMove(x, y - spd)) {
						y -= spd;					
						state = smart;
						dir = up;
						move1 = true;
					}
				}
				
				if(canMove(x - spd, y)) {
					x -= spd;
					dir = left;
					move1 = true;
				}
			}
			else if(lastDir == up) {
				if(x < Game.player.x) {
					if(canMove(x + spd, y)) {
						x += spd;
						state = smart;
						dir = right;
						move1 = true;
					}
				}
				else {
					if(canMove(x - spd, y)) {
						x -= spd;						
						state = smart;
						dir = left;
						move1 = true;
					}
				}
				
				if(canMove(x, y - spd)) {
					y -= spd;
					move1 = true;
					dir = up;
				}
			}
			else if(lastDir == down) {
				if(x < Game.player.x) {
					if(canMove(x + spd, y)) {
						x += spd;
						state = smart;
						dir = right;
						move1 = true;
					}
				}
				else {
					if(canMove(x - spd, y)) {
						x -= spd;						
						state = smart;
						dir = left;
						move1 = true;
					}
				}
				
				if(canMove(x, y + spd)) {
					y += spd;
					dir = down;
					move1 = true;
				}
			}
			
			if(!move1 && x < Game.player.x) {
				state = 3;
			}
			if(!move1 && x > Game.player.x) {
				state = 4;
			}
			
			time++;
			
			if(time > smartTime) {
				time = 0;
				state = random;
			}

		}
		else if(state == 3) {
			if(canMove(x, y + spd)) {
				y += spd;
				dir = down;
			}
			if(canMove(x + spd, y)) {
				x += spd;
				dir = right;
				state = find_path;
			}
			
			time++;
			
			if(time > smartTime) {
				time = 0;
				state = random;
			}
			
		}
		else if(state == 4) {
			if(canMove(x, y + spd)) {
				y += spd;
				dir = down;
			}
			if(canMove(x - spd, y)) {
				x -= spd;
				dir = left;
				state = find_path;
			}
			
			time++;
			
			if(time > smartTime) {
				time = 0;
				state = random;
			}
			
		}
		
		if(y > gateY[0]*32 + 32)
			pastTheGate = true;
		
		time1++;
		if(time1 == targetIndex) {
			time1 = 0;
			index++;
		}
		
	}
	
	public void resetGhost() {
		this.x = xx;
		this.y = yy;
		spd = 2;
	}
	
	public void render(Graphics g) {
		if(Game.fruitTime < Game.fruitTimeTarget - 100) {
				
			if(!vulnerable) {
			if(nr == 0) renderGhost(g, nr * 4, this.x, this.y);
			else if(nr == 1)renderGhost(g, nr * 4, this.x, this.y);
			else if(nr == 2)renderGhost(g, nr * 4, this.x, this.y);
			else renderGhost(g, nr * 4, this.x, this.y);
			}
			else 
				g.drawImage(Texture.ghosts[16][index%4], x, y, 32, 32, null);
		}
		else if(Game.fruitTime >= Game.fruitTimeTarget - 100 && vulnerable){
			almostVEnd = ((int) (System.nanoTime() * 0.0000000075) % 3) > 0;
			
			if(!almostVEnd) {
				if(nr == 0) renderGhost(g, nr * 4, this.x, this.y);
				else if(nr == 1)renderGhost(g, nr * 4, this.x, this.y);
				else if(nr == 2)renderGhost(g, nr * 4, this.x, this.y);
				else renderGhost(g, nr * 4, this.x, this.y);
				}
			else {
				g.drawImage(Texture.ghosts[16][index%4], x, y, 32, 32, null);
			}
		}
		else {
			if(nr == 0) renderGhost(g, nr * 4, this.x, this.y);
			else if(nr == 1)renderGhost(g, nr * 4, this.x, this.y);
			else if(nr == 2)renderGhost(g, nr * 4, this.x, this.y);
			else renderGhost(g, nr * 4, this.x, this.y);
		}
		
	}
	
	private void renderGhost(Graphics g, int nr, int x, int y) {
		if(dir == right)g.drawImage(Texture.ghosts[0 + nr][index%4], x, y, 32, 32, null);
		else if(dir == left) g.drawImage(Texture.ghosts[1 + nr][index%4], x, y, 32, 32, null);
		else if(dir == down) g.drawImage(Texture.ghosts[2 + nr][index%4], x, y, 32, 32, null);
		else g.drawImage(Texture.ghosts[3 + nr][index%4], x, y, 32, 32, null);
		
	}
}
