package todo;

import done.AbstractWashingMachine;
import se.lth.cs.realtime.PeriodicThread;
import se.lth.cs.realtime.RTThread;

public class WaterController extends PeriodicThread {
	public final static double WATER_LEVEL_EMPTY = 0.0;
	public final static double WATER_LEVEL_FULL = 0.4;
	
	private AbstractWashingMachine mach;
	private int mode = WaterEvent.WATER_IDLE;
	private double target;
	private RTThread source;

	public WaterController(AbstractWashingMachine mach, double speed) {
		super((long) (1000 / speed));
		this.mach = mach;
	}
	
	/* TODO
	 * Are the events correct like this?
	 */

	public void perform() {
		WaterEvent e = (WaterEvent) mailbox.tryFetch();
		if (e != null) {
			mode   = e.getMode();
			target = e.getLevel();
			source = (RTThread) e.getSource();

			switch (mode) {
			case WaterEvent.WATER_IDLE:
				mach.setDrain(false);
				mach.setFill(false);
				break;
			case WaterEvent.WATER_FILL:
				mach.setDrain(false);
				mach.setFill(true);
				break;
			case WaterEvent.WATER_DRAIN:
				mach.setDrain(true);
				mach.setFill(false);
				break;
			default:
				System.out.println(
						"Error: WaterController - mode unrecognized (" + mode + ").");
				break;
			}
		} else {
			System.out.println("Hmm... WaterEvent (mailbox.tryFetch()) was null.");
		}
		
		double level = mach.getWaterLevel();
		if (mode == WaterEvent.WATER_FILL && level >= target) {
				mach.setFill(false);
				mode = WaterEvent.WATER_IDLE;
				source.putEvent(new AckEvent(this));
		} else if (mode == WaterEvent.WATER_DRAIN && level <= target) {
				mach.setDrain(false);
				mode = WaterEvent.WATER_IDLE;
				source.putEvent(new AckEvent(this));
		}
	}
}
