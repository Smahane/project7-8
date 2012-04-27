import java.awt.Point;

import robocode.HitWallEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;



public class ClairvoyanceBot extends TeamRobot {

	double bearingInDegrees = 0;
	
	@Override
	public void onScannedRobot(ScannedRobotEvent e) {
		// TODO Auto-generated method stub
		
		System.out.println(e.getName() + " " + e.getBearing());
		
		/*bearingInDegrees = event.getBearing();
		if(bearingInDegrees != 0){
			
			turnRight(bearingInDegrees);
			fire(10);
			bearingInDegrees = 0; 
		

		} */
		
		// Don't fire on teammates
		if (isTeammate(e.getName())) {
			return;
		}
		// Calculate enemy bearing
		double enemyBearing = this.getHeading() + e.getBearing();
		// Calculate enemy's position
		double enemyX = getX() + e.getDistance() * Math.sin(Math.toRadians(enemyBearing));
		double enemyY = getY() + e.getDistance() * Math.cos(Math.toRadians(enemyBearing));
		
		
		Point p = (Point)enemyX, enemyY; 
		
		// Calculate x and y to target
		double dx = p.getX() - this.getX();
		double dy = p.getY() - this.getY();
		// Calculate angle to target
		double theta = Math.toDegrees(Math.atan2(dx, dy));

		// Turn gun to target
		turnGunRight(normalRelativeAngleDegrees(theta - getGunHeading()));
		// Fire hard!
		fire(3);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//super.run();
		while (true) {
			ahead(50);
			//turnRight(90);			
		}
	}
	

	@Override
	public double getRadarHeading() {
		// TODO Auto-generated method stub
		return super.getRadarHeading();
	}

	@Override
	public void scan() {
		// TODO Auto-generated method stub

		super.scan();
	}
	
	@Override
	public void onHitWall(HitWallEvent event) {
		// TODO Auto-generated method stub
		super.onHitWall(event);
		
		turnRight(100);
		
	}

	
}
