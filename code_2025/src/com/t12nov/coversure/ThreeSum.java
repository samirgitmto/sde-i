package com.t12nov.coversure;

import java.util.Arrays;

/**
 * return true if the target sum is possible using 3 distinct elements from the input array
 * 2 approaches possible - 1. 2 pointer; 2. HashSet
 * @since 29-11-2025
 */
public class ThreeSum {

	/**
	 * using 2 pointers
	 * Quick sort takes O(n logn) and 2 pointer search takes O(n^2)
	 * Complexity: Time - O(n^2) 	Space - O(1)
	 * @param arr
	 * @param target
	 * @return true if 3 sum possible
	 */
	static boolean isThreeSumPossible(int[] arr, int target) {
		if (arr == null || arr.length < 3)	return false;
		
		Arrays.sort(arr);
		
		for (int i = 0; i < arr.length - 2; i++) {
		    if (i > 0 && arr[i] == arr[i - 1]) continue;

			int current = arr[i];
			int left = i + 1;
			int right = arr.length - 1;
			while (left < right) {
				if (current + arr[left] + arr[right] == target) {
					return true;
				}
				else if (current + arr[left] + arr[right] < target) {
					left++;
				}
				else {
					right--;
				}
			}
		}
		
		return false;
	}
	
	public static void main(String[] args) {
		int[] arr = {10, 3, 5, 17, 3};
		int target= 18;
		System.out.println(isThreeSumPossible(arr, target));
	}
}