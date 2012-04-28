package schiet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import robocode.BulletHitEvent;
import robocode.HitRobotEvent;
import robocode.CustomEvent;
import robocode.HitWallEvent;
import robocode.MessageEvent;
import robocode.RadarTurnCompleteCondition;
import robocode.Robot;
import robocode.Rules;
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
		System.out.println(getBattleFieldHeight() + "  " + getBattleFieldWidth());
		
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
			
			double dy = this.getY();
			double dx = this.getX();
			
			/*if(dy <= 40 || dy <= (getBattleFieldHeight() - 40)){
				back(100);
				System.out.println("Dodged the Wall");
			}
			if(dx <= 40 || dx <= (getBattleFieldWidth() - 40)){
				back(100);
				System.out.println("Dodged the Wall");
			}	*/
			
			ahead(50);
			
			for(EnemyBot em : enemies.values()) {
				System.out.println(em.getName() + " " + em.getDistance());
			}

		}
	}

		
	@Override
	public void onHitWall(HitWallEvent event) {
		// TODO Auto-generated method stub
		super.onHitWall(event);
		
		System.out.println("HIT THE WALL I FAILED");
		
	}

	public void onMessageReceived(MessageEvent e) {
		// Fire at a point
		System.out.println("punt ontvangen van "+ e.getSender());
		if (e.getMessage() instanceof Point) {
			Point p = (Point) e.getMessage();
			// Calculate x and y to target
			double dx = p.getX() - this.getX();
			double dy = p.getY() - this.getY();
			// Calculate angle to target
			double theta = Math.toDegrees(Math.atan2(dx, dy));

			// Turn gun to target
			turnGunRight(normalRelativeAngleDegrees(theta - getGunHeading()));
			// Fire hard!
			fire(3);
		} // Set our colors
	}
	@Override
	public void onBulletHit(BulletHitEvent event) {
		// TODO Auto-generated method stub
		super.onBulletHit(event);
		if(event.getName().contains("schiet")){
			ahead(10);
			System.out.println("EIGEN TEAM GESCHOTEN");
		}
	}
	
	@Override
	public void onHitRobot(HitRobotEvent event) {
		// TODO Auto-generated method stub
		super.onHitRobot(event);
		turnRight(180);
		if (getGunHeat() == 0) {
			fire(Rules.MAX_BULLET_POWER);
		}
	}

	@Override
	public double getBattleFieldHeight() {
		// TODO Auto-generated method stub
		return super.getBattleFieldHeight();
	}

	@Override
	public double getBattleFieldWidth() {
		// TODO Auto-generated method stub
		return super.getBattleFieldWidth();
	}

}
