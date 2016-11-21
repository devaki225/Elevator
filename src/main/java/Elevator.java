import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by devaki on 11/19/16.
 * This class represents an elevator
 */
public class Elevator implements Runnable {
    private ElevatorStatusEnum elevatorStatus;
    private int nextFloor;
    private int currentFloor;
    private TreeSet<Integer> nextUpFloors;
    TreeSet<Integer> nextDownFloors;
    private DirectionEnum direction;
    private int elevatorNum;
    private int DEFAULT_SLEEP_TIME = 1000; //default sleep time in milliseconds

    public Elevator(int elevatorNum) {
        elevatorStatus = ElevatorStatusEnum.IDLE; //initialize to stationary
        nextFloor = 0;
        nextUpFloors = new TreeSet<>();
        nextDownFloors = new TreeSet<Integer>(Collections.reverseOrder());
        currentFloor = 0;
        direction = DirectionEnum.NONE;
        this.elevatorNum = elevatorNum;

    }

    public TreeSet<Integer> getNextUpFloors() {
        return nextUpFloors;
    }

    public DirectionEnum getDirection() {
        return direction;
    }

    public ElevatorStatusEnum getElevatorStatus() {
        return elevatorStatus;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    /**
     * This method will service a floor. i.e stop at a floor, wait for passengers and then close doors
     * @param elevatorNum
     * @param currentFloor
     * @param direction
     */
    public void ServiceFloor(int elevatorNum, int currentFloor, DirectionEnum direction) {
        try {
            elevatorStatus = ElevatorStatusEnum.OPEN;
            System.out.println("Elevator " + elevatorNum + " STATUS " + elevatorStatus + " currentFloor " + currentFloor + " direction " + direction);
            Thread.sleep(DEFAULT_SLEEP_TIME); //wait at the current floor for doors to open
            elevatorStatus = ElevatorStatusEnum.CLOSE;
            System.out.println("Elevator " + elevatorNum + " STATUS " + elevatorStatus + " currentFloor " + currentFloor + " direction " + direction);
            Thread.sleep(DEFAULT_SLEEP_TIME); //wait at the current floor for closing doors
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /***
     * This method will be responsible for the movement of elevators. The elevator will move up and down depending upon which set has values
     * Once it has serviced all the requests, it will go to the IDLE state and wait the floor where the last request was serviced
     */
    public synchronized void move() {
        try {

            while (!nextDownFloors.isEmpty()) {
                elevatorStatus = ElevatorStatusEnum.RUNNING;
                direction = DirectionEnum.DOWN;
                //First reach the starting floor from where to begin servicing request
                if (currentFloor < nextDownFloors.first())
                    currentFloor++;
                else
                    currentFloor--;
                System.out.println("Elevator: " + elevatorNum + " STATUS: " + elevatorStatus + " currentFloor: " + currentFloor + " nextFloors: " + nextDownFloors.toString() + " direction: " + direction);
                Thread.sleep(DEFAULT_SLEEP_TIME); //time taken to reach the next floor
                if (currentFloor == nextDownFloors.first()) //get the  next floor to stop at
                {
                    nextDownFloors.pollFirst(); //remove floor from set
                    ServiceFloor(elevatorNum, currentFloor, direction); //service the floor
                }
            }

            while (!nextUpFloors.isEmpty()) {
                elevatorStatus = ElevatorStatusEnum.RUNNING;
                direction = DirectionEnum.UP;
                if (currentFloor < nextUpFloors.first())
                    currentFloor++;
                else
                    currentFloor--;
                System.out.println("Elevator " + elevatorNum + " STATUS:" + elevatorStatus + " currentFloor" + currentFloor + " nextFloor:" + nextUpFloors.toString() + " direction:" + direction);
                Thread.sleep(DEFAULT_SLEEP_TIME); //wait at the current floor for doors to open

                if (currentFloor == nextUpFloors.first()) //get the  next floor to stop at
                {
                    nextUpFloors.pollFirst(); //get the  next floor to stop at
                    ServiceFloor(elevatorNum, currentFloor, direction);
                }
            }
            //If all requests have been serviced by the elevator, make it idle
            if (nextDownFloors.isEmpty() && nextUpFloors.isEmpty()) {
                elevatorStatus = ElevatorStatusEnum.IDLE;
                direction = DirectionEnum.NONE;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void addFloors(List<Integer> floors, DirectionEnum direction) {
        for (int floor : floors) {
            if (direction == DirectionEnum.DOWN)
                nextDownFloors.add(floor);
            else if (direction == DirectionEnum.UP)
                nextUpFloors.add(floor);
        }
    }


    public void setElevatorStatus(ElevatorStatusEnum elevatorStatus) {
        this.elevatorStatus = elevatorStatus;
    }


    @Override
    public void run() {
        while (true) {
            try {

                if (elevatorStatus != ElevatorStatusEnum.MAINTENANCE) {
                    move();
                }

                Thread.sleep(DEFAULT_SLEEP_TIME);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "Elevator{" +
                "elevatorStatus=" + elevatorStatus +
                ", currentFloor=" + currentFloor +
                ", nextUpFloors=" + nextUpFloors +
                ", nextDownFloors=" + nextDownFloors +
                ", direction=" + direction +
                '}';
    }

}
