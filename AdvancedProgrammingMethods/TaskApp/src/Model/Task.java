package Model;

import java.util.Objects;

public abstract class Task {
    private String task_id;

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    public Task(String task_id, String description) {
        this.task_id = task_id;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return task_id + " " + description;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTask_id(), getDescription());
    }

    @Override
    public boolean equals(Object obj){
        if(this  == obj)
            return true;

        if(!(obj instanceof Task))
            return false;

        Task task = (Task) obj;
        return Objects.equals(getTask_id(), task.getTask_id()) && Objects.equals(getDescription(), task.getDescription());
    }


    public String getTask_id() {
        return task_id;
    }

    public abstract void execute();
}
