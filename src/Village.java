/**
 * Village with its unique id. 
 * Villages can be visited by tourists and are connected by trains.
 * 
 * @author SiruiYan
 *
 */

public class Village {
	
	// a unique identifier for this village
	protected int id;
	
	//identify if any group is in the village
	protected boolean isOccupied;
	
	//the group in the village
	protected Group group;
	
	//the train that groups in this village takes to next village
	protected Train train;
	
	//create a new village with a given Id
	Village(int id){
		this.id = id;
		isOccupied = false;
	}
	
	//wait to enter this village until it is empty
	public synchronized void waitForArrival() {
		while(isOccupied) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//a group of tourists arrives this village
	public synchronized void arrive(Group group) {
		isOccupied = true;
		this.group = group;
		System.out.println(group.toString() + " enters " + this.toString());
		
		this.train.setWaitGroup(group);
	}
	
	//a group of tourists leaves this village
	public synchronized void depart() {	
		isOccupied = false;
		group = null;
		notifyAll();
		
	}
	
	public synchronized void setTrain(Train train) {
		this.train = train;
	}

	public synchronized String toString() {
		return "village " + this.id;
	}
}
