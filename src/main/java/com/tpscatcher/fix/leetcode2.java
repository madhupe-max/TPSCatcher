package com.tpscatcher.fix;

import java.util.HashMap;
import java.util.Map;

public class leetcode2 {
    //leetcode 583 
    public int minimumDeleteSum(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();

        // dp[i][j] = min ASCII delete sum to equalize s1[0..i-1] and s2[0..j-1].
        int[][] dp = new int[m + 1][n + 1];

        // Base case: equalize a prefix of s1 with empty s2 by deleting all of s1's prefix.
        for (int i = 1; i <= m; i++) {
            dp[i][0] = dp[i - 1][0] + s1.charAt(i - 1);
        }
        // Symmetric for s2 vs empty s1.
        for (int j = 1; j <= n; j++) {
            dp[0][j] = dp[0][j - 1] + s2.charAt(j - 1);
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    // Characters match — no deletion needed for this pair.
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Either delete s1[i-1] or delete s2[j-1]; pick the cheaper.
                    int deleteFromS1 = dp[i - 1][j] + s1.charAt(i - 1);
                    int deleteFromS2 = dp[i][j - 1] + s2.charAt(j - 1);
                    dp[i][j] = Math.min(deleteFromS1, deleteFromS2);
                }
            }
        }

        return dp[m][n];
    }
    //leetcode 3
    public int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> lastSeen = new HashMap<>();

        int left = 0;
        int maxLength = 0;

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);

            if (lastSeen.containsKey(c) && lastSeen.get(c) >= left) {
                left = lastSeen.get(c) + 1;
            }

            lastSeen.put(c, right);
            maxLength = Math.max(maxLength, right - left + 1);
        }

        return maxLength;
    }
    //leetcode 12
    public String intToRoman(int num) {
        String roman= "";

        while (num>0) {
            if (num >= 1000) {
                roman += "M";
                num -= 1000;
            } else if (num >= 900) {
                roman += "CM";
                num -= 900;
            } else if (num >= 500) {
                roman += "D";
                num -= 500;
            } else if (num >= 400) {
                roman += "CD";
                num -= 400;
            } else if (num >= 100) {
                roman += "C";
                num -= 100;
            } else if (num >= 90) {
                roman += "XC";
                num -= 90;
            } else if (num >= 50) {
                roman += "L";
                num -= 50;
            } else if (num >= 40) {
                roman += "XL";
                num -= 40;
            } else if (num >= 10) {
                roman += "X";
                num -= 10;
            } else if (num >= 9) {
                roman += "IX";
                num -= 9;
            } else if (num >= 5) {
                roman += "V";
                num -= 5;
            } else if (num >= 4) {
                roman += "IV";
                num -= 4;
            } else {
                roman += "I";
                num -= 1;
            }
        }

        return roman;
        
    }


    
    
    private static final int[]    VALUES  = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    private static final String[] SYMBOLS = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

    public String intToRoman2(int num) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < VALUES.length; i++) {
            // Repeatedly subtract the current denomination while it still fits.
            while (num >= VALUES[i]) {
                sb.append(SYMBOLS[i]);
                num -= VALUES[i];
            }
        }
        return sb.toString();
    }
    
    

    public static void main(String[] args) {
        leetcode2 m = new leetcode2();
        String s1 = "sea";
        String s2 = "eat";
        System.out.println(m.minimumDeleteSum(s1, s2));
        System.out.println(m.lengthOfLongestSubstring("abcabcbb"));
        System.out.println(m.intToRoman2(1001));
    }
}
