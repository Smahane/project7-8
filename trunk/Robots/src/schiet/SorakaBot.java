package schiet;
import robocode.HitWallEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;

public class SorakaBot extends Robot {

	@Override
	public void onScannedRobot(ScannedRobotEvent event) {
		// TODO Auto-generated method stub
		super.onScannedRobot(event);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//super.run();
		while (true) {
			ahead(100);
			//turnRight(90);
			
			if(getOthers() != 0){
		fireBullet(3);}
		}
	}

	@Override
	public void onHitWall(HitWallEvent event) {
		super.onHitWall(event);
		
		turnLeft(180);
	}

	@Override
	public int getOthers() {
		
		return super.getOthers();
	}

}
