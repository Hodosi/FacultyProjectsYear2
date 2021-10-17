package Sorter;

public interface Sorter {
    AbstractSorter createSorter(SortingStrategy strategy);
}
