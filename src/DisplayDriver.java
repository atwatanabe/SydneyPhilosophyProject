import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import org.newdawn.slick.TrueTypeFont;

import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import static org.lwjgl.opengl.GL11.*;

/**
 * Main driver class.
 * Contains code for core game loop, rendering, input
 * 
 * @author Anthony Watanabe
 *
 */

public class DisplayDriver {
	private int resW = 1200, resH = 900;
	private GameState currentState;
	private Player player;
	private int currentLevel;
	private ArrayList<Level> levels;
	private boolean bballComplete = false;
	private boolean hballComplete = false;
	private boolean linkComplete = false;
	private String text = "";
	private NPC speaker;
	boolean flag = false;
	
	private TrueTypeFont font;
	
	long lastFrame;
	int fps;
	long lastFPS;
	
	public void start(){
		//System.out.println(res.Strings.test_string);
		initGL(resH, resW);
		init();
		loadLevels();
				
		//core game loop; ends when user closes window (future: or when user chooses exit option)
		while (!Display.isCloseRequested()){
			int delta = getDelta();
			
			update(delta);
			render();
			//System.out.println(Mouse.getX() + ", " + Mouse.getY());
			Display.update();
			Display.sync(60);
		}
		Display.destroy();
	}


	private void update(int delta){
		switch (currentState){
			case LAUNCH_MENU: {
				
				break;
			} case NAME_INPUT: {
				
				break;
			} case SHOW_CONTROLS: {
				
				break;
			} case CORE_GAME: {
				float x = player.getLocation().getX(), y = player.getLocation().getY();
				if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
					player.setDirection(Direction.UP);
					player.setY(player.getY() - 0.35f * delta);
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
					player.setDirection(Direction.DOWN);
					player.setY(player.getY() + 0.35f * delta);
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
					player.setDirection(Direction.LEFT);
					player.setX(player.getX() - 0.35f * delta);
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
					player.setDirection(Direction.RIGHT);
					player.setX(player.getX() + 0.35f * delta);
				}
				
				if (player.getX() < 0)
					player.setX(0);
				if (player.getX() > resW)
					player.setX(resW);;
				if (player.getY() < 0)
					player.setY(0);
				if (player.getY() > resH)
					player.setY(resH);
				
				Set<GameObject> objs = getCurrentLevel().getIntersectingObjects(player);
				Iterator<GameObject> iter = objs.iterator();
				if (!objs.isEmpty()) {
					while (iter.hasNext()) {
						GameObject o = iter.next();
						if (o instanceof WarpZone) {
							Level l = getCurrentLevel();
							WarpZone temp = (WarpZone) o;
							setLevel(temp.getDestination());
							if (l.equals(levels.get(2)))
								player.setLocation(new Vertex3f(resW / 2, player.getDimensions().getY() / 2 + 20, 0f));
						}
						if (o instanceof NPC && Keyboard.next() && Keyboard.getEventKeyState() && Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
							currentState = GameState.DIALOGUE;
							speaker = (NPC) o;
							if (hballComplete && bballComplete && linkComplete && speaker.getName().equals("teacher") && !flag) {
								speaker.loadAltText("teacher0.txt");
								flag = true;
							}
							if (speaker.getName().equals("bball_trio")) {
								bballComplete = true;
							}
							if (speaker.getName().equals("hball_trio")) {
								hballComplete = true;
							}
							if (speaker.getName().equals("Link")) {
								linkComplete = true;
							}
							
							if (!speaker.isDialogueComplete()) {
								setDialogue(speaker.getNextString());
								speaker.advanceIndex();
							}
						}
						
						
						if (o instanceof Wall || o instanceof Obstacle) {
							player.setX(x);
							player.setY(y);
						}
					}
				}
				
				break;
			} case DIALOGUE: {
				if (!speaker.getNextString().equals("") && speaker.getNextString().charAt(0) == '[') {
					int choice = Keyboard.getEventKey();
					if (Keyboard.next() && Keyboard.getEventKeyState()) {
						setDialogue(speaker.getNextString());
						speaker.advanceIndex();
					}
				}
				if (Keyboard.next() && Keyboard.getEventKeyState() && Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
					setDialogue(speaker.getNextString());
					speaker.advanceIndex();
				}
				if (Keyboard.next() && Keyboard.getEventKeyState() && Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
					currentState = GameState.CORE_GAME;
				}
				break;
			} case CUTSCENE: {
							
				break;
			}
		}
			
		
//		if (Keyboard.isKeyDown(Keyboard.KEY_0)) {
//			setLevel(levels.get(0));
//		}
//		if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
//			setLevel(levels.get(1));
//		}
		
		updateFPS();
	}
	
	private void setDialogue(String s) {
		if (!s.equals("_")) {
			System.out.println(s);
			text = s;
		} else {
			text = "";
			currentState = GameState.CORE_GAME;
		}
	}
	
	private void render() {
		Color.white.bind();
		
		glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		getCurrentLevel().render();
		player.render();
		
		font.drawString(5f, 800f, text, Color.green);
		
//		font.drawString(100, 50, "THE LIGHTWEIGHT JAVA GAMES LIBRARY", Color.yellow);
//		font.drawString(100, 100, "NICE LOOKING FONTS!", Color.green);
		
	}
	
	public Level getCurrentLevel() {
		return levels.get(currentLevel);
	}
	
	private void setLevel(Level nextLevel) {
		currentLevel = levels.indexOf(nextLevel);
		player.setLocation(nextLevel.getSpawnPoint());
		//currentLevel.addObject(player);
	}

	private void initGL(int height, int width) {
		try{
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle("Sydney's Philosophy Project");
			Display.create();
		} catch (LWJGLException e){
			Display.destroy();
			System.exit(1);
		}
		
		glEnable(GL_TEXTURE_2D);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glViewport(0, 0, width, height);
		glMatrixMode(GL_MODELVIEW);
		
		glMatrixMode(GL11.GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, width, height, 0, 1, -1);
		glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	private void init()	{
		getDelta();
		lastFPS = getTime();
		
		currentState = GameState.CORE_GAME;
		player = Player.getInstance();
		player.setX(resW * 0.4f);
		player.setY(resH / 2);
		try {
			player.setTexture(TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("src\\res\\player.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Font awtFont = new Font("Times New Roman", Font.BOLD, 24);
		font = new TrueTypeFont(awtFont, false);
	}
	
	private void loadLevels() {
		levels = new ArrayList<Level>();
		
		Level level0 = new Level(new Vertex3f(0f, 0f, 0f), new Vertex3f(resW, resH, 0));
		level0.setSpawnPoint(new Vertex3f(resW - 60, resH * 0.92f, 0));
		NPC teacher = new NPC(new Vertex3f(resW * 0.85f, resH * 0.17f, 0f), new Vertex3f(90f, 120f, 0f));
		teacher.setName("teacher");
		teacher.loadText();
		level0.addObject(teacher);
		
		//up-left corner
		level0.addObject(new Wall(new Vertex3f(100f, 0f, 0f), new Vertex3f(100f, 100f, 0f)));
		level0.addObject(new Wall(new Vertex3f(100f, 100f, 0f), new Vertex3f(0f, 100f, 0f)));
		//screen
		level0.addObject(new Wall(new Vertex3f(370f, 0f, 0f), new Vertex3f(370f, 85f, 0f)));
		level0.addObject(new Wall(new Vertex3f(370f, 85f, 0f), new Vertex3f(820f, 85f, 0f)));
		level0.addObject(new Wall(new Vertex3f(820f, 85f, 0f), new Vertex3f(820f, 0f, 0f)));
		//up-right corner
		level0.addObject(new Wall(new Vertex3f(1070f, 0f, 0f), new Vertex3f(1070f, 85f, 0f)));
		level0.addObject(new Wall(new Vertex3f(1070f, 85f, 0f), new Vertex3f(1200f, 85f, 0f)));
		//desk
		level0.addObject(new Wall(new Vertex3f(1025f, 180f, 0f), new Vertex3f(1025f, 220f, 0f)));
		level0.addObject(new Wall(new Vertex3f(1025f, 220f, 0f), new Vertex3f(1200f, 220f, 0f)));
		level0.addObject(new Wall(new Vertex3f(1200f, 220f, 0f), new Vertex3f(1200f, 180f, 0f)));
		level0.addObject(new Wall(new Vertex3f(1200f, 180f, 0f), new Vertex3f(1025f, 180f, 0f)));
		//down-right walls
		level0.addObject(new Wall(new Vertex3f(1200f, 560f, 0f), new Vertex3f(930f, 560f, 0f)));
		level0.addObject(new Wall(new Vertex3f(930f, 560f, 0f), new Vertex3f(930f, 640f, 0f)));
		level0.addObject(new Wall(new Vertex3f(930f, 640f, 0f), new Vertex3f(620f, 640f, 0f)));
		level0.addObject(new Wall(new Vertex3f(620f, 640f, 0f), new Vertex3f(620f, 730f, 0f)));
		level0.addObject(new Wall(new Vertex3f(620f, 730f, 0f), new Vertex3f(850f, 730f, 0f)));
		level0.addObject(new Wall(new Vertex3f(850f, 730f, 0f), new Vertex3f(850f, 775f, 0f)));
		level0.addObject(new Wall(new Vertex3f(850f, 775f, 0f), new Vertex3f(1200f, 775f, 0f)));
		//down-left walls
		level0.addObject(new Wall(new Vertex3f(0f, 565f, 0f), new Vertex3f(145f, 565f, 0f)));
		level0.addObject(new Wall(new Vertex3f(145f, 565f, 0f), new Vertex3f(145f, 645f, 0f)));
		level0.addObject(new Wall(new Vertex3f(45f, 645f, 0f), new Vertex3f(410f, 645f, 0f)));
		level0.addObject(new Wall(new Vertex3f(410f, 645f, 0f), new Vertex3f(410f, 820f, 0f)));
		level0.addObject(new Wall(new Vertex3f(410f, 820f, 0f), new Vertex3f(475f, 820f, 0f)));
		level0.addObject(new Wall(new Vertex3f(475f, 820f, 0f), new Vertex3f(475f, 885f, 0f)));
		level0.addObject(new Wall(new Vertex3f(475f, 885f, 0f), new Vertex3f(1200f, 885f, 0f)));
		//tables
		level0.addObject(new Obstacle(new Vertex3f(126.5f, 302.5f, 0f), new Vertex3f(63f, 275f, 0f)));
		level0.addObject(new Obstacle(new Vertex3f(317.5f, 310f, 0f), new Vertex3f(195f, 60f, 0f)));
		level0.addObject(new Obstacle(new Vertex3f(310f, 481f, 0f), new Vertex3f(180f, 62f, 0f)));
		level0.addObject(new Obstacle(new Vertex3f(750f, 322.5f, 0f), new Vertex3f(200f, 55f, 0f)));
		level0.addObject(new Obstacle(new Vertex3f(747.5f, 485f, 0f), new Vertex3f(215f, 70f, 0f)));
		level0.addObject(new Obstacle(new Vertex3f(1047.5f, 373.5f, 0f), new Vertex3f(215f, 77f, 0f)));
		
		
		Level level1 = new Level(new Vertex3f(0f, 0f, 0f), new Vertex3f(resW, resH, 0));
		level1.setSpawnPoint(new Vertex3f(player.getDimensions().getX() / 2 + 20, resH * 0.9f, 0f));
		
		//handball wall
		level1.addObject(new Obstacle(new Vertex3f(335f, 192.5f, 0f), new Vertex3f(270f, 235f, 0f)));
		//basketball hoops
		level1.addObject(new Obstacle(new Vertex3f(975.5f, 87.5f, 0f), new Vertex3f(95f, 55f, 0f)));
		level1.addObject(new Obstacle(new Vertex3f(967.5f, 577.5f, 0f), new Vertex3f(95f, 55f, 0f)));
		
		NPC hball_trio = new NPC(new Vertex3f(resW * 0.3f, resH * 0.5f, 0f), new Vertex3f(260f, 240f, 0f));
		hball_trio.setName("hball_trio");
		hball_trio.loadText();
		level1.addObject(hball_trio);
		
		NPC bulliedGirl = new NPC(new Vertex3f(resW * 0.65f, resH * 0.5f, 0f), new Vertex3f(90f, 120f, 0f));
		bulliedGirl.setName("bulliedGirl");
		bulliedGirl.loadText();
		level1.addObject(bulliedGirl);
		
		NPC bball_trio = new NPC(new Vertex3f(resW * 0.8f, resH * 0.5f, 0f), new Vertex3f(260, 240f, 0f));
		bball_trio.setName("bball_trio");
		bball_trio.setFlipped(true);
		bball_trio.loadText();
		level1.addObject(bball_trio);
		
		Level level2 = new Level(new Vertex3f(0f, 0f, 0f), new Vertex3f(resW, resH, 0));
		level2.setSpawnPoint(new Vertex3f(resW / 2, resH - player.getDimensions().getY() / 2 - 20, 0f));
		level1.addObject(new WarpZone(new Vertex3f(resW / 2, 1f, 0f), new Vertex3f(resW, 2, 0), level2));
		level2.addObject(new WarpZone(new Vertex3f(resW / 2, resH - 1f, 0f), new Vertex3f(resW, 2, 0), level1));
		NPC link = new NPC(new Vertex3f(resW / 2, resH / 2, 0), new Vertex3f(60f, 80f, 0f));
		link.setName("Link");
		link.loadText();
		level2.addObject(link);
		
		NPC loner = new NPC(new Vertex3f(resW * 0.41f, resH * 0.5f, 0f), new Vertex3f(90f, 120f, 0f));
		loner.setName("loner");
		loner.loadText();
		level1.addObject(loner);
		
		level0.addObject(new WarpZone(new Vertex3f(resW - 1, resH * 0.9f, 0), new Vertex3f(2, resH / 6, 0), level1));
		level1.addObject(new WarpZone(new Vertex3f(1, resH * 0.9f, 0), new Vertex3f(2, resH / 6, 0), level0));
		
		
		levels.add(level0);
		levels.add(level1);
		levels.add(level2);
		
		currentLevel = 0;
		
		
		try {
			level0.setTexture(TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("src\\res\\level1_2.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			teacher.setTexture(TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("src\\res\\teacher.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			level1.setTexture(TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("src\\res\\handball_and_bball_courts.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			hball_trio.setTexture(TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("src\\res\\hball_trio.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			level2.setTexture(TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("src\\res\\field.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			link.setTexture(TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("src\\res\\link.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			bball_trio.setTexture(TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("src\\res\\hball_trio.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			loner.setTexture(TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("src\\res\\loner.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			bulliedGirl.setTexture(TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("src\\res\\bulliedGirl.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	private int getDelta() {
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;
		return delta;
	}
	
	private long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	private void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}
	
	public static void main(String[] args){
		DisplayDriver driver = new DisplayDriver();
		driver.start();		
	}
}
