
import static org.junit.Assert.*;

import java.util.ArrayList;

import javax.swing.JLabel;

import org.junit.Test;

public class PlayerTest {
	
	JLabel l = new JLabel("");
	GameCourt court = new GameCourt(l);
	Player p = new Player(0, 0, 100, 100, 10, 10);
	
	@Test
	public void testBoostSize() {
		Powerup p1 = new BoostSize(0, 0, 100, 100);
		assertTrue("They intersect", p1.intersects(p));
		p1.effect(p);
		assertTrue("Size is increased", 30 == p.getMSize());
		p1.endEffect(p);
		assertTrue("Size is decreased", 20 == p.getMSize());
		}
	
	@Test
	public void testBoostSpeed() {
		Powerup p2 = new BoostSpeed(0, 0, 100, 100);
		assertTrue("They intersect", p2.intersects(p));
		p2.effect(p);
		assertTrue("Speed is increased", 15 == p.getPlayerVelocity());
		p2.endEffect(p);
		assertTrue("Speed is decreased", 10 == p.getPlayerVelocity());
	}
	
	@Test
	public void testHealth() {
		int health = p.getHealth();
		assertTrue("Health is created", health == 100);
		p.decPlayerHealth();
		assertTrue("Health is decreased", p.getHealth() == 90);
		p.incPlayerHealth();
		assertTrue("Health is increased", p.getHealth() == 100);
		for (int i = 0; i < 11; i++) {
			p.decPlayerHealth();
		}
		assertTrue("Player has died", p.zeroHealth());
	}
	
	@Test
	public void testMissiles() {
		Missile1 m = new Missile1(0, 0, 100, 100, 10);
		Missile2 m2 = new Missile2(15, 15, 100, 100, 20);
		p.addMissile1(m.pos_x, m.pos_y, 0, 0);
		p.addMissile2(m2.pos_x, m2.pos_y, 0, 0);
		ArrayList<Bullet> missiles = new ArrayList<Bullet>();
		missiles.add(m);
		missiles.add(m2);
		assertTrue("Missile added", 2 == p.getMissiles().size());
	}
	
	@Test
	public void testDynamicDispatch() {
		Player p = new Player(0, 0, 100, 100, 500, 500);
		assertTrue("Base health", p.getHealth() == 100);
		assertTrue("Base size", p.getMSize() == 20);
		assertTrue("Base speed", p.getPlayerVelocity() == 10);
		Powerup speed = new BoostSpeed(0, 0, 100, 100);
		speed.effect(p);
		assertTrue("Health unchanged", p.getHealth() == 100);
		assertTrue("Size unchanged", p.getMSize() == 20);
		assertTrue("Speed changed", p.getPlayerVelocity() == 15);
		speed.endEffect(p);
		Powerup size = new BoostSize(0, 0, 100, 100);
		size.effect(p);
		assertTrue("Health unchanged", p.getHealth() == 100);
		assertTrue("Size changed", p.getMSize() == 30);
		assertTrue("Speed unchanged", p.getPlayerVelocity() == 10);
		size.endEffect(p);
		Powerup h = new BoostHealth(0, 0, 100, 100);
		h.effect(p);
		assertTrue("Health changed", p.getHealth() == 110);
		assertTrue("Size unchanged", p.getMSize() == 20);
		assertTrue("Speed unchanged", p.getPlayerVelocity() == 10);
	}
	
	@Test
	public void testIncr() {
		Powerup speed = new BoostSpeed(0, 0, 100, 100);
		Powerup size = new BoostSize(0, 0, 100, 100);
		Powerup h = new BoostHealth(0, 0, 100, 100);
		assertTrue("speed duration is 100", 100 == speed.getIncr());
		assertTrue("size duration is 100", 100 == size.getIncr());
		assertTrue("health duration is 1", 1 == h.getIncr());
	}
}
