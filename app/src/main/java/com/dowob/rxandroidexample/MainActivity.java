package com.dowob.rxandroidexample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ProgressBar;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends RxAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button1).setOnClickListener(v -> rx1());
        findViewById(R.id.button2).setOnClickListener(v -> rx2());
        findViewById(R.id.button3).setOnClickListener(v -> rx3());
        findViewById(R.id.button4).setOnClickListener(v -> rx4());
        findViewById(R.id.button5).setOnClickListener(v -> rx5());
        findViewById(R.id.button6).setOnClickListener(v -> rx6());
        findViewById(R.id.button7).setOnClickListener(v -> rx7());
    }

    // 基本的訂閱
    private void rx1() {
        Log.d("MainActivity", "rx1");
        int a = 0;
        Observable.just(a)
                .subscribe(s -> System.out.println(s));
    }

    // map + thread 切換
    private void rx2() {
        Log.d("MainActivity", "rx2");
        int a = 0;
        Observable.just(a)
                .map(n -> String.valueOf(n))
                .map(s -> {
                    Log.d("MainActivity", "s + s");
                    return s + s;
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> System.out.println(s));
    }

    // lambda 用法, doOnSubscribe, doOnUnSubscribe
    private void rx3() {
        Log.d("MainActivity", "rx3");
        int a = 0;
        ProgressDialog pd = new ProgressDialog(this);
        Observable.just(a)
                .map(n -> String.valueOf(n))
                .subscribeOn(Schedulers.computation())
                .map(s -> appendA(s))
                .map(s -> {
                    return appendB(s);
                })
                .map(this::appendC)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> pd.show())
                .doOnUnsubscribe(() -> pd.dismiss())
                .subscribe(s -> {
                    Log.d("MainActivity", s);
                });
    }

    // just 的 emit 是無法改變 thread 的
    private void rx4() {
        Log.d("MainActivity", "rx4");
        Observable.just(createString())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(System.out::println);
    }

    // 例外處理
    private void rx5() {
        Log.d("MainActivity", "rx5");
        Observable.just("will not see this")
                .map(n -> {
                    Log.d("MainActivity", "throw ex");
                    throw new RuntimeException("runtime ex");
                })
                .doOnError(error -> {
                    Log.d("MainActivity", "on error");
                })
                .retry(3)
                .subscribe(
                        System.out::println,
                        error -> System.out.println(error.toString())
                );
    }

    // 使用回傳 observable 的 function 訂閱
    private void rx6() {
        Log.d("MainActivity", "rx6");
        getString()
                .subscribe(System.out::println);
    }

    // lifecycle binding
    private void rx7() {
        Log.d("MainActivity", "rx7");
        ProgressDialog pd = new ProgressDialog(this);
        Observable.just("OK")
                .map(n -> {
                    Log.d("MainActivity", "map");
                    sleep(5000);
                    return n;
                })
                .subscribeOn(Schedulers.newThread())
                .doOnSubscribe(pd::show)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnUnsubscribe(pd::dismiss)
                .compose(this.bindUntilEvent(ActivityEvent.STOP))
                .subscribe(
                        System.out::println,
                        System.out::println,
                        () -> Log.d("MainActivity", "on com"));

    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Observable<String> getString() {
        return Observable.just(0)
                .map(n -> String.valueOf(n));
    }

    private String createString() {
        Log.d("MainActivity", "createString()");
        return "S-T-R-I-N-G";
    }

    private String appendA(String s) {
        Log.d("MainActivity", "appendA()");
        sleep(1000);
        return s + "A";
    }

    private String appendB(String s) {
        Log.d("MainActivity", "appendB()");
        sleep(1000);
        return s + "B";
    }

    private String appendC(String s) {
        Log.d("MainActivity", "appendC()");
        sleep(1000);
        return s + "C";
    }
}
