package com.hx.practice.string;

import com.alibaba.fastjson.JSON;

import java.util.*;

public class StrHotIssue {


    public int getLongestSubstring(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        Map<Character, Integer> map = new HashMap<>();
        int maxLen = 0;
        int left = 0;
        for (int i = 0; i < str.length(); i++) {
            if (map.containsKey(str.charAt(i))) {
                //这一步是取出上一次出现该元素的位置，让left往右边移动一位，把上次出现的重复元素去掉
                left = Math.max(left, map.get(str.charAt(i)) + 1);
            }
            map.put(str.charAt(i), i);
            maxLen = Math.max(maxLen, i - left + 1);

        }
        return maxLen;
    }


    public int myAtoi(String s) {
        int i = 0, n = s.length();
        // 去掉前导空格
        while (i < n && s.charAt(i) == ' ') i++;

        // 特判：全是空格
        if (i == n) return 0;

        // 判断正负号
        int sign = 1;
        if (s.charAt(i) == '+') {
            i++;
        } else if (s.charAt(i) == '-') {
            sign = -1;
            i++;
        }

        // 结果值
        long res = 0;

        // 读数字
        while (i < n && Character.isDigit(s.charAt(i))) {
            int digit = s.charAt(i) - '0';
            res = res * 10 + digit;

            // 超过int范围就提前返回
            if (sign == 1 && res > Integer.MAX_VALUE) return Integer.MAX_VALUE;
            if (sign == -1 && -res < Integer.MIN_VALUE) return Integer.MIN_VALUE;

            i++;
        }

        return (int) (sign * res);
    }

    public String addStrings(String num1, String num2) {
        StringBuilder result = new StringBuilder();
        int i = num1.length() - 1; // 指向 num1 的末尾
        int j = num2.length() - 1; // 指向 num2 的末尾
        int carry = 0; // 进位

        // 从右向左遍历，直到两个字符串都处理完且无进位
        while (i >= 0 || j >= 0 || carry > 0) {
            // 获取当前位数字，缺位补 0
            int digit1 = i >= 0 ? num1.charAt(i) - '0' : 0;
            int digit2 = j >= 0 ? num2.charAt(j) - '0' : 0;

            // 计算当前位和与进位
            int sum = digit1 + digit2 + carry;
            carry = sum / 10; // 新进位
            result.append(sum % 10); // 添加当前位结果

            // 移动指针
            i--;
            j--;
        }

        // 反转结果并返回
        return result.reverse().toString();
    }


    public String minWindow(String s, String t) {
        Map<Character, Integer> need = new HashMap<>();
        Map<Character, Integer> window = new HashMap<>();
        for (char c : t.toCharArray()) {
            need.put(c, need.getOrDefault(c, 0) + 1);
        }

        int left = 0, right = 0;
        int valid = 0;
        // 记录最小覆盖子串的起始索引及长度
        int start = 0, len = Integer.MAX_VALUE;
        while (right < s.length()) {
            // c 是将移入窗口的字符
            char c = s.charAt(right);
            // 扩大窗口
            right++;
            // 进行窗口内数据的一系列更新
            if (need.containsKey(c)) {
                window.put(c, window.getOrDefault(c, 0) + 1);
                if (window.get(c).equals(need.get(c)))
                    valid++;
            }

            // 判断左侧窗口是否要收缩
            while (valid == need.size()) {
                // 在这里更新最小覆盖子串
                if (right - left < len) {
                    start = left;
                    len = right - left;
                }
                // d 是将移出窗口的字符
                char d = s.charAt(left);
                // 缩小窗口
                left++;
                // 进行窗口内数据的一系列更新
                if (need.containsKey(d)) {
                    if (window.get(d).equals(need.get(d)))
                        valid--;
                    window.put(d, window.get(d) - 1);
                }
            }
        }
        // 返回最小覆盖子串
        return len == Integer.MAX_VALUE ? "" : s.substring(start, start + len);
    }

    /**
     * 无重复的最小子串
     * @param s
     * @return
     */
    public int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> window = new HashMap<>();

        int left = 0, right = 0;
        // 记录结果
        int res = 0;
        while (right < s.length()) {
            char c = s.charAt(right);
            right++;
            // 进行窗口内数据的一系列更新
            window.put(c, window.getOrDefault(c, 0) + 1);
            // 判断左侧窗口是否要收缩
            while (window.get(c) > 1) {
                char d = s.charAt(left);
                left++;
                // 进行窗口内数据的一系列更新
                window.put(d, window.get(d) - 1);
            }
            // 在这里更新答案
            res = Math.max(res, right - left);
        }
        return res;
    }


    public String minWindowV2(String s, String t) {
        Map<Character,Integer> window = new HashMap();
        Map<Character,Integer> need = new HashMap();
        int left = 0,right = 0;
        int valid = 0;
        int start = 0;
        int len = Integer.MAX_VALUE;
        for(char tt:t.toCharArray()){
            need.put(tt,need.getOrDefault(tt,0)+1);
        }
        while(right<s.length()){
            char c = s.charAt(right);
            right++;
            if(need.containsKey(c)){
                window.put(c,window.getOrDefault(c,0)+1);
                if(window.get(c).equals(need.get(c))){
                    valid++;
                }
            }

            while(valid == need.size()){
                if(right-left<len){
                    start =left;
                    len = right -left;
                }
                char d = s.charAt(left);
                left++;
                // 进行窗口内数据的一系列更新
                if (need.containsKey(d)) {
                    if (window.get(d).equals(need.get(d)))
                        valid--;
                    window.put(d, window.get(d) - 1);
                }
            }
        }
        return s.substring(start,start+len);
    }









        public String minWindowV3(String s, String t) {
            Map<Character,Integer> window = new HashMap();
            Map<Character,Integer> need = new HashMap();
            int l = 0,r=0;
            int valid = 0;
            int len = Integer.MAX_VALUE;
            int start = 0;
            for(char tt:t.toCharArray()){
                need.put(tt,need.getOrDefault(tt,0)+1);
            }
            while(r<s.length()){
                char c = s.charAt(r);
                r++;
                if(need.containsKey(c)){
                    window.put(c,window.getOrDefault(c,0)+1);
                    if(window.get(c) == need.get(c)){
                        valid++;
                    }
                }
                while(valid == need.size()){
                    if(r-l<len){
                        start = l;
                        len = r- l;
                    }
                    char temp = s.charAt(l);
                    l++;
                    if(need.containsKey(temp)){
                        if(window.get(temp).equals(need.get(temp))){
                            valid--;
                        }
                        window.put(temp,window.get(temp) - 1);
                    }
                }
            }
            return len==Integer.MAX_VALUE?"":s.substring(start,start+len);
        }



    public boolean isValid(String s) {
        Stack<Character> stack = new Stack();
        for(char c:s.toCharArray()){
            if(c=='(')stack.push(')');
            else if(c=='{')stack.push('}');
            else if(c=='[')stack.push(']');
            else{
                if(stack.isEmpty() || stack.pop() !=c){
                    return false;
                }
            }
        }
        return stack.isEmpty();

    }


    public int maxArea(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int maxArea = 0;
        while(left<right){
            int currentArea = Math.min(height[right],height[left])*(right-left);
            maxArea = Math.max(currentArea,maxArea);
            if(height[left]<height[right]){
                left++;
            }else{
                right--;
            }
        }
        return maxArea;

    }

    /**
     * 比较版本号
     * @param v1
     * @param v2
     * @return
     */
    public static int compareVersion(String v1, String v2) {
        String[] arr1 = v1.split("\\.");
        String[] arr2 = v2.split("\\.");

        int length = Math.max(arr1.length, arr2.length);

        for (int i = 0; i < length; i++) {
            int num1 = i < arr1.length ? Integer.parseInt(arr1[i]) : 0;
            int num2 = i < arr2.length ? Integer.parseInt(arr2[i]) : 0;

            if (num1 != num2) {
                return Integer.compare(num1, num2);
            }
        }

        return 0;
    }



    public static void main(String[] args) {
        StrHotIssue strHotIssue = new StrHotIssue();
//        String num1 = "127";
//        String num2 = "4";
//        System.out.println(strHotIssue.getLongestSubstring("abba"));
//        System.out.println(strHotIssue.myAtoi(" -233"));
//        System.out.println(strHotIssue.addStrings(num1, num2));
//        System.out.println(strHotIssue.minWindowV3("ADOBECODEBANC", "ABC"));
//        System.out.println(strHotIssue.lengthOfLongestSubstring("abcbbcbb"));
//        String s= "([])";
//        System.out.println(strHotIssue.isValid(s));

//        int[] height = {1,8,6,2,5,4,8,3,7};
//        int i = strHotIssue.maxArea(height);
//        System.out.println(i);


        List<String> versions = Arrays.asList("2.2.0", "1.2", "3", "1.3", "2", "1.2.0.1");

        // 排序：升序
        versions.sort((a, b) -> compareVersion(a, b));

        System.out.println("排序后：" + versions);

        // 找最大版本
        String maxVersion = versions.stream().max((a, b) -> compareVersion(a, b)).orElse(null);

        System.out.println("最大版本：" + maxVersion);

        List<Integer> list = new ArrayList<>();
        list.add(5);
        list.add(2);
        list.add(39);
        list.add(4);
        list.sort((a,b)->compareVersion2(a,b));
        System.out.println(JSON.toJSONString(list));

    }

    public static  int compareVersion2(int v1, int v2) {
        return  Integer.compare(v1,v2);
    }
}
