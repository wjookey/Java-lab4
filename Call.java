public class Call {
    private final int idPerson;
    private final int currentFloor;
    private final int requiredFloor;
    private final String direction;

    public Call (int idPerson, int currentFloor, int requiredFloor, String direction) {
        this.idPerson = idPerson;
        this.currentFloor = currentFloor;
        this.requiredFloor = requiredFloor;
        this.direction = direction;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public int getRequiredFloor() {
        return requiredFloor;
    }

    public String getDirection() {
        return direction;
    }

    public int getIdPerson() {
        return idPerson;
    }
}
