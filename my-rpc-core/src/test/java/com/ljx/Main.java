package com.ljx;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        int[] nums = {96117,80613,72852,-70020,-66572};
        int k = 9520;
        System.out.println(maxSubarraySum(nums, k));
    }

    static long[] copy(int[] nums) {
        long[] res = new long[nums.length];
        for (int i = 0; i < nums.length; i ++) {
            res[i] = nums[i];
        }
        return res;
    }

    static long maxSubarraySum(int[] nums, int k) {
        int n = nums.length;
        long[] back = copy(nums);

        for (int i = 0; i < n; i ++) {
            back[i] *= k;
        }
        for (int i = 1; i < n; i ++) {
            back[i] += back[i - 1];
        }
        long mn = 0;
        long res = -Long.MAX_VALUE;
        for (int i = 0; i < n; i ++) {
            res = Math.max(res, back[i] - mn);
            mn = Math.min(mn, back[i]);
        }

        back = copy(nums);
        for (int i = 0; i < n; i ++) {
            if (back[i] > 0) {
                back[i] = (int) Math.floor((double) back[i] / k);
            } else {
                back[i] = (int) Math.ceil((double) back[i] / k);
            }
        }
        for (int i = 1; i < n; i ++) {
            back[i] += back[i - 1];
        }
        mn = 0;
        long ans = -Long.MAX_VALUE;
        for (int i = 0; i < n; i ++) {
            ans = Math.max(ans, back[i] - mn);
            mn = Math.min(mn, back[i]);
        }

        return Math.max(res, ans);
    }
}
