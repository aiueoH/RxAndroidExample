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

}