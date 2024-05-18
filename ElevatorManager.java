import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ElevatorManager implements Runnable{
    private final Elevator elevator_1;
    private final Elevator elevator_2;
    private final ExecutorService service;
    public static boolean execution = true;
    public final static ConcurrentLinkedQueue<Call> calls = new ConcurrentLinkedQueue<Call>();

    public ElevatorManager() {
        elevator_1 = new Elevator(1);
        elevator_2 = new Elevator(2);
        service = Executors.newFixedThreadPool(2);
    }

    @Override
    public void run() {
        service.execute(elevator_1);
        service.execute(elevator_2);
        while (execution || !calls.isEmpty()) {
            if (elevator_1.getAction().equals("stay") || elevator_2.getAction().equals("stay")) {
                Call curCall = calls.poll();
                if (curCall == null) continue;

                Elevator curElevator = null;
                if (elevator_1.getAction().equals("stay") && elevator_2.getAction().equals("stay")) {
                    if (Math.abs(elevator_1.getCurrentFloor() - curCall.getCurrentFloor()) <= Math.abs(elevator_2.getCurrentFloor() - curCall.getCurrentFloor())) {
                        curElevator = elevator_1;
                    }
                    else {
                        curElevator = elevator_2;
                    }
                }
                else if (elevator_1.getAction().equals("stay")) {
                    curElevator = elevator_1;
                }
                else {
                    curElevator = elevator_2;
                }

                curElevator.setCurCall(curCall);
                String action = curElevator.getCurrentFloor() < curCall.getCurrentFloor() ? "up" : "down";
                curElevator.setAction(action);
            }
        }
        elevator_1.setExecution(false);
        elevator_2.setExecution(false);
        service.shutdown();
    }
}
