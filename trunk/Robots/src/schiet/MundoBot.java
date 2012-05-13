package schiet;

import java.awt.Color;
import java.awt.geom.Point2D;

import data.EnemyBot;
import data.FriendlyMap;

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

public class MundoBot extends TeamRobot implements Elections {

	public static double energy = 100.0;
	private static Targeting targeting;
	private String target;
	private FriendlyMap friendlyMap;

	public MundoBot() {
		targeting = new Targeting(this);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		friendlyMap = (FriendlyMap) FriendlyMap.getInstance();
		
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
	}

	@Override
	public void onScannedRobot(ScannedRobotEvent e) {

		friendlyMap.log(this);
		System.out.println(friendlyMap.size());
		//FriendlyMap.getInstance().log(this);

		if (isTeammate(e.getName())) {
			return;
		}

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

	@Override
	public void setLeader(boolean isLeader) {
		// TODO Auto-generated method stub

	}

}
