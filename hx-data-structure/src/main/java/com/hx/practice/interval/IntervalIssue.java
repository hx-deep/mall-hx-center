package com.hx.practice.interval;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IntervalIssue {

    public int[][] merge(int[][] intervals) {
        if (intervals.length == 0) return new int[0][2];

        // 按起点排序
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);

        List<int[]> result = new ArrayList<>();

        // 遍历每个区间
        for (int[] interval : intervals) {
            // 如果结果为空，或者当前区间的起点 > 结果集中最后一个区间的终点，直接加入
            if (result.isEmpty() || interval[0] > result.get(result.size() - 1)[1]) {
                result.add(interval);
            } else {
                // 否则，合并两个区间，更新结果集中最后一个区间的终点
                result.get(result.size() - 1)[1] = Math.max(result.get(result.size() - 1)[1], interval[1]);
            }
        }

        // 转成二维数组返回
        return result.toArray(new int[result.size()][]);
    }

    public static void main(String[] args) {
        IntervalIssue intervalIssue = new IntervalIssue();
        int[][] intervals = new int[][]{
                {1, 5},
                {4, 8}
        };
        System.out.println(JSON.toJSONString(intervalIssue.merge(intervals)));

    }
}
