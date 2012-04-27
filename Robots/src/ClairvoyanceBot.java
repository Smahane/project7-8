import robocode.HitWallEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;



public class ClairvoyanceBot extends Robot {

	double bearingInDegrees = 0;
	
	@Override
	public void onScannedRobot(ScannedRobotEvent event) {
		// TODO Auto-generated method stub
		
		System.out.println(event.getName() + " " + event.getBearing());
		
		bearingInDegrees = event.getBearing();
		if(bearingInDegrees != 0){
			
			turnRight(bearingInDegrees);
			fire(1);
			bearingInDegrees = 0;

		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//super.run();
		while (true) {
			ahead(100);
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
