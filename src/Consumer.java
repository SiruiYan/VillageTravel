/**
 * Consumes finished tour groups from the base of the mountain park.
 * 
 * @author ngeard@unimelb.edu.au
 *
 */

public class Consumer extends Thread {

	// the cable car from which groups will depart
	private CableCar cableCar;
	
	Consumer(CableCar cableCar) {
		this.cableCar = cableCar;
	}
	
	public void run() {
		while (!isInterrupted()) {
			try {
				// remove a vessel that has returned from the mountain park
				cableCar.depart();
		
				// let some time pass before the next departure
				sleep(Params.departureLapse());
			}
			catch (InterruptedException e) {
				this.interrupt();
			}
		}
	}
}
