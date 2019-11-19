package com.leo.structure2019.main.helpers;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.util.Log;

import com.leo.homeloan.main.helpers.NotifyEvent;

import java.util.concurrent.atomic.AtomicBoolean;

public class NotifyLiveEvent extends MutableLiveData<NotifyEvent> {

    private static final String TAG = "SingleLiveEvent";

    private final AtomicBoolean mPending = new AtomicBoolean(false);

    @MainThread
    public void observe(LifecycleOwner owner, final Observer<NotifyEvent> observer) {

        if (hasActiveObservers()) {
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.");
        }

        // Observe the internal MutableLiveData
        super.observe(owner, new Observer<NotifyEvent>() {
            @Override
            public void onChanged(@Nullable NotifyEvent event) {
                if (mPending.compareAndSet(true, false)) {
                    // Only, If Content has not been handled.
                    if (! event.isContentHandled()){
                        observer.onChanged(event);
                    }
                }
            }
        });
    }

    @MainThread
    public void setValue(@Nullable NotifyEvent t) {
        mPending.set(true);
        super.setValue(t);
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    public void call() {
        setValue(null);
    }
}
