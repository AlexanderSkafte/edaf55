package todo;

import done.ClockInput;
import se.lth.cs.realtime.semaphore.Semaphore;

public class EditThread extends Thread {
	
	private Semaphore signal;
	private ClockInput input;
	private Clock clock;
	private int currentChoice;
	private int previousChoice = ClockInput.SHOW_TIME;
	
	public EditThread(ClockInput input, Clock clock) {
		this.input = input;
		this.clock = clock;
		this.signal = input.getSemaphoreInstance();
	}
	
	public void run() {
		while (true) {
			signal.take();

			// Set whether the alarm is activated or not.
			clock.setAlarmFlag(input.getAlarmFlag());
			
			// Get the state of the radio button
			// (either SHOW_TIME, SET_TIME or SET_ALARM).
			currentChoice = input.getChoice();
	
			// Turn off the beeping if any button was pressed,
			// i.e. if we reach this part of the code.
			if (clock.isBeeping()) {
				clock.resetAlarm();
			}
			
			// When SET_TIME is pressed, do nothing but update state.
			if (currentChoice == ClockInput.SET_TIME) {
				previousChoice = currentChoice;
			}
			
			// When SET_ALARM is pressed, do nothing but update state.
			if (currentChoice == ClockInput.SET_ALARM) {
				previousChoice = currentChoice;
			}

			// This happens when we leave the SET_TIME state.
			if (previousChoice == ClockInput.SET_TIME) {
				if (currentChoice != ClockInput.SET_TIME) {
					clock.setClockTime(input.getValue());
					previousChoice = currentChoice;
				}
			}
			
			// This happens when we leave the SET_ALARM state.
			if (previousChoice == ClockInput.SET_ALARM) {
				if (currentChoice != ClockInput.SET_ALARM) {
					clock.setAlarmTime(input.getValue());
					previousChoice = currentChoice;
				}
			}
		}
	}
}
