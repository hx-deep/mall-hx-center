package com.hx.practice.labuladong;

import com.hx.practice.tree.TreeNode;

import java.util.*;

public class DFSTree {

    // DFS 递归遍历解法
    List<Integer> res = new ArrayList<>();
    // 记录递归的层数
    int depth = 0;

    /**
     * 右视图
     * @param root
     * @return
     */
    public List<Integer> rightSideView_DFS(TreeNode root) {
        traverse(root);
        return res;
    }

    // 二叉树遍历函数
    void traverse(TreeNode root) {
        if (root == null) {
            return;
        }
        // 前序遍历位置
        depth++;
        if (res.size() < depth) {
            // 这一层还没有记录值
            // 说明 root 就是右侧视图的第一个节点
            res.add(root.val);
        }
        // 注意，这里反过来，先遍历右子树再遍历左子树
        // 这样首先遍历的一定是右侧节点
        traverse(root.right);
        traverse(root.left);
        // 后序遍历位置
        depth--;
    }






    public static class Solution {
        public String smallestFromLeaf(TreeNode root) {
            traverse(root);
            return res;
        }
        // 遍历过程中的路径
        StringBuilder path = new StringBuilder();
        String res = null;

        // 二叉树遍历函数
        void traverse(TreeNode root) {
            if (root == null) {
                return;
            }
            if (root.left == null && root.right == null) {
                // 找到叶子结点，比较字典序最小的路径
                // 结果字符串是从叶子向根，所以需要反转
                path.append((char) ('a' + root.val));
                path.reverse();

                String s = path.toString();
                if (res == null || res.compareTo(s) > 0) {
                    // 如果字典序更小，则更新 res
                    res = s;
                }

                // 恢复，正确维护 path 中的元素
                path.reverse();
                path.deleteCharAt(path.length() - 1);
                return;
            }
            // 前序位置
            path.append((char) ('a' + root.val));

            traverse(root.left);
            traverse(root.right);

            // 后序位置
            path.deleteCharAt(path.length() - 1);
        }
    }

    /**
     * 从根节点到叶子节点所有二进制数字和
     */
    static class SolutionV2 {
        StringBuilder sb= new StringBuilder();
        int sum = 0;
        public int sumRootToLeaf(TreeNode root) {
            traverse(root);
            return sum;
        }

        void traverse(TreeNode root){
            if(root == null) return;
            if(root.left == null && root.right == null){
                sb.append(root.val);
                sum = sum+ Integer.parseInt(sb.toString(),2);
                return;
            }
            sb.append(""+root.val);
            traverse(root.left);
            traverse(root.right);
            sb.delete(sb.length()-1,sb.length());
        }
    }

    /**
     * 623. 在二叉树中增加一行
     */
    static class SolutionV3 {
        private int curDepth = 0;
        int targetVal = 0;
        int targetDepth = 0;
        public TreeNode addOneRow(TreeNode root, int val, int depth) {
            this.targetDepth = depth;
            this.targetVal = val;
            if (depth ==1){
                TreeNode node = new TreeNode(val);
                node.left = root;
                return node;
            }
            traverse(root);
            return root;
        }

        void traverse(TreeNode root) {
            if (root == null) return;
            curDepth++;
            if(curDepth == targetDepth-1){
                TreeNode nodeLeft = new TreeNode(targetVal);
                TreeNode nodeRight = new TreeNode(targetVal);
                nodeLeft.left = root.left;
                nodeRight.right = root.right;
                root.left = nodeLeft;
                root.right = nodeRight;
            }
            traverse(root.left);
            traverse(root.right);
            curDepth--;
        }
    }

     static class SolutionV4 {
        public List<Integer> flipMatchVoyage(TreeNode root, int[] voyage) {
            this.voyage = voyage;
            // 遍历的过程中尝试进行反转
            traverse(root);

            if (canFlip) {
                return res;
            }
            return Arrays.asList(-1);
        }


        List<Integer> res = new LinkedList<>();
        int i = 0;
        int[] voyage;
        boolean canFlip = true;

        void traverse(TreeNode root) {
            if (root == null || !canFlip) {
                return;
            }
            if (root.val != voyage[i++]) {
                // 节点的 val 对不上，必然无解
                canFlip = false;
                return;
            }
            if (root.left != null && root.left.val != voyage[i]) {
                // 前序遍历结果不对，尝试翻转左右子树
                TreeNode temp = root.left;
                root.left = root.right;
                root.right = temp;
                // 记录翻转节点
                res.add(root.val);
            }

            traverse(root.left);
            traverse(root.right);
        }
    }

    /**
     * 987. 二叉树的垂序遍历
     */
   static class SolutionV5 {
        // 记录每个节点和对应的坐标 (row, col)
        class Triple {
            public int row, col;
            public TreeNode node;

            public Triple(TreeNode node, int row, int col) {
                this.node = node;
                this.row = row;
                this.col = col;
            }
        }

        public List<List<Integer>> verticalTraversal(TreeNode root) {
            // 遍历二叉树，并且为所有节点生成对应的坐标
            traverse(root, 0, 0);
            // 根据题意，根据坐标值对所有节点进行排序：
            // 按照 col 从小到大排序，col 相同的话按 row 从小到大排序，
            // 如果 col 和 row 都相同，按照 node.val 从小到大排序。
            Collections.sort(nodes, (Triple a, Triple b) -> {
                if (a.col == b.col && a.row == b.row) {
                    return a.node.val - b.node.val;
                }
                if (a.col == b.col) {
                    return a.row - b.row;
                }
                return a.col - b.col;
            });
            // 将排好序的节点组装成题目要求的返回格式
            LinkedList<List<Integer>> res = new LinkedList<>();
            // 记录上一列编号，初始化一个特殊值
            int preCol = Integer.MIN_VALUE;
            for (int i = 0; i < nodes.size(); i++) {
                Triple cur = nodes.get(i);
                if (cur.col != preCol) {
                    // 开始记录新的一列
                    res.addLast(new LinkedList<>());
                    preCol = cur.col;
                }
                res.getLast().add(cur.node.val);
            }

            return res;
        }

        ArrayList<Triple> nodes = new ArrayList<>();
        // 二叉树遍历函数，记录所有节点对应的坐标
        void traverse(TreeNode root, int row, int col) {
            if (root == null) {
                return;
            }
            // 记录坐标
            nodes.add(new Triple(root, row, col));
            // 二叉树遍历框架
            traverse(root.left, row + 1, col - 1);
            traverse(root.right, row + 1, col + 1);
        }
    }





    public static void main(String[] args) {
//        TreeNode root = new TreeNode(1);
//        root.left = new TreeNode(2);
//        root.right = new TreeNode(3);
//        root.right.right = new TreeNode(5);
//        root.left.left = new TreeNode(4);
//        BinaryTree binaryTree = new BinaryTree();
//        System.out.println(bfs.rightSideView_DFS(root));
//        Solution solution = new Solution();
//        System.out.println(solution.smallestFromLeaf(root));
        //v3的测试数据
//        TreeNode root = new TreeNode(4);
//        root.left = new TreeNode(2);
//        root.left.left = new TreeNode(3);
//        root.left.left.left = new TreeNode(9);
//        root.left.right = new TreeNode(1);
//        root.right = new TreeNode(6);
//        root.right.left = new TreeNode(5);
//        TreeNode treeNode = new SolutionV3().addOneRow(root, 7, 3);
//        List<Integer> integers = treeNode.preOrderRes(treeNode);
//        System.out.println(integers);
//        TreeNode root = new TreeNode(1);
//        root.left = new TreeNode(2);
//        root.right = new TreeNode(3);
//        root.right.left = new TreeNode(4);
//        root.right.right = new TreeNode(5);
//        List<Integer> integers = new SolutionV4().flipMatchVoyage(root, new int[]{1, 3, 4, 5, 2});
//        System.out.println(integers);
            TreeNode root = new TreeNode(1);
            root.left = new TreeNode(2);
            root.right = new TreeNode(3);
            root.left.left = new TreeNode(4);
            root.left.right = new TreeNode(6);
            root.right.left = new TreeNode(5);
            root.right.right = new TreeNode(7);
        SolutionV5 solutionV5 = new SolutionV5();
        System.out.println(solutionV5.verticalTraversal(root));

    }

}
