package lift;

public class Monitor {

	private LiftView view;
	private int here;
	private int next;
	private int[] waitEntry;
	private int[] waitExit;
	private int load;

	public Monitor(LiftView view) {
		this.view = view;
		here = 0;
		next = 1;
		waitEntry = new int[7];
		waitExit = new int[7];
		load = 0;
	}

	public synchronized void addToEntryQueue(int floor) {
		waitEntry[floor] += 1;
		view.drawLevel(floor, waitEntry[floor]);
		notifyAll();
	}
	
	public synchronized void travel(int from, int to) {
		System.out.println("Traveling from " + from + " to " +  to + ".");
		while (load >= 4 || here != from) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		waitEntry[here] -= 1;
		load += 1;
		waitExit[to] += 1;
		view.drawLevel(here, waitEntry[here]);
		view.drawLift(here, load);
		notifyAll();
	}

	public synchronized void addToExitQueue(int to) {
		while (here != to) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		load -= 1;
		waitExit[here] -= 1;
		view.drawLift(here, load);
		notifyAll();
	}

	public synchronized void waitForTravelers() {
		int sum = 0;
		for (int n : waitEntry) {
			sum += n;
		}
		boolean emptySystem = (load == 0 && sum == 0);
		if (emptySystem || waitExit[here] != 0) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
	}

	public synchronized void updateFloor() {
		if (next > here) {
			here = next;
			next = (next == 6) ? next - 1 : next + 1;
		} else {
			here = next;
			next = (next == 0) ? next + 1 : next - 1;
		}
		notifyAll();
	}

	public synchronized int[] getHereAndNext() {
		return new int[] { here, next };
	}
}
