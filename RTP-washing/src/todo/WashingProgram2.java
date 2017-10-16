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
class WashingProgram2 extends WashingProgram {

	// ------------------------------------------------------------- CONSTRUCTOR

	/**
	 * @param   mach             The washing machine to control
	 * @param   speed            Simulation speed
	 * @param   tempController   The TemperatureController to use
	 * @param   waterController  The WaterController to use
	 * @param   spinController   The SpinController to use
	 */
	public WashingProgram2(AbstractWashingMachine mach,
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
		
		// Lock the hatch
		myMachine.setLock(true);

		// Fill the machine
		myWaterController.putEvent(new WaterEvent(this,
				WaterEvent.WATER_FILL,
				WaterController.WATER_LEVEL_FULL));
		
		// Wait for AckEvent
		mailbox.doFetch();
		
		// Stop filling, let it be
		myWaterController.putEvent(new WaterEvent(this,
				WaterEvent.WATER_IDLE,
				WaterController.WATER_LEVEL_EMPTY));
		
		// Start spinning
		mySpinController.putEvent(new SpinEvent(this,
				SpinEvent.SPIN_SLOW));
		
		// ----------------------------------
		// Pre-wash
		
		// Heat to 40 degrees Celsius
		myTempController.putEvent(new TemperatureEvent(this,
				TemperatureEvent.TEMP_SET,
				TemperatureController.TEMP_WHITE_PRE_WASH));

		// Wait for AckEvent
		mailbox.doFetch();
		
		// Keep temperature for 15 minutes
		Thread.sleep((long) (15 * 60 * 1000 / mySpeed));

		// Stop heating
		myTempController.putEvent(new TemperatureEvent(this,
				TemperatureEvent.TEMP_IDLE,
				0.0));
		
		// Drain
		myWaterController.putEvent(new WaterEvent(this,
				WaterEvent.WATER_DRAIN,
				0.0));

		// Wait for AckEvent
		mailbox.doFetch();
		
		// ----------------------------------
		// Main wash

		// Fill the machine
		myWaterController.putEvent(new WaterEvent(this,
				WaterEvent.WATER_FILL,
				WaterController.WATER_LEVEL_FULL));
	
		// Wait for AckEvent
		mailbox.doFetch();
		
		// Heat to 90 degrees Celsius
		myTempController.putEvent(new TemperatureEvent(this,
				TemperatureEvent.TEMP_SET,
				TemperatureController.TEMP_WHITE_MAIN_WASH));
		
		// Wait for AckEvent
		mailbox.doFetch();
		
		// Keep temperature for 30 minutes
		Thread.sleep((long) (30 * 60 * 1000 / mySpeed));
		
		// Turn off temperature control
		myTempController.putEvent(new TemperatureEvent(this,
				TemperatureEvent.TEMP_IDLE,
				0.0));
		
		// Drain
		myWaterController.putEvent(new WaterEvent(this,
				WaterEvent.WATER_DRAIN,
				WaterController.WATER_LEVEL_EMPTY));
		
		// Wait for AckEvent
		mailbox.doFetch();
		
		// Rinse 5 times 2 minutes in cold water
		for (int i = 0; i < 5; i++) {
			
			myWaterController.putEvent(new WaterEvent(this,
					WaterEvent.WATER_FILL,
					WaterController.WATER_LEVEL_FULL));
			
			mailbox.doFetch();
			
			Thread.sleep((long) (2 * 60 * 1000 / mySpeed));
			
			myWaterController.putEvent(new WaterEvent(this,
					WaterEvent.WATER_DRAIN,
					WaterController.WATER_LEVEL_EMPTY));
			
			mailbox.doFetch();
		}
		
		// Turn on centrifuge, wait 5 minutes, turn off centrifuge
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_FAST));
		Thread.sleep((long) (5 * 60 * 1000 / mySpeed));
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_OFF));

		// Unlock the hatch
		myMachine.setLock(false);
	}
}
