package schiet;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import data.EnemyBot;
import data.Friend;
import data.FriendlyMap;

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

public class IgniteBot extends TeamRobot implements IntfElections {

	private static double energy = 100.0;
	private static Targeting targeting;
	private String target;
	private FriendlyMap friendlyMap;
	private boolean isLeader = false;
	private static List<Friend> map;

	public IgniteBot() {
		targeting = new Targeting(this);
		map = new LinkedList<Friend>();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		//friendlyMap = (FriendlyMap) FriendlyMap.getInstance();
		
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);

		while (true) {

			if(map.size() == 3) {
				map.clear();
			}
			
			setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
		}

	}

	public void onMessageReceived(MessageEvent e) {
		// Fire at a point
		// System.out.println("punt ontvangen");
		if (e.getMessage() instanceof EnemyBot) {
			EnemyBot ev = (EnemyBot) e.getMessage();

			target = ev.getName();
		}
		
		if (e.getMessage() instanceof Friend) {
			Friend friend = (Friend) e.getMessage();
			
			int check = 0;
			for(Friend f : map) {
				if(f.name.equalsIgnoreCase(friend.name)) {
					check = 1;
				}
			}
			
			if(check == 0) {
				map.add(friend);
			} else {
				
			}
		}
	}

	@Override
	public void onScannedRobot(ScannedRobotEvent e) {

		out.println("banaan");

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

	public static List<Friend> getMap() {
		// TODO Auto-generated method stub
		return map;
	}

}
