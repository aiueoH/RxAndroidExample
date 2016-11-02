package com.dowob.rxandroidexample;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

import static org.junit.Assert.*;

/**
 * Created by Wei on 2016/11/1.
 */
public class MainActivityTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void test1() throws Exception {
        Subscription s = Observable.just(0)
                .map(n -> {
                    for (int i = 0; i < 3; i++) {
                        System.out.println("in");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("end");
                    return 0;
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.immediate())
                .subscribe();
        Thread.sleep(1200);
        s.unsubscribe();
    }

    // 不使用 blocking 在非主 thread subscribe
    @Test
    public void test2() throws Exception {
        println("test2 start");
        Observable.just(1, 2, 3)
                .map(n -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    println("map");
                    return n;
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(n -> {
                   println("sub " + n);
                });
        println("test2 end");
    }

    // 使用 blocking 在非主 thread subscribe
    @Test
    public void test3() throws Exception {
        println("test3 start");
        Observable.just(1, 2, 3)
                .map(n -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    println("map");
                    return n;
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .toBlocking()
                .forEach(n -> {
                    println("for each " + n);
                });
        println("test3 end");
    }

    // 直接拿值
    @Test
    public void test4() {
        println("test4 start");
        int num = Observable.just(1)
                .map(n -> {
                    println("map");
                    return n + 399;
                })
                .toBlocking()
                .first();
        println("num = " + num);
        println("test4 end");
    }

    private void println(String s) {
        long id = Thread.currentThread().getId();
        System.out.println(String.format("[Thread %3d] : %s", id, s));
    }
}