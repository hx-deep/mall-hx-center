package com.hx.practice.linked;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

import static com.hx.practice.linked.ListNode.createCycleList;
import static com.hx.practice.linked.ListNode.createNoCycleList;

@Slf4j
public class LinkedIssue {


    /**
     * 判断是否存在环形链表
     * 使用快慢指针法
     *
     * @param head
     * @return
     */
    public static boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) return false;
//        定义慢指针
        ListNode slow = head;
//        定义快指针
        ListNode fast = head.next;

        while (fast != null && fast.next != null) {
            //如果慢指针和快指针相等说明存在环形链表
            if (slow == fast) {
                return true;
            }
            // 否则让慢指针前进一步
            slow = slow.next;
            //快指针前进两部
            fast = fast.next.next;
        }
        return false;
    }

    public static boolean hasCycleV2(ListNode head) {
        // 定义一个hash表来存储判断
        Set<ListNode> set = new HashSet<>();
        while (head != null) {
            //如果hash表中存在，那说明存在环形链表
            if (set.contains(head)) {
                return true;
            }
            //否则将这个节点添加到hash表中，并让节点往前移动继续进行判断
            set.add(head);
            head = head.next;
        }
        return false;
    }

    /**
     * 判断两个链表是否相交
     *
     * @param headA
     * @param headB
     * @return
     */
    public static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) return null;

        ListNode pA = headA;
        ListNode pB = headB;

        while (pA != pB) {
            pA = (pA == null) ? headB : pA.next;
            pB = (pB == null) ? headA : pB.next;
        }

        return pA;  // 如果没有相交，返回 null
    }

    /**
     * 找出如环节点
     *
     * @param head
     * @return
     */
    public static ListNode detectCycle(ListNode head) {
        if (head == null || head.next == null) return null;

        ListNode slow = head;
        ListNode fast = head;

        // 判断是否有环
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;

            if (slow == fast) {
                //入环之后，相遇点的前一步和从头节点往后走的距离是相等的
                ListNode ptr = head;
                while (ptr != slow) {
                    ptr = ptr.next;
                    slow = slow.next;
                }
                return ptr; // 入环点
            }
        }
        return null;
    }


    /**
     * 反转链表
     *
     * @param head
     * @return
     */
    public static ListNode reverseList(ListNode head) {
        // 递归出口：链表为空或只有一个节点，直接返回
        if (head == null || head.next == null) {
            return head;
        }

        // 递归反转 head.next 之后的链表
        ListNode newHead = reverseList(head.next);

        // 将当前 head 的 next 节点的 next 指向 head
        head.next.next = head;

        // 断开 head 和后面节点的连接
        head.next = null;

        // 返回反转后的新头节点
        return newHead;
    }

    public ListNode reverseListV2(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;

        while (curr != null) {
            ListNode nextTemp = curr.next;  // 暂存下一个节点
            curr.next = prev;               // 反转当前节点指向
            prev = curr;                    // prev 向后移动
            curr = nextTemp;                // curr 向后移动
        }

        return prev;  // prev 最后会指向新的头节点
    }

    /**
     * 合并两个有序链表
     *
     * @param l1 1->3
     * @param l2 2->4
     * @return
     */
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if (l1 == null) return l2;
        if (l2 == null) return l1;

        if (l1.val < l2.val) {
            l1.next = mergeTwoLists(l1.next, l2);
            return l1;
        } else {
            l2.next = mergeTwoLists(l1, l2.next);
            return l2;
        }
    }

    /**
     * Definition for singly-linked list.
     * public class ListNode {
     * int val;
     * ListNode next;
     * ListNode() {}
     * ListNode(int val) { this.val = val; }
     * ListNode(int val, ListNode next) { this.val = val; this.next = next; }
     * }
     */
    public ListNode reverseKGroup(ListNode head, int k) {
        // 边界情况：空链表或 k=1，无需翻转
        if (head == null || k == 1) {
            return head;
        }
        // 创建哑节点，简化头节点处理
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prevGroup = dummy; // 前一组的最后一个节点
        // 计算链表长度
        int length = 0;
        ListNode curr = head;
        while (curr != null) {
            length++;
            curr = curr.next;
        }
        // 按组翻转，每组 k 个节点
        while (length >= k) {
            curr = prevGroup.next; // 当前组的第一个节点
            ListNode nextGroup = curr.next; // 当前组的第二个节点
            // 翻转当前组的 k-1 条边
            for (int i = 0; i < k - 1; i++) {
                curr.next = nextGroup.next; // 当前节点指向下下个节点
                nextGroup.next = prevGroup.next; // 下个节点指向当前组的头
                prevGroup.next = nextGroup; // 前一组连接到新头
                nextGroup = curr.next; // 更新下个节点
            }
            // 移动到下一组
            prevGroup = curr; // 当前组的最后一个节点成为前一组的尾
            length -= k; // 剩余长度减少 k
        }

        return dummy.next;
    }

    public ListNode reverseKGroupV2(ListNode head, int k) {
        if(head == null || k ==1){
            return head;
        }
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prevGroup = dummy;
        int length = 0;
        ListNode curr = head;
        while (curr != null) {
            length++;
            curr = curr.next;
        }
        while (length >= k) {
            curr = prevGroup.next;
            ListNode nextGroup = curr.next;
            for (int i = 0; i < k - 1; i++) {
                curr.next = nextGroup.next; //1 ->3
                nextGroup.next  = prevGroup.next; // 2-->1
                prevGroup.next = nextGroup; // 0 -->2
                //更新下一次要移动的节点
                nextGroup = curr.next;
            }
            prevGroup = curr;
            length -= k;
        }
        return dummy.next;
    }






    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(4);
        head.next.next.next.next = new ListNode(5);
        LinkedIssue linkedIssue = new LinkedIssue();
//        ListNode head1 = new ListNode(2);
//        head1.next = new ListNode(3);
//        ListNode node = new LinkedIssue().mergeTwoLists(head, head1);
        ListNode node = linkedIssue.reverseKGroupV2(head,2);
        log.info("反转后的链表为：{}", node);
    }


    // 创建相交链表
//        public static  void main(String[] args) {
//            // 公共部分
//            ListNode c1 = new ListNode(8);
//            ListNode c2 = new ListNode(9);
//            c1.next = c2;
//
//            // 链表A：4 -> 1 -> 8 -> 9
//            ListNode a1 = new ListNode(4);
//            ListNode a2 = new ListNode(1);
//            a1.next = a2;
//            a2.next = c1;
//
//            // 链表B：5 -> 0 -> 1 -> 8 -> 9
//            ListNode b1 = new ListNode(5);
//            ListNode b2 = new ListNode(0);
//            ListNode b3 = new ListNode(1);
//            b1.next = b2;
//            b2.next = b3;
//            b3.next = c1;
//
//            // 调用相交检测方法
//            ListNode intersection = getIntersectionNode(a1, b1);
//            if (intersection != null) {
//                System.out.println("相交节点值：" + intersection.val); // 应该是 8
//            } else {
//                System.out.println("两个链表不相交");
//            }
//        }


//        public static void main(String[] args) {
//        // 测试带环链表
//        ListNode cycleList = createCycleList();
//        log.info("带环链表检测结果：{}" , hasCycleV2(cycleList));  // 应该是 true
//
//        // 测试无环链表
//        ListNode noCycleList = createNoCycleList();
//        log.info("无环链表检测结果：{}" ,hasCycleV2(noCycleList));  // 应该是 false
//            // 构建链表
//            ListNode head = new ListNode(1);
//            ListNode node2 = new ListNode(2);
//            ListNode node3 = new ListNode(3);
//            ListNode node4 = new ListNode(4);
//            ListNode node5 = new ListNode(5);
//            head.next = node2;
//            node2.next = node3;
//            node3.next = node4;
//            node4.next = node5;
//            node5.next = null;
//
//            // 调用方法找入环点
//            ListNode entry = detectCycle(head);
//            if (entry != null) {
//                System.out.println("入环点是：" + entry.val);
//            } else {
//                System.out.println("没有环");
//            }
//
//
//
//            ListNode nodeRe = reverseList(head);
//
//
//            log.info("反转得到的链表为：{}",JSON.toJSONString(nodeRe));

//        }


}
