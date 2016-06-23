import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Meteor extends GameObj {
	public static final String img_file1 = "meteorupleft.png";
	public static final String img_file2 = "meteorupright.png";
	public static final String img_file3 = "meteordownleft.png";
	public static final String img_file4 = "meteordownright.png";
	private BufferedImage image = null;
	
	public Meteor (int initvx, int initvy, 
			int initx, int inity, int courtWidth, int courtHeight) {
		super(initvx, initvy, initx, inity, (int) (Math.random() * 20) + 40, 
				(int) (Math.random() * 20) + 40, courtWidth, courtHeight);
		try {
			if (image == null) {
				if (this.pos_x >= 500 && this.pos_y <= 300) {
					image = ImageIO.read(new File(img_file1));
				}
				else if (this.pos_x < 500 && this.pos_y < 300) {
					image = ImageIO.read(new File(img_file2));
				}
				else if (this.pos_x <= 500 && this.pos_y >= 300) {
					image = ImageIO.read(new File(img_file3));
				}
				else {
					image = ImageIO.read(new File(img_file4));
				}
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
