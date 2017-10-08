package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;


public class TemperatureController extends PeriodicThread {
	public final static int TEMP_WHITE_PRE_WASH = 40;
	public final static int TEMP_WHITE_MAIN_WASH = 90;
	public final static int TEMP_COLOR_MAIN_WASH = 60;
	private AbstractWashingMachine mach;
	

	public TemperatureController(AbstractWashingMachine mach, double speed) {
		super((long) (60 * 1000 / speed));
		this.mach = mach;
	}

	public void perform() {
		
	}
}
