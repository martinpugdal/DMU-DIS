package opgave2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    private final static int COUNT_OF_ELEMENTS = 1_000_000;

    /*
     * køretid for 1_000_000 elementer uden tråde
     * 746
     * 741
     * 664
     * 743
     * 788
     */

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < COUNT_OF_ELEMENTS; i++) {
            list.add(Math.abs(r.nextInt() % 10000));
        }
        FletteSortering sort = new FletteSortering();
        //mergeSort(sort, list);
        parallelMergeSort(sort, list);
    }

    private static void mergeSort(FletteSortering sort, List<Integer> list) {
        List<Integer> listCopy = new ArrayList<>(list);
        long l1, l2;
        l1 = System.nanoTime();
        sort.mergesort(listCopy, 0, listCopy.size() - 1);
        l2 = System.nanoTime();
        System.out.println();
        System.out.println("Koeretiden var " + (l2 - l1) / 1000000);
        System.out.println();
        System.out.println(isSorted(listCopy));
    }

    private static void parallelMergeSort(FletteSortering sort, List<Integer> list) {
        List<Integer> listCopy = new ArrayList<>(list);
        long l1, l2;
        l1 = System.nanoTime();
        sort.parallelMergeSort(listCopy, 0, listCopy.size() - 1);
        l2 = System.nanoTime();
        System.out.println();
        System.out.println("Koeretiden var " + (l2 - l1) / 1000000);
        System.out.println();
        System.out.println(isSorted(listCopy));
    }

    public static boolean isSorted(List<Integer> list) {
        int size = list.size();
        for (int i = 0; i < size - 1; i++) {
            if (list.get(i).compareTo(list.get(i + 1)) > 0) {
                return false;
            }
        }
        return true;
    }
}