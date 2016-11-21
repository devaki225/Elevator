import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by devaki on 11/19/16.
 */
public class ElevatorController {
    private List<Elevator> elevatorList;
    private DirectionEnum directionEnum;
    private int floorCount;

    public ElevatorController(DirectionEnum directionEnum, int floorCount, List<Elevator> elevatorList) {
        this.directionEnum = directionEnum;
        this.floorCount = floorCount;
        this.elevatorList = elevatorList;
    }

    /***
     * This method will choose the best elevator to give the request to in case fo multiple elevators.
     * In case of a single elevator, it will decide if the request can be serviced immediately or has to wait
     *
     * @param elevatorRequest
     */
    public synchronized void decideNextElevator(ElevatorRequest elevatorRequest) {
        int constDiff = 2;
        int startFloor = elevatorRequest.getStartFloor();
        int endFloor = elevatorRequest.getEndFloor();
        //Validate  start and end floors of each request
        if (startFloor > floorCount || startFloor < 1 || endFloor > floorCount || endFloor < 1) {
            System.out.println("Enter a valid floor. Floor should be between 1  and " + floorCount);
            return;
        }
        //find out the direction
        if (startFloor > endFloor)
            directionEnum = DirectionEnum.DOWN;
        else
            directionEnum = DirectionEnum.UP;
        int minDiff = Integer.MAX_VALUE;
        Elevator chosenElevator = null;
        List<Integer> floors = new ArrayList<>(Arrays.asList(startFloor, endFloor));
        //Continue until a valid elevator has not been found to service the request
        while (true) {
            chosenElevator = null;
            for (Elevator elevator : elevatorList) {

                int currDiff = 0;
                if ((elevator.getDirection() == DirectionEnum.NONE) || elevator.getDirection() == directionEnum) {

                    //We have chosen an elevator which is idle or moving in the same direction
                    if ((elevator.getDirection() == DirectionEnum.NONE)) {
                        currDiff = Math.abs(startFloor - elevator.getCurrentFloor());
                    } else if ((directionEnum == DirectionEnum.UP) && (elevator.getDirection() == DirectionEnum.UP)) {
                        currDiff = startFloor - elevator.getCurrentFloor();
                        //We want to be sure that the elevator can process the current request.
                        //So any elevator has to be more than constDiff away from the start floor if in a running state
                        if (currDiff <= constDiff) {
                            continue;
                        }
                    } else if ((directionEnum == DirectionEnum.DOWN) && (elevator.getDirection() == DirectionEnum.DOWN)) {
                        currDiff = elevator.getCurrentFloor() - startFloor;
                        if (currDiff <= constDiff) {
                            continue;
                        }
                    }

                    minDiff = Math.min(minDiff, currDiff);
                    if (minDiff == currDiff) {
                        chosenElevator = elevator;
                    }
                }
            }
            if (chosenElevator != null) {
                System.out.println("Elevator chosen " + chosenElevator.toString());
                chosenElevator.addFloors(floors, directionEnum);
                chosenElevator.setElevatorStatus(ElevatorStatusEnum.RUNNING);
                break;
            }

        }

    }


    public ElevatorController(int elevatorCount, int floorCount) {
        this.elevatorList = new ArrayList<>(elevatorCount);
        this.floorCount = floorCount;
        createElevators(elevatorCount);
    }

    public void createElevators(int elevatorCount) {
        for (int i = 0; i < elevatorCount; i++) {
            Elevator elevator = new Elevator(i + 1);
            elevatorList.add(elevator);
            Thread t = new Thread(elevator);
            t.start();

        }
    }

    public Elevator getElevatorInfo(int elevatorNum) {
        if (elevatorList == null) return null;
        if (elevatorNum > elevatorList.size()) {
            System.out.println("Enter a valid elevator number");
            return null;
        }
        return elevatorList.get(elevatorNum - 1);

    }


}
