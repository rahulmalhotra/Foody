package io.github.rahulmalhotra.foody.Utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RestaurantExecutors {

    private static final Object LOCK = new Object();
    private static RestaurantExecutors restaurantExecutorsInstance;
    private final Executor diskIO;

    public RestaurantExecutors(Executor diskIO) {
        this.diskIO = diskIO;
    }

    public Executor getDiskIO() {
        return diskIO;
    }

    public static RestaurantExecutors getRestaurantExecutorsInstance() {
        if(restaurantExecutorsInstance==null) {
            synchronized (LOCK) {
                restaurantExecutorsInstance = new RestaurantExecutors(Executors.newSingleThreadExecutor());
            }
        }
        return restaurantExecutorsInstance;
    }
}
