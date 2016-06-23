import java.awt.*;

import javax.swing.JTextField;

public class Health extends GameObj{
	private int health;
	
	public static final int SIZE = 30;
	public static final int INIT_VEL_X = 0;
	public static final int INIT_VEL_Y = 0;
	
	public Health(int initx, int inity, int courtWidth, int courtHeight) {
		super(INIT_VEL_X, INIT_VEL_Y, initx, inity, SIZE, SIZE, courtWidth,
				courtHeight);
		health = 100;
	}
	
	/**
	 * Decreases the health
	 */
	public void decHealth() {
		health -= 10;
	}
	
	/** Increases the health
	 * 
	 */
	public void incHealth() {
		this.health += 10;
	}
	
	/** Returns the health
	 * 
	 * @return The int health of the Health object
	 */
	public int getHealth() {
		return this.health;
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(pos_x, pos_y, health, 20);
		g.setColor(Color.WHITE);
		g.fillRect(pos_x + health, pos_y, 100 - health, 20);
	}
}
