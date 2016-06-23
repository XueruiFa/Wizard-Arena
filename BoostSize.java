import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BoostSize extends Powerup {
	public static final String img_file = "ChargeIcon.png";
	private static BufferedImage img = null;
	
	public BoostSize (int initx, int inity, int courtWidth, int courtHeight) {
		super(initx, inity, courtWidth, courtHeight, 100);
		try {
			if (img == null) {
				img = ImageIO.read(new File(img_file));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
	}
	
	@Override
	public void draw(Graphics g) {
		g.drawImage(img, pos_x, pos_y, width, height, null);
	}
	
	@Override
	/**
	 * Increases the size of the player's missiles
	 * 
	 * @param A player p
	 */
	public void effect(Player p) {
		p.incSize();
	}
	
	@Override
	/**
	 * Reduces the size of the player's missiles
	 * 
	 * @param A player p
	 */
	public void endEffect(Player p) {
		p.decSize();
	}

}
