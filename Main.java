import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the number of floors: ");
        int floorsNumber = sc.nextInt();
        System.out.print("Enter the number of elevator calls: ");
        int callNumber = sc.nextInt();
        System.out.print("Enter interval between elevator calls (seconds): ");
        int timeInterval = sc.nextInt();
        System.out.print("\n");

        CallGenerator callGenerator = new CallGenerator(callNumber, timeInterval, floorsNumber);
        Thread generator = new Thread(callGenerator);
        ElevatorManager elevatorManager = new ElevatorManager();
        Thread manager = new Thread(elevatorManager);

        ExecutorService service = Executors.newFixedThreadPool(2);
        service.execute(generator);
        service.execute(manager);
        service.shutdown();
    }
}
