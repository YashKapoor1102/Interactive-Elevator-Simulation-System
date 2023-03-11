import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Elevator Class that consists of the elevator thread that will execute after the scheduler sends the request.
 * 
 * @author Yash Kapoor
 * @author Faiaz Ahsan
 * @author Zeid Alwash
 * @author Fareen Lavji
 * @author Harishan Amutheesan
 * 
 * @version 02.27.2023
 */
public class Elevator implements Runnable {
	private Scheduler scheduler;
	private int elevatorToSchedulerCondition;
	
	
	//private int currentFloor;	
	// the floor the elevator needs to go to, so it can take the request
	// the initial floor of the request
	
	private int upState;
	private int downState;
	private int idle;
	
	private boolean canService;
	// true if the request is serviceable, false otherwise
	
	private int direction;
	private static long travelTime = 1000; // average time elevator takes to move 1 floor
	Collection<Integer> requestsQueue;
	private Integer currentFloor;
	
	private int portNumber;
	
	DatagramPacket sendPacket, receivePacket;
	DatagramSocket socketScheduler, socketFloor;
	
	
	
	/**
	 * Constructor for Elevator.
	 * 
	 * @param s	A Scheduler object that is used to communicate between the two clients (i.e., floor and elevator).
	 */
	public Elevator( int portNumber) {
		this.elevatorToSchedulerCondition = 0;
		this.upState = 0;
		this.downState = 0;
		this.idle = 1;
		this.currentFloor = 2; // assume elevator starts at floor 2 
		this.portNumber = portNumber;
		this.requestsQueue = Collections.synchronizedCollection(new LinkedList<>());
		
		
		  try {
		         socketScheduler = new DatagramSocket(portNumber);
		      } catch (SocketException se) {
		         se.printStackTrace();
		         System.exit(1);
		      }						
	}
	
	/**
	 * Returns the direction of the elevator.
	 * 
	 * @return Up if moving up, down if moving down, stationary if not moving.
	 */
	public int getDirection() { return this.direction; }
	
	/**
	 * Returns the size of the elevators queue.
	 * 
	 * @return An integer value with the elevator's queue size.
	 */
	public int getQueueSize() { return requestsQueue.size(); }
	
	/**
	 * Adds request to queue based on most efficient position.
	 * 
	 * @param destinationFloor The destination floor of the given request.
	 */
	public void addRequest(int destinationFloor, int position) {
		requestsQueue.add(destinationFloor);// need to implement the position as well

	}
	
	/**
	 * Sets the elevatorToScheduler Condition to 1 when it is time to go back to the scheduler.
	 * Sets the schedulerToElevatorCondition to false when it is time to go back to the
	 * scheduler so the elevator thread stops executing.
	 * 
	 * @return	An integer value (either 0 or 1) that is used to go back to scheduler from the elevator.
	 */
	public int notifyElevatorToScheduler() {
 		elevatorToSchedulerCondition = 1;
 		scheduler.setSchedulerToElevatorConditionToFalse();
 		
 		
 		return elevatorToSchedulerCondition;
	}

	/**
	 * Executes a request based on the destination floor. Switches the state to active and calculates how long the
	 * elevator would take to reach the destination floor. If there are no more requests in the queue, state stays
	 * active.
	 * 
	 * @param destinationFloor The destination floor that the elevator needs to go to.
	 */
	private void executeRequest(Integer destinationFloor) {
		
		// switch the state of the elevator
		int comparisor = destinationFloor.compareTo(this.currentFloor);
		
		if (comparisor < 0) { this.direction = 0; }
		else if (comparisor > 0) {this.direction = 1; }
		else { this.direction = 2; }
		
		// processing request
		try {
			Thread.sleep(Elevator.travelTime * (destinationFloor - this.currentFloor));
		}
		catch (InterruptedException e) {}
		
		// switch elevator back to stationary if there are no more requests
		if (!this.requestsQueue.isEmpty()) { this.direction = 2; }
		
		this.currentFloor = destinationFloor; // update current floor
		
		//0 for up, 1 or down and 2 for stationnary
		
	}
	
	public void sendReceive() {
		while(true) {
			byte schedulerData[] = new byte[100];
			byte ackData[] = new byte[100];
			String ack = "Data received";

			
			//form packet to receive data from scheduler
			receivePacket = new DatagramPacket(schedulerData, schedulerData.length);
			
			
			//receive Packet from scheduler
			try {        
		         System.out.println("Waiting..."); // so we know we're waiting
		         socketScheduler.receive(receivePacket);
		        
		      } catch (IOException e) {
		         System.out.print("IO Exception: likely:");
		         System.out.println("Receive Socket Timed Out.\n" + e);
		         e.printStackTrace();
		         System.exit(1);
		      }
			
			
			//print the received datagram from the scheduler
		      System.out.println("Scheduler: Packet received from Floor:");
		      System.out.println("From host: " + receivePacket.getAddress());
		      System.out.println("Host port: " + receivePacket.getPort());
		      System.out.println("Length: " + receivePacket.getLength());
		      System.out.println("Containing: " + new String(receivePacket.getData(),0,receivePacket.getLength()));
		      System.out.println("Containing in bytes: " + Arrays.toString(receivePacket.getData()));
			
			//process the request, call the internal method
			executeRequest( Integer.parseInt(Arrays.toString(receivePacket.getData())));
			
			//Print request has been serviced
			System.out.println("This Request has been fulfilled!");
			
			

		      		      		      
		      //form ackPacket to send to scheduler
		      ackData = ack.getBytes();
		      try {
		      sendPacket = new DatagramPacket(ackData, ackData.length, InetAddress.getLocalHost(), receivePacket.getPort());
		      }catch(UnknownHostException e) {
			         e.printStackTrace();
			         System.exit(1);
			      }
		      
		      //send the Ack Packet to the scheduler
		      try {
		    	  socketScheduler.send(sendPacket);
		      } catch (IOException e) {
		         e.printStackTrace();
		         System.exit(1);
		      }
		      
			  //print the sent to the scheduler
		      System.out.println("Scheduler: Packet received from Floor:");
		      System.out.println("From host: " + sendPacket.getAddress());
		      System.out.println("Host port: " + sendPacket.getPort());
		      System.out.println("Length: " + sendPacket.getLength());
		      System.out.println("Containing: " + new String(sendPacket.getData(),0,sendPacket.getLength()));
		      System.out.println("Containing in bytes: " + Arrays.toString(sendPacket.getData()));
		      			
		}		
	}
	
	
	/**
	 * Used to run the Elevator threads.
	 */
	@Override
    public void run() { 
		
		sendReceive();
		
//        while(true) {
//        	if (scheduler.getSchedulerToElevatorCondition() == 1) {
//        		if(scheduler.getServiceableRequests().isEmpty() && !scheduler.getAllRequests().isEmpty()) {
//        			// as long as the serviceableRequests and allFloorRequests queue are not empty
//        			// keep checking whether the request is serviceable or not
//        			currentFloor = ((FloorData) scheduler.getAllRequests().element()).getInitialFloor();
//    	        	for(FloorData item: scheduler.getAllRequests()) {
//    	        		// if request is serviceable, add the FloorData object to the serviceableRequests queue
//    	        		if(executeRequest(item))  
//    	        		{
//    	        			    scheduler.addServiceableRequests(item);
//    	        			    
//    	        			    
//    	        			    // setting these to 0 since our elevator is stationary
//    	        			    // since it is neither going up nor down - it is not servicing anything yet
//    	        			    upState = 0;
//    	        			    downState = 0;
//    	        		}
//    	        		else {
//    	        			// Otherwise, output a message that the request cannot be serviced at the moment
//    	        			System.out.println("Request at time: " + item.getTime() + " cannot be processed at the moment.");
//    	        			
//    	        		}
//    	        	}
//        		}
//	        	while(!scheduler.getServiceableRequests().isEmpty()) {
//	        		// Go through the serviceableRequests queue
//	        		// until all requests have been serviced by the elevator
//	        		
//   	            	System.out.println("\nElevator: processing request");
//   	                System.out.println("\n\tElevator Received Request: " +
//   	                		"\n\t\tInitial Floor: " + ((FloorData) scheduler.getServiceableRequests().element()).getInitialFloor() +
//	                		" Destination Floor: " + ((FloorData) scheduler.getServiceableRequests().element()).getDestinationFloor() +
//	                		" Floor Button: " + ((FloorData)scheduler.getServiceableRequests().element()).getFloorButton() +
//	                		" Time: " + ((FloorData)scheduler.getServiceableRequests().element()).getTime() + "\n");
//   	                
//   	          
//   	                // calling this method to update the states
//  	                executeRequest(scheduler.getServiceableRequests().element());
//   	 			    System.out.println("Elevator upState = " + upState);
//   	 			    System.out.println("Elevator downState = " + downState);
//   	                System.out.println("Elevator: request was processed. Elevator is Stationary.");
//   	               
//   	                upState = 0;
//   	                downState = 0;
//   	                // Elevator is idle only when it executes ALL the requests 
//   	                // Therefore, we are setting idle equals 0 since the elevator is stationary, but not idle yet
//   	                idle = 0;
//   	                
//   	                notifyElevatorToScheduler();    // going back to scheduler from elevator, so scheduler can send the data to the floor
//   	                break;    	
//       	         }
//        	} else {
//	    		idle = 1;
//	    		if (idle == 1 && scheduler.getAllRequests().isEmpty() && scheduler.getServiceableRequests().isEmpty()) {
//	    			// idle when all the requests in the allFloorRequests have been serviced
//	    			// and both queues are empty
//	    			System.out.println("Elevator State = Idle");
//	    			
//	    			// setting idle equal to 0 to break out of the while loop and stop the elevator from executing
//	    			idle = 0;
//	    		}
//        		try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {}
//        	}
//        }
	}
	
	public static void main(String args[])
	   {
		Elevator e = new Elevator(69);		
	    Thread t1 = new Thread(e);	    	    
	    t1.start();
	    
	   }
}
        		

         	
            
    
	

