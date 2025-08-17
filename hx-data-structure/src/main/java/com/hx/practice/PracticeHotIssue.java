package com.hx.practice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PracticeHotIssue {

    /**
     * 合并两个有序数组
     */
    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        for (int i = 0; i <n; ++i) {
            nums1[m + i] = nums2[i];
        }
        Arrays.sort(nums1);
    }




//    public static void merge(int[] nums1, int m, int[] nums2, int n) {
//        int p1 = 0, p2 = 0;
//        int[] sorted = new int[m + n];
//        int cur;
//        while (p1 < m || p2 < n) {
//            if (p1 == m) {
//                cur = nums2[p2++];
//            } else if (p2 == n) {
//                cur = nums1[p1++];
//            } else if (nums1[p1] < nums2[p2]) {
//                cur = nums1[p1++];
//            } else {
//                cur = nums2[p2++];
//            }
//            sorted[p1 + p2 - 1] = cur;
//        }
//        for (int i = 0; i != m + n; ++i) {
//            nums1[i] = sorted[i];
//        }
//    }


    /**
     * 移除元素
     * @param args
     */
    public static int removeArrElement(int[] nums, int val) {
        int temp = 0;
        for (int num : nums) {
            if (num != val) {
               nums[temp] = num;
               temp++;
            }
        }
        return temp;
    }

    public static int[]  remove(int[] nums, int val) {
        int temp = 0;
            for (int num = 0;num< nums.length;num++) {
            if (nums[num] !=val){
                nums[temp] = nums[num];
                temp++;
            }
        }
        return Arrays.copyOf(nums,temp);

    }




    public static int[] removeDuplicates(int[] nums) {
        int n = nums.length;
        int j = 0;
        for (int i = 0; i < n; i++) {
            if (nums[i] != nums[j]) {
                nums[++j] = nums[i];
            }
        }
        return Arrays.copyOf(nums,j +1);
    }


    // 翻转数组中 [start, end] 区间的元素
    private static void reverse(int[] nums, int start, int end) {
        while (start < end) {
            int temp = nums[start];
            nums[start] = nums[end];
            nums[end] = temp;
            start++;
            end--;
        }
    }

    /**
     * 从指定位置开始轮转
     * @param nums
     * @param k
     */
    public static void reverseV2(int[] nums, int k) {
        k = k % nums.length;
        reverse(nums, 0, nums.length - 1);
        reverse(nums, 0, k-1);
        reverse(nums, k, nums.length - 1);
    }


    /**
     * 两数之和
     * @param nums
     * @param target
     * @return
     */
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];

            if (map.containsKey(complement)) {
                return new int[]{map.get(complement), i};
            }

            map.put(nums[i], i);
        }

        return new int[]{};
    }


    public static void main(String[] args) {
        int[] a1 = {1, 2,3,0,0,0};
        int[] a2 = {1,1,2,3,5};
//        int[] remove = PracticeHotIssue.remove(a2, 2);
//        System.out.println("移除后的元素列表"+Arrays.toString(remove));
        int[] ints = removeDuplicates(a2);
        System.out.println("有序数组去重"+Arrays.toString(ints));
//        System.out.println(ints);

        int[] arr = {1,2,3,4,5,6};
        reverseV2(arr,2);
        System.out.println("反转后的数组"+Arrays.toString(arr));

        int[] nums = {1, 2, 3, 2};
        int target = 9;
        int[] result = twoSum(nums, 4);
        System.out.println("下标: [" + result[0] + ", " + result[1] + "]");


    }















    










}

