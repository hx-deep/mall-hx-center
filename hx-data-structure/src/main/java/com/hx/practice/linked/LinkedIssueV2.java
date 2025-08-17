package com.hx.practice.linked;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LinkedIssueV2 {

    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode newHead = reverseList(head.next);
        head.next.next = head;
        head.next = null;
        return newHead;
    }

    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if (l1 == null) {return l2;}
        if (l2 == null) {return l1;}
        if (l1.val < l2.val) {
            l1.next = mergeTwoLists(l1.next, l2);
            return l1;
        }else {
            l2.next = mergeTwoLists(l1, l2.next);
            return l2;
        }
    }

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
//        ListNode node = new LinkedIssueV2().reverseList(head);
        ListNode listNode = new ListNode(2);
        listNode.next= new ListNode(4);
        listNode.next.next = new ListNode(5);
        ListNode node = new LinkedIssueV2().mergeTwoLists(head, listNode);
        System.out.println("反转后的链表为:" + node);
    }


    List<List<Integer>> res = new ArrayList();
    public List<List<Integer>> combine(int n, int k) {
        LinkedList<Integer> path =new LinkedList();
        bsckTrack(n,k,1,path);
        return res;
    }

    public void bsckTrack(int n,int k,int start,LinkedList<Integer> path){
        if(k == path.size()){
            res.add(new ArrayList<>(path));
            return;
        }

        for(int i = start;i<=n+1-k+path.size();i++){
            path.add(i);
            bsckTrack(n,k,i+1,path);
            path.removeLast();
        }
    }
}
