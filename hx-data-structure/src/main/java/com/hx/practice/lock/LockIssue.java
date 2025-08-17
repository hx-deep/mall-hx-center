package com.hx.practice.lock;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LockIssue {



//     class myThread1 extends Thread {
//
//         Object lock1;
//         Object lock2;
//
//         myThread1(Object lock1, Object lock2) {
//             this.lock1 = lock1;
//             this.lock2 = lock2;
//         }
//         public void run() {
//             synchronized (lock1){};
//             synchronized (lock2){};
//         }
//     }
//     class myThread2 extends Thread {
//         Object lock1;
//         Object lock2;
//         myThread2(Object o,Object o1) {
//             this.lock1 = o;
//             this.lock2 = o1;
//         }
//         public void run() {
//             synchronized (lock2){};
//             synchronized (lock1){};
//         }
//     }
private static final Object lock1 = new Object();
private static final Object lock2 = new Object();

    public static void main(String[] args) {

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock1) {
                    log.info("线程1获取到A锁：等待b 锁");
                    synchronized (lock2) {
                        System.out.println("线程1获取到B");
                    }
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock2) {
                    //
                    try {
                        Thread.sleep(1000);
                        System.out.println("线程2获取到B锁，等待A锁。。。。。");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    synchronized (lock1) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });
        t1.start();
        t2.start();
    }






}
