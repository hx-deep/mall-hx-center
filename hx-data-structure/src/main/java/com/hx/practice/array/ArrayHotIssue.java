package com.hx.practice.array;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.*;

@Data
public class ArrayHotIssue {
    /**
     * 合并两个有序数组
     *
     * @param
     * @param m
     * @param
     * @param n
     * @return
     */
    public void mergeTwoArraysV2(int[] nums1, int m, int[] nums2, int n) {
        int p1 = m - 1;
        int p2 = n - 1;
        int p = m + n - 1;
        while (p2 >= 0) { // nums2 还有要合并的元素
            // 如果 p1 < 0，那么走 else 分支，把 nums2 合并到 nums1 中
            if (p1 >= 0 && nums1[p1] > nums2[p2]) {
                nums1[p] = nums1[p1];
                p--;
                p1--;// 填入 nums1[p1]
            } else {
                nums1[p] = nums2[p2];
                p--;// 填入 nums2[p1]
                p2--;
            }
        }
    }


    public int longestConsecutive(int[] nums) {
        HashSet<Integer> set = new HashSet<>();
        int maxLength = 0;
        for (int num : nums) {
            set.add(num);
        }
        for (int num : nums) {
            if (!set.contains(num - 1)) {
                int currentNum = num;
                int currentLength = 1;
                while (set.contains(currentNum + 1)) {
                    currentLength++;
                    currentNum++;
                }
                maxLength = Math.max(maxLength, currentLength);
            }
        }
        return maxLength;
    }



    /**
     * 两数之和
     *
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







    public List<List<Integer>> threeSumV2(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null ||nums.length < 3) {
            return res;
        }
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i++) {
            if (nums[i]>0) break;
            if (i > 0 && nums[i] == nums[i-1]) continue;
            int L = i+1;
            int R = nums.length-1;
            while (L < R) {
                int sum = nums[i] + nums[L] + nums[R];
                if(sum == 0){
                    res.add(Arrays.asList(nums[i],nums[L],nums[R]));
                    while (L<R && nums[L] == nums[L+1]) L++;
                    while(L<R &&nums[R] == nums[R-1]) R--;
                    L++;
                    R--;
                }
                else if (sum < 0) L++;
                else if (sum > 0) R--;
            }
        }
        return res;
    }


    public  List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> ans = new ArrayList();
        int len = nums.length;
        if(nums == null || len < 3) return ans;
        Arrays.sort(nums); // 排序
        for (int i = 0; i < len ; i++) {
            if(nums[i] > 0) break; // 如果当前数字大于0，则三数之和一定大于0，所以结束循环
            if(i > 0 && nums[i] == nums[i-1]) continue; // 去重
            int L = i+1;
            int R = len-1;
            while(L < R){
                int sum = nums[i] + nums[L] + nums[R];
                if(sum == 0){
                    ans.add(Arrays.asList(nums[i],nums[L],nums[R]));
                    while (L<R && nums[L] == nums[L+1]) L++; // 去重
                    while (L<R && nums[R] == nums[R-1]) R--; // 去重
                    L++;
                    R--;
                }
                else if (sum < 0) L++;
                else if (sum > 0) R--;
            }
        }
        return ans;
    }

    /**
     *  n 个数的和
     * @param target
     * @return
     */
    public int nSum(int target) {
        if (target == 0) {
            return 0;
        }
        if (target == 1) {
            return 1;
        }

      return target+nSum(target-1);
    }


    public int maxProfit(int[] prices) {
        Integer cost = Integer.MAX_VALUE;
        Integer profit = 0;
        for(int price : prices) {
            cost = Math.min(cost,price);
            profit = Math.max(profit,price - cost);
        }
        return profit;
    }

    public int maxProfit2(int[] prices) {
        int profit = 0;
        for (int i = 1;i<prices.length ; i++) {
            int temp = prices[i] - prices[i-1];
            if (temp > 0) profit += temp;
        }
        return profit;
    }

    /**
     * 求有序数组的平方
     * @param nums
     * @return
     */
    public int[] sortedSquares(int[] nums) {
        int[] res = new int[nums.length];
        int left = 0;
        int right = nums.length - 1;
        int p = res.length-1;
        //只有<= 才能比较到最后一个元素
        while (left <= right) {
            int x = nums[left] * nums[left];
            int y = nums[right] * nums[right];
            if (x < y) {
                res[p] = y;
                p--;
                right--;
            }else {
                res[p] = x;
                p--;
                left++;
            }
        }
        return res;
    }

//    public String longestPalindrome(String s) {
//
//    }

    public boolean isPalindrome(String s) {
        if (s == null || s.length() == 0) return false;
        int left = 0, right = s.length() - 1;
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) return false;
            left++;
            right--;
        }
        return true;
    }


    public List<int[]> removeCoveredIntervals(int[][] intervals) {
        List<int[]> res = new ArrayList<>();
        Arrays.sort(intervals,(a,b)->a[0]==b[0]?b[1]-a[1]:a[0]-b[0]);
        int maxLen = -1;
        for (int[] interval : intervals) {
            int end = interval[1];
            if (end > maxLen) {
                maxLen = end;
                res.add(interval);
            }
        }
        return res;
    }

    /**
     * 输入：intervals = [[1,3],[2,6],[8,10],[15,18]]
     * 输出：[[1,6],[8,10],[15,18]]
     * 解释：区间 [1,3] 和 [2,6] 重叠, 将它们合并为 [1,6].
     * @param intervals
     * @return
     */
    public int[][] merge(int[][] intervals) {
        List<List<int[]>> lists = new ArrayList<>();
        for (int[] interval : intervals) {
            List<int[]> temp = new ArrayList<>();
            for (int i = 1; i < interval.length; i++) {
                if (isInteval(interval,intervals[i])) {
                    temp.add(interval);
                    temp.add(intervals[i]);
                }
            }
            lists.add(temp);
        }
        return lists.toArray(new int[lists.size()][]);
        
    }


    public boolean isInteval(int[] nums,int[] intervalv) {
        List<int[]> list = new ArrayList<>();
        List<int[]> list1 = Arrays.asList(nums, intervalv);
        list1.sort((a,b)->a[0]==b[0]?b[1]-a[1]:a[0]-b[0]);
        int max = -1;
        for (int[] interval: list1) {
            int end = interval[1];
            if (end > max) {
                max = end;
                list.add(interval);
            }
        }
        return list.size()==1?true:false;


    }


















    public static void main(String[] args) {
        ArrayHotIssue arrayHotIssue = new ArrayHotIssue();
        int[] nums1 = {1, 5, 0, 0, 0, 0};
        int[] nums2 = {2, 3, 4, 6,-1,-2,-1};
        int[] prices = {7,1,5,3,6,4};
//        List<List<Integer>> lists = arrayHotIssue.threeSumV2(nums2);
//        int i = arrayHotIssue.nSum(4);
        int i = arrayHotIssue.maxProfit2(prices);
        System.out.println("得到的有序数组为" +JSON.toJSONString(i));
        int[][] intervals = {{1,2},{1,4},{3,4}};
        System.out.println(JSON.toJSONString(arrayHotIssue.removeCoveredIntervals(intervals)));
    }




}
