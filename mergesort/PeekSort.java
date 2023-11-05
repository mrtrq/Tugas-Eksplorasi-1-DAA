package mergesort;


import java.util.Arrays;
import java.util.Random;

import static mergesort.MergesAndRuns.*;

/**
 * Implementation of peeksort as described in the paper.
 *
 * If the subproblem has size at most insertionsortThreshold,
 * it is sorted by straight insertion sort instead of merging.
 * If onlyIncreasingRuns is true, we only find weakly increasing runs
 * while peeking into the middle. That simplifies run detection a bit,
 * but it does not detect descending runs.
 *
 * @author Sebastian Wild (wild@uwaterloo.ca)
 */
public class PeekSort implements Sorter {

	private final int myInsertionsortThreshold;
	private final boolean onlyIncreasingRuns;

	public PeekSort(final int insertionSortThreshold, final boolean onlyIncreasingRuns) {
		this.myInsertionsortThreshold = insertionSortThreshold;
		this.onlyIncreasingRuns = onlyIncreasingRuns;
	}

	@Override
	public void sort(final int[] A, final int left, final int right) {
		insertionSortThreshold = myInsertionsortThreshold;
		if (onlyIncreasingRuns)
			peeksortOnlyIncreasing(A, left, right);
		else
			peeksort(A, left, right);
	}

	public static void peeksort(final int[] a, final int l, final int r) {
		int n = r - l + 1;
		peeksort(a, l, r, l, r, new int[n]);
	}

	public static void peeksortOnlyIncreasing(final int[] a, final int l, final int r) {
		int n = r - l + 1;
		peeksortOnlyIncreasing(a, l, r, l, r, new int[n]);
	}

	private static int insertionSortThreshold = 10;

	public static void peeksort(int[] A, int left, int right, int leftRunEnd, int rightRunStart, final int[] B) {
		if (leftRunEnd == right || rightRunStart == left) return;
		if (right - left + 1 <= insertionSortThreshold) {
			// Possible optimization: use insertionsortRight if right run longer.
			Insertionsort.insertionsort(A, left, right, leftRunEnd - left + 1);
			return;
		}
		int mid = left + ((right - left) >> 1);
		if (mid <= leftRunEnd) {
			// |XXXXXXXX|XX     X|
			peeksort(A, leftRunEnd+1, right, leftRunEnd+1,rightRunStart, B);
			mergeRuns(A, left, leftRunEnd+1, right, B);
		} else if (mid >= rightRunStart) {
			// |XX     X|XXXXXXXX|
			peeksort(A, left, rightRunStart-1, leftRunEnd, rightRunStart-1, B);
			mergeRuns(A, left, rightRunStart, right, B);
		} else {
			// find middle run
			final int i, j;
			if (A[mid] <= A[mid+1]) {
				i = extendWeaklyIncreasingRunLeft(A, mid, left == leftRunEnd ? left : leftRunEnd+1);
				j = mid+1 == rightRunStart ? mid : extendWeaklyIncreasingRunRight(A, mid+1, right == rightRunStart ? right : rightRunStart-1);
			} else {
				i = extendStrictlyDecreasingRunLeft(A, mid, left == leftRunEnd ? left : leftRunEnd+1);
				j = mid+1 == rightRunStart ? mid : extendStrictlyDecreasingRunRight(A, mid+1,right == rightRunStart ? right : rightRunStart-1);
				reverseRange(A, i, j);
			}
			if (i == left && j == right) return;
			if (mid - i < j - mid) {
				// |XX     x|xxxx   X|
				peeksort(A, left, i-1, leftRunEnd, i-1, B);
				peeksort(A, i, right, j, rightRunStart, B);
				mergeRuns(A,left, i, right, B);
			} else {
				// |XX   xxx|x      X|
				peeksort(A, left, j, leftRunEnd, i, B);
				peeksort(A, j+1, right, j+1, rightRunStart, B);
				mergeRuns(A,left, j+1, right, B);
			}
		}
	}

	public static void peeksortOnlyIncreasing(int[] A, int left, int right, int leftRunEnd, int rightRunStart, final int[] B) {
		if (leftRunEnd == right || rightRunStart == left) return;
		if (right - left + 1 <= insertionSortThreshold) {
			Insertionsort.insertionsort(A, left, right);
			return;
		}
		int mid = left + ((right - left) >> 1);
		if (mid <= leftRunEnd) {
			// |XXXXXXXX|XX     X|
			peeksortOnlyIncreasing(A, leftRunEnd+1, right, leftRunEnd+1,rightRunStart, B);
			mergeRuns(A, left, leftRunEnd+1, right, B);
		} else if (mid >= rightRunStart) {
			// |XX     X|XXXXXXXX|
			peeksortOnlyIncreasing(A, left, rightRunStart-1, leftRunEnd, rightRunStart-1, B);
			mergeRuns(A, left, rightRunStart, right, B);
		} else {
			// find middle run
			int i = extendWeaklyIncreasingRunLeft(A, mid, left == leftRunEnd ? left : leftRunEnd+1);
			int j = extendWeaklyIncreasingRunRight(A, mid, right == rightRunStart ? right : rightRunStart-1) ;
			if (i == left && j == right) return;
			if (mid - i < j - mid) {
				// |XX     x|xxxx   X|
				peeksortOnlyIncreasing(A, left, i-1, leftRunEnd, i-1, B);
				peeksortOnlyIncreasing(A, i, right, j, rightRunStart, B);
				mergeRuns(A,left, i, right, B);
			} else {
				// |XX   xxx|x      X|
				peeksortOnlyIncreasing(A, left, j, leftRunEnd, i, B);
				peeksortOnlyIncreasing(A, j+1, right, j+1, rightRunStart, B);
				mergeRuns(A,left, j+1, right, B);
			}
		}
	}

	public static void main(String[] args) {
		Random random = new Random();
		// Small
        int[] randomListS = new int[1000];
        int[] sortedListS = new int[1000];
        int[] reversedListS = new int[1000];

        for (int i = 0; i < 1000; i++) {
            randomListS[i] = random.nextInt(20) + 1;
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
            randomListM[i] = random.nextInt(20) + 1;
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
            randomListL[i] = random.nextInt(20) + 1;
            sortedListL[i] = i;
        }

        for (int i = 0; i < 100000; i++) {
            reversedListL[i] = 100000 - i;
        }
		//System.out.println("Unsorted array: " + Arrays.toString(randomListS));
		//System.out.println("Sorted array: " + Arrays.toString(randomListS));

		// --random--
		 // Small
//		 System.out.println("Small Random:");
//		 peeksort(randomListS, 0, randomListS.length -1);
//		 System.out.println(System.nanoTime());
//
//		 //Medium
//		 System.out.println("Medium Random:");
//		 peeksort(randomListM, 0, randomListM.length -1);
//		 System.out.println(System.nanoTime());
//
//		 //Large
//		 System.out.println("Large Random:");
//		 peeksort(randomListL, 0, randomListL.length -1);
//		 System.out.println(System.nanoTime());
//
//		//--sorted--
//		// Small
//		System.out.println("Small Random:");
//		peeksort(sortedListS, 0, sortedListS.length -1);
//		System.out.println(System.nanoTime());
//
//
//		 //Medium
//		 System.out.println("Medium sorted:");
//		 peeksort(sortedListM, 0, sortedListM.length -1);
//		 System.out.println(System.nanoTime());
//
//		 //Large
//		 System.out.println("Large sorted:");
//		 peeksort(sortedListL, 0, sortedListL.length -1);
//		 System.out.println(System.nanoTime());
//
//		 //--reversed--
//		 // Small
//		 System.out.println("Small Random:");
//		 peeksort(reversedListS, 0, reversedListS.length -1);
//		 System.out.println(System.nanoTime());
//
//		 //Medium
//		 System.out.println("Medium reversed:");
//		 peeksort(reversedListM, 0, reversedListM.length -1);
//		 System.out.println(System.nanoTime());
//
//		 //Large
//		 System.out.println("Large reversed:");
//		 peeksort(reversedListL, 0, reversedListL.length -1);
//		 System.out.println(System.nanoTime());

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

	private static void runAndMeasure(String label, int[] array) {
		System.out.println(label + ":");
		long startTime = System.nanoTime();
		// Call your sorting method here (e.g., peeksort)
		peeksort(array, 0, array.length - 1);
		long endTime = System.nanoTime();
		long memoryUsage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		System.out.println("_____________ " + label + " _____________");
		System.out.println("Time taken: " + (float)(endTime - startTime)/1_000_000 + " ms");
		System.out.println("Memory usage: " + memoryUsage);
	}


	@Override
	public String toString() {
		return getClass().getSimpleName()
				+ "+iscutoff=" + myInsertionsortThreshold
				+ "+onlyIncRuns=" + onlyIncreasingRuns;
	}
}