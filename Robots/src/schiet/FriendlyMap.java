package schiet;

import java.util.ArrayList;
import java.util.HashSet;

import robocode.ScannedRobotEvent;
import robocode.TeamRobot;

public class FriendlyMap {

	public static HashSet<Friend> friendlyMap = null;
	
	private static FriendlyMap instance = new FriendlyMap();
	
	public static final FriendlyMap getInstance() {
		return instance;
	}
	
	public FriendlyMap() {
		friendlyMap = new HashSet<Friend>();
	}
	
	public void log(TeamRobot e) {
		friendlyMap.add(new Friend(e));
	}

}
