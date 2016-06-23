import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BoostHealth extends Powerup {
	public static final String img_file = "boostHealth.png";
	private static BufferedImage img = null;
	private int duration = 1;
	
	public BoostHealth (int initx, int inity, int courtWidth, int courtHeight) {
		super(initx, inity, courtWidth, courtHeight, 1);
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
	public void effect(Player p) {
		p.incPlayerHealth();
	}
	
	@Override
	public void endEffect(Player p) {
		
	}

}
