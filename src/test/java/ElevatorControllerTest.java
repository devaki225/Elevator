import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created by devaki on 11/20/16.
 */

public class ElevatorControllerTest {

    private ElevatorController elevatorController;

    @BeforeClass
    public void setup() {
    }

    @Test
    //This method will test that for one elevator, requests are added to the proper sets and executed
    public void testDecideNextElevatorSingle() {
        ElevatorRequest elevatorRequest = new ElevatorRequest(3, 8);
        List<Elevator> elevatorMockList = new ArrayList<>();
        Elevator elevatorMock = mock(Elevator.class);
        when(elevatorMock.getCurrentFloor()).thenReturn(1);
        when(elevatorMock.getDirection()).thenReturn(DirectionEnum.NONE);
        when(elevatorMock.getElevatorStatus()).thenReturn(ElevatorStatusEnum.IDLE);
        elevatorMockList.add(elevatorMock);
        elevatorController = new ElevatorController(DirectionEnum.UP, 10, elevatorMockList);
        elevatorController.decideNextElevator(elevatorRequest);
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(elevatorMock, times(1)).addFloors(captor.capture(), eq(DirectionEnum.UP));
        Assert.assertEquals(captor.getValue().get(0), 3);
        Assert.assertEquals(captor.getValue().get(1), 8);

    }

    @Test
    public void testDecideNextElevatorSameDirection() {
        //Test that closest elevator is chosen when you have 2 elevators in the same direction
        ElevatorRequest elevatorRequest = new ElevatorRequest(6, 10);
        List<Elevator> elevatorMockList = new ArrayList<>();
        //elevator 1 at floor 1
        Elevator elevatorMock1 = mock(Elevator.class);
        when(elevatorMock1.getCurrentFloor()).thenReturn(1);
        when(elevatorMock1.getDirection()).thenReturn(DirectionEnum.UP);
        when(elevatorMock1.getElevatorStatus()).thenReturn(ElevatorStatusEnum.RUNNING);

        //elevator 2 at floor 3
        Elevator elevatorMock2 = mock(Elevator.class);
        when(elevatorMock2.getCurrentFloor()).thenReturn(3);
        when(elevatorMock2.getDirection()).thenReturn(DirectionEnum.UP);
        when(elevatorMock2.getElevatorStatus()).thenReturn(ElevatorStatusEnum.RUNNING);

        elevatorMockList.add(elevatorMock1);
        elevatorMockList.add(elevatorMock2);

        elevatorController = new ElevatorController(DirectionEnum.NONE, 15, elevatorMockList);
        elevatorController.decideNextElevator(elevatorRequest);

        //Test that  second elevator has been selected
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(elevatorMock2, times(1)).addFloors(captor.capture(), eq(DirectionEnum.UP));
        Assert.assertEquals(captor.getValue().get(0), 6);
        Assert.assertEquals(captor.getValue().get(1), 10);

    }


    @Test
    public void testDecideNextElevatorDifferentDirection() {
        ElevatorRequest elevatorRequest = new ElevatorRequest(6, 10);
        List<Elevator> elevatorMockList = new ArrayList<>();
        //elevator 1 at floor 1
        Elevator elevatorMock1 = mock(Elevator.class);
        when(elevatorMock1.getCurrentFloor()).thenReturn(1);
        when(elevatorMock1.getDirection()).thenReturn(DirectionEnum.UP);
        when(elevatorMock1.getElevatorStatus()).thenReturn(ElevatorStatusEnum.RUNNING);

        //elevator 2 at floor 3
        Elevator elevatorMock2 = mock(Elevator.class);
        when(elevatorMock2.getCurrentFloor()).thenReturn(3);
        when(elevatorMock2.getDirection()).thenReturn(DirectionEnum.DOWN);
        when(elevatorMock2.getElevatorStatus()).thenReturn(ElevatorStatusEnum.RUNNING);

        elevatorMockList.add(elevatorMock1);
        elevatorMockList.add(elevatorMock2);

        elevatorController = new ElevatorController(DirectionEnum.NONE, 15, elevatorMockList);
        elevatorController.decideNextElevator(elevatorRequest);

        //Test that  first elevator has been selected since it gas the same direction
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(elevatorMock1, times(1)).addFloors(captor.capture(), eq(DirectionEnum.UP));
        Assert.assertEquals(captor.getValue().get(0), 6);
        Assert.assertEquals(captor.getValue().get(1), 10);


    }

}
