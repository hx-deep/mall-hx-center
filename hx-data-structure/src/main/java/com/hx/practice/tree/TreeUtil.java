package com.hx.practice.tree;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class TreeUtil {

    public static List<TreeNodeParent>  buildTree(List<TreeNodeParent> treeNodes) {
        List<TreeNodeParent> tree = new ArrayList<>();
        HashMap<Integer, TreeNodeParent> map = new HashMap<>();
        for (TreeNodeParent treeNode : treeNodes) {
            map.put(treeNode.id, treeNode);
        }
        for (TreeNodeParent treeNode : treeNodes) {
            if (treeNode.parentId == 0) {
            tree.add(treeNode);
            }else {
                TreeNodeParent parent = map.get(treeNode.parentId);
                if (null != parent) {
                    parent.children.add(treeNode);
                }
            }
        }
        return tree;
    }



    // 递归打印树结构（不使用 repeat）
    public static void printTree(List<TreeNodeParent> tree, int level) {
        for (TreeNodeParent node : tree) {
            // 用循环拼接缩进
            StringBuilder indentBuilder = new StringBuilder();
            for (int i = 0; i < level; i++) {
                indentBuilder.append("  ");  // 每层两个空格
            }
            String indent = indentBuilder.toString();

            // 打印当前节点
            System.out.println(indent + "├ " + node.name);

            // 递归打印子节点
            if (!node.children.isEmpty()) {
                printTree(node.children, level + 1);
            }
        }
    }


    // 测试用例
    public static void main(String[] args) {
        List<TreeNodeParent> nodeList = new ArrayList<>();
        nodeList.add(new TreeNodeParent(1, 0, "根节点"));
        nodeList.add(new TreeNodeParent(2, 1, "子节点A"));
        nodeList.add(new TreeNodeParent(3, 1, "子节点B"));
        nodeList.add(new TreeNodeParent(4, 2, "子节点A-1"));
        nodeList.add(new TreeNodeParent(5, 3, "子节点B-1"));
        nodeList.add(new TreeNodeParent(6, 5, "子节点B-1-1"));

        // 构建树
        List<TreeNodeParent> tree = buildTree(nodeList);
        log.info("tree:{}", JSON.toJSONString(tree));
    }

}
