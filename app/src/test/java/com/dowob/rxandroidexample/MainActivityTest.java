package com.dowob.rxandroidexample;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rx.Observable;
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
        Observable.just("aaa")
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.immediate())
                .subscribe(s -> System.out.println(s));
    }

}