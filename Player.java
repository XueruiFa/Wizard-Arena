import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JTextField;


public class Player extends GameObj {
	public static final String img_file = "wizard2.png";
	public static final int SIZE = 80;
	public static int INIT_X;
	public static int INIT_Y;
	public static final int INIT_VEL_X = 0;
	public static final int INIT_VEL_Y = 0;
	public static int PLAYER_VELOCITY = 10;
	
	public int ammobarx;
	public int ammobary;
	
	private ArrayList<Bullet> missiles = new ArrayList<Bullet>();
	
	public int mSize = 20;
 	
	public Health health;
	
	private static BufferedImage image = null;
	
	public static final int COURT_WIDTH = 1000;
	public static final int COURT_HEIGHT = 600;

	public Player (int initx, int inity, int courtWidth, int courtHeight, 
			int ammobarx, int ammobary) {
		super(INIT_VEL_X, INIT_VEL_Y, initx, inity, SIZE, SIZE, courtWidth, courtHeight);
		int p = pos_x;
		if (p >= COURT_WIDTH) {
			p = COURT_WIDTH - 100;
		}
		health = new Health(p, 0, courtWidth, courtHeight);
		this.ammobarx = ammobarx;
		this.ammobary = ammobary;
		try {
			if (image == null) {
				image = ImageIO.read(new File(img_file));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
	}
	
	
	@Override
	public void draw(Graphics g) {
		g.drawImage(image, pos_x, pos_y, width, height, null);
		health.draw(g);
		g.setColor(Color.WHITE);
		g.fillRect(ammobarx, ammobary, 100, 20);
		g.setColor(Color.GREEN);
		g.fillRect(ammobarx, ammobary, 100 - (missiles.size() * 10), 20);
	}
	
	public int getPlayerVelocity() {
		return PLAYER_VELOCITY;
	}
	
	public static void boostSpeed() {
		PLAYER_VELOCITY = 15;
	}
	
	public static void endBoost() {
		PLAYER_VELOCITY = 10;
	}
	
	public void decPlayerHealth() {
		health.decHealth();
	}
	
	public void incPlayerHealth() {
		health.incHealth();
	}

	public boolean zeroHealth() {
		if (health.getHealth() <= 0) {
			return true;
		}
		return false;
	}
	
	public void addMissile1(int posx, int posy, int v_x, int v_y) {
		Missile1 m = new Missile1(posx, posy, COURT_WIDTH, COURT_HEIGHT, this.mSize);
		m.v_x = v_x;
		m.v_y = v_y;
		missiles.add(m);
	}
	
	public void addMissile2(int posx, int posy, int v_x, int v_y) {
		Missile2 m = new Missile2(posx, posy, COURT_WIDTH, COURT_HEIGHT, this.mSize);
		m.v_x = v_x;
		m.v_y = v_y;
		missiles.add(m);
	}
	
	public void addMissile3(int posx, int posy, int v_x, int v_y) {
		Missile2 m = new Missile2(posx, posy, COURT_WIDTH, COURT_HEIGHT, this.mSize);
		m.v_x = v_x;
		m.v_y = v_y;
		missiles.add(m);
	}

	public ArrayList<Bullet> getMissiles() {
		return this.missiles;
	}
	
	
	public void incSize() {
		this.mSize = 30;
	}
	
	
	public void decSize() {
		this.mSize = 20;
	}
	
	public void intersectsHealth() {
		if (this.intersects(health)) {
			this.v_x = 0;
			this.v_y = 0;
		}
	}
	
	public int getMSize() {
		return this.mSize;
	}
	
	public int getHealth() {
		return health.getHealth();
	}
}
