package schiet;

import java.util.ArrayList;
import java.util.HashSet;

import robocode.ScannedRobotEvent;

public class EnemyMap {

	public static HashSet<EnemyBot> enemyMap = null;
	
	private static EnemyMap instance = new EnemyMap();
	
	public static final EnemyMap getInstance() {
		return instance;
	}
	
	public EnemyMap() {
		enemyMap = new HashSet<EnemyBot>();
	}
	
	public void log(ScannedRobotEvent e) {
		enemyMap.add(new EnemyBot(e));
	}

}
