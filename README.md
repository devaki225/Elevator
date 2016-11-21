Elevator 
=========

This an elevator simulation that simulates elevators in a building. 
Building can contain N floors(N>1). It works for 1 as well as multiple elevators.

Build
=========
to build, clone this repo locally and run- mvn clean install
It will take upto 30-40 seconds to run all tests

Run
====
- mvn exec:java
- First select the number of elevators you would like to simulate and the floors in the building.
- Then select the actions you would like to perform.
- 1. Know the status of the elevator
- 2. Call an elevator by entering the starting and ending floor

Expected Output
-------------- 
- Enter the number of elevators you want: <Enter a number between 1 and upto 10> 1
- Enter the number of floors in the building: <Enter a number more than 1> 20
- Select a choice from below: 
- 1. Find out Status of elevator 
- 2. Schedule an elevator 
- 3. Exit the system(or press ctnrl + c 
- <Enter choice> 2
- Enter start floor: 
- 2
- Enter end floor: 
- 20
- Elevator 1 STATUS:RUNNING currentFloor1 nextFloor:[2, 20] direction:UP
- Elevator 1 STATUS:RUNNING currentFloor2 nextFloor:[2, 20] direction:UP
- Elevator 1 STATUS:OPEN currentFloor:2 direction:UP
- Elevator 1 STATUS:CLOSE currentFloor:2 direction:UP
- Elevator 1 STATUS:RUNNING currentFloor3 nextFloor:[20] direction:UP
- Elevator 1 STATUS:RUNNING currentFloor4 nextFloor:[20] direction:UP
- Elevator 1 STATUS:CLOSE currentFloor:20 direction:UP
- 2
- Enter start floor: 
- 5
- Enter end floor: 
- 1
- Elevator{elevatorStatus=IDLE, nextFloor=0, currentFloor=20, nextUpFloors=[], nextDownFloors=[], direction=NONE}
- Elevator 1 STATUS:RUNNING currentFloor: 18 nextFloors:[5, 1] direction:DOWN
- Elevator 1 STATUS:RUNNING currentFloor: 17 nextFloors:[5, 1] direction:DOWN
- Elevator 1 STATUS:RUNNING currentFloor: 16 nextFloors:[5, 1] direction:DOWN

<Elevator status>
Enter elevator number: 
1
Elevator{elevatorStatus=IDLE, nextFloor=0, currentFloor=1, nextUpFloors=[], nextDownFloors=[], direction=NONE}


Design
=======
- Each elevator has a list of up and down floors associated with it.
- Initially all elevators at the 1st floor and in the idle state.
- When a person requests an elevator, the elevator control system decides which elevator the request should go to.
- In case of a single elevator system, the controller decides whether the request can be accommodated immediately 
- or does it have to wait till the other requests have been completed.

Single Elevator system
-----------------------
1. Initially the elevator is at floor 1 and in IDLE state.
2. Say a person requests to go from floor 4 to 6.
3. The elevator control system checks if the elevator is in a idle state. If so, it will immediately assign the elevator to go from 4 to 6
4. If the elevator after adding the above requests to its queue gets another request to go from floor 5 to 8, then the controller will check
   if the request can be accommodated immediately. To check that it will first check the direction. 
   Since the current and incoming requests are in the same direction, it will check if we have already passed the start floor of the incoming request.
   If so, it will wait until the current request is complete and then add it to its queue. All other requests will be waiting until this can be added in 
   the UP queue.
   In the current case we have not passed 5 so the UP queue becomes [4,5,6,8] with stops at each of those floors
5. If the  elevator is moving in the UP direction and it gets a new request to move in the DOWN direction, then it will add its to its DOWN queue.As soon as it is done
  servicing the UP requests, it will move in the DOWN direction.

Multiple Elevator System
--------------------------
In multiple elevator system, we choose the elevators that are idle or moving in the same direction.
For each elevator we check which one is the closest to the starting point of the new request. We then add the incoming request to that elevator's queue
Basically, it is the same logic for single elevators extended to chose the best elevator as well each time.
 
Next Steps
----------
To convert to a REST application, we can have the below services
- 1. /v1/createElevators : The request params to this will be the umber of elevators and the number of floors to the building
- 2. /v1/requestElevator: This will contain the start and end floor of the request
- 3. /v1/showElevatorStatus: This will show the elevator info. Query parameter will be the elevator id
- 4. /v1/realTimeElevatorInfo : This wil show the all the current elevator logs so far. For this the change we will have to do in the backend is that
   instead of printing logs to console collect them into some object and return that object back to the calling request
   
    
    




  



