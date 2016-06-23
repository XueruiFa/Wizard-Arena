
public abstract class Powerup extends GameObj{
	public static final int SIZE = 30;
	public static final int INIT_VEL_X = 0;
	public static final int INIT_VEL_Y = 0;
	public int duration = 0;
	public int incr;
	public Player p = null;
	public Powerup(int initx, int inity, int courtWidth, int courtHeight, int duration) {
		super(INIT_VEL_X, INIT_VEL_Y, initx, inity, SIZE, SIZE, courtWidth, courtHeight);
		this.incr = duration;
	}
	
	@Override
	public boolean intersects (GameObj obj) {
		String t = obj.getClass().getName();
		if (t.equals("Player")) {
			return (pos_x + width >= obj.pos_x
					&& pos_y + height >= obj.pos_y
					&& obj.pos_x + obj.width >= pos_x 
					&& obj.pos_y + obj.height >= pos_y);
		}
		else 
			return false;
	}
	
	/** Sets the player object that this powerup belongs to
	 * 
	 * @param p The player
	 */
	public void setPlayer(Player p) {
		this.p = p;
	}
	
	/** Returns the play object that this powerup belongs to
	 * 
	 * @return The player
	 */
	public Player getPlayer() {
		return this.p;
	}
	
	/** Returns the duration of the powerup
	 * 
	 * @return The duration
	 */
	public int getDuration() {
		return this.duration;
	}
	
	/** Decreases the duration of the powerup
	 * 
	 */
	public void decDuration() {
		this.duration--;
	}
	
	/**
	 * Increases the duration of the powerup
	 */
	public void incDuration() {
		duration = duration + incr;
	}
	
	/** Returns the increase of duration of the specific powerup
	 * 
	 * @return The increase of duration every time the powerup is picked up
	 */
	public int getIncr() {
		return this.incr;
	}
	
	public abstract void effect(Player p);
	
	public abstract void endEffect(Player p);
}
