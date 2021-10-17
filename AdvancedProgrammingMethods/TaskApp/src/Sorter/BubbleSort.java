package Sorter;

// https://www.geeksforgeeks.org/bubble-sort/
public class BubbleSort extends AbstractSorter{

    @Override
    public int[] sort(int[] numbers) {
        int n = numbers.length;
        for (int i = 0; i < n-1; i++)
            for (int j = 0; j < n-i-1; j++)
                if (numbers[j] > numbers[j+1])
                {
                    // swap arr[j+1] and arr[j]
                    int temp = numbers[j];
                    numbers[j] = numbers[j+1];
                    numbers[j+1] = temp;
                }
        return numbers;
    }
}
