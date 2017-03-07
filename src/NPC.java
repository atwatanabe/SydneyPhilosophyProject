import static org.lwjgl.opengl.GL11.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
/**
 * 
 * @author Anthony Watanabe
 *
 */

public class NPC extends GameObject {
	
	public static final String END = "_";
	private ArrayList<String> script;
	private int index;
	private boolean dialogueComplete;
	private boolean isFlipped;
	
	public boolean isFlipped() {
		return isFlipped;
	}

	public void setFlipped(boolean isFlipped) {
		this.isFlipped = isFlipped;
	}

	public NPC(Vertex3f loc, Vertex3f dim) {
		super(loc, dim);
		script = new ArrayList<String>();
		index = 0;
		dialogueComplete = false;
		isFlipped = false;
	}
	
	public boolean isDialogueComplete() {
		return dialogueComplete;
	}

	public void setDialogueComplete(boolean dialogueComplete) {
		this.dialogueComplete = dialogueComplete;
	}

	public boolean advanceIndex() {
		if (index + 1 < script.size()){
			++index;
			return true;
		}
		dialogueComplete = true;
		return false;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void render(){
		texture.bind();
		float left = 0f, right = 1f, up = 0f, down = 1f;
		if (isFlipped){
			left = 1f;
			right = 0f;
		}
		float xadj = dimensions.getX() / 2, yadj = dimensions.getY() / 2;
		glBegin(GL_QUADS);
			glTexCoord2f(left, up);
			glVertex3f(location.getX() - xadj, location.getY() - yadj, 0.1f);
			glTexCoord2f(right, up);
			glVertex3f(location.getX() + xadj, location.getY() - yadj, 0.1f);
			glTexCoord2f(right, down);
			glVertex3f(location.getX() + xadj, location.getY() + yadj, 0.1f);
			glTexCoord2f(left, down);
			glVertex3f(location.getX() - xadj, location.getY() + yadj, 0.1f);
		glEnd();
	}
	
	public boolean loadAltText(String fileName) {
		index = 0;
		script = new ArrayList<String>();
		try {
			File file = new File("src\\res\\" + fileName);
			Scanner s = new Scanner(file);
			while (s.hasNextLine()) {
				String temp = s.nextLine();
				script.add(temp);
				System.out.println(temp);
			}
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		script.add(END);
		return true;
	}
	
	public boolean loadText() {
		try {
			File file = new File("src\\res\\" + name + ".txt");
			Scanner s = new Scanner(file);
			while (s.hasNextLine()) {
				String temp = s.nextLine();
				script.add(temp);
				//System.out.println(temp);
			}
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		script.add(END);
		return true;
	}
	
	public String getNextString() {
		return script.get(index);
	}
}
