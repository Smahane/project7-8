package schiet;

import java.awt.Color;

import robocode.GunTurnCompleteCondition;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.MessageEvent;
import robocode.Robot;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;
import robocode.util.Utils;

import static robocode.util.Utils.normalRelativeAngleDegrees;

public class IgniteBot extends TeamRobot {

	public static double energy = 100.0;
	private String target;

	@Override
	public void run() {
		// TODO Auto-generated method stub

		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);

		while (true) {
			ahead(50);
			turnRadarRightRadians(Double.POSITIVE_INFINITY);
		}

	}

	public void onMessageReceived(MessageEvent e) {
		// Fire at a point
		// System.out.println("punt ontvangen");
		if (e.getMessage() instanceof EnemyBot) {
			EnemyBot ev = (EnemyBot) e.getMessage();

			target = ev.getName();
		}
		if (e.getMessage() instanceof Point) {
			Point p = (Point) e.getMessage();
			// Calculate x and y to target
			double dx = p.getX() - this.getX();
			double dy = p.getY() - this.getY();
			// Calculate angle to target
			double theta = Math.toDegrees(Math.atan2(dx, dy));
			double radarTurn = Utils.normalRelativeAngle(theta
					- getRadarHeadingRadians());

			double extraTurn = Math.min(Math.atan(36.0 / 40),
					Rules.RADAR_TURN_RATE_RADIANS);
			radarTurn += (radarTurn < 0 ? -extraTurn : extraTurn);
			out.println(radarTurn);

			// Turn gun to target
			turnRadarRight(normalRelativeAngleDegrees(theta - getGunHeading()));
			turnGunRight(normalRelativeAngleDegrees(theta - getGunHeading()));
			// Fire hard!
			fire(3);
		} // Set our colors
	}

	@Override
	public void onScannedRobot(ScannedRobotEvent e) {

		double cur_power = energy - e.getEnergy();
		if (cur_power < 3.01 && cur_power > 0.09) {
			out.println("shot");
			turnLeft(90);
			ahead(50);
		}

		energy = e.getEnergy();

		if (target == null) {
		}

		if (e.getName().equals(target)) {
			double absBearing = e.getBearingRadians() + getHeadingRadians();

			setTurnRadarRightRadians(Utils.normalRelativeAngle(absBearing
					- getRadarHeadingRadians()) * 2);

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

			fire(3);
		}

	}

	@Override
	public void onHitByBullet(HitByBulletEvent event) {
		turnRight(50);
		ahead(50);
	}

	@Override
	public void onHitRobot(HitRobotEvent event) {
		// TODO Auto-generated method stub
		super.onHitRobot(event);

		if (getGunHeat() == 0) {
			fire(Rules.MAX_BULLET_POWER);
		}
	}

	@Override
	public void onHitWall(HitWallEvent event) {
		// TODO Auto-generated method stub
		super.onHitWall(event);

		turnRight(100);

	}

}
