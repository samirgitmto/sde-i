package xtracapfintech;

public class Test3 {

	static char getMaxRepeatChar(String str) {
		if (str == null || str.isEmpty()) return '0';
		
		int maxCount = 1;
		int currMax = 1;
		char prev = str.charAt(0);
		char result = prev;
		for (int i = 1; i < str.length(); i++) {
			if (i == str.length() - 1) {   // Edge Case
				if (str.charAt(i) == prev) {
					currMax++;
				}
				if (currMax > maxCount) {
					return str.charAt(i);
				}
				else
					return result;
			}
			if (str.charAt(i) == prev) {
				currMax++;
			}
			else {
				if (currMax > maxCount) {
//					result = str.charAt(i);
					result = prev;
					maxCount = currMax;
				}
				
				currMax = 1;
			}
			
//			maxCount = Math.max(currMax, maxCount);
			prev = str.charAt(i);
		}
		
		return result;
	}
	
	public static void main(String[] args) {
//		char res = getMaxRepeatChar("aaaabbcccccccdddddddddddddd");
		char res = getMaxRepeatChar("aabbbaa");
		System.out.println(res);
	}
}
