package todo;

import done.*;

public class WashingController implements ButtonListener {
	// TODO: add suitable attributes

	public WashingController(AbstractWashingMachine theMachine, double theSpeed) {
		// TODO: implement this constructor

		// Create and start your controller threads here
		TemperatureController temp = new TemperatureController(theMachine, theSpeed);
		WaterController       water = new WaterController(theMachine, theSpeed);
		SpinController        spin = new SpinController(theMachine, theSpeed);

		temp.start();
		water.start();
		spin.start();
	}

	// Handle button presses (0, 1, 2, or 3). A button press
	// corresponds to starting a new washing program. What should
	// happen if there is already a running washing program?
	public void processButton(int theButton) {
		// TODO: implement this method
	}
}
