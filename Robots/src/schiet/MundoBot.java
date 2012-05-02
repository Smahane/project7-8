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

public class MundoBot extends TeamRobot {

	public static double energy = 100.0;
	private static Targeting targeting;
	private String target;

	public MundoBot() {
		targeting = new Targeting(this);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);

		while (true) {
			turnRadarRightRadians(Double.POSITIVE_INFINITY);
		}

	}

	public void onMessageReceived(MessageEvent e) {
		// Fire at a point
		// System.out.println("punt ontvangen");
		if (e.getMessage() instanceof EnemyBot) {
			EnemyBot ev = (EnemyBot) e.getMessage();

			if (target == null) {
				target = ev.getName();
			}
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

		double enemyAbsoluteBearing = getHeadingRadians()
				+ e.getBearingRadians();

		targeting.onScannedRobot(e);

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
