package com.hx.practice.back;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BackTrackV2 {








    public int[] twoSum(int[] nums, int target) {
        if (nums == null || nums.length<1) return null;
        Arrays.sort(nums);
        int left = 0, right = nums.length-1;
        while (left < right) {
            int sum = nums[left] + nums[right];
            if (sum == target) {
                return new int[]{left,right};
            }else if (sum < target) {
                left++;
            } else if (sum > target) {
                right--;

            }
        }
        return null;
    }


    public boolean isPalindrome(String s) {
        int left = 0;
        int right = s.length()-1;
        while(left<right){
            if(s.charAt(left) != s.charAt(right)) return false;
            left++;
            right--;
        }
        return true;

    }




    public static void main(String[] args) {
//        int[] ints = new BackTrackV2().twoSum(new int[]{3,2,4}, 6);
        String a = "abc";
        boolean palindrome = new BackTrackV2().isPalindrome(a);
        System.out.println(palindrome);
    }


    private final ThreadPoolExecutor EXECUTOR_SERVICE = new ThreadPoolExecutor(5, 10, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100), new ThreadPoolExecutor.CallerRunsPolicy());








}
