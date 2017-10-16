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
	private boolean acked;

	public WaterController(AbstractWashingMachine mach, double speed) {
		super((long) (1000 / speed));
		this.mach = mach;
	}
	
	public void perform() {
		WaterEvent e = (WaterEvent) mailbox.tryFetch();
		if (e != null) {
			mode   = e.getMode();
			target = e.getLevel();
			source = (RTThread) e.getSource();
			acked  = (mode == WaterEvent.WATER_IDLE);
		}
		
		double level = mach.getWaterLevel();

		switch (mode) {
		
		case WaterEvent.WATER_IDLE:
			mach.setDrain(false);
			mach.setFill(false);
			break;
		
		case WaterEvent.WATER_FILL:
			mach.setDrain(false);
			if (level < target) {
				mach.setFill(true);
			} else {
				mach.setFill(false);
				if (!acked) {
					source.putEvent(new AckEvent(this));
					acked = true;
					System.out.println("WaterController: Sent ack [filled]");
				}
			}
			break;
			
		case WaterEvent.WATER_DRAIN:
			mach.setDrain(true);
			mach.setFill(false);
			if (level == WaterController.WATER_LEVEL_EMPTY && !acked) {
				source.putEvent(new AckEvent(this));
				acked = true;
				System.out.println("WaterController: Sent ack [drained]");
			}
			break;
			
		default:
			System.out.println(
					"Error: WaterController - mode unrecognized (" + mode + ").");
			break;
		}
//		else {
//			System.out.println("Hmm... WaterEvent (mailbox.tryFetch()) was null.");
//		}
	}
}
