import java.util.Scanner;

/**
 * Created by devaki on 11/19/16.
 */
public class ElevatorMain {

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter the number of elevators you want: ");
        int elevatorCount = reader.nextInt();
        System.out.println("Enter the number of floors in the building: ");
        int floorCount = reader.nextInt();
        ElevatorController elevatorController = new ElevatorController(elevatorCount,floorCount);
        boolean start = true;
        while (start) {
            System.out.println("Select a choice from below: ");
            System.out.println("1. Find out Status of elevator ");
            System.out.println("2. Schedule an elevator ");
            System.out.println("3. Exit the system(or press ctnrl + c ");
            int choice = reader.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Enter elevator number: ");
                    int elevatorNum = reader.nextInt();
                    Elevator elevatorDTO = elevatorController.getElevatorInfo(elevatorNum);
                    System.out.println(elevatorDTO.toString());
                    break;

                case 2:
                    System.out.println("Enter start floor: ");
                    int startFloor = reader.nextInt();
                    System.out.println("Enter end floor: ");
                    int endFloor = reader.nextInt();
                    ElevatorRequest elevatorRequest = new ElevatorRequest(startFloor, endFloor);
                    elevatorController.decideNextElevator(elevatorRequest);
                    break;
                case 3:
                    System.out.println("Exiting");
                    start=false;
                default:
                    System.out.println("Enter a valid choice");

            }
        }
    }

}
