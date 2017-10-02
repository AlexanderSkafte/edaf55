package lift;

public class Main {
	public static void main(String[] args) {

		// Lift view
		LiftView view = new LiftView();

		// Monitor
		Monitor monitor = new Monitor(view);

		// Lift thread
		Lift lift = new Lift(view, monitor);

		// Person threads
		for (int i = 0; i < 20; i++) {
			new Person(monitor).start();
		}
		
		lift.start();
	}
}
