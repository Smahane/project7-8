import robocode.Robot;
import robocode.control.BattlefieldSpecification;


public class MundoBot extends Robot {

	private BattlefieldSpecification bf;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		bf = new BattlefieldSpecification();
		
		while(true) {
			ahead(50);
			System.out.println("Mundo Mundo");
			
			if(getX() > bf.getWidth()) {
				turnLeft(180);
			}
			
			if(getY() > bf.getHeight()) {
				turnLeft(180);
			}
		}
	}
	
}
