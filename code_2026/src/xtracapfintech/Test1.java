package xtracapfintech;

public class Test1 {

	static void printPattern(int[] arr) {
		if (arr == null)	return;
		
		int start = 0, window = 1;
		
		for (int i = 0; i < arr.length; i++) {
			if (i - start == window) {
				System.out.println();
				start = i;
				window++;
			}
			System.out.print(arr[i]);
		}
		
	}
	
	public static void main(String[] args) {
		System.out.println("pattern printing");
		int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0,4, 5, 6, 7, 8, 9, 0};
		printPattern(arr);
	}
	
}
