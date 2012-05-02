package test;


import robocode.*;
import robocode.util.Utils;
import java.awt.Color;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;
 
// GFTargetingBot, by PEZ. A simple GuessFactorTargeting bot for tutorial purposes.
// Use the code as you see fit. Of course if I do not mind credits.
 
public class ClairvoyanceBot extends AdvancedRobot {
	private static final double BULLET_POWER = 1.9;
 
	private static double lateralDirection;
	private static double lastEnemyVelocity;
	private static GFTMovement movement;
	public static List<Wave> waves;
 
	public ClairvoyanceBot() {
		movement = new GFTMovement(this);	
	}
 
	public void run() {
		waves = new ArrayList<Wave>();
		setColors(Color.BLUE, Color.BLACK, Color.YELLOW);
		lateralDirection = 1;
		lastEnemyVelocity = 0;
		setAdjustRadarForGunTurn(true);
		setAdjustGunForRobotTurn(true);
		do {
			turnRadarRightRadians(Double.POSITIVE_INFINITY); 
		} while (true);
	}
 
	public void onScannedRobot(ScannedRobotEvent e) {
		double enemyAbsoluteBearing = getHeadingRadians() + e.getBearingRadians();
		double enemyDistance = e.getDistance();
		double enemyVelocity = e.getVelocity();
		
		if (enemyVelocity != 0) {
			lateralDirection = test.Utils.sign(enemyVelocity * Math.sin(e.getHeadingRadians() - enemyAbsoluteBearing));
			//System.out.println(lateralDirection);
		}
		Wave wave = new Wave(this);
		wave.gunLocation = new Point2D.Double(getX(), getY());
		Wave.targetLocation = test.Utils.project(wave.gunLocation, enemyAbsoluteBearing, enemyDistance);
		wave.lateralDirection = lateralDirection;
		wave.bulletPower = BULLET_POWER;
		wave.setSegmentations(enemyDistance, enemyVelocity, lastEnemyVelocity);
		lastEnemyVelocity = enemyVelocity;
		wave.bearing = enemyAbsoluteBearing;
		setTurnGunRightRadians(Utils.normalRelativeAngle(enemyAbsoluteBearing - getGunHeadingRadians() + wave.mostVisitedBearingOffset()));
		setFire(wave.bulletPower);
		if (getEnergy() >= BULLET_POWER) {
			addCustomEvent(wave);
			waves.add(wave);
		}
		movement.onScannedRobot(e);
		setTurnRadarRightRadians(Utils.normalRelativeAngle(enemyAbsoluteBearing - getRadarHeadingRadians()) * 2);
	}
	
	public void onPaint(java.awt.Graphics2D g) {
        g.setColor(java.awt.Color.red);
        for(int i = 0; i < waves.size(); i++){
           Wave w = (Wave)(waves.get(i));
           Point2D.Double center = (java.awt.geom.Point2D.Double) w.gunLocation;
           Point2D.Double myLoc = new Point2D.Double(getX(), getY());
           //int radius = (int)(w.distanceTraveled + w.bulletVelocity);
           //hack to make waves line up visually, due to execution sequence in robocode engine
           //use this only if you advance waves in the event handlers (eg. in onScannedRobot())
           //NB! above hack is now only necessary for robocode versions before 1.4.2
           //otherwise use: 
           int radius = (int)w.distanceTraveled;

           //Point2D.Double center = w.fireLocation;
           if(radius - 40 < center.distance(myLoc))
              g.drawOval((int)(center.x - radius ), (int)(center.y - radius), radius*2, radius*2);
        }
     }
    
}
 
 
class GFTMovement {
	private static final double BATTLE_FIELD_WIDTH = 800;
	private static final double BATTLE_FIELD_HEIGHT = 600;
	private static final double WALL_MARGIN = 18;
	private static final double MAX_TRIES = 125;
	private static final double REVERSE_TUNER = 0.421075;
	private static final double DEFAULT_EVASION = 1.2;
	private static final double WALL_BOUNCE_TUNER = 0.699484;
 
	private AdvancedRobot robot;
	private Rectangle2D fieldRectangle = new Rectangle2D.Double(WALL_MARGIN, WALL_MARGIN,
		BATTLE_FIELD_WIDTH - WALL_MARGIN * 2, BATTLE_FIELD_HEIGHT - WALL_MARGIN * 2);
	private double enemyFirePower = 3;
	private double direction = 0.4;
 
	GFTMovement(AdvancedRobot _robot) {
		this.robot = _robot;
	}
 
	public void onScannedRobot(ScannedRobotEvent e) {
		double enemyAbsoluteBearing = robot.getHeadingRadians() + e.getBearingRadians();
		double enemyDistance = e.getDistance();
		Point2D robotLocation = new Point2D.Double(robot.getX(), robot.getY());
		Point2D enemyLocation = test.Utils.project(robotLocation, enemyAbsoluteBearing, enemyDistance);
		Point2D robotDestination;
		double tries = 0;
		while (!fieldRectangle.contains(robotDestination = test.Utils.project(enemyLocation, enemyAbsoluteBearing + Math.PI + direction,
				enemyDistance * (DEFAULT_EVASION - tries / 100.0))) && tries < MAX_TRIES) {
			tries++;
		}
		if ((Math.random() < (test.Utils.bulletVelocity(enemyFirePower) / REVERSE_TUNER) / enemyDistance ||
				tries > (enemyDistance / test.Utils.bulletVelocity(enemyFirePower) / WALL_BOUNCE_TUNER))) {
			direction = -direction;
		}
		// Jamougha's cool way
		double angle = test.Utils.absoluteBearing(robotLocation, robotDestination) - robot.getHeadingRadians();
		robot.setAhead(Math.cos(angle) * 100);
		robot.setTurnRightRadians(Math.tan(angle));
	}
}
