package com.tpscatcher.fix;

public class leetcode {
     
    public int minMoves(int[] nums, int limit) {
        int n = nums.length;
        // diff[T] holds the change in "discount from baseline 2" at target T.
        // We need indices up to 2*limit + 1, so size 2*limit + 2.
        int[] diff = new int[2 * limit + 2];

        // Baseline: every pair costs 2 for any target T. Total baseline = 2 * (n/2) = n.
        // We then accumulate negative deltas for ranges/points where cost drops to 1 or 0.
        for (int i = 0; i < n / 2; i++) {
            int a = nums[i];
            int b = nums[n - 1 - i];
            int lo = Math.min(a, b);
            int hi = Math.max(a, b);
            int s = a + b;

            // Range [lo+1, hi+limit]: cost drops from 2 to 1 (one element change suffices).
            diff[lo + 1]      -= 1;
            diff[hi + limit + 1] += 1;

            // Point T = s: cost drops further from 1 to 0 (no change needed).
            diff[s]     -= 1;
            diff[s + 1] += 1;
        }

        // Sweep T from 2 to 2*limit, maintaining running discount.
        // Total moves at target T = baseline (n) + running discount.
        int runningDiscount = 0;
        int minMoves = Integer.MAX_VALUE;
        for (int T = 2; T <= 2 * limit; T++) {
            runningDiscount += diff[T];
            minMoves = Math.min(minMoves, n + runningDiscount);
        }
        return minMoves;
    }

    public static void main(String[] args) {
        leetcode m = new leetcode();
        int[] nums = {1,4,3,4};
        int limit = 4;
        System.out.println(m.minMoves(nums, limit));
}
}
