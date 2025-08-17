package com.hx.practice.labuladong;



import com.hx.practice.tree.TreeNode;

import java.util.LinkedList;
import java.util.List;

public class BinaryTree {

    public List<String> binaryTreePaths(TreeNode root) {
        // 遍历一遍二叉树就能出结果了
        traverse(root);
        return res;
    }

    // 记录 traverse 函数递归时的路径
    LinkedList<String> path = new LinkedList<>();
    // 记录所有从根节点到叶子节点的路径
    LinkedList<String> res = new LinkedList<>();

    void traverse(TreeNode root) {
        if (root == null) {
            return;
        }
        // root 是叶子节点
        if (root.left == null && root.right == null) {
            path.addLast(root.val + "");
            // 将这条路径装入 res
            res.addLast(String.join("->", path));
            path.removeLast();
            return;
        }
        // 前序遍历位置
        path.addLast(root.val + "");
        // 递归遍历左右子树
        traverse(root.left);
        traverse(root.right);
        // 后序遍历位置
        path.removeLast();
    }
    /**
     *                    1
     *             2              3
     *        4                        5
     * @param args
     */
    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.right.right = new TreeNode(5);
        root.left.left = new TreeNode(4);
        BinaryTree binaryTree = new BinaryTree();
        List<String> strings = binaryTree.binaryTreePaths(root);
        System.out.println(strings);


    }
}
