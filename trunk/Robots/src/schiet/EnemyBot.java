package schiet;

import robocode.ScannedRobotEvent;

public class EnemyBot {

	private double bearing;
	private double distance;
	private double energy;
	private double heading;
	private String name;
	private double velocity;
	
	public EnemyBot() {
		
	}

	public EnemyBot(ScannedRobotEvent e) {
		this.bearing = e.getBearing();
		this.distance = e.getDistance();
		this.energy = e.getEnergy();
		this.heading = e.getHeading();
		this.name = e.getName();
		this.velocity = e.getVelocity();
	}

	void update(ScannedRobotEvent e) {
		this.bearing = e.getBearing();
		this.distance = e.getDistance();
		this.energy = e.getEnergy();
		this.heading = e.getHeading();
		this.name = e.getName();
		this.velocity = e.getVelocity();
	}

	void reset() {
		this.bearing = 0.0;
		this.distance = 0.0;
		this.energy = 0.0;
		this.heading = 0.0;
		this.name = "";
		this.velocity = 0.0;
	}

	boolean none() {
		if (name.equals("")) {
			return true;
		}

		return false;
	}

	public double getBearing() {
		return bearing;
	}

	public double getDistance() {
		return distance;
	}

	public double getEnergy() {
		return energy;
	}

	public double getHealing() {
		return heading;
	}

	public String getName() {
		return name;
	}

	public double getVelocity() {
		return velocity;
	}

}
