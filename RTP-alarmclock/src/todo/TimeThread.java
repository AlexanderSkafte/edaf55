package todo;

public class TimeThread extends Thread {
	private Clock clock;

	public TimeThread(Clock clock) {
		this.clock = clock;
	}

	public void run() {
		long time = System.currentTimeMillis();
		long delta;
		while (true) {
			time += 1000;
			delta = time - System.currentTimeMillis();
			if (delta > 0) {
				try {
					sleep(delta);
				} catch (InterruptedException e) {
					/* Praise the sun! \[T]/ */
				}
				clock.tick();
			}
		}
	}
}
