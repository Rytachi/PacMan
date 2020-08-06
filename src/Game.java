import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable, KeyListener{

	private static final long serialVersionUID = 1L;

	private boolean isRunning = false;
	
	public static final int WIDTH = 640, HEIGHT = 550;
	
	public static final String TITLE = "PacMan";
	
	private Thread thread;
	
	public static Player player;
	
	public static Level level;
	
	public static SpriteSheet spritesheet;
	
	public static Score score;
	
	public static Menu menu;
	
	public static int waitTime = 1;
	
	public static int fruitTime = 1;
	
	public static int fruitTimeTarget = 300;
	
	private boolean spaceState = false;
	
	public static enum STATE{
		MENU,
		NEWGAME,
		GAMEREADY,
		DIED,
		INTERSECTS,
		GAMEOVER,
		LEVEL2,
		LEVEL3,
		GHOSTCOUGHT,
		FRUITEATEN,
	};
	
	public static STATE state = STATE.MENU;
	
	public Game() {
		Dimension dimension = new Dimension(Game.WIDTH, Game.HEIGHT);
		setPreferredSize(dimension);
		setMinimumSize(dimension);
		setMaximumSize(dimension);
		
		addKeyListener(this);
		player = new Player(Game.WIDTH/2, HEIGHT/2);
		level = new Level("map1.png");
		spritesheet = new SpriteSheet("spritesheet.png");
		score = new Score();
		menu = new Menu();
		new Texture("backGround1.png", "backGround1White.png");
	}
	
	public void start() {
		if(isRunning )return;
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	
	}
	
	public void stop() {
		if(!isRunning)return;
		isRunning = false;
		try {
			thread.join();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	private void tick() {
		if(state == STATE.GAMEREADY) {
			player.tick();
			level.tick();
		}
		else if(state == STATE.FRUITEATEN) {
			player.tick();
			level.tick();
		}
	}
	
	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

		
		if(state == STATE.MENU) {
			score.render(g);
			menu.render(g);
		}
		
		else if(state == STATE.NEWGAME) {

			level.render(g);
			score.render(g);
			score.renderReady(g);
			waitTime++;
			
			if(waitTime > 50) {
				player.render(g);
			}
			
			if (waitTime > 120) {
				state = STATE.GAMEREADY;
				waitTime = 0;
			}
			
		}
		
		else if(state == STATE.GAMEREADY) {
			level.render(g);
			player.render(g);
			score.render(g);
		}
		
		else if(state == STATE.INTERSECTS) {
			level.render(g);
			player.render(g);
			score.render(g);
			
			waitTime++;
			if(waitTime == 120) {
				fruitTime = 0;
				player.fruitEaten = false;
				for(int j = 0; j < level.enemies.size(); j++) {
					level.enemies.get(j).vulnerable = false;
					level.enemies.get(j).pastTheGate = false;
				}
				level.resetActors();
				state = STATE.NEWGAME;
				waitTime = 0;
			}
		}
		
		else if(state == STATE.FRUITEATEN) {
			level.render(g);
			player.render(g);
			score.render(g);
			
			fruitTime++;
			if(fruitTime > fruitTimeTarget) {
				state = STATE.GAMEREADY;
				fruitTime = 0;
				player.fruitEaten = false;
				for(int j = 0; j < level.enemies.size(); j++) {
					level.enemies.get(j).vulnerable = false;
					level.enemies.get(j).spd = 2;
				}
			}
			
		}
		
		else if(state == STATE.GHOSTCOUGHT) {
			level.render(g);
			int i = player.returnGhostIndex();
			player.render(g);
			score.render(g);
			score.renderGhostEaten(g, level.enemies.get(i).x, level.enemies.get(i).y);
			
			fruitTime++;
			waitTime++;
			if(waitTime == 50) {
				level.renderOneGhost(player.returnGhostIndex());
				level.enemies.get(player.returnGhostIndex()).vulnerable = false;
				level.enemies.get(player.returnGhostIndex()).pastTheGate = false;
				level.enemies.get(player.returnGhostIndex()).spd = 2;
				state = STATE.FRUITEATEN;
				waitTime = 0;
			}
			
		}
		
		else if(state == STATE.GAMEOVER) {
			level.renderLevel(g);
			score.renderGameOver(g);
			score.render(g);
			
			waitTime++;
			if (waitTime == 300) {
				state = STATE.MENU;
				spaceState = false;
				waitTime = 0;
				score.resetScore();
				level = new Level("map1.png");
				new Texture("backGround1.png", "backGround1White.png");
			}
		}
		
		else if(state == STATE.LEVEL2) {
			level.renderLevelCleared(g);
			
			waitTime++;
			if(waitTime > 200) {
				new Texture("backGround2.png", "backGround2White.png");
				level = new Level("map2.png");
				level.increaseDifficulty();
				fruitTimeTarget -= 50;
				state = STATE.NEWGAME;
				waitTime = 0;
			}
		else if(state == STATE.LEVEL3) {
			level.renderLevelCleared(g);
			
			waitTime++;
			if(waitTime > 200) {
				new Texture("backGround3.png", "backGround3White.png");
				level = new Level("map3.png");
				level.increaseDifficulty();
				if(fruitTimeTarget < 0)
					fruitTimeTarget = 100;
				else	
					fruitTimeTarget -= 50;
					
				state = STATE.NEWGAME;
				waitTime = 0;
			}
		}

	
		}
		g.dispose();
		bs.show();
	}
	
	@Override
	public void run() {
		requestFocus();
		double timer = System.currentTimeMillis();
		int fps = 0;
		long lastTime = System.nanoTime();
		double delta = 0;
		double targetTick = 60;
		double ns = 1000000000 / targetTick;
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			while(delta >= 1) {
				tick();
				render();
				fps++;
				delta--;
			}
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println(fps);
				fps = 0;
				timer += 1000;
			}
			
		}
		
		stop();
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		JFrame frame = new JFrame();
		frame.setTitle(Game.TITLE);
		frame.add(game);
		frame.setResizable(false);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		game.start();
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) player.right = true;
		if(e.getKeyCode() == KeyEvent.VK_LEFT)player.left = true;
		if(e.getKeyCode() == KeyEvent.VK_UP)player.up = true;
		if(e.getKeyCode() == KeyEvent.VK_DOWN)player.down = true;
		if(e.getKeyCode() == KeyEvent.VK_SPACE && spaceState == false) state = STATE.NEWGAME;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT)player.right = false;
		if(e.getKeyCode() == KeyEvent.VK_LEFT)player.left = false;
		if(e.getKeyCode() == KeyEvent.VK_UP)player.up = false;
		if(e.getKeyCode() == KeyEvent.VK_DOWN)player.down = false;
		if(e.getKeyCode() == KeyEvent.VK_SPACE) spaceState = true;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}
}
