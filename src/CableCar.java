/**
 * The cable car connecting valley and terminus
 * 
 * @author SiruiYan
 *
 */

public class CableCar {

	// the group that wants to go up to terminus
	protected Group goUpGroup = null;

	// the group that wants to go down to valley
	protected Group goDownGroup = null;

	// the group in the cable car
	protected Group group = null;

	// the first train that connects terminus and the first village
	protected Train firstTrain;

	// identify if the train is running
	protected boolean isRunning = false;

	// identify if the train is at valley
	protected boolean atValley = true;

	// identify if the train is at terminus
	protected boolean atTerminus = false;

	// identify if there is a group that wants to leave and use cable car to go down
	// when the cable car is at terminus
	protected boolean isOccupied = false;

	// identify if there is a group that wants to leave and is ready to take the
	// cable car
	protected boolean readyForDepart = false;

	// a new group arrives at valley and waits for the cable car
	public synchronized void arrive(Group group) {
		this.goUpGroup = group;
		this.firstTrain.setWaitGroup(goUpGroup);

		while (goUpGroup != null) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// a group take the cable car and departs when the cable car is ready to leave
	public synchronized void depart() {
		while (!readyForDepart) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// System.out.println("Ready For Depart!!");
		// System.out.println("Go Down Group:" + goDownGroup);
		// System.out.println("lastVillage: " + lastVillage);

		group = goDownGroup;
		goDownGroup = null;
		notifyAll();

		System.out.println(group.toString() + " enters cable car to go down");
		moveToValley();
		System.out.println(group.toString() + " departs");
		this.group = null;
		this.isOccupied = false;
		this.readyForDepart = false;

		notifyAll();
	}

	// operate the cable between valley and terminus when it is not running and is
	// not occupied
	public synchronized void operate() {
		while (isRunning || isOccupied) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (atValley)
			moveToTerminus();
		else
			moveToValley();
	}

	// the group waiting to go up takes the cable car to go up
	public synchronized void goUp() {
		while (!atValley) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		group = this.goUpGroup;
		this.goUpGroup = null;
		notifyAll();
		System.out.println(this.group.toString() + " enters cable car to go up");

		moveToTerminus();

		System.out.println(this.group.toString() + " leaves the cable car");
		group = null;
	}
	
	//the group that wants to leave wait for the cable car go to terminus
	public synchronized void waitForGoDown(Group group) {
		while (!atTerminus) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		isOccupied = true;
		this.goDownGroup = group;
		// System.out.println("setGoDownGroup: " + group);
	}
	
	//move the cable car to terminus
	public synchronized void moveToTerminus() {
		atValley = false;
		isRunning = true;

		System.out.println("cable car ascends");
		try {
			Thread.sleep(Params.JOURNEY_TIME);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		atTerminus = true;
		isRunning = false;

		notifyAll();
	}

	//move the cable car to valley
	public synchronized void moveToValley() {
		atTerminus = false;
		isRunning = true;

		System.out.println("cable car descends");
		try {
			Thread.sleep(Params.JOURNEY_TIME);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		atValley = true;
		isRunning = false;

		notifyAll();
	}
	
	//set firstTrain
	public synchronized void setFirstTrain(Train firstTrain) {
		this.firstTrain = firstTrain;
	}

	//set readyForDepart
	public synchronized void setReadyForDepart(boolean readyForDepart) {
		this.readyForDepart = readyForDepart;
		notifyAll();
	}

}
