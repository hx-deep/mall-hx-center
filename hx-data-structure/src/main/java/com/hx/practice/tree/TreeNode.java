package com.hx.practice.tree;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.*;

@Data
public class TreeNode {
    public int val;
    public TreeNode left;
    public TreeNode right;

    public  TreeNode(int val) {
        this.val = val;
    }


    /**
     * 遍历输出
     */
    public List<Integer> preOrderRes(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        preOrder(root, res);
        return res;

    }

    public void preOrder(TreeNode root, List<Integer> res) {
        if (root == null) return;
        res.add(root.val);
        preOrder(root.left, res);
        preOrder(root.right, res);
    }

    public List<Integer> inOrderRes(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        inOrder(root, res);
        return res;

    }
    /**
     * 中序遍历
     */
    public void inOrder(TreeNode root,List<Integer> res) {
        if (root == null) return;
        inOrder(root.left,res);
        res.add(root.val);
        inOrder(root.right,res);

    }

    /**
     * 后序遍历
     * @param root
     * @return
     */
    public List<Integer> postOrderRes(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        postOrder(root, res);
        return res;
    }

    public void postOrder(TreeNode root, List<Integer> res) {
        if (root == null) return;
        postOrder(root.left, res);
        postOrder(root.right, res);
        res.add(root.val);
    }

    /**
     * 第一种层序遍历
     * @param root
     * @param res
     */
    public void levelOrderTraverse(TreeNode root, List<Integer> res) {
        if (root == null) return;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        while (!q.isEmpty()) {
            TreeNode cur = q.poll();
            // 访问 cur 节点
            res.add((cur.val));

            // 把 cur 的左右子节点加入队列
            if (cur.left != null) {
                q.offer(cur.left);
            }
            if (cur.right != null) {
                q.offer(cur.right);
            }
        }

    }

    public void levelOrderTraverseV2(TreeNode root, List<Integer> res) {
        if (root == null) return;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        int depth = 1;
        while (!q.isEmpty()) {
            int sz = q.size();
            for (int i = 0; i < sz; i++) {
                TreeNode cur = q.poll();
                // 访问 cur 节点
                res.add((cur.val));

                // 把 cur 的左右子节点加入队列
                if (cur.left != null) {
                    q.offer(cur.left);
                }
                if (cur.right != null) {
                    q.offer(cur.right);
                }
            }
            depth++;
        }

    }

    // 定义：输入一棵二叉树，返回这棵二叉树的节点总数
    int count(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int leftCount = count(root.left);
        int rightCount = count(root.right);
        // 后序位置
        System.out.println("root:"+root.val+"---leftCount:" + leftCount + "---,rightCount:" + rightCount);
        return leftCount + rightCount + 1;
    }

    /**
     *                    1
     *             2              3
     *        4                        5
     * @param args
     */
    public static void main(String[] args) {
        // 创建节点
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.right.right = new TreeNode(5);
        root.left.left = new TreeNode(4);

//        System.out.println("前序遍历"+root.preOrderRes(root));
//        System.out.println("中序遍历"+root.inOrderRes(root));
//        System.out.println("后序遍历"+root.postOrderRes(root));
//        List<Integer> treeNodes = new ArrayList();
//        root.levelOrderTraverse(root,treeNodes);
//        int count = root.count(root);
        root.trave(root);
//        System.out.println(JSON.toJSONString(res));

    }

    /**
     * 从根节点到叶子节点所有路径数字之和
     */
    StringBuilder sb = new StringBuilder();
    List<String> res = new ArrayList<>();
    int sum = 0;
   public  void trave(TreeNode root) {
        if (root == null) return;
        if (root.left == null && root.right == null) {
            sb.append(root.val);
            sum = sum+Integer.parseInt(sb.toString());
            return;
        }
        sb.append(root.val);
        trave(root.left);
        trave(root.right);
        sb.delete(sb.length()-1, sb.length());
    }

    public List<Integer> rightSideView(TreeNode root) {
       List<Integer> res = new ArrayList<>();
       Queue<TreeNode> q = new LinkedList<>();
       q.offer(root);
       while (!q.isEmpty()) {
           int sz = q.size();
           TreeNode last = q.peek();
           for (int i = 0; i < sz; i++) {
               TreeNode  curr = q.poll();
               if (curr.right != null) {
                   q.offer(curr.right);
               }
               if (curr.left != null) {
                   q.offer(curr.left);
               }
           }
           res.add(last.val);
       }
       return res;
    }


}
