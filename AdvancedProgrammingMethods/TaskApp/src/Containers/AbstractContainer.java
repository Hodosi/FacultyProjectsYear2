package Containers;

import Model.Task;

import static Utils.Constants.INITIAL_TASK_SIZE;

public abstract class AbstractContainer implements Container{
    protected Task[] tasks;
    protected int size;

    public AbstractContainer() {
        this.tasks = new Task[INITIAL_TASK_SIZE];
        this.size = 0;
    }

    public abstract Task remove();

    public void add(Task task) {
        int l = tasks.length;
        if(l == size) {
            Task t[] = new Task[l * 2];
            System.arraycopy(tasks, 0, t, 0, l);
            tasks = t;
        }
        tasks[size] = task;
        size++;

    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
