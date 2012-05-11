package schiet;

import java.awt.geom.Point2D;

public class Friend {

	public static String name;
	public double energy;
	public Point2D.Double loc;
	
	public void update(double energy, Point2D.Double locatie) {
		this.energy = energy;
		this.loc = locatie;
	}
}
