
public class Bullet extends GameObj {
	
	public static final int INIT_VEL_X = 0;
	public static final int INIT_VEL_Y = 0;
	
	public Bullet(int initx, int inity, int courtWidth, int courtHeight, int size) {
		super(INIT_VEL_X, INIT_VEL_Y, initx, inity, size, size, courtWidth, courtHeight);
	}
}
