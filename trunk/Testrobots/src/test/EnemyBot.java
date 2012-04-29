package test;


import robocode.ScannedRobotEvent;

public class EnemyBot implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private double bearing;
	private double bearingRadians;
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
		this.bearingRadians = e.getBearingRadians();
	}

	void update(ScannedRobotEvent e) {
		this.bearing = e.getBearing();
		this.distance = e.getDistance();
		this.energy = e.getEnergy();
		this.heading = e.getHeading();
		this.name = e.getName();
		this.velocity = e.getVelocity();
		this.bearingRadians = e.getBearingRadians();
	}

	public double getBearingRadians() {
		return bearingRadians;
	}

	void reset() {
		this.bearing = 0.0;
		this.distance = 0.0;
		this.energy = 0.0;
		this.heading = 0.0;
		this.name = "";
		this.velocity = 0.0;
		this.bearingRadians = 0.0;
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

	public double getHeading() {
		return heading;
	}

	public String getName() {
		return name;
	}

	public double getVelocity() {
		return velocity;
	}

}
