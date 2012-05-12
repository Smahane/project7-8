package schiet;

import java.awt.Color;
import java.awt.geom.Point2D;

import robocode.AdvancedRobot;
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

public class IgniteBot extends TeamRobot implements Elections {

	private static double energy = 100.0;
	private static Targeting targeting;
	private String target;

	public IgniteBot() {
		targeting = new Targeting(this);
	}

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
	}

	@Override
	public void onScannedRobot(ScannedRobotEvent e) {

		out.println("banaan");

		EnemyMap.getInstance().FriendlyMap.add(new EnemyBot(e));
		//EnemyMap.FriendlyMap.add(new EnemyBot(e));
		//TeamComp.getInstance().addd(new Friend(this));
		// Friend me = new Friend(this);


		if (isTeammate(e.getName())) {
			return;
		}

		targeting.onScannedRobot(e);

	}

	@Override
	public void onHitByBullet(HitByBulletEvent event) {
		ahead(50);
	}

	@Override
	public void onHitRobot(HitRobotEvent event) {
		// TODO Auto-generated method stub
		super.onHitRobot(event);

	}

	@Override
	public void onHitWall(HitWallEvent event) {
		// TODO Auto-generated method stub
		super.onHitWall(event);

		turnRight(100);

	}

	@Override
	public void setLeader(boolean isLeader) {
		// TODO Auto-generated method stub

	}

}
