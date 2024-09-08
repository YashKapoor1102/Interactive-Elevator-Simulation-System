# Interactive Elevator Simulation System

## Introduction
Welcome to our Elevator Simulation project! This system is designed to emulate the operation of a multi-floor
elevator, using a CSV file to simulate various floor requests. Dive into our demo and code explanations
below to see our project in action and understand the intricate details of our implementation.

## Demo
See our elevator system in action. Watch this quick demo recorded by Yash Kapoor:
https://drive.google.com/file/d/12HUt9LV8JaaVcVEB9E1GLQNU7gGyJdwV/view?usp=drive_link

## Detailed Code Explanations
Gain deeper insights into our project's core components through these detailed video
explanations recorded by Yash Kapoor:

- Floor Class Explanation:
https://drive.google.com/file/d/1_TeJdQQIhGewdK3BS8KhRDB1rjVV7eeC/view?usp=drive_link

- Scheduler Class Explanation:
https://drive.google.com/file/d/1Hoiw_Mp5SNxVOwvAQfhB8jekU4U6Ig5t/view?usp=drive_link

- Elevator Class Explanation:
https://drive.google.com/file/d/1jeznb7a8ZJLK5elIyOsHcvyAQVPL14qL/view?usp=drive_link


## Description
There are three subsystems in this project: Floor, Elevator, and Scheduler.
The scheduler is used as a communication channel between the clients (i.e., floor and elevator).
The data in the CSV file gets passed from the floor -> scheduler -> elevator -> scheduler -> floor. 
Specifically, the Floor subsystem reads the CSV file and sends the values to the scheduler.
The scheduler stores those values in a queue AllFloorRequests, the elevator determines which requests are serviceable based on the first request and notifies the scheduler to store those requests in another queue serviceableRequests. The elevator then iterates through both queues until they're empty. The  execution of this program is simple. The Floor subsystem executes first and prints "Starting at Floor". Then, The Floor subsystem sends the data that it reads from the CSV file to the scheduler class. The scheduler notifies the elevator, which then begins executing and prints out "Elevator Success", along with the data in the CSV file. Then, the elevator sends a request back to the scheduler that it is done. Hence, the scheduler sends a request back to the floor, telling it to start executing again. Once the floor starts executing again, it prints out "Ending at Floor", along with the data in the CSV file. 

This program is made up of six essential files:<br><br>
	**Floor.java:** Floor Class that consists of the floor thread that executes first to send requests to the scheduler at the time of the request.<br>   
	**Scheduler.java:** Scheduler Class that consists of a thread that is used as a communication channel between the clients (i.e., floor and elevator).<br>   
	**Elevator.java:** Elevator Class that consists of the elevator thread that will 			   
	               execute after the scheduler sends the request.
			   Receives the request from the scheduler, processes the 
			   request, and then sends an acknowledgement to the scheduler
		         that indicates the request has been successfully serviced by
			   the elevator. There are a total of 4 elevators (a separate 
			   thread is used for each elevator).
		         There are 8 states for each elevator: 
		         0 (stationary), 1 (moving up), 2 (moving down), 3 (doors 
		         opening), 4 (doors closing), 5 (floor fault), 6 (door fault), 
                     7 (out of service).<br>  
	**ElevatorGUI.java:** The ElevatorGUI class represents a graphical user 
 				interface for an elevator system. It extends the JFrame  
		            class and includes components such as text fields, labels, 
				and icons for displaying elevator information and status.<br>  
	**Pair.java:** Pair Class that the Elevator Class uses to clearly differentiate 
		     one request from another and store Pair Objects in a queue.<br>  
	**DestinationFloor.java:** DestinationFloor Class that is used by the Elevator 
				     Class to move passengers to a specific destination 
				     floor. It is used to differentiate destination floors 
				     of requests from initial floors of requests
 			           For example, request: 2, 8 -> initial floor is 2 
				     (passengers get picked up) and destination floor is 8 
				     (passengers get dropped off)

## UML and State Machine Diagrams
For a better understanding of the system's architecture and internal states, you can refer to the following diagrams:

- UML Class Diagram: <a href="https://drive.google.com/file/d/18IO8JgMc2rWlKJIZXzgAUJFwDqAMJe6L/view?usp=sharing">View UML Diagram</a><br><br>
This diagram outlines the structure of the system, including the relationships between the Floor, Scheduler, and Elevator classes.

- Scheduler State Machine Diagram: <a href="https://drive.google.com/file/d/1LLcUjhjRdyU99J8BJ0xk_lxXDMPlRvvU/view?usp=sharing">View Scheduler State Machine Diagram</a><br><br>
This diagram shows the various states the scheduler can be in and how it transitions between them during the simulation.

- Elevator State Machine Diagram: <a href="https://drive.google.com/file/d/1OEEc9zVYza_VgoMYx4cvt3c06JyMMdsG/view?usp=sharing">View Elevator State Machine Diagram</a><br><br>
This diagram outlines the different states the elevator can be in (e.g., moving up, moving down, stationary) and the transitions triggered by requests or errors.

## Installation
Most versions of Java will be able to run this program, but JDK 18 is recommended. 

A Java IDE such as Eclipse is recommended as well. 

If Eclipse is not already installed, use the following link that provides step-by-step instructions
on how to install it for popular operating systems:

https://www.eclipse.org/downloads/packages/installer

## Usage
**Step 1:** Download our project, A3G8_final_submission.zip, from here:   
https://drive.google.com/file/d/1tvgxvHA4b67YnvCytwONXd2wpTdGh5nT/view?usp=drive_link  

**Step 2:** Save A3G8_final_submission.zip to a folder of your choice.

**Step 3:** Open Eclipse and ensure the "Java Browsing" perspective is selected
	  by going to Window > Perspective > Open Perspective > Java Browsing.

**Step 4:** Click on File > Import from the Eclipse main menu.

**Step 5:** Expand General, click on "Existing Projects into Workspace", and click Next.

**Step 6:** Ensure that Select Archive File is checked and browse for A3G8_final_submission.zip.

**Step 7:** Click Finish. 

**Step 8:** The project should now be in Package Explorer.

**Step 9:** Expand project and expand src.

**Step 10:** Click on the project package. You need to run the Scheduler Class first, then run the Elevator Class, 
	then finally, run the Floor Class. Also, we included the GUI in the Elevator Class, so we have a GUI
	for each elevator (1, 2, 3, and 4). Hence, open the GUIs and spread them out on your screen to monitor
	which floor/state the elevator is currently on/in. You can also look at the console for each class
	(separate console for Floor, Scheduler, and Elevator since they are supposed to be running on separate computers),
	which provides a detailed description of what each Class is currently doing (e.g., floor sends request to scheduler,
	scheduler sends request to appropriate elevator, then elevator processes that request, and sends an acknowledgement
	to the scheduler, which sends that acknowledgement to the floor, indicating the request has been successfully
	processed by the elevator). 

**Step 11:** IMPORTANT: to see the output of the floor, scheduler, and the elevator, you must click on “Display Selected Console”
	and click on the console of the class that you want to view the output of. If you cannot find where “Display Selected Console”
	is on Eclipse, then you can click on the console and press ALT + F7 on your keyboard to bring up the menu that allows you to
	switch between the consoles of each class.

## Testing
Once you are able to run the program, simply click on the SchedulerTest/ElevatorTest/FloorTest/FloorDataTest JUnit classes
to run the JUnit tests for our system. 


## Credits
- Yash Kapoor 		(Worked on code, refactoring JUnit Tests, refactoring code, UML, and README)
- Faiaz Ahsan 		(JUnit tests, rough draft of UML and Sequence Diagram)
- Zeid Alwash 		(Worked on code, rough draft of UML and README)
- Fareen Lavji	  	(JUnit Tests and updating README) 
- Harishan Amutheesan	(JUnit tests, rough draft of UML, and State Machine Diagram)
