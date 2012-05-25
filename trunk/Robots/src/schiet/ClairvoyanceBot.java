package schiet;

import static robocode.util.Utils.normalRelativeAngleDegrees;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import data.EnemyBot;
import data.EnemyMap;
import data.Friend;
import data.FriendlyMap;

import robocode.BulletHitEvent;
import robocode.DeathEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.RobotDeathEvent;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;

public class ClairvoyanceBot extends TeamRobot implements Elections {

	double bearingInDegrees = 0;
	private int radarDirection = 1;
	private static double energy = 100.0;
	private EnemyMap enemyMap;
	private FriendlyMap friendlyMap;
	Point2D emloc = null;
	private boolean isLeader = true;

	@SuppressWarnings("static-access")
	@Override
	public void onScannedRobot(ScannedRobotEvent e) {
		// TODO Auto-generated method stub

		//out.println(friendlyMap.size());

		/*
		if (!leader) {
			Friend me = new Friend(this);
			// TeamComp.getInstance().addd(me);
		}*/

		for (EnemyBot em : enemyMap.enemyMap) {
			if (em.getName().equals(e.getName())) {
				double cur_power = em.getEnergy() - e.getEnergy();

				if (cur_power <= Rules.MAX_BULLET_POWER
						&& cur_power >= Rules.MIN_BULLET_POWER) {
					out.println("Schot.." + e.getName());
				}
			}
		}

		EnemyBot tmp = new EnemyBot(e);
		for (EnemyBot em : enemyMap.enemyMap) {
			if (em.getEnergy() < tmp.getEnergy()) {
				tmp = em;
			}
		}

		int check = 0;
		for (EnemyBot em : enemyMap.enemyMap) {
			if (e.getName().equals(em.getName())) {
				em.update(e);
				check = 1;
			}
		}

		if (check == 0 && !(isTeammate(e.getName()))) {
			enemyMap.enemyMap.add(new EnemyBot(e));
		}

		if (enemyMap.enemyMap.size() >= getOthers()) {
			enemyMap.enemyMap.clear();
			setTurnRadarLeft(getRadarTurnRemaining());
		}

		// out.println(tmp.getName() + " is victim");

		// Don't fire on teammates
		if (isTeammate(e.getName())) {
			return;
		}

		// Calculate enemy bearing
		double enemyBearing = this.getHeading() + tmp.getBearing();
		// Calculate enemy's position
		double enemyX = getX() + tmp.getDistance()
				* Math.sin(Math.toRadians(enemyBearing));
		double enemyY = getY() + tmp.getDistance()
				* Math.cos(Math.toRadians(enemyBearing));

		Point p = new Point(enemyX, enemyY);

		// Calculate x and y to target
		double dx = p.getX() - this.getX();
		double dy = p.getY() - this.getY();
		// Calculate angle to target
		double theta = Math.toDegrees(Math.atan2(dx, dy));

		// Turn gun to target
		turnGunRight(normalRelativeAngleDegrees(theta - getGunHeading()));
		// Fire hard!

		try {
			broadcastMessage(tmp);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			out.println("Kon niet verzenden");
			e1.printStackTrace();
		}

		fire(3);

	}

	@Override
	public void onPaint(Graphics2D g) {
		// TODO Auto-generated method stub
		g.setColor(java.awt.Color.blue);
		g.drawRect((int) emloc.getX(), (int) emloc.getY(), 50, 50);
	}

	@Override
	public void onRobotDeath(RobotDeathEvent event) {
		// out.println(event.getName());

		Iterator<EnemyBot> itr = enemyMap.enemyMap.iterator();
		while (itr.hasNext()) {
			EnemyBot tmp = (EnemyBot) itr.next();

			if (tmp.getName().equals(event.getName())) {
				itr.remove();
			}
		}
	}

	int sign(double v) {
		return v > 0 ? 1 : -1;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// super.run()
		//enemyMap = (EnemyMap) EnemyMap.getInstance();
		//friendlyMap = (FriendlyMap) FriendlyMap.getInstance();
		
		do {
			out.println("hoi");
			execute();

			ahead(50);
			turnRadarRightRadians(Double.POSITIVE_INFINITY);

		} while (true);

	}

	// Don't get too close to the walls

	@Override
	public void onHitWall(HitWallEvent event) {
		// TODO Auto-generated method stub

		turnLeft(180);
		ahead(100);
		super.onHitWall(event);

		System.out.println("HIT THE WALL I FAILED");

	}

	@Override
	public void onBulletHit(BulletHitEvent event) {
		// TODO Auto-generated method stub
		super.onBulletHit(event);
		if (event.getName().contains("schiet")) {
			ahead(60);
			System.out.println("EIGEN TEAM GESCHOTEN");
		}
	}

	@Override
	public void onHitRobot(HitRobotEvent event) {
		// TODO Auto-generated method stub
		super.onHitRobot(event);
		turnLeft(180);
		ahead(100);
		if (getGunHeat() == 0) {
			fire(Rules.MAX_BULLET_POWER);
		}
	}

	@Override
	public void setLeader(boolean isLeader) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeath(DeathEvent event) {

	}

}
