package lift;

public class Lift extends Thread {
	
	private LiftView view;
	private Monitor monitor;
	
	public Lift(LiftView view, Monitor monitor) {
		this.view = view;
		this.monitor = monitor;
	}
	
	public void run() {
		while (true) {
			monitor.waitForTravelers();
			int[] hn = monitor.getHereAndNext();
			view.moveLift(hn[0], hn[1]);
			monitor.updateFloor();
		}
	}
}
