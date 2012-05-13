package data;

import java.lang.reflect.*;
import java.util.Comparator;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Vector;

import robocode.ScannedRobotEvent;
import robocode.TeamRobot;

/**
 * There can be only one - ie. even if the class is loaded in several different
 * classloaders, there will be only one instance of the object.
 */
public class EnemyMap implements Enemyintf {

	public static HashSet<Friend> friendlyMaps = null;
	public TreeSet<Friend> tree = null;

	private static Comparator<Friend> comparator = null;
	private static Vector<EnemyBot> enemyMap = new Vector<EnemyBot>();

	/**
	 * This is effectively an instance of this class (although actually it may
	 * be instead a java.lang.reflect.Proxy wrapping an instance from the
	 * original classloader).
	 */
	public static Enemyintf instance = null;

	/**
	 * Retrieve an instance of AbsoluteSingleton from the original classloader.
	 * This is a true Singleton, in that there will only be one instance of this
	 * object in the virtual machine, even though there may be several copies of
	 * its class file loaded in different classloaders.
	 */
	public synchronized static Enemyintf getInstance() {
		ClassLoader myClassLoader = EnemyMap.class.getClassLoader();
		if (instance == null) {
			// The root classloader is sun.misc.Launcher package. If we are not
			// in a sun package,
			// we need to get hold of the instance of ourself from the class in
			// the root classloader.
			if (!myClassLoader.toString().startsWith("sun.")) {
				try {
					// So we find our parent classloader
					ClassLoader parentClassLoader = EnemyMap.class
							.getClassLoader().getParent();
					// And get the other version of our current class
					Class otherClassInstance = parentClassLoader
							.loadClass(EnemyMap.class.getName());
					// And call its getInstance method - this gives the correct
					// instance of ourself
					Method getInstanceMethod = otherClassInstance
							.getDeclaredMethod("getInstance", new Class[] {});
					Object otherAbsoluteSingleton = getInstanceMethod.invoke(
							null, new Object[] {});
					// But, we can't cast it to our own interface directly
					// because classes loaded from
					// different classloaders implement different versions of an
					// interface.
					// So instead, we use java.lang.reflect.Proxy to wrap it in
					// an object that *does*
					// support our interface, and the proxy will use reflection
					// to pass through all calls
					// to the object.
					instance = (Enemyintf) Proxy
							.newProxyInstance(myClassLoader,
									new Class[] { Enemyintf.class },
									new PassThroughProxyHandler(
											otherAbsoluteSingleton));
					// And catch the usual tedious set of reflection exceptions
					// We're cheating here and just catching everything - don't
					// do this in real code
				} catch (Exception e) {
					e.printStackTrace();
				}
				// We're in the root classloader, so the instance we have here
				// is the correct one
			} else {
				instance = new EnemyMap();
			}
		}

		return instance;
	}

	private EnemyMap() {
	}

	@Override
	public int size() {
		return enemyMap.size();
	}

	@Override
	public void log(ScannedRobotEvent r) {
		enemyMap.add(new EnemyBot(r));

	}

}
