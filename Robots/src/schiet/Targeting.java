package schiet;

import java.awt.geom.Point2D;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class Targeting {
	private AdvancedRobot robot;
	private static double lateralDirection;
	private static double lastEnemyVelocity;
	private static final double BULLET_POWER = 1.92;

	Targeting(AdvancedRobot robot) {
		this.robot = robot;
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		double enemyAbsoluteBearing = robot.getHeadingRadians()
				+ e.getBearingRadians();
		double enemyDistance = e.getDistance();
		double enemyVelocity = e.getVelocity();

		if (enemyVelocity != 0) {
			lateralDirection = schiet.Utils.sign(enemyVelocity
					* Math.sin(e.getHeadingRadians() - enemyAbsoluteBearing));
			// System.out.println(lateralDirection);
		}
		Wave wave = new Wave(robot);
		wave.gunLocation = new Point2D.Double(robot.getX(), robot.getY());
		Wave.targetLocation = schiet.Utils.project(wave.gunLocation,
				enemyAbsoluteBearing, enemyDistance);
		wave.lateralDirection = lateralDirection;
		wave.bulletPower = BULLET_POWER;
		wave.setSegmentations(enemyDistance, enemyVelocity, lastEnemyVelocity);
		lastEnemyVelocity = enemyVelocity;
		wave.bearing = enemyAbsoluteBearing;
		robot.setTurnGunRightRadians(Utils
				.normalRelativeAngle(enemyAbsoluteBearing
						- robot.getGunHeadingRadians()
						+ wave.mostVisitedBearingOffset()));
		robot.setFire(wave.bulletPower);
		if (robot.getEnergy() >= BULLET_POWER) {
			robot.addCustomEvent(wave);
			//ClairvoyanceBot.waves.add(wave);
		}
		robot.setTurnRadarRightRadians(Utils
				.normalRelativeAngle(enemyAbsoluteBearing
						- robot.getRadarHeadingRadians()) * 2);
	}
}
