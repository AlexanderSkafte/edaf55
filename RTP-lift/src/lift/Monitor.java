package lift;

public class Monitor {

	private LiftView view;
	private int here;
	private int next;
	private int[] waitEntry;
	private int[] waitExit;
	private int load;
	
	private boolean standingStill = false;
	
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
		System.out.println("Traveling from " + from + " to " + to + ".");
		while (load >= 4 || here != from || !standingStill) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// Get on lift
		waitEntry[here] -= 1;
		waitExit[to] += 1;
		load += 1;
		view.drawLevel(here, waitEntry[here]);
		view.drawLift(here, load);
		notifyAll();
	}

	public synchronized void addToExitQueue(int to) {
		while (here != to || !standingStill) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		load -= 1;
		waitExit[here] -= 1;
		view.drawLevel(here, waitEntry[here]);
		view.drawLift(here, load);
		notifyAll();
	}

	public synchronized void waitIfEmptySystem() {
		int sum = 0;
		for (int n : waitEntry) {
			sum += n;
		}
		boolean emptySystem = (sum == 0 && load == 0);
		if (emptySystem || waitExit[here] != 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void openDoors() {
		standingStill = true;
		notifyAll();
		while (waitExit[here] != 0 || waitEntry[here] != 0 && load < 4) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		standingStill = false;
		notifyAll();
	}
	
	public void moveLift() {
		int[] hn = getHN();
		view.moveLift(hn[0], hn[1]);
	}
	
	private synchronized int[] getHN() {
		return new int[] { here, next };
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
}
