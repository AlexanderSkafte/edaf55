package todo;

import done.AbstractWashingMachine;
import se.lth.cs.realtime.PeriodicThread;
import se.lth.cs.realtime.RTThread;

public class TemperatureController extends PeriodicThread {
	public final static int TEMP_WHITE_PRE_WASH = 40;
	public final static int TEMP_WHITE_MAIN_WASH = 90;
	public final static int TEMP_COLOR_MAIN_WASH = 60;
	
	private AbstractWashingMachine mach;
	private int mode = TemperatureEvent.TEMP_IDLE;
	private double target;
	private RTThread source;
	private boolean isHeating;

	public TemperatureController(AbstractWashingMachine mach, double speed) {
		super((long) (10 * 1000 / speed));
		this.mach = mach;
	}

	/* TODO
	 * Where do we put the event stuff? (putEvent)
	 */
	
	public void perform() {
		TemperatureEvent e = (TemperatureEvent) mailbox.tryFetch();
		if (e != null) {
			mode   = e.getMode();
			target = e.getTemperature();
			source = (RTThread) e.getSource();

			switch (mode) {
			case TemperatureEvent.TEMP_IDLE:
				mach.setHeating(false);
				isHeating = false;
				break;
			case TemperatureEvent.TEMP_SET:
				mach.setHeating(true);
				isHeating = true;
				break;
			default:
				System.out.println(
						"Error: TemperatureController - mode unrecognized (" + mode + ").");
				break;
			}
		} else {
			System.out.println("Hmm... TemperatureEvent (mailbox.tryFetch()) was null.");
		}
		
		/* Don't forget this requirement:
		 - The machine must not be heated while it is free of water
		 */
		
		if (mode == TemperatureEvent.TEMP_SET) {
			double temp = mach.getTemperature();
			if (isHeating && temp >= target - 0.4) {
				mach.setHeating(false);
				isHeating = false;
			} else if (!isHeating && temp <= target - 1.6) {
				mach.setHeating(true);
				isHeating = true;
			}
		}
	}
}
