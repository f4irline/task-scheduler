package com.github.f4irline.taskscheduler.LiveDataUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

public class LiveDataUtils {
    public static <T> void observeOnce (LiveData<T> liveData, Observer<T> observer) {
        liveData.observeForever(result -> {
            liveData.removeObserver(observer);
            observer.onChanged(result);
        });
    }
}
