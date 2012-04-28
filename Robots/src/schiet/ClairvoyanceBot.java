package schiet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import robocode.BulletHitEvent;
import robocode.Condition;
import robocode.HitRobotEvent;
import robocode.CustomEvent;
import robocode.HitWallEvent;
import robocode.MessageEvent;
import robocode.RadarTurnCompleteCondition;
import robocode.Robot;
import robocode.Rules;
import robocode.RobotDeathEvent;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;
import robocode._Robot;
import static robocode.util.Utils.normalRelativeAngleDegrees;
import static robocode.util.Utils.normalRelativeAngle;

public class ClairvoyanceBot extends TeamRobot {

	double bearingInDegrees = 0;
	private int radarDirection = 1;
	private static double energy = 100.0;

	@Override
	public void onScannedRobot(ScannedRobotEvent e) {
		// TODO Auto-generated method stub
		for(EnemyBot em : EnemyMap.EnemyMap) {
			if(em.getName().equals(e.getName())) {
				double cur_power = em.getEnergy() - e.getEnergy();
				
				if(cur_power <= Rules.MAX_BULLET_POWER && cur_power >= Rules.MIN_BULLET_POWER) {
					out.println("Schot.." + e.getName());
				}
			}
		}
		
		int check = 0;
		for (EnemyBot em : EnemyMap.EnemyMap) {
			if (e.getName().equals(em.getName())) {
				em.update(e);
				check = 1;
			}
		}

		if (check == 0 && !(isTeammate(e.getName()))) {
			EnemyMap.EnemyMap.add(new EnemyBot(e));
		}
		
		EnemyBot tmp = new EnemyBot(e);
		for(EnemyBot em : EnemyMap.EnemyMap) {
			if(em.getEnergy() < tmp.getEnergy()) {
				tmp = em;
			}
		}


		
		//out.println(tmp.getName() + " is victim");
		


		// Don't fire on teammates
		if (isTeammate(e.getName())) {
			return;
		}

		// Calculate enemy bearing
		double enemyBearing = this.getHeading() + tmp.getBearing();
		// Calculate enemy's position
		double enemyX = getX() + tmp.getDistance()
				* Math.sin(Math.toRadians(enemyBearing));
		double enemyY = getY() + tmp.getDistance()
				* Math.cos(Math.toRadians(enemyBearing));

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
	public void onRobotDeath(RobotDeathEvent event) {
		//out.println(event.getName());
		
		Iterator<EnemyBot> itr = EnemyMap.EnemyMap.iterator();
		while(itr.hasNext()) {
			EnemyBot tmp = (EnemyBot) itr.next();
			
			if(tmp.getName().equals(event.getName())) {
				itr.remove();
			}
		}
	}

	private void sweep() {
		double maxBearingAbs = 0, maxBearing = 0;
		int scannedBots = 0;
		Iterator iterator = EnemyMap.EnemyMap.iterator();

		while (iterator.hasNext()) {
			EnemyBot tmp = (EnemyBot) iterator.next();

			if (tmp != null && !(tmp.none())) {
				double bearing = normalRelativeAngle(getHeading()
						+ tmp.getBearing() - getRadarHeading());
				if (Math.abs(bearing) > maxBearingAbs) {
					maxBearingAbs = Math.abs(bearing);
					maxBearing = bearing;
				}
				scannedBots++;
			}
		}

		double radarTurn = 180 * radarDirection;
		if (scannedBots == getOthers())
			radarTurn = maxBearing + sign(maxBearing) * 3;

		setTurnRadarRight(radarTurn);
		radarDirection = sign(radarTurn);
	}

	int sign(double v) {
		return v > 0 ? 1 : -1;
	}

	public void onCustomEvent(CustomEvent e) {
		if (e.getCondition() instanceof RadarTurnCompleteCondition)
			sweep();
		if (e.getCondition().getName().contains("too_close_to_walls"))
		{
			double richtingBot = getHeading();
	
			if(muurCheck() == "Boven"){
				System.out.println("Boven");
					if(richtingBot <= 90){
						turnRight(180);
						ahead(100);
					}if(richtingBot >90 && richtingBot<= 180){
						turnRight(90);
						ahead(100);
					}if(richtingBot >180 && richtingBot<= 270){
						ahead(100);
					}else{
						turnLeft(90);
						ahead(100);
					}
			}
			if(muurCheck() == "Onder"){
				System.out.println("Onder " + richtingBot);
				if(richtingBot <= 90){
					turnLeft(45);
					ahead(100);
				}if(richtingBot >90 && richtingBot<= 180){
					ahead(100);
					turnRight(90);
					ahead(100);
				}if(richtingBot >180 && richtingBot<= 270){
					turnLeft(180);
					ahead(100);
				}else{
					turnRight(90);
					ahead(100);
				}
			}
			if(muurCheck() ==  ("Rechts")){
				System.out.println("Rechts");
				if(richtingBot <= 90){
					turnLeft(90);
					ahead(100);
				}if(richtingBot >90 && richtingBot<= 180){
					turnRight(180);
					ahead(100);
				}if(richtingBot >180 && richtingBot<= 270){
					turnRight(90);
					ahead(100);
				}else{
					ahead(100);
				}
			}
			if(muurCheck() == ("Links")){
				System.out.println("Links " + richtingBot);
				if(richtingBot <= 90){
					turnLeft(90);
					ahead(100);
				}if(richtingBot >90 && richtingBot<= 180){
					turnLeft(180);
					ahead(100);
				}if(richtingBot >180 && richtingBot<= 270){
					turnRight(90);
					ahead(100);
				}else{
					turnRight(180);
					ahead(100);
				}
			}
			// Note that the heading in Robocode is like a compass, where 0 means North, 90 means East, 180 means South, and 270 means West. 

	
				
		//Richting *= (-1);

			//setAhead(10000 * Richting);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// super.run()
		EnemyMap.EnemyMap = new ArrayList<EnemyBot>();
		
		addCustomEvent(new RadarTurnCompleteCondition(this));
		
		addCustomEvent(new Condition("too_close_to_walls " + muurCheck()) {	
			public boolean test() {
				double wallMargin = 60;
				return (
					// we're too close to the left wall
					(getX() <= wallMargin  ||
					 // or we're too close to the right wall
					 getX() >= getBattleFieldWidth() - wallMargin ||
					 // or we're too close to the bottom wall
					 getY() <= wallMargin ||
					 // or we're too close to the top wall
					 getY() >= getBattleFieldHeight() - wallMargin)
					);
				}
			});
		
		

		setAdjustRadarForGunTurn(true);
		setTurnRadarRight(360);

		while (true) {		
			ahead(50);
			
			for(EnemyBot em : EnemyMap.EnemyMap) {
				//System.out.println(em.getName() + " " + em.getDistance());

			}

		}
	}
	
	public String muurCheck() {
		double wallMargin = 60;
		if(getX() <= wallMargin){ 	// Te dicht bij linker muur
			return "Links";
		}
		if(getX() >= getBattleFieldWidth() - wallMargin){  // Te dicht bij rechter muur
			return "Rechts";
		}
		if(getY() <= wallMargin){	// Te dicht bij onderste muur
			return "Onder";
		}
		if(getY() >= getBattleFieldHeight() - wallMargin){   // te dicht bij bovenste muur
			return "Boven";
		}
		return "niets";
		 
			
		}



	// Don't get too close to the walls
	

		
	@Override
	public void onHitWall(HitWallEvent event) {
		// TODO Auto-generated method stub
		
		
		turnLeft(180);
		ahead(100);
		super.onHitWall(event);
		
		System.out.println("HIT THE WALL I FAILED");
		
	}

	public void onMessageReceived(MessageEvent e) {
		// Fire at a point
		//System.out.println("punt ontvangen van "+ e.getSender());
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
	public void onBulletHit(BulletHitEvent event) {
		// TODO Auto-generated method stub
		super.onBulletHit(event);
		if(event.getName().contains("schiet")){
			ahead(10);
			System.out.println("EIGEN TEAM GESCHOTEN");
		}
	}
	
	@Override
	public void onHitRobot(HitRobotEvent event) {
		// TODO Auto-generated method stub
		super.onHitRobot(event);
		turnLeft(180);
		ahead(100);
		if (getGunHeat() == 0) {
			fire(Rules.MAX_BULLET_POWER);
		}
	}

	@Override
	public double getBattleFieldHeight() {
		// TODO Auto-generated method stub
		return super.getBattleFieldHeight();
	}

	@Override
	public double getBattleFieldWidth() {
		// TODO Auto-generated method stub
		return super.getBattleFieldWidth();
	}

}
