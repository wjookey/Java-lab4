import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class Elevator implements Runnable {
    private final int elevatorID;
    private int currentFloor = 1;
    private final Hashtable<Integer, Integer> stops;
    private volatile String action = "stay";
    private Call curCall = null;
    private boolean execution = true;

    Elevator(int elevatorID) {
        this.elevatorID = elevatorID;
        this.stops = new Hashtable<>();
    }

    @Override
    public void run() {
        while (execution || !stops.isEmpty()) {
            if (action.equals("stay")) continue;

            System.out.println("> Elevator " + elevatorID + " has received call on floor " + curCall.getCurrentFloor());
            delay();
            while (currentFloor != curCall.getCurrentFloor()) {
                leavePassengers();
                getPassengers();
                delay();
                updateFloor();
            }
            System.out.println("> Elevator " + elevatorID + " took passenger " + curCall.getIdPerson());
            delay();
            stops.put(curCall.getIdPerson(), curCall.getRequiredFloor());
            action = currentFloor > curCall.getRequiredFloor() ? "down" : "up";
            while (true) {
                leavePassengers();
                getPassengers();
                delay();
                if (stops.isEmpty()) break;
                updateFloor();
            }
            curCall = null; action = "stay";
        }
    }

    private void leavePassengers() {
        Iterator<Map.Entry<Integer, Integer>> iter = stops.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer, Integer> curStop = iter.next();
            if (curStop.getValue() == currentFloor) {
                System.out.println("> Elevator " + elevatorID + " has delivered person " +
                        curStop.getKey() + " to the floor " + currentFloor);
                delay();
                iter.remove();
            }
        }
    }

    private void getPassengers() {
        synchronized (ElevatorManager.calls) {
            Iterator<Call> iter = ElevatorManager.calls.iterator();
            while (iter.hasNext()) {
                Call call = iter.next();
                if (currentFloor == call.getCurrentFloor() && call.getDirection().equals(action)
                        && call.getDirection().equals(curCall.getDirection())) {
                    System.out.println("> Elevator " + elevatorID + " took passenger " + call.getIdPerson());
                    delay();
                    iter.remove();
                    stops.put(call.getIdPerson(), call.getRequiredFloor());
                }
            }
        }
    }

    private synchronized void updateFloor() {
        if (action.equals("up")) currentFloor++;
        else if (action.equals("down")) currentFloor--;
    }

    private void delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void setExecution(boolean state) {
        this.execution = state;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setCurCall(Call curCall) {
        this.curCall = curCall;
    }

    public Call getCurCall() {
        return curCall;
    }
}
