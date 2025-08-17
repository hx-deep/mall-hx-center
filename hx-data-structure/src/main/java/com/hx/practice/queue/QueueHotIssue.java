package com.hx.practice.queue;

import java.util.LinkedList;
import java.util.Queue;

public class QueueHotIssue {

    /**
     * 用队列实现一个栈
     */
    Queue<Integer> queue;
    QueueHotIssue (){
        queue = new LinkedList<>();
    }

    /**
     * 入栈
     * @param x[1,2,3]--->[3,2,1]
     */
    public void push(int x) {
        //把元素放到队尾
        queue.offer(x);
        int size = queue.size();
        //把除了刚放进的元素之外的其他元素poll出来在从新放到队尾:所以是size-1
        for (int i = 0; i < size -1; i++) {
            queue.offer(queue.poll());
        }
    }

    public int pop() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int top(){
        return queue.peek();
    }


    public static void main(String[] args) {
        QueueHotIssue queueHotIssue = new QueueHotIssue();
        queueHotIssue.push(1);
        queueHotIssue.push(2);
        queueHotIssue.push(3);
        System.out.println(queueHotIssue.pop());
    }



}
