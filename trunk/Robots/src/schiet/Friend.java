package schiet;

import java.awt.geom.Point2D;

import robocode.TeamRobot;

public class Friend {

	private TeamRobot me;
	public String name;
	public double energy;
	public Point2D.Double loc;
	
	public Friend(TeamRobot r) {
		this.name = r.getName();
		this.energy = r.getEnergy();
		this.loc = new Point2D.Double(r.getX(), r.getY());
	}
	
	public Friend(String bla) {
		this.name = bla;
	}
	
}
