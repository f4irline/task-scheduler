package com.github.f4irline.taskscheduler.Http;

/**
 * Interface which is used to implement a listener which is called when the HttpFetch
 * asynchronous task is finished.
 *
 * @author Tommi Lepola
 * @version 3.0
 * @since 2019.0406
 */
public interface TaskListener {
    void onTaskCompleted(String result);
}
