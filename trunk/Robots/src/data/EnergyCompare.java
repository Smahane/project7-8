package data;

import java.util.Comparator;

public class EnergyCompare implements Comparator<Friend> {

	@Override
	public int compare(Friend arg0, Friend arg1) {
		// TODO Auto-generated method stub

		if (arg0.energy < arg1.energy) {
			return -1;
		}
		if (arg0.energy > arg1.energy) {
			return 1;
		}

		return 0;
	}

}
