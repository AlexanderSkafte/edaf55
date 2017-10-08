package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;


public class SpinController extends PeriodicThread {
	private AbstractWashingMachine mach;
	private int direction;

	public SpinController(AbstractWashingMachine mach, double speed) {
		super((long) (60 * 1000 / speed));
		this.mach = mach;
		this.direction = AbstractWashingMachine.SPIN_LEFT;
	}

	public void perform() {
		mach.setSpin(direction);
		
		// Change direction once per period
		if (direction == AbstractWashingMachine.SPIN_LEFT) {
			direction = AbstractWashingMachine.SPIN_RIGHT;
		} else if (direction == AbstractWashingMachine.SPIN_RIGHT) {
			direction = AbstractWashingMachine.SPIN_LEFT;
		}
	}
}
