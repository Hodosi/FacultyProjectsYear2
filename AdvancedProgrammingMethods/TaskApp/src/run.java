import Containers.Strategy;
import Model.MessageTask;
import Runner.DelayTaskRunner;
import Runner.PrinterTaskRunner;
import Runner.StrategyTaskRunner;
import Sorter.SortingStrategy;
import Sorter.SortingTask;

import java.time.LocalDateTime;
import java.util.Scanner;

import static Utils.Constants.DATE_TIME_FORMATTER;

public class run {

    public static MessageTask[] createMessageTaskArray(){
        MessageTask t1 = new MessageTask("1", "Task1", "message", "me", "you", LocalDateTime.now());
        MessageTask t2 = new MessageTask("2", "Task2", "message", "me", "you", LocalDateTime.now());
        MessageTask t3 = new MessageTask("3", "Task3", "message", "me", "you", LocalDateTime.now());
        MessageTask t4 = new MessageTask("4", "Task4", "message", "me", "you", LocalDateTime.now());
        MessageTask t5 = new MessageTask("5", "Task5", "message", "me", "you", LocalDateTime.now());
        return new MessageTask[]{
                t1, t2, t3, t4, t5
        };
    }

    public static void printMessageTaskArray(MessageTask[] messageTasks){
        for (MessageTask t : messageTasks){
            System.out.print("id=");
            System.out.print(t.getTask_id());
            System.out.print("|");
            System.out.print("description=");
            System.out.print(t.getDescription());
            System.out.print("|");
            System.out.print("message=");
            System.out.print(t.getMessage());
            System.out.print("|");
            System.out.print("from=");
            System.out.print(t.getFrom());
            System.out.print("|");
            System.out.print("to=");
            System.out.print(t.getTo());
            System.out.print("|");
            System.out.print("date=");
            System.out.print(t.getDate().format(DATE_TIME_FORMATTER));
            System.out.println();
        }
    }

    /**
     * Create and executes message tasks
     * @param args - should contain strategy
     */
    public static void main(String[] args) {
        System.out.println();
        System.out.println("Sorting\n");

        SortingTask sortingTask = new SortingTask("","");
        int[] sorting_array = new int[]{2,5,4,3,1};
        sortingTask.execute(sorting_array.clone(), SortingStrategy.BUBBLE);
        sortingTask.execute(sorting_array.clone(), SortingStrategy.MERGE);

        System.out.println("\n");


        System.out.println("Print message tasks\n");

        MessageTask[] messageTaskArray = createMessageTaskArray();
        printMessageTaskArray(messageTaskArray);

        System.out.println("\n");


        System.out.println("LIFO Strategy task runner\n");

        StrategyTaskRunner lifoTaskRunner = new StrategyTaskRunner(Strategy.LIFO);
        for(MessageTask t: messageTaskArray){
            lifoTaskRunner.addTask(t);
        }
        lifoTaskRunner.executeAll();

        System.out.println("\n");


        System.out.println("FIFO Strategy task runner\n");

        StrategyTaskRunner fifoTaskRunner = new StrategyTaskRunner(Strategy.FIFO);
        for(MessageTask t: messageTaskArray){
            fifoTaskRunner.addTask(t);
        }
        fifoTaskRunner.executeAll();

        System.out.println("\n");


        Scanner in = new Scanner(System.in);
        Strategy consoleStrategy;
        while (true){
            System.out.println("Choose the task runner strategy(LIFO OR FIFO).");
            String inStrategy = in.nextLine();
            if(inStrategy.equals("LIFO")){
                consoleStrategy = Strategy.LIFO;
                break;
            }
            if(inStrategy.equals("FIFO")){
                consoleStrategy = Strategy.FIFO;
                break;
            }
            System.out.println("Incorrect strategy, please try again.");
        }
        in.close();

        StrategyTaskRunner taskRunner = new StrategyTaskRunner(consoleStrategy);
        for(MessageTask t: messageTaskArray){
            taskRunner.addTask(t);
        }

        System.out.println("Delay task runner\n");

        DelayTaskRunner delayTaskRunner = new DelayTaskRunner(taskRunner);
        delayTaskRunner.executeAll();

        System.out.println("\n");


        for(MessageTask t: messageTaskArray){
            taskRunner.addTask(t);
        }

        System.out.println("Printer task runner\n");

        PrinterTaskRunner printerTaskRunner = new PrinterTaskRunner(taskRunner);
        printerTaskRunner.executeAll();

        System.out.println("\n");
    }
}
