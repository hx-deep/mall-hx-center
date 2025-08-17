package com.hx.practice.labuladong;


import com.hx.practice.tree.TreeNode;

import java.util.Arrays;

public class DecomposeBuildTree {

    /**
     * 构造最大二叉树
     * 给定一个数组，找到数组中的最大值，最大值的左边子数组用来构建左子树，最大值的右边子数组用来构建右子树
     */
    static class SolutionV1 {

        public TreeNode constructMaximumBinaryTree(int[] nums) {
            return build(nums, 0, nums.length - 1);
        }

        // 定义：将 nums[lo..hi] 构造成符合条件的树，返回根节点
        TreeNode build(int[] nums, int lo, int hi) {
            // base case
            if (lo > hi) {
                return null;
            }

            // 找到数组中的最大值和对应的索引
            int index = -1, maxVal = Integer.MIN_VALUE;
            for (int i = lo; i <= hi; i++) {
                if (maxVal < nums[i]) {
                    index = i;
                    maxVal = nums[i];
                }
            }

            // 先构造出根节点
            TreeNode root = new TreeNode(maxVal);
            // 递归调用构造左右子树
            root.left = build(nums, lo, index - 1);
            root.right = build(nums, index + 1, hi);

            return root;
        }
    }


    static class SolutionV2 {

        public TreeNode buildTree(int[] inOrder,int[] preorder) {
            int index = 0;
            int rootVal;
            TreeNode root = null;
            for (int i = 0; i < preorder.length; i++) {
                 rootVal = preorder[i];
                for (int j = 0; i < inOrder.length; i++) {
                    if (inOrder[j] == rootVal) {
                        index = i;
                    }
                }
                root = new TreeNode(rootVal);
                int[] inOrderLeft = Arrays.copyOf(inOrder, index-1);
                int[] inOrderRight = Arrays.copyOfRange(inOrder, index+1,inOrder.length);
//                preorder = Arrays.copyOfRange(preorder, index,preorder.length);
                root.left = buildTree(inOrderLeft, preorder);
                root.right = buildTree(inOrderRight, preorder);
            }
          return root;
        }
    }


    public static void main(String[] args) {
        int [] preorder = {3,9,20,15,7}, inorder = {9,3,15,20,7};
        TreeNode treeNode = new SolutionV2().buildTree(preorder, inorder);
        System.out.println(treeNode.val);

    }
}
