package lift;

public class Person extends Thread {
	private Monitor monitor;
	
	public Person(Monitor monitor) {
		this.monitor = monitor;
	}
	
	public void run() {
		while (true) {
			randomSleep();
			int start = randomFloor();
			int end;
			do {
				end = randomFloor();
			} while (start == end);
			monitor.addToEntryQueue(start);
			monitor.travel(start, end);
			monitor.addToExitQueue(end);
		}
	}
	
	private int randomFloor() {
		return (int) (Math.random() * 7);
	}
	
	private void randomSleep() {
		try {
			sleep(1000 * ((int)(Math.random() * 46.0)));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
