import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Missile1 extends Bullet {
	public static final String img_file = "laser1.png";
	private static BufferedImage image = null;
	public static final int INIT_VEL_X = 0;
	public static final int INIT_VEL_Y = 0;

	public Missile1(int initx, int inity, int courtWidth, int courtHeight, int a) {
		super(initx, inity, courtWidth, courtHeight, a);
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
}
