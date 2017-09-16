package todo;
import done.ClockInput;
import done.ClockOutput;

/**
 * Main class of alarm-clock application.
 * Constructor providing access to IO.
 * Method start corresponding to main,
 * with closing down done in terminate.
 */
public class AlarmClock extends Thread {

	private ClockInput	input;
	private ClockOutput	output;
	
	private Clock clock;
	private TimeThread timeThread;
	private EditThread editThread;
	
	/**
	 * Create main application and bind attributes to device drivers.
	 * @param input The input from simulator/emulator/hardware.
	 * @param output Dito for output.
	 */
	public AlarmClock(ClockInput input, ClockOutput output) {
		this.input = input;
		this.output = output;

		clock = new Clock(output);
		timeThread = new TimeThread(clock);
		editThread = new EditThread(input, clock);
		
		clock.setAlarmTime(2955);
		clock.setAlarmTime(30000);
		clock.setAlarmFlag(true);
		
		timeThread.start();
		editThread.start();
	}

	/**
	 * Tell threads to terminate and wait until they are dead.
	 */
	public void terminate() {
		// Do something more clever here...
		output.console("AlarmClock exit.");
	}
	
	/**
	 * Create thread objects, and start threads
	 */
	public void run() {
		while (true) {
			output.showTime(clock.getClockTime());
		}
	}
	
//	class InputOutputTest implements Runnable {
//		public void run() {
//			long curr; int time, mode; boolean flag;
//			output.console("Click on GUI to obtain key presses!");
//			while (!Thread.currentThread().isInterrupted()) {
//				curr = System.currentTimeMillis();
//				time = input.getValue();
//				flag = input.getAlarmFlag();
//				mode = input.getChoice();
//				output.doAlarm();
//				output.console(curr, time, flag, mode);
//				if (time == 120000) break; // Swe: Bryter f�r middag
//				signal.take();
//			}
//			output.console("IO-test terminated #");
//		}
//	}

}
