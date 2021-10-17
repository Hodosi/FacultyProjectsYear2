package Runner;

public class DelayTaskRunner extends AbstractTaskRunner{
    public DelayTaskRunner(TaskRunner taskRunner) {
        super(taskRunner);
    }

    @Override
    public void executeOneTask() {
        try {
            this.taskRunner.executeOneTask();
            Thread.sleep(2000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
