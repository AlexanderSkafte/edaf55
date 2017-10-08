package todo;

import done.*;

public class WashingController implements ButtonListener {
	private AbstractWashingMachine mach;
	private double speed;
	private TemperatureController temp;
	private WaterController water;
	private SpinController spin;
	private WashingProgram program;
	
	public WashingController(AbstractWashingMachine theMachine, double theSpeed) {
		mach = theMachine;
		speed = theSpeed;

		temp = new TemperatureController(mach, speed);
		water = new WaterController(mach, speed);
		spin = new SpinController(mach, speed);
		temp.start();
		water.start();
		spin.start();
		
		program = new WashingProgram3(mach, speed, temp, water, spin);
		program.start();
	}

	public void processButton(int theButton) {
		if (!program.isAlive() && theButton != 0) {
			program = createProgram(theButton);
			program.start();
		} else if (theButton == 0) {
			program.interrupt();
		} else {
			System.out.println("Error: processButton(" + theButton + ")");
		}
	}
	
	private WashingProgram createProgram(int button) {
		WashingProgram wp;
		switch (button) {
		case 1:
			wp = new WashingProgram1(mach, speed, temp, water, spin);
			break;
		case 2:
			wp = new WashingProgram2(mach, speed, temp, water, spin);
			break;
		case 3:
			wp = new WashingProgram3(mach, speed, temp, water, spin);
			break;
		default:
			wp = null;
			System.out.println("Error: Invalid program " + button + ".");
			break;
		}
		return wp;
	}
}
