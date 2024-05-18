import java.util.Random;

public class CallGenerator implements Runnable {
    private final int callNumber;
    private final int floorsNumber;
    public final int interval;

    public CallGenerator(int callNumber, int interval, int floorsNumber) {
        this.callNumber = callNumber;
        this.interval = interval;
        this.floorsNumber = floorsNumber;
    }

    @Override
    public void run() {
        Random random = new Random();
        for (int i = 0; i < callNumber; ++i) {
            delay();
            int currentFloor = random.nextInt(1, floorsNumber);
            int requiredFloor = random.nextInt(1, floorsNumber);
            while (currentFloor == requiredFloor) {
                requiredFloor = random.nextInt(1, floorsNumber);
            }
            String direction;
            if (currentFloor > requiredFloor) direction = "down";
            else direction = "up";

            Call call = new Call(i + 1, currentFloor, requiredFloor, direction);
            System.out.println("> (" + (i + 1) + ") Call from " + currentFloor + " to " + requiredFloor + " has been added to queue");
            ElevatorManager.calls.add(call);
        }
        ElevatorManager.execution = false;
    }

    public void delay() {
        try {
            Thread.sleep(interval * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
