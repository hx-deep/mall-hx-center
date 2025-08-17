package com.hx.practice.linked;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

@Data
class ListNode {
    int val;
    ListNode next;
    ListNode(int x) {
        val = x;
        next = null;
    }

    // 创建一个带环链表
    public static ListNode createCycleList() {
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);

        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node2;  // 这里让 node4 指向 node2，形成环

        return node1;
    }

    public static ListNode createNoCycleList() {
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);

        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = null;  // 正常结束

        return node1;
    }

    /**
     * 1,2,3,4
     * @param nums
     * @return
     */
    public static ListNode createCycleList(int[] nums) {
        ListNode head = new ListNode(nums[0]);
        ListNode cur = head;
        for (int i = 1; i < nums.length; i++) {
            cur.next = new ListNode(nums[i]);
            cur = cur.next;
        }
        return head;
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        ListNode current = this;
        while (current != null) {
            sb.append(current.val);
            if (current.next != null) {
                sb.append(" -> ");
            }
            current = current.next;
        }
        return sb.toString();
    }


    public static void  getListNode(ListNode head) {
        if (head == null || head.next == null) return;
        while (head != null) {
            System.out.println(head.val);
            head = head.next;
        }
    }

    public static void getListNode2(ListNode head) {
        if(head == null) return;
        getListNode2(head.next);
        System.out.println(head.val);
    }


    public static void main(String[] args) {
        int[] nums = {1, 2, 3, 4, 5};
        ListNode head = ListNode.createCycleList(nums);
//        System.out.println(head);
        getListNode2(head);


    }
}
