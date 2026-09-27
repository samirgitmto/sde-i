package com.t6ag.ppltch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
You are given an m x n integer array grid. There is a robot initially located at the top-left corner (i.e., grid[0][0]).
The robot tries to move to the bottom-right corner (i.e., grid[m - 1][n - 1]). The robot can only move either down or right at any point in time.
An obstacle and space are marked as 1 or 0 respectively in grid. A path that the robot takes cannot include any square that is an obstacle.
Return the number of possible unique paths that the robot can take to reach the bottom-right corner.
The testcases are generated so that the answer will be less than or equal to 2 * 109.
 
Example 1:
 
Input: obstacleGrid = [[0,0,0],[0,1,0],[0,0,0]]
Output: 2
Explanation: There is one obstacle in the middle of the 3x3 grid above.
There are two ways to reach the bottom-right corner:
1. Right -> Right -> Down -> Down
2. Down -> Down -> Right -> Right
Example 2:
 
Input: obstacleGrid = [[0,1],[0,0]]
Output: 1
 
 */

public class Driver {

	static int getNumberofWays0(int[][] arr) {
		
		int m = arr.length-1;
		int n = arr[0].length-1;
		int count = 0;
		
		for (int i=0; i<=m; i++) {		// row
			
			for (int j=0; j<=n; j++) {	// col
				if (arr[i][j]==1) {	
					continue;
				}
				if (i==m && j==n) {
					count++;
				}
			}	
		}
		
		return count;
	}
	static int getNumberofWays(int[][] arr) {
		if (arr[0][0] == 1)
			return 0;
		
		int rows = arr.length;
		int cols = arr[0].length;
		
//		prepare dp array first
		int[][] dp = new int[rows][cols];
		dp[0][0] = 1;
		
		// prepare 1st col
		for (int i=1; i<rows; i++) {
			if (arr[i][0]==1) {
				dp[i][0] = 0;
			}
			else {
				dp[i][0] = dp[i-1][0];
			}
		}
		
		// prepare 1st row
		for (int j = 1; j < cols; j++) {
			if (arr[0][j]==1) {
				dp[0][j] = 0;
			}
			else {
				dp[0][j] = dp[0][j-1];
			}
		}
		
		// prepare the remaining - number of ways from top + left as robot can only move right or down
		for (int k=1; k<rows; k++) {
			for (int l=1; l<cols; l++) {
				if (arr[k][l]==1) {
					dp[k][l] = 0;
				}
				else {
					dp[k][l] = dp[k-1][l] + dp[k][l-1];
				}
			}
		}
		
		for (int[] is : dp) {
			System.err.println(Arrays.toString(is));
		}
		
		// count the number of ways
		int count = dp[rows-1][cols-1];
		return count;
	}
	
	public static void main(String[] args) {
		int[][] arr = {{0,0,0}, {0,1,0}, {0,0,0}};
//		System.out.println(getNumberofWays0(arr));
		System.out.println(getNumberofWays(arr));
		
		String s = "abcabcbb";
		System.out.println(getLongestLength(s));
		String s2 = "bbbbb";
		System.out.println(getLongestLength(s2));
//		String s3 = "pwwkew";
		String s3 = "aab";
		System.out.println(getLongestLength(s3));
		
		String s6 = "abcabcbba";
		char[] chars2 = s6.toCharArray();
		List<Character> list = new ArrayList<Character>();
		for (char c : chars2) {
			list.add(c);
		}
		
//		list.stream().collect()
		// Find duplicates: characters that appear more than once
        List<Character> duplicates = list.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue() > 1)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
        
        System.out.println("Duplicate characters: " + duplicates);
	}
	
	static int getLongestLength(String str) {
		int maxLength = -1;
		
		char[] chars = str.toCharArray();
		Set<Character> set = new HashSet<Character>();
		int currentLength = 0;
		for (int i = 0; i < chars.length; i++) {
			Character current = chars[i];
			if (set.contains(current)) {
				maxLength = Math.max(maxLength, currentLength);
				set.clear();
				currentLength = 0;
			}
//			else {
				set.add(current);
				currentLength++;
//			}	
		}
		maxLength = Math.max(maxLength, currentLength);
 		return maxLength;
	}
	
/*
Given a string s, find the length of the longest substring without duplicate characters.
 
Example 1:
Input: s = "abcabcbb"
Output: 3
Explanation: The answer is "abc", with the length of 3.


Example 2:
Input: s = "bbbbb"
Output: 1
Explanation: The answer is "b", with the length of 1.


Example 3:
Input: s = "pwwkew"
Output: 3
Explanation: The answer is "wke", with the length of 3.
Notice that the answer must be a substring, "pwke" is a subsequence and not a substring.	
 */
	
}