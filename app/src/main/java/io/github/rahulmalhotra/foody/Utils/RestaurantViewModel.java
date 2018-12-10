package io.github.rahulmalhotra.foody.Utils;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import io.github.rahulmalhotra.foody.DB.RestaurantDatabase;
import io.github.rahulmalhotra.foody.Objects.Restaurant_;

public class RestaurantViewModel extends ViewModel {

    private LiveData<Restaurant_> restaurantLiveData;

    public LiveData<Restaurant_> getRestaurantLiveData() {
        return restaurantLiveData;
    }

    public RestaurantViewModel(RestaurantDatabase restaurantDatabase, String id) {
        restaurantLiveData = restaurantDatabase.restaurantDao().loadSingleRestaurant(id);
    }
}