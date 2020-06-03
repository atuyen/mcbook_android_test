package com.bon.eventbus;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.bon.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by dangpp on 2/9/2018.
 */

public class RxBus<T extends IEvent> {
    private static final String TAG = RxBus.class.getSimpleName();

    /**
     * Event bus executor.
     */
    private final Subject<T, T> bus = new SerializedSubject<>(PublishSubject.<T>create());
    private final Handler handler = new Handler(Looper.getMainLooper());

    /**
     * Cache from all Subscriptions.
     * <p>String (key ) - object identity, which subscribe to catch event</p>
     * <p>List<{@link TypedSubscription}> - list of events to catch in current subscriber</p>
     */
    private final HashMap<String, List<TypedSubscription>> subscriptions = new HashMap<>();

    /**
     * Send event to broadcast.
     *
     * @param event event to be caught.
     */
    public void send(T event) {
        try {
            handler.post(() -> bus.onNext(event));
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /*
     * Subscriber on specific event.
     *
     * @param subscriber - Subscriber, witch catch the event.
     * @param clazz      - Event class to catch.
     * @param action     - Action to call after getting an event.
     * @param <A>        - Type of event.
     */
    public <A extends T> Subscription subscribe(@NonNull Object subscriber, @NonNull Class<A> clazz, @NonNull final Action1<A> action) {
        try {
            final Subscription subscription = bus.ofType(clazz).subscribe(action);
            final String key = getKey(subscriber);
            final String clazzKey = clazz.getCanonicalName();

            if (subscriptions.containsKey(key)) {
                List<TypedSubscription> lostSubscriptions = subscriptions.get(key);

                // check if contains the same class to catch
                Iterator<TypedSubscription> iterator = lostSubscriptions.iterator();
                while (iterator.hasNext()) {
                    TypedSubscription typedSubscription = iterator.next();
                    if (typedSubscription.getClazz().equals(clazzKey)) {
                        unSubscribe(typedSubscription);
                        iterator.remove();
                    }
                }

                // addConversation confirmSubscription to cache
                lostSubscriptions.add(new TypedSubscription(subscription, clazzKey));
            } else {
                List<TypedSubscription> localSubscriptions = new ArrayList<>();
                localSubscriptions.add(new TypedSubscription(subscription, clazzKey));
                subscriptions.put(key, localSubscriptions);
            }

            return subscription;
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    /**
     * @param subscriber
     * @return
     */
    private String getKey(@NonNull Object subscriber) {
        return subscriber.getClass().getCanonicalName() + "_" + System.identityHashCode(subscriber);
    }

    /**
     * @param subscription
     */
    private void unSubscribe(Subscription subscription) {
        try {
            if (!subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param subscriber
     */
    public void unSubscribe(@NonNull Object subscriber) {
        try {
            final String key = getKey(subscriber);
            if (subscriptions.containsKey(key)) {
                // unSubscribe all of subscriber elements
                List<TypedSubscription> localSubscriptions = subscriptions.remove(key);
                for (Subscription subscription : localSubscriptions) {
                    unSubscribe(subscription);
                }

                // clear confirmSubscription cache
                localSubscriptions.clear();
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * Class delegate {@link Subscription} logic with adding an object identifier,
     * witch has been subscribe. Needed to protect subscriber from duplicating event calls
     * after few confirmSubscription to the same {@link IEvent}.
     */
    static class TypedSubscription implements Subscription {
        private final Subscription subscription;
        private final String clazz;

        /**
         * @param subscription
         * @param clazz
         */
        TypedSubscription(@NonNull Subscription subscription, @NonNull String clazz) {
            this.subscription = subscription;
            this.clazz = clazz;
        }

        @Override
        public void unsubscribe() {
            subscription.unsubscribe();
        }

        @Override
        public boolean isUnsubscribed() {
            return subscription.isUnsubscribed();
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;

            TypedSubscription that = (TypedSubscription) object;
            return clazz.equals(that.clazz);

        }

        @Override
        public int hashCode() {
            return clazz.hashCode();
        }

        public String getClazz() {
            return clazz;
        }
    }
}
