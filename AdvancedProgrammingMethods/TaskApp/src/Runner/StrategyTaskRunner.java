package Runner;

import Containers.Container;
import Containers.Strategy;
import Factory.TaskContainerFactory;
import Model.Task;

public class StrategyTaskRunner implements TaskRunner{
    private Container container;

    public StrategyTaskRunner(Strategy strategy) {
        this.container = TaskContainerFactory.getInstance().createContainer(strategy);
    }


    @Override
    public void executeOneTask() {
        if(!container.isEmpty()){
            container.remove().execute();
        }
    }

    @Override
    public void executeAll() {
         while (!container.isEmpty()){
             this.executeOneTask();
         }
    }

    @Override
    public void addTask(Task t) {
        container.add(t);
    }

    @Override
    public boolean hasTask() {
        return !container.isEmpty();
    }
}
