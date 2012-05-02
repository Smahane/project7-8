package schiet;

import robocode.AdvancedRobot;
import robocode.HitWallEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class SorakaBot extends AdvancedRobot {

	
	public SorakaBot() {
		
	}
	@Override
	public void onScannedRobot(ScannedRobotEvent e) {
		// TODO Auto-generated method stub
		double absBearing = e.getBearingRadians() + getHeadingRadians();

		setTurnRadarRightRadians(Utils.normalRelativeAngle(absBearing
				- getRadarHeadingRadians()) * 2);

		ahead(100);
		

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// super.run();
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		
		while (true) {

			turnRadarRightRadians(Double.POSITIVE_INFINITY);
		}
	}

	@Override
	public void onHitWall(HitWallEvent event) {
		super.onHitWall(event);

		turnLeft(90);
	}

	@Override
	public int getOthers() {

		return super.getOthers();
	}

}
