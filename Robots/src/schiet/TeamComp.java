package schiet;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeSet;

import robocode.TeamRobot;

public class TeamComp {

	private static Comparator<Friend> compare = new EnergyCompare();
	public static TreeSet<Friend> friends = new TreeSet<Friend>(compare);
	
	public static void elect() {
		System.out.println(friends.size());
		System.out.println("elect : " + friends.first().energy + " " + friends.first().name);
	}
	
}
