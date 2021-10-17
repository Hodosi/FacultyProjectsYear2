package Sorter;

public class SorterFactory implements Sorter {

    private static SorterFactory instance = null;

    private SorterFactory(){}

    public static SorterFactory getInstance(){
        if(instance == null){
            instance = new SorterFactory();
        }
        return instance;
    }

    @Override
    public AbstractSorter createSorter(SortingStrategy strategy) {
        if(strategy == SortingStrategy.BUBBLE){
            return new BubbleSort();
        }
        else if(strategy == SortingStrategy.MERGE) {
            return new MergeSort();
        }
        return null;
    }
}
