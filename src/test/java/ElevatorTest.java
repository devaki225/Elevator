import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by devaki on 11/20/16.
 */
public class ElevatorTest {
    private Elevator elevator;
    @BeforeClass
    public void setUp() {
        elevator = new Elevator(1);
        elevator.setElevatorStatus(ElevatorStatusEnum.RUNNING);
    }

    @Test
    /* test that one elevator is moving up and down as expected*/
    public void testMoveSingleRequest(){
        //moving up
        List<Integer> floors = new ArrayList<>(Arrays.asList(1, 2));
        elevator.addFloors(floors,DirectionEnum.UP);
        elevator.setElevatorStatus(ElevatorStatusEnum.RUNNING);
        elevator.move();
        Assert.assertEquals(elevator.getCurrentFloor(),2);
        Assert.assertEquals(elevator.getElevatorStatus(),ElevatorStatusEnum.IDLE);

        //moving down
        floors = new ArrayList<>(Arrays.asList(3, 1));
        elevator.addFloors(floors,DirectionEnum.DOWN);
        elevator.setElevatorStatus(ElevatorStatusEnum.RUNNING);
        elevator.move();
        Assert.assertEquals(elevator.getCurrentFloor(),1);
        Assert.assertEquals(elevator.getElevatorStatus(),ElevatorStatusEnum.IDLE);
    }

    @Test
    //test that multiple requests in the same direction are clubbed together
    public void testMoveMultipleRequestsSameDirection() throws Exception{
        Thread thread = new Thread(elevator);
        thread.start();
        List<Integer> floors = new ArrayList<>(Arrays.asList(1, 6));
        elevator.addFloors(floors,DirectionEnum.UP);
        elevator.setElevatorStatus(ElevatorStatusEnum.RUNNING);
        floors = new ArrayList<>(Arrays.asList(4, 5));
        elevator.addFloors(floors,DirectionEnum.UP);
        elevator.setElevatorStatus(ElevatorStatusEnum.RUNNING);

        Thread.sleep(3000);
        Assert.assertEquals((int)elevator.getNextUpFloors().first(),4); //the next floors should be 4,5, and 10
        Thread.sleep(10000);
         Assert.assertEquals(elevator.getCurrentFloor(),6);
      }

    @Test
    //Test that requests coming from different directions are added to the proper sets and are executed serially
    public void testMoveMultipleRequestsDifferentDirection(){
        Thread thread = new Thread(elevator);
        thread.start();
        List<Integer> floors = new ArrayList<>(Arrays.asList(1, 3));
        elevator.addFloors(floors,DirectionEnum.UP);
        elevator.setElevatorStatus(ElevatorStatusEnum.RUNNING);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        floors = new ArrayList<>(Arrays.asList(5, 2));
        elevator.addFloors(floors,DirectionEnum.DOWN);
        elevator.setElevatorStatus(ElevatorStatusEnum.RUNNING);
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(elevator.getCurrentFloor(),2);
        Assert.assertEquals(elevator.getElevatorStatus(),ElevatorStatusEnum.IDLE);

    }




}
