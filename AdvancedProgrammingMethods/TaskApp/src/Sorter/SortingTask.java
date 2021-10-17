package Sorter;

import Model.Task;

import java.util.Arrays;

public class SortingTask extends Task {


    public SortingTask(String task_id, String description) {
        super(task_id, description);
    }

    @Override
    public void execute() {}

    public void execute(int[] numbers, SortingStrategy strategy) {
        SorterFactory sorterFactory = SorterFactory.getInstance();
        AbstractSorter abstractSorter = sorterFactory.createSorter(strategy);
        int[] sortedNumbers = abstractSorter.sort(numbers);
        System.out.println(Arrays.toString(numbers));
    }
}
