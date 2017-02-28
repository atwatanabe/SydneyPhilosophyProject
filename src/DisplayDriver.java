import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.util.HashMap;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

/**
 * Main driver class.
 * Contains code for core game loop, rendering, input
 * 
 * @author Anthony Watanabe
 *
 */

public class DisplayDriver 
{
	//private static HashMap<Controls, Integer> keyBinds;
	
	public static void main(String[] args)
	{
		int resW = 640, resH = 480;
			
		
		try
		{
			//create window
			Display.setDisplayMode(new DisplayMode(resW, resH));
			Display.setTitle("Sydney's Philosophy Project");
			Display.create();
		}
		catch (LWJGLException e)
		{
			Display.destroy();
			System.exit(1);
		}
		
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, resW, resH, 0, 1, -1);		//left, right, bottom, top, near, far

		//core game loop; ends when user closes window (future: or when user chooses exit option)
		while (!Display.isCloseRequested())
		{
			glMatrixMode(GL_MODELVIEW);
			glClear(GL_COLOR_BUFFER_BIT);
			
			//glBindTexture(GL_TEXTURE_2D, level1.getTextureID());
			
//			glBegin(GL_TRIANGLES);
//				glVertex3f(resW, 0f, 0f);
//				glTexCoord2f(1, 0);
//				glVertex2i(450, 10);
//				glTexCoord2f(0, 0);
//				glVertex2i(10, 10);
//				glTexCoord2f(0, 1);
//				glVertex2i(10, 450);
//				 
//				glTexCoord2f(0, 1);
//				glVertex2i(10, 450);
//				glTexCoord2f(1, 1);
//				glVertex2i(450, 450);
//				glTexCoord2f(1, 0);
//				glVertex2i(450, 10);
//			glEnd();
			
			glEnable(GL_TEXTURE_2D);
			glBegin(GL_TRIANGLES);
				glVertex2f(50f, 50f);
				glVertex2f(50f, 200f);
				glVertex2f(200f, 100f);
			glEnd();
			
			
			//polling loop for controls
			while(Keyboard.next())
			{
				if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
				{
					
					//animate walking and translate game world
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
				{
					
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
				{
					
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_UP))
				{
					
				}	
			}
			
			Display.update();
			Display.sync(60);
		}
		
		//level1.release();
		Display.destroy();
		System.exit(0);
	}
	
	
	
	/*
	 * restore key bindings to defaults
	 */
	public static void restoreDefaultBindings()
	{
//		keyBinds = {(Controls.LEFT, Keyboard.KEY_LEFT),
//					(Controls.DOWN, Keyboard.KEY_DOWN),
//					()};
	}
}
