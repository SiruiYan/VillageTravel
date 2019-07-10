/**
 * Inspects the cable car at random intervals and, once it is unoccupied, sends
 * it from the valley to the terminus, or from the terminus to the valley.
 * 
 * @author SiruiYan
 *
 */
public class Operator extends Thread {

	private CableCar cableCar;

	Operator(CableCar cableCar) {
		this.cableCar = cableCar;
	}

	public void run() {
		while (!isInterrupted()) {
			try {
				// let some time pass before the next operate
				Thread.sleep(Params.checkLapse());
				
				//operate the cable between valley and terminus
				cableCar.operate();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
