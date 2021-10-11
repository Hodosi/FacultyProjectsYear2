import Containers.Strategy;
import Model.MessageTask;
import Runner.PrinterTaskRunner;
import Runner.StrategyTaskRunner;

import java.time.LocalDateTime;

public class run {

    public static MessageTask[] createMessageTaskArray(){
        MessageTask t1 = new MessageTask("1", "Task1", "message", "me", "you", LocalDateTime.now());
        MessageTask t2 = new MessageTask("2", "Task2", "message", "me", "you", LocalDateTime.now());
        MessageTask t3 = new MessageTask("3", "Task3", "message", "me", "you", LocalDateTime.now());
        return new MessageTask[]{
                t1, t2, t3
        };
    }

    /**
     * Create and executes message tasks
     * @param args - should contain strategy
     */
    public static void main(String[] args) {
//        MessageTask msg = new MessageTask("1", "Task1", "message", "me", "you", LocalDateTime.now());
//        msg.execute();


        MessageTask[] messageTaskArray = createMessageTaskArray();
        StrategyTaskRunner task_runner = new StrategyTaskRunner(Strategy.LIFO);
        for(MessageTask t: messageTaskArray){
            //t.execute();
            task_runner.addTask(t);
        }
        PrinterTaskRunner printerTaskRunner = new PrinterTaskRunner(task_runner);
        printerTaskRunner.executeAll();
//        task_runner.executeAll();
    }
}
