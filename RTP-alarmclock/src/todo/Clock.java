package todo;

import done.ClockOutput;
import se.lth.cs.realtime.semaphore.MutexSem;

public class Clock {
	private ClockOutput output;
	private MutexSem sem;
	
	private int clockTime = 000000; 	// format: hhmmss
	private int alarmTime = 000000; 	// format: hhmmss
	private int counter = 20;			// alarm counter
	private boolean alarmIsOn = false;	// true if the alarm functionality is activated
	private boolean isBeeping = false;	// turn on when alarm should ring, turn off when done

	public Clock(ClockOutput output) {
		this.output = output;
		this.sem = new MutexSem();
//		clockTime = 2955;
//		alarmTime = 030000;
//		setClockTime(clockTime);
//		setAlarmTime(alarmTime);
//		setAlarmFlag(true);
	}

	public int getClockTime() {
		return clockTime;
	}
	
	public void setClockTime(int clockTime) {
		sem.take();
		this.clockTime = clockTime;
		sem.give();
	}
	
	public void setAlarmTime(int alarmTime) {
		sem.take();
		this.alarmTime = alarmTime;
		sem.give();
	}

	public void setAlarmFlag(boolean flag) {
		sem.take();
		alarmIsOn = flag;
		sem.give();
	}

	public void resetAlarm() {
		sem.take();
		counter = 20;
		alarmIsOn = false;
		isBeeping = false;
		sem.give();
	}
	
	public boolean isBeeping() {
		return isBeeping;
	}

	public void tick() {
		sem.take();
		clockTime += 1;
		int s = clockTime % 100;
		if (s > 59) {
			clockTime += 40;
			s = 0;
		}
		int m = (clockTime / 100) % 100;
		if (m > 59) {
			clockTime += 4000;
			m = 0;
		}
		int h = clockTime / 10000;
		if (h > 23) {
			clockTime = 0;
			h = 0;
		}
		
		clockTime = h * 10000 + m * 100 + s;
//		co.showTime(clockTime);

		if (clockTime == alarmTime) {
			isBeeping = true;
		}
		
		if (alarmIsOn && isBeeping) {
			output.doAlarm();
			counter -= 1;
			if (counter == 0) {
				resetAlarm();
			}
		}
		sem.give();
	}
}
