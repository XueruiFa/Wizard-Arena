import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Missile2 extends Bullet {
	public static final String img_file = "laser2.png";
	private static BufferedImage image = null;
	private static boolean bounced = false;
	public static final int INIT_VEL_X = 0;
	public static final int INIT_VEL_Y = 0;
	
	public Missile2 (int initx, int inity, int courtWidth, int courtHeight, int size) {
		super(initx, inity, courtWidth, courtHeight, size);
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
	}
	
	
	public void setBounced() {
		this.bounced = !this.bounced;
	}
	
	public boolean getBounced() {
		return this.bounced;
	}
}
