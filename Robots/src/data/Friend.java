package data;

import java.awt.geom.Point2D;
import java.io.Serializable;

import robocode.TeamRobot;

public class Friend implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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
