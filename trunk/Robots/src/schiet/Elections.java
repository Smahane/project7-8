package schiet;

import java.util.LinkedList;
import java.util.List;

import robocode.TeamRobot;

import data.Friend;

public class Elections {

	public static void callElections() {
		List<Friend> result = new LinkedList<Friend>();
		List<Friend> tmp1 = IgniteBot.getMap();

		Friend tmp = null;
		for (Friend e : tmp1) {
			if (tmp == null) {
				tmp = e;
			} else if (e.energy > tmp.energy) {
				tmp = e;
			}
		}
		
		result.add(tmp);
		
		tmp1 = MundoBot.getMap();
		tmp = null;
		for (Friend e : tmp1) {
			if (tmp == null) {
				tmp = e;
			} else if (e.energy > tmp.energy) {
				tmp = e;
			}
		}
		
		result.add(tmp);
	}
}
