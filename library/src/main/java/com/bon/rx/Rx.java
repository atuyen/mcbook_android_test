package com.bon.rx;

import android.support.annotation.Nullable;

import java.util.concurrent.Callable;

import java8.util.function.Consumer;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by dangpp on 2/9/2018.
 */

public class Rx {
    private static final String TAG = "Rx";

    /**
     * @param subscription
     */
    public static void unSubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    /**
     * @param action
     * @param consumer
     * @param <T>
     */
    public static <T> void async(Func0<T> action, Consumer<T> consumer) {
        rawObservable(Observable
                .create(subscriber -> {
                    if (action != null) subscriber.onNext(action.call());
                    subscriber.onCompleted();
                    subscriber.unsubscribe();
                }))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(t -> {
                    if (consumer != null) consumer.accept((T) t);
                });
    }

    /**
     * @param action
     * @param <T>
     * @return
     */
    public static <T> Observable<T> async(Func1<Subscriber, T> action) {
        return rawObservable((Observable<T>) Observable.create(action::call).compose(applySchedulers()));
    }

    /**
     * @param action
     * @param <T>
     * @return
     */
    public static <T> Observable<T> async(Callable<T> action) {
        return rawObservable(Observable.fromCallable(action).compose(applySchedulers()));
    }

    /**
     * @param observable
     * @param <T>
     * @return
     */
    public static <T> Observable<T> rawObservable(Observable<T> observable) {
        return observable.onBackpressureBuffer();
    }

    /**
     * @param <T>
     * @return
     */
    private static <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @param subscription
     * @return
     */
    public static boolean isSubscribed(@Nullable Subscription subscription) {
        return subscription != null && !subscription.isUnsubscribed();
    }
}
