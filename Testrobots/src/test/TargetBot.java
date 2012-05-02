package test;

import java.util.ArrayList;
import java.util.List;

import robocode.AdvancedRobot;
import robocode.Rules;
import robocode.ScannedRobotEvent;

public class TargetBot extends AdvancedRobot {

	List<Wave> waves;
	static int[] stats;
	int direction = 1;
	private static double BULLET_POWER = 1.9;

	@Override
	public void onScannedRobot(ScannedRobotEvent e) {

		double enemyAbsoluteBearing = getHeadingRadians() + e.getBearingRadians();
		
		setTurnRadarRightRadians(robocode.util.Utils.normalRelativeAngle(enemyAbsoluteBearing - getRadarHeadingRadians()) * 2);
		
	}

	@Override
	public void run() {
		
		setAdjustRadarForGunTurn(true);
		setAdjustGunForRobotTurn(true);
		do {
			turnRadarRightRadians(Double.POSITIVE_INFINITY); 
		} while (true);
	}

}
