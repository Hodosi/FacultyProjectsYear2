package Containers;

import Model.Task;

public interface Container {
    Task remove();
    void add(Task task);
    int size();
    boolean isEmpty();
}
