/**
 * 
 * Trains connecting each village and terminus
 * 
 * @author SiruiYan
 *
 */

class Train extends Thread {
	// the start of the train
	protected Object start;

	// the destination of the train
	protected Object destination;

	// identify if the train is running
	protected boolean isOccupied = false;

	// identify if a group waits to use the train
	protected boolean isRequired = false;

	// the group on the train
	protected Group group;

	// the group waiting to use the train
	protected Group waitGroup;

	// create a new train with start and destination
	Train(Object start, Object destination) {
		this.start = start;
		this.destination = destination;
	}

	public void run() {
		while (true) {
			// wait to operate the train until any group wants to use the train
			// do not operate the train when the car is running
			while (isOccupied || !isRequired) {
				try {
					Thread.sleep(Params.CHECK_INTERVAL);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			operate();
		}
	}

	// operate the train
	public void operate() {
		group = waitGroup;
		waitGroup = null;

		CableCar destCableCar, startCableCar;
		Village destVillage, startVillage;

		// if the start of the train is cable car
		if (start instanceof CableCar) {
			startCableCar = (CableCar) start;
			destVillage = (Village) destination;

			// wait to head to the village until it is empty
			destVillage.waitForArrival();
			startCableCar.goUp();
			forwards();
			destVillage.arrive(group);
		}
		// if the start of the train is village
		else {
			startVillage = (Village) start;

			// if the destination of the train is cable car
			if (destination instanceof CableCar) {
				destCableCar = (CableCar) destination;
				// System.out.println("！！！destination is cable car!!!");
				// System.out.println(group.toString());

				// wait to head to terminus until cable car is ready to leave
				destCableCar.waitForGoDown(group);
				System.out.println(group.toString() + " leaves " + getStart());
				startVillage.depart();
				forwards();
				// tell the cable car that the group is at terminus and is ready to depart
				destCableCar.setReadyForDepart(true);
			}
			// if the destination of the train is village
			else {
				destVillage = (Village) destination;
				destVillage.waitForArrival();
				System.out.println(group.toString() + " leaves " + getStart());
				startVillage.depart();
				forwards();
				destVillage.arrive(group);
			}
		}
		group = null;
		backwards();

	}
	
	//move the train from its start to its destination
	public void forwards() {
		this.isOccupied = true;
		isRequired = false;
		// System.out.println("train to " + this.destination + " forwards");
		try {
			Thread.sleep(Params.JOURNEY_TIME);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	//move the train from its destination to its start
	public void backwards() {
		// System.out.println("train to " + this.destination + " backwards");
		try {
			Thread.sleep(Params.JOURNEY_TIME);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.isOccupied = false;
	}

	public void setDestination(Object destination) {
		this.destination = destination;
	}

	public void setWaitGroup(Group waitGroup) {
		this.waitGroup = waitGroup;
		this.isRequired = true;
	}

	public String getStart() {
		if (start instanceof CableCar) {
			return "the cable car";
		} else {
			Village startVillage = (Village) start;
			return startVillage.toString();
		}
	}

}
