package com.hx.practice;

import java.util.*;

public class Permutations {
    List<List<Integer>> res = new ArrayList<>();
    LinkedList<Integer> path = new LinkedList<>();
    boolean[] used;

    public List<List<Integer>> permute(int[] nums) {
        used = new boolean[nums.length];  // 初始化为false，表示所有数字未被选择
        backtrack(nums);  // 开始回溯
        return res;
    }

    private void backtrack(int[] nums) {
        // 递归的终止条件：当前路径的大小等于数组的长度，说明选满了
        if (path.size() == nums.length) {
            res.add(new ArrayList<>(path));  // 将当前路径加入结果
            return;
        }

        // 遍历所有数字，尝试将每个数字加入到路径中
        for (int i = 0; i < nums.length; i++) {
            // 如果数字已经使用过，跳过
            if (used[i]) continue;

            // 做选择：加入数字到路径，并标记已使用
            path.add(nums[i]);
            used[i] = true;

            // 递归处理下一个数字
            backtrack(nums);

            // 撤销选择：从路径中删除数字，并标记为未使用
            path.removeLast();
            used[i] = false;
        }
    }

    public static void main(String[] args) {
        Permutations p = new Permutations();
        System.out.println(p.permute(new int[]{1, 2, 3}));
    }
}

