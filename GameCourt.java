/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

import javax.imageio.ImageIO;
import javax.swing.*;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 * 
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

	// the state of the game logic
	public Player p1;
	public Player p2;
	public ArrayList<Bullet> missilesP1 = new ArrayList<Bullet>();
	public ArrayList<Bullet> missilesP2 = new ArrayList<Bullet>();
	public ArrayList<Powerup> powerups = new ArrayList<Powerup>();
	public ArrayList<Meteor> meteors = new ArrayList<Meteor>();
	public ArrayList<Integer> inputs = new ArrayList<Integer>();
	
	private String nameP1 = null;
	private String nameP2 = null;
	
	private boolean canShootP1 = true;
	private boolean canShootP2 = true;
	
	Random rand = new Random();
	
	public boolean playing = false; // whether the game is running
	private JLabel status; // Current status text (i.e. Running...)

	// Game constants
	public static final int COURT_WIDTH = 1000;
	public static final int COURT_HEIGHT = 600;
	public static final int MISSLE1_VELOCITY = 10;
	public static final int MISSLE2_VELOCITY = 10;
	// Update interval for timer, in milliseconds
	public static final int INTERVAL = 35;
	
	//Counters for tracking highg score
	private int countP1 = 0;
	private int countM1 = 0;
	private int countP2 = 0;
	private int countM2 = 0;
	public static BufferedImage background;
	public static final String imgFile = "background.png";
	
	//IO objects
	public Writer f;
	public BufferedReader br;
	public File file;
	
	public ArrayList<String[]> scores = new ArrayList<String[]>();

	public GameCourt(JLabel status) {
		// creates border around the court area, JComponent method
		setBorder(BorderFactory.createLineBorder(Color.BLACK));

		try {
			if (background == null) {
				background = ImageIO.read(new File(imgFile));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
		
		try {
			//file = new File("HighScores.txt");
			PrintWriter pw = new PrintWriter("HighScores.txt");
			pw.write("");
			f = new BufferedWriter(new FileWriter("HighScores.txt", true));
		} catch (IOException e) {
			System.out.println("IO Exception High Scores");
		}
		
		// The timer is an object which triggers an action periodically
		// with the given INTERVAL. One registers an ActionListener with
		// this timer, whose actionPerformed() method will be called
		// each time the timer triggers. We define a helper method
		// called tick() that actually does everything that should
		// be done in a single timestep.
		
		Timer timer = new Timer(INTERVAL, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tick();
			}
		});
		timer.start(); // MAKE SURE TO START THE TIMER!

		// Enable keyboard focus on the court area.
		// When this component has the keyboard focus, key
		// events will be handled by its key listener.
		setFocusable(true);

		// This key listener allows the square to move as long
		// as an arrow key is pressed, by changing the square's
		// velocity accordingly. (The tick method below actually
		// moves the square.)
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
					inputs.add(e.getKeyCode());
			}

			public void keyReleased(KeyEvent e) {
				p1.v_x = 0;
				p1.v_y = 0;
				p2.v_x = 0;
				p2.v_y = 0;
			}
		});

		this.status = status;
	}

	/**
	 * (Re-)set the game to its initial state.
	 */
	public void reset() {
		playing = false;
		clear();
		p1 = new Player(0, 150, COURT_WIDTH, COURT_HEIGHT, 150, 0);
		p2 = new Player(1000, 150, COURT_WIDTH, COURT_HEIGHT, 750, 0);
		
		countM1 = 0;
		countM2 = 0;
		countP1 = 0;
		countP2 = 0;
		
		nameP1 = JOptionPane.showInputDialog(null, 
				"Welcome to Wizard Duel! \nPlayer 1, please input your name");
		nameP2 = JOptionPane.showInputDialog(null,
				"Player 2, please input your name");
		if (nameP1 == null || nameP1 == "") {
			JOptionPane.showMessageDialog(null, "Please enter your name, Player 1");
			System.exit(0);
		}
		if (nameP2 == null || nameP2 == "") {
			JOptionPane.showMessageDialog(null, "Please enter your name, Player 2");
			System.exit(0);
		}
		JOptionPane.showMessageDialog(null, 
				"Welcome to Wizard Duel! To win, you must bring your opponent's "
				+ "life to zero!");
		JOptionPane.showMessageDialog(null, "Player 1, your controls are: \n"
				+ "W: Move up \nS: Move down \nD: Shoot a straight red bullet"
				+ "\nA: Shoot two blue bullets that bounce towards the enemy");
		JOptionPane.showMessageDialog(null, "Player 2, your controls are: \n"
				+ "Up: Move up \nDown: Move down \nLeft: Shoot a straight red bullet"
				+ "\nRight: Shoot two blue bullets that bounce towards the enemy");
		JOptionPane.showMessageDialog(null, "Throughout the game, powerups will spawn. \n"
				+ "The horse powerup increases speed \n"
				+ "The bull powerup increases the size of your missiles\n"
				+ "The health powerup increases your health level");
		JOptionPane.showMessageDialog(null, "You can only have up to 10 "
				+ "missiles on the board at once. \nThe green bar represents"
				+ " your current ammo bar.");
		JOptionPane.showMessageDialog(null, "Meteors will randomly spawn, flying "
				+ "in random directions towards the player. Getting hit by a meteor "
				+ "will reduce health.");
		JOptionPane.showMessageDialog(null, "Missiles can destroy meteors and other "
				+ "missiles! Try to ensure your missiles will reach your opponent while"
				+ "destroying your opponent's missiles");
		playing = true;
		status.setText("Running...");

		// Make sure that this component has the keyboard focus
		requestFocusInWindow();
	}

	/**
	 * This method is called every time the timer defined in the constructor
	 * triggers.
	 */
	void tick() {
		if (playing) {
			
			for (Integer i: inputs) {
				int input = (int) i;
				switch (input) {
				case KeyEvent.VK_DOWN:
					p2.v_y = p2.getPlayerVelocity();
					break;
				case KeyEvent.VK_UP:
					p2.v_y = -p2.getPlayerVelocity();
					break;
				case KeyEvent.VK_W:
					p1.v_y = -p1.getPlayerVelocity();
					break;
				case KeyEvent.VK_S:
					p1.v_y = p1.getPlayerVelocity();
					break;
				case KeyEvent.VK_LEFT:
					if (canShootP2) {
						p2.addMissile1(p2.pos_x - 50, p2.pos_y + 40, -MISSLE1_VELOCITY, 0);
						countM2++;
					}
					break;
				case KeyEvent.VK_RIGHT:
					if (canShootP2) {
						p2.addMissile2(p2.pos_x - 30, p2.pos_y + 40, 
								-MISSLE2_VELOCITY, -MISSLE2_VELOCITY);
						p2.addMissile3(p2.pos_x - 30, p2.pos_y + 40, 
								-MISSLE2_VELOCITY, MISSLE2_VELOCITY);
						countM2 += 2;
					}
					break;
				case KeyEvent.VK_D:
					if (canShootP1) {
						p1.addMissile1(p1.pos_x + 80, p1.pos_y + 40, MISSLE1_VELOCITY, 0);
						countM1++;
					}
					break;
				case KeyEvent.VK_A:
					if (canShootP1) {
						p1.addMissile2(p1.pos_x + 30, p1.pos_y + 40, 
								MISSLE2_VELOCITY, -MISSLE2_VELOCITY);
						p1.addMissile3(p1.pos_x + 30, p1.pos_y + 40, 
								MISSLE2_VELOCITY, MISSLE2_VELOCITY);
						countM2 += 2;
					}
					break;
				default:
					break;
				}
			}
			inputs.clear();
			p1.move();
			p2.move();
			p1.intersectsHealth();
			p2.intersectsHealth();
			
			if (missilesP1.size() > 10) {
				canShootP1 = false;
			}
			else 
				canShootP1 = true;
			if (missilesP2.size() > 10) {
				canShootP2 = false;
			}
			else
				canShootP2 = true;
			
			
			if (rand.nextInt(101) < 2) {
				int random = rand.nextInt(3);
				int p_posx = rand.nextInt(2);
				int p_posy = 0;
				switch (random) {
				case 0:
					if (p_posx == 0) 
						p_posx = 25;
					else
						p_posx = 950;
					p_posy = rand.nextInt(550);
					Powerup p1 = new BoostSize(p_posx, p_posy, COURT_WIDTH, COURT_HEIGHT);
					powerups.add(p1);
					break;
				case 1:
					if (p_posx == 0) 
						p_posx = 25;
					else
						p_posx = 950;
					p_posy = rand.nextInt(550);
					Powerup p2 = new BoostSpeed(p_posx, p_posy, COURT_WIDTH, COURT_HEIGHT);
					powerups.add(p2);
					break;
				case 2:
					if (p_posx == 0) 
						p_posx = 25;
					else
						p_posx = 950;
					p_posy = rand.nextInt(550);
					Powerup p3 = new BoostHealth(p_posx, p_posy, COURT_WIDTH, COURT_HEIGHT);
					powerups.add(p3);
				}
			}
			
			if (rand.nextInt(101) < 10) {
				int random = rand.nextInt(4);
				int me_vx = 0;
				int me_vy = 0;
				int me_pos_x = 0;
				int me_pos_y = 0;
				switch (random) {
				case 0:
					me_vx = rand.nextInt(10) + 7;
					me_vy = rand.nextInt(10) + 5;	
					me_pos_x = rand.nextInt(251) + 250;
					me_pos_y = 0;
					Meteor me1 = new Meteor(me_vx, me_vy, me_pos_x, me_pos_y, 
							COURT_WIDTH, COURT_HEIGHT);
					meteors.add(me1);
					break;
				case 1:
					me_vx = -(rand.nextInt(10) + 7);
					me_vy = rand.nextInt(10) + 5;	
					me_pos_x = rand.nextInt(251) + 500;
					me_pos_y = 0;
					Meteor me2 = new Meteor(me_vx, me_vy, me_pos_x, me_pos_y, 
							COURT_WIDTH, COURT_HEIGHT);
					meteors.add(me2);
					break;
				case 2:
					me_vx = rand.nextInt(10) + 7;
					me_vy = -((rand.nextInt(10)) + 5);	
					me_pos_x = rand.nextInt(251) + 500;
					me_pos_y = COURT_HEIGHT - 70;
					Meteor me3 = new Meteor(me_vx, me_vy, me_pos_x, me_pos_y, 
							COURT_WIDTH, COURT_HEIGHT);
					meteors.add(me3);
					break;
				case 3:
					me_vx = -(rand.nextInt(10) + 7);
					me_vy = -((rand.nextInt(10)) + 5);
					me_pos_x = rand.nextInt(251) + 250;
					me_pos_y = COURT_HEIGHT - 70;
					Meteor me4 = new Meteor(me_vx, me_vy, me_pos_x, me_pos_y, 
							COURT_WIDTH, COURT_HEIGHT);
					meteors.add(me4);
					break;
				}
	
			}	
			Iterator<Powerup> iterP = powerups.iterator();
			while (iterP.hasNext()) {
				Powerup p = iterP.next();
				if ((p.intersects(p2))) {
					p.incDuration();
					p.setPlayer(p2);
					p.changeLocation(500, 600);
					countP2++;
				}
				else if (p.intersects(p1)) {
					p.incDuration();
					p.setPlayer(p1);
					p.changeLocation(500, 600);
					countP1++;
				}
				if (p.getDuration() > 0) {
					p.effect(p.getPlayer());
					p.decDuration();
				}
				else if (p.getPlayer() != null) {
					p.endEffect(p.getPlayer());
				}
			}
			
			missilesP1 = p1.getMissiles();
			missilesP2 = p2.getMissiles();
			
			Iterator<Bullet> iter1 = missilesP1.iterator();
			while (iter1.hasNext()) {
				Bullet m = iter1.next();
				m.move();
				if (m.intersects(p2)) {
					p2.decPlayerHealth();
					iter1.remove();
				}
				else if (m.hitWall() == Direction.RIGHT)
					iter1.remove();
				else if (m.hitWall() != Direction.LEFT
						&& m.hitWall() != Direction.RIGHT) {
					m.bounce(m.hitWall());
				}
			}
			Iterator<Bullet> iter2 = missilesP2.iterator();
			while (iter2.hasNext()) {
				Bullet m = iter2.next();
				m.move();
				if (m.intersects(p1)) {
					p1.decPlayerHealth();
					iter2.remove();
				}
				else if (m.hitWall() != Direction.LEFT
					&& m.hitWall() != Direction.RIGHT) {
					m.bounce(m.hitWall());
				}
				else if (m.hitWall() == Direction.LEFT) {
					iter2.remove();
				}
		}
			
			Iterator<Meteor> iter7 = meteors.iterator();
			while(iter7.hasNext()) {
				Meteor m = iter7.next();
				m.move();
				if (m.hitWall() != null) {
					iter7.remove();
				}
				else if (m.intersects(p1)) {
					p1.decPlayerHealth();
					iter7.remove();
				}
				else if (m.intersects(p2)) {
					p2.decPlayerHealth();
					iter7.remove();
				}
				else {
					boolean b1 = intersectMeteorMissile(m, missilesP1);
					boolean b2 = intersectMeteorMissile(m, missilesP2);
					if (b1 || b2) {
						iter7.remove();
					}
				}
				
			}
			if (p1.zeroHealth()) {
				playing = false;
				JOptionPane.showMessageDialog(null, "GAME OVER \n" + nameP2 + " wins!");
				status.setText(nameP2 + " wins!");
				freeze();
				try {
					System.out.println(countM2 + "," + countP2 + "," + nameP2);
					f.write(countM2 + "," + countP2 + "," + nameP2 + "\n");
					f.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			else if (p2.zeroHealth()) {
				playing = false;
				JOptionPane.showMessageDialog(null, "GAME OVER \n" + nameP1 + " wins!");
				status.setText(nameP1 + " wins!");
				freeze();
				try {
					System.out.println(countM1 + "," + countP1 + "," + nameP1);
					f.write(countM1 + "," + countP1 + "," + nameP1 + "\n");
					f.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			intersectMissileMissile(missilesP1, missilesP2);
			
			
			repaint();
		}
	}
	

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g); 
		g.drawImage(background, 0, 0, 1000, 600, 0, 0, 
				background.getWidth(), background.getHeight(), null);
		p1.draw(g);
		p2.draw(g);
		for (Bullet m: missilesP1) {
			m.draw(g);
		}
		for (Bullet m: missilesP2) {
			m.draw(g);
		}
		for (Powerup p: powerups) {
			p.draw(g);
		}
		for (Meteor m: meteors) {
			m.draw(g);
		}
	}
	
	/** Tests if a Meteor intersects any missiles in an ArrayList of missiles. 
	 * If it does intersect a missile, it removes the missile from that ArrayList
	 * 
	 * @param me The meteor
	 * @param missiles An ArrayList of Bullets
	 * @return True if it intersects any, false if it doesn't.
	 */
	public boolean intersectMeteorMissile (Meteor me, ArrayList<Bullet> missiles) {
		for (Bullet m: missiles) {
			if (m.intersects(me)) {
				missiles.remove(m);
				return true;
			}
		}
		return false;
	}
	
	/** Tests if any missile in an ArrayList of Bullets intersects any missiles in 
	 * another ArrayList of Bullets. 
	 * If it does intersect a missile, it removes both missiles from the ArrayLists
	 * 
	 * @param missiles1 The first ArrayList
	 * @param missiles2 The second ArrayList
	 * @return True if it intersects any, false if it doesn't.
	 */
	public boolean intersectMissileMissile(ArrayList<Bullet> missiles1,
			ArrayList<Bullet> missiles2) {
		for (Bullet m1: missiles1) {
			for (Bullet m2: missiles2) {
				if (m1.intersects(m2)) {
					missiles1.remove(m1);
					missiles2.remove(m2);
					return true;
					}
				}
			}
		return false;
	}
	
	public void freeze() {
		for (Bullet m: missilesP1) {
			m.v_x = 0;
			m.v_y = 0;
		}
		for (Bullet m: missilesP2) {
			m.v_x = 0;
			m.v_y = 0;
		}
		for (Meteor m: meteors) {
			m.v_x = 0;
			m.v_y = 0;
		}
	}
	
	/** Parses through a text file, gets the highest score, stores them, displays the top
	 * 10 high scores of the game.
	 * Used in Game.java
	 * @throws IOException
	 */
	public void getHighScores() throws IOException {
		try {
			br = new BufferedReader(new FileReader("HighScores.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("File is not found");
		}
		String nextLine = null;
		 while ((nextLine = br.readLine()) != null) {
			 System.out.println("Reaches here");
			  if (!nextLine.contains(",")) {
				  throw new IOException("no comma");
			  }
			  String[] splitLine = nextLine.split(",");
			  if (splitLine.length < 3) {
				  throw new IOException();
			  }
			  scores.add(splitLine);
		        Collections.sort(scores,new Comparator<String[]>() {
		            public int compare(String[] str, String[] otherStr) {
		                return str[0].compareTo(otherStr[0]);
		            }
		        });		 
		 }
		 String str = "\t\t\t\t\t\t\t" + 
				 "\t\t\t\t\t\t\t\t\t\t\t\tHIGH SCORES\nName\t"
				 + "\t\t\t\t\t\t\t\t\t\t\tMissiles Used\t\t\t\t Powerups Used\n";
		 for (int i = 0; i < scores.size() && i < 10; i++) {
			 String[] s = scores.get(i);
			 str = str + (i+1) + ": " + s[2] + "\t\t\t\t\t\t\t\t\t\t\t" 
					 + s[0] + "\t\t\t\t\t\t\t\t\t\t\t"
					 		+ "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + s[1] + "\n";
		 }
		 scores.clear();
		 JOptionPane.showMessageDialog(null, str);
		 br.close();
	}
	
	/** Clears all collections on the board. Called every time the game is restarted
	 * 
	 */
	public void clear() {
		missilesP1.clear();
		missilesP2.clear();
		meteors.clear();
		powerups.clear();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(COURT_WIDTH, COURT_HEIGHT);
	}
}
