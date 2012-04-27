import java.awt.Color;

import robocode.GunTurnCompleteCondition;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.Robot;
import robocode.Rules;
import robocode.ScannedRobotEvent;

public class MundoBot extends Robot {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		setAllColors(Color.pink);
		while (true) {
			ahead(50);
			System.out.println("Mundo Mundo");
			
			fire(1);
			
		}
		
	}
	
	@Override
	public void onScannedRobot(ScannedRobotEvent event) {
		// TODO Auto-generated method stub
		super.onScannedRobot(event);
		
		System.out.println(event.getName());
	}

	@Override
	public void onHitByBullet(HitByBulletEvent event) {
	       out.println(event.getName() + " hit me!");
	   }
	
	@Override
	public void onHitRobot(HitRobotEvent event) {
		// TODO Auto-generated method stub
		super.onHitRobot(event);
		
		if(getGunHeat() == 0) {
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
