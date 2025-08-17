package com.hx.practice.back;

import java.util.ArrayList;
import java.util.List;

public class BackTrack {

    /**
     * 括号组合
     * @param n
     * @return
     */
    public List<String> generateParenthesis(int n) {
        List<String> result = new ArrayList<>();
        backtrack(result, new StringBuilder(), 0, 0, n);
        return result;
    }

    // 回溯方法
    private void backtrack(List<String> result, StringBuilder sb, int open, int close, int max) {
        // 如果当前构造的字符串长度达到 2 * n，加入结果
        if (sb.length() == max * 2) {
            result.add(sb.toString());
            return;
        }

        // 如果左括号数量还没用完，可以继续添加左括号
        if (open < max) {
            sb.append('(');
            backtrack(result, sb, open + 1, close, max);
            sb.deleteCharAt(sb.length() - 1); // 回溯，移除最后一个字符
        }

        // 如果右括号数量小于左括号数量，可以添加右括号
        if (close < open) {
            sb.append(')');
            backtrack(result, sb, open, close + 1, max);
            sb.deleteCharAt(sb.length() - 1); // 回溯，移除最后一个字符
        }
    }


    public static void main(String[] args) {
        BackTrack backTrack = new BackTrack();
        List<String> strings = backTrack.generateParenthesis(3);
        System.out.println(strings);
    }



}
