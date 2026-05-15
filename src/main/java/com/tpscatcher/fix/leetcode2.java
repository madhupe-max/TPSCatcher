package com.tpscatcher.fix;

import java.util.HashMap;
import java.util.Map;

public class leetcode2 {
     
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

public int lengthOfLongestSubstring(String s) {
        Map <String, Integer> seen = new HashMap<>();
        int len=0;
        for(int i=0; i< s.length(); i++){
            if(seen.containsKey(""+s.chartAt(i))){
                return seen.length;
            }
            seen.put(s.charAt(i),i);
        }
        return len;
    }

    public static void main(String[] args) {
        leetcode2 m = new leetcode2();
        String s1 = "sea";
        String s2 = "eat";
        System.out.println(m.minimumDeleteSum(s1, s2));
}
}
