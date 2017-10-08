package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;


public class WaterController extends PeriodicThread {
	public final static double WATER_LEVEL_EMPTY = 0.0;
	public final static double WATER_LEVEL_FULL = 0.4;
	private AbstractWashingMachine mach;

	public WaterController(AbstractWashingMachine mach, double speed) {
		super((long) (1000 / speed));
		this.mach = mach;
	}

	public void perform() {
		
	}
}
