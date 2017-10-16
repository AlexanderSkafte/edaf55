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
	private boolean acked;

	public TemperatureController(AbstractWashingMachine mach, double speed) {
		super((long) (1000 / speed));
		this.mach = mach;
	}

	public void perform() {
		TemperatureEvent e = (TemperatureEvent) mailbox.tryFetch();
		if (e != null) {
			mode   = e.getMode();
			target = e.getTemperature();
			source = (RTThread) e.getSource();
			acked  = (mode != TemperatureEvent.TEMP_SET);
		}

		switch (mode) {
		
		case TemperatureEvent.TEMP_IDLE:
			mach.setHeating(false);
			break;
			
		case TemperatureEvent.TEMP_SET:
			double temp = mach.getTemperature();
			if (temp >= target - 0.2) {
				mach.setHeating(false);
				if (!acked) {
					source.putEvent(new AckEvent(this));
					acked = true;
					System.out.println("TemperatureController: Sent ack.");
				}
			} else if (temp <= target - 1.8
					&& mach.getWaterLevel() > WaterController.WATER_LEVEL_EMPTY) {
				mach.setHeating(true);
			}
			break;
			
		default:
			System.out.println(
					"Error: TemperatureController - mode unrecognized (" + mode + ").");
			break;
		}
	}
}
