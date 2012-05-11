package schiet;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;

import robocode.TeamRobot;

public class TeamComp {

	static Comparator<Friend> compare = new EnergyCompare();
	public static PriorityQueue<Friend> friends = new PriorityQueue<Friend>(5, compare);
	
	public void add(Friend friend) {
		friends.add(friend);
	}
	
	public void remove(Friend friend) {
		friends.remove(friend);
	}
	
	public void elect() {
		System.out.println(friends.element().energy + " " + friends.element().energy);
	}
}
