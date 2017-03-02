import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import java.io.IOException;
import java.util.HashSet;
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
	private int resW = 800, resH = 600;
	//private Texture level1;
	private Player player;
	private Level currentLevel;
	
	
	float rotation = 0;
	long lastFrame;
	int fps;
	long lastFPS;
	
	public void start(){
		player = new Player(resW / 2, resH / 2, "player1");
		currentLevel = new Level();
		initGL(resH, resW);
		init();
		
		getDelta();
		lastFPS = getTime();
		
		//core game loop; ends when user closes window (future: or when user chooses exit option)
		while (!Display.isCloseRequested()){
			int delta = getDelta();
			
			update(delta);
			render();
			
			Display.update();
			Display.sync(60);
		}
		Display.destroy();
	}

	private void update(int delta){
		//rotation += 0.15f * delta;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
			player.setX(player.getX() - 0.35f * delta);
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
			player.setX(player.getX() + 0.35f * delta);
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) 
			player.setY(player.getY() - 0.35f * delta);
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) 
			player.setY(player.getY() + 0.35f * delta);
		
		if (player.getX() < 0)
			player.setX(0);
		if (player.getX() > resW)
			player.setX(resW);;
		if (player.getY() < 0)
			player.setY(0);
		if (player.getY() > resH)
			player.setY(resH);
		
		updateFPS();
	}
	
	private void render() {
		Color.white.bind();
		
		glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		//glColor3f(0.5f, 0.5f, 1.0f);
		

		glPushMatrix();
		
		currentLevel.getTexture().bind();
		glBegin(GL_QUADS);
			glTexCoord2f(0, 0);
			glVertex2f(0, 0);
			glTexCoord2f(1, 0);
			glVertex2f(800, 0);
			glTexCoord2f(1, 1);
			glVertex2f(800, 800);
			glTexCoord2f(0, 1);
			glVertex2f(0, 800);
		glEnd();
		
	
		//playerTexture.bind();
		player.getTexture().bind();
		glTranslatef(player.getX(), player.getY(), 0);
		//glRotatef(rotation, 0f, 0f, 1f);
		glTranslatef(-player.getX(), -player.getY(), 0);
		glBegin(GL_QUADS);
			glTexCoord2f(0, 0);
			glVertex3f(player.getX() - 50, player.getY() - 50, 0.2f);
			glTexCoord2f(1, 0);
			glVertex3f(player.getX() + 50, player.getY() - 50, 0.2f);
			glTexCoord2f(1, 0.7f);
			glVertex3f(player.getX() + 50, player.getY() + 50, 0.2f);
			glTexCoord2f(0, 0.7f);
			glVertex3f(player.getX() - 50, player.getY() + 50, 0.2f);
		glEnd();
		glPopMatrix();
		
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
		try {
			currentLevel.setTexture(TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("src\\res\\level1_1.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			//playerTexture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("src\\res\\tylerTwitchEmote.png"));
			player.setTexture(TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("src\\res\\tylerTwitchEmote.png")));
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
			//Display.setTitle("FPS: " + fps);
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
