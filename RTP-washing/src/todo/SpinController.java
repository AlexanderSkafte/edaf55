package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;


public class SpinController extends PeriodicThread {
	private AbstractWashingMachine mach;
	private int direction;
	private int mode;

	public SpinController(AbstractWashingMachine mach, double speed) {
		super((long) (60 * 1000 / speed));
		this.mach = mach;
		this.direction = AbstractWashingMachine.SPIN_LEFT;
	}
	
	public void perform() {
		SpinEvent e = (SpinEvent) mailbox.tryFetch();
		if (e != null) {
			mode = e.getMode();
			switch (mode) {
			case SpinEvent.SPIN_OFF:
				mach.setSpin(AbstractWashingMachine.SPIN_OFF);
				break;
			case SpinEvent.SPIN_SLOW:
				mach.setSpin(AbstractWashingMachine.SPIN_LEFT);
				break;
			case SpinEvent.SPIN_FAST:
				mach.setSpin(AbstractWashingMachine.SPIN_FAST);
				break;
			default:
				System.out.println(
						"Error: SpinController - mode unrecognized (" + mode + ").");
				break;
			}
		} else {
			System.out.println("Hmm... WaterEvent (mailbox.tryFetch()) was null.");
		}
		
		mach.setSpin(direction);
		
		// Change direction once per period
		if (direction == AbstractWashingMachine.SPIN_LEFT) {
			direction = AbstractWashingMachine.SPIN_RIGHT;
		} else if (direction == AbstractWashingMachine.SPIN_RIGHT) {
			direction = AbstractWashingMachine.SPIN_LEFT;
		}
	}
}
