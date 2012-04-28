package schiet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import robocode.CustomEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.RadarTurnCompleteCondition;
import robocode.Robot;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;

import static robocode.util.Utils.normalRelativeAngleDegrees;
import static robocode.util.Utils.normalRelativeAngle;

public class ClairvoyanceBot extends TeamRobot {

	double bearingInDegrees = 0;
	private int radarDirection = 1;
	private HashMap<String, EnemyBot> enemies;

	@Override
	public void onScannedRobot(ScannedRobotEvent e) {
		// TODO Auto-generated method stub

		int check = 0;
		for (EnemyBot em : enemies.values()) {
			if (e.getName().equals(em.getName())) {
				em.update(e);
				check = 1;
			}
		}

		if (check == 0 && !(isTeammate(e.getName()))) {
			enemies.put(e.getName(), new EnemyBot(e));
		}

		EnemyBot tmp = null;
		for (EnemyBot em : enemies.values()) {
			if (tmp != null) {
				if (em.getDistance() < tmp.getDistance()) {
					tmp = em;
				}
			} else if(tmp == null) {
				tmp = em;
			}
		}

		// Don't fire on teammates
		if (isTeammate(e.getName())) {
			return;
		}

		// Calculate enemy bearing
		double enemyBearing = this.getHeading() + e.getBearing();
		// Calculate enemy's position
		double enemyX = getX() + e.getDistance()
				* Math.sin(Math.toRadians(enemyBearing));
		double enemyY = getY() + e.getDistance()
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
			broadcastMessage(new Point(enemyX, enemyY));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			out.println("Kon niet verzenden");
			e1.printStackTrace();
		}

		fire(3);

	}

	@Override
	public void onHitRobot(HitRobotEvent event) {
		turnLeft(100);
		ahead(100);
	}

	@Override
	public void onRobotDeath(RobotDeathEvent event) {
		out.println(event.getName());
		
		Iterator<EnemyBot> itr = enemies.values().iterator();
		while(itr.hasNext()) {
			EnemyBot tmp = (EnemyBot) itr.next();
			
			if(tmp.getName().equals(event.getName())) {
				itr.remove();
			}
		}
	}

	private void sweep() {
		double maxBearingAbs = 0, maxBearing = 0;
		int scannedBots = 0;
		Iterator iterator = enemies.values().iterator();

		while (iterator.hasNext()) {
			EnemyBot tmp = (EnemyBot) iterator.next();

			if (tmp != null && !(tmp.none())) {
				double bearing = normalRelativeAngle(getHeading()
						+ tmp.getBearing() - getRadarHeading());
				if (Math.abs(bearing) > maxBearingAbs) {
					maxBearingAbs = Math.abs(bearing);
					maxBearing = bearing;
				}
				scannedBots++;
			}
		}

		double radarTurn = 180 * radarDirection;
		if (scannedBots == getOthers())
			radarTurn = maxBearing + sign(maxBearing) * 22.5;

		setTurnRadarRight(radarTurn);
		radarDirection = sign(radarTurn);
	}

	int sign(double v) {
		return v > 0 ? 1 : -1;
	}

	public void onCustomEvent(CustomEvent e) {
		if (e.getCondition() instanceof RadarTurnCompleteCondition)
			sweep();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// super.run();
		enemies = new HashMap<String, EnemyBot>();

		addCustomEvent(new RadarTurnCompleteCondition(this));
		setAdjustRadarForGunTurn(true);
		setTurnRadarRight(360);

		while (true) {
			ahead(50);
			
			for(EnemyBot em : enemies.values()) {
				System.out.println(em.getName() + " " + em.getDistance());
			}
		}
	}

	@Override
	public double getRadarHeading() {
		// TODO Auto-generated method stub
		return super.getRadarHeading();
	}

	@Override
	public void scan() {
		// TODO Auto-generated method stub

		super.scan();
	}

	@Override
	public void onHitWall(HitWallEvent event) {
		// TODO Auto-generated method stub
		super.onHitWall(event);

		turnRight(100);

	}

}
