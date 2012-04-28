package schiet;
import java.awt.Color;
import java.io.IOException;

import robocode.BulletHitEvent;
import robocode.GunTurnCompleteCondition;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.MessageEvent;
import robocode.Robot;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;


import static robocode.util.Utils.normalRelativeAngleDegrees;

public class MundoBot extends TeamRobot {

	@Override
	public void run() {
		// TODO Auto-generated method stub
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
			//System.out.println("Test");


		}
	}

	

	public void onMessageReceived(MessageEvent e) {
		// Fire at a point
		//System.out.println("punt ontvangen van " +  e.getSender());
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
	public void onScannedRobot(ScannedRobotEvent e) {
		// TODO Auto-generated method stub
		super.onScannedRobot(e);

		if (isTeammate(e.getName())) {
			return;
		}
		// Calculate enemy bearing
		double enemyBearing = this.getHeading() + e.getBearing();
		// Calculate enemy's position
		double enemyX = getX() + e.getDistance() * Math.sin(Math.toRadians(enemyBearing));
		double enemyY = getY() + e.getDistance() * Math.cos(Math.toRadians(enemyBearing));
		
		
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
	public void onHitByBullet(HitByBulletEvent event) {
		out.println(event.getName() + " hit me!");
	}

	@Override
	public void onHitRobot(HitRobotEvent event) {
		// TODO Auto-generated method stub
		super.onHitRobot(event);
		
		turnRight(90);
		
		if (getGunHeat() == 0) {
			fire(Rules.MAX_BULLET_POWER);
		}
	}

	@Override
	public void onHitWall(HitWallEvent event) {
		// TODO Auto-generated method stub
		super.onHitWall(event);

		System.out.println("HIT THE WALL I FAILED");

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
	
	

}
