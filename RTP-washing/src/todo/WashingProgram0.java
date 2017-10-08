package todo;

import done.*;

/**
 * Program 1 of washing machine. Does the following:
 * <UL>
 *   <LI>
 *   <LI>
 *   <LI>
 *   <LI>
 * </UL>
 */
class WashingProgram0 extends WashingProgram {

	// ------------------------------------------------------------- CONSTRUCTOR

	/**
	 * @param   mach             The washing machine to control
	 * @param   speed            Simulation speed
	 * @param   tempController   The TemperatureController to use
	 * @param   waterController  The WaterController to use
	 * @param   spinController   The SpinController to use
	 */
	public WashingProgram0(AbstractWashingMachine mach,
			double speed,
			TemperatureController tempController,
			WaterController waterController,
			SpinController spinController) {
		super(mach, speed, tempController, waterController, spinController);
	}

	// ---------------------------------------------------------- PUBLIC METHODS

	/**
	 * This method contains the actual code for the washing program. Executed
	 * when the start() method is called.
	 */
	protected void wash() throws InterruptedException {
		
		// Turn off water pumps
		myWaterController.putEvent(new WaterEvent(this,
				WaterEvent.WATER_IDLE,
				0.0));
		
		// Turn off spinning motor
		mySpinController.putEvent(new SpinEvent(this,
				SpinEvent.SPIN_OFF));
	}
}
