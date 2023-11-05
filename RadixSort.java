import java.util.Arrays;
import java.util.Random;

public class RadixSort {

    public static long memoryUsage;

    // Counting Sort implementation for a single digit place value
    private static void countingSort(int[] arr, int place) {
        int maxVal = Arrays.stream(arr).max().getAsInt();
        int[] output = new int[arr.length];
        int[] count = new int[maxVal + 1];

        // Count the frequency of each digit at the specified place value
        for (int num : arr) {
            int index = (num / place) % 10;
            count[index]++;
        }

        // Calculate the cumulative count of each digit
        for (int i = 1; i < count.length; i++) {
            count[i] += count[i - 1];
        }

        // Build the output array
        for (int i = arr.length - 1; i >= 0; i--) {
            int index = (arr[i] / place) % 10;
            output[count[index] - 1] = arr[i];
            count[index]--;
        }

        // Copy the sorted elements back to the original array
        System.arraycopy(output, 0, arr, 0, arr.length);
    }

    // Radix Sort implementation using Counting Sort
    public static void radixSort(int[] arr) {
        int maxVal = Arrays.stream(arr).max().getAsInt();
        int maxDigits = (int) Math.log10(maxVal) + 1;

        int place = 1;
        for (int i = 0; i < maxDigits; i++) {
            countingSort(arr, place);
            place *= 10;
        }
    }

    public static void main(String[] args) {

        // Generate random number
        Random random = new Random();

        // Small
        int[] randomListS = new int[1000];
        int[] sortedListS = new int[1000];
        int[] reversedListS = new int[1000];

        for (int i = 0; i < 1000; i++) {
            randomListS[i] = random.nextInt(25) + 1;
            sortedListS[i] = i;
        }

        for (int i = 0; i < 1000; i++) {
            reversedListS[i] = 1000 - i;
        }

        // Medium
        int[] randomListM = new int[10000];
        int[] sortedListM = new int[10000];
        int[] reversedListM = new int[10000];

        for (int i = 0; i < 10000; i++) {
            randomListM[i] = random.nextInt(25) + 1;
            sortedListM[i] = i;
        }

        for (int i = 0; i < 10000; i++) {
            reversedListM[i] = 10000 - i;
        }

        // Large
        int[] randomListL = new int[100000];
        int[] sortedListL = new int[100000];
        int[] reversedListL = new int[100000];

        for (int i = 0; i < 100000; i++) {
            randomListL[i] = random.nextInt(25) + 1;
            sortedListL[i] = i;
        }

        for (int i = 0; i < 100000; i++) {
            reversedListL[i] = 100000 - i;
        }
//        System.out.println("_____________Random____________");

        //System.out.println("Init array: " + Arrays.toString(randomListS));

//        //--random
//         radixSort(randomListS);
//         //System.out.println("Sorted array: " + Arrays.toString(randomListS));
//         System.out.println("Time to sort S: " + System.nanoTime());
//        memoryUsage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
//        System.out.println("Memory usage: " + memoryUsage);
//
//         radixSort(randomListM);
//         System.out.println("Time to sort M: " + System.nanoTime());
//        memoryUsage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
//        System.out.println("Memory usage: " + memoryUsage);
//
//         radixSort(randomListL);
//         System.out.println("Time to sort L: " + System.nanoTime());
//        memoryUsage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
//        System.out.println("Memory usage: " + memoryUsage);
//
//        System.out.println("_____________Sorted___________");
//
//
//        //--sorted
//        radixSort(sortedListS);
//        System.out.println("Time to sort S: " + System.nanoTime());
//        memoryUsage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
//        System.out.println("Memory usage: " + memoryUsage);
//
//
//         radixSort(sortedListM);
//         System.out.println("Time to sort M: " + System.nanoTime());
//         memoryUsage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
//         System.out.println("Memory usage: " + memoryUsage);
//
//         radixSort(sortedListL);
//         System.out.println("Time to sort L: " + System.nanoTime());
//        memoryUsage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
//        System.out.println("Memory usage: " + memoryUsage);
//
//        System.out.println("_____________Reversed___________");
//
//
//
//         //--reversed
//         radixSort(reversedListS);
//         System.out.println("Time to sort S: " + System.nanoTime());
//        memoryUsage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
//        System.out.println("Memory usage: " + memoryUsage);
//
//         radixSort(reversedListM);
//         System.out.println("Time to sort M: " + System.nanoTime());
//        memoryUsage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
//        System.out.println("Memory usage: " + memoryUsage);
//
//         radixSort(reversedListL);
//         System.out.println("Time to sort L: " + System.nanoTime());
//        memoryUsage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
//        System.out.println("Memory usage: " + memoryUsage);

        runAndMeasure("Random S", randomListS);
        runAndMeasure("Random M", randomListM);
        runAndMeasure("Random L", randomListL);

        runAndMeasure("Sorted S", sortedListS);
        runAndMeasure("Sorted M", sortedListM);
        runAndMeasure("Sorted L", sortedListL);

        runAndMeasure("Reversed S", reversedListS);
        runAndMeasure("Reversed M", reversedListM);
        runAndMeasure("Reversed L", reversedListL);

    }
    public static void runAndMeasure(String label, int[] list) {
        long startTime = System.nanoTime();
        radixSort(list);
        long endTime = System.nanoTime();
        long memoryUsage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        System.out.println("_____________ " + label + " _____________");
        System.out.println("Time to sort " + label + ": " + (float)(endTime - startTime)/1_000_000 + " miliseconds");
        System.out.println("Memory usage: " + memoryUsage);
    }

}
