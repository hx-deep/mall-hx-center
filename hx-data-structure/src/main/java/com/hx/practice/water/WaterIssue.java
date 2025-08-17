package com.hx.practice.water;

public class WaterIssue {

    /**
     * 能盛最多水的容器
     */
    public int maxArea(int[] height) {
        int left = 0, right = height.length - 1;
        int maxArea = 0;
        while (left < right) {
            int area =(right - left) * Math.min(height[left], height[right]);
            maxArea = Math.max(maxArea, area);
            if (height[left] < height[right]) {
                left++;
            }else{
                right--;
            }
        }
        return maxArea;
    }

    public static void main(String[] args) {
       int[] height =new int[]{ 0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
        WaterIssue waterIssue = new WaterIssue();
        System.out.println("能成最多水的容器值为"+waterIssue.maxArea(new int[]{1,8,6,2,5,4,8,3,7}));
        System.out.println("接雨水2可接住水的量为:"+waterIssue.maxArea2(height));

    }


    public int maxArea2(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int leftMax = 0;
        int rightMax = 0;
        int ans = 0;
        while(left < right) {
            if(height[left]<height[right]) {
                if(height[left] >leftMax){
                    leftMax = height[left];
                }else{
                    ans = ans +leftMax - height[left];
                }
                left++;
            }else {
                if(height[right]>rightMax){
                    rightMax = height[right];
                }else {
                    ans = ans +rightMax - height[right];
                }
                right--;
            }
        }
        return ans;
    }



    public int maxAreaUat(int[] height) {
        int left = 0, right = height.length - 1;
        int ans = 0;
        while(left < right) {
            int currentAre = (right-left)*Math.min(height[left], height[right]);
            ans = Math.max(ans, currentAre);
            if (height[left]<height[right]) {
                left++;
            }else {
                right--;
            }
        }
        return ans;
    }
}
