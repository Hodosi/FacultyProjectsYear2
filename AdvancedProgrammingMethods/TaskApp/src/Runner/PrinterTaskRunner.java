package Runner;

import Utils.Constants;

import java.time.LocalDateTime;

public class PrinterTaskRunner extends AbstractTaskRunner{
    public PrinterTaskRunner(TaskRunner taskRunner){
        super(taskRunner);
    }

    @Override
    public void executeOneTask() {
        this.taskRunner.executeOneTask();
        decorateExecuteOneTask();
    }

    public void decorateExecuteOneTask(){
        System.out.println("Task executed on: " + LocalDateTime.now().format(Constants.DATE_TIME_FORMATTER));
    }
}
