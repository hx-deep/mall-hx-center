package com.hx.practice;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class CombineWithPrune {
    List<List<Integer>> res = new ArrayList<>();

    public List<List<Integer>> combine(int n, int k) {
        LinkedList<Integer> path = new LinkedList<>();
        backtrack(n, k, 1, path);
        return res;
    }

    /**
     * 剩余可选数字个数：n - i + 1
     * 还需要选的数字个数：k - path.size()
     * n - i + 1 ≥ k - path.size()
     * i ≤ n - (k - path.size()) + 1
     * @param n
     * @param k
     * @param start
     * @param path
     */
    private void backtrack(int n, int k, int start, LinkedList<Integer> path) {
        // 终止条件：path 满足 k 个数字，加入结果
        if (path.size() == k) {
            res.add(new ArrayList<>(path));
            return;
        }

        // 剪枝优化：剩余可选数字个数 >= 还需要选的数字个数
        // i 最多取到 n - (k - path.size()) + 1
        for (int i = start; i <= n - (k - path.size()) + 1; i++) {
            path.add(i);
            backtrack(n, k, i + 1, path); // 递归
            path.removeLast();            // 回溯，撤销选择 把上一步选择的元素删除
        }
    }


    public static void main(String[] args) {
        List<List<Integer>> combine = new CombineWithPrune().combine(4, 3);
        log.info("组合的结果：{}", combine);
    }
}

