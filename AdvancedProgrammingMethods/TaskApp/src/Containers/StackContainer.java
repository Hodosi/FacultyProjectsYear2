package Containers;

import Model.Task;

//public class StackContainer implements Container{
public class StackContainer extends AbstractContainer{
//    private Task[] tasks;
//    private int size;
//
//    public StackContainer() {
//        this.tasks = new Task[INITIAL_TASK_SIZE];
//        this.size = 0;
//    }

    public StackContainer(){
        super();
    }

    @Override
    public Task remove() {
        if(!isEmpty()){
            size--;
            return tasks[size];
        }
        return null;
    }

//    @Override
//    public void add(Task task) {
//        int l = tasks.length;
//        if(l == size) {
//            Task t[] = new Task[l * 2];
//            System.arraycopy(tasks, 0, t, 0, l);
//            tasks = t;
//        }
//        tasks[size] = task;
//        size++;
//
//    }
//
//    @Override
//    public int size() {
//        return size;
//    }
//
//
//    @Override
//    public boolean isEmpty() {
//        return size == 0;
//    }
}
