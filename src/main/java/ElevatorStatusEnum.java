
/**
 * Created by devaki on 11/19/16.
 */

public enum ElevatorStatusEnum {
    IDLE(0),
    RUNNING(1),
    MAINTENANCE(2),
    OPEN(3),
    CLOSE(4);

    private int status;

    ElevatorStatusEnum(int status) {
      this.status = status;
    }

    public int getStatus() {
        return status;
    }

}
