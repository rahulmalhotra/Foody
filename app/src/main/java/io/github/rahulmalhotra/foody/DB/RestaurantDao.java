package io.github.rahulmalhotra.foody.DB;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.github.rahulmalhotra.foody.Objects.Restaurant_;

@Dao
public interface RestaurantDao {

    @Query("SELECT * FROM restaurant")
    LiveData<List<Restaurant_>> loadFavoriteRestaurants();

    @Query("SELECT * FROM restaurant")
    List<Restaurant_> loadFavoriteRestaurantsForWidget();

    @Insert
    void insertFavoriteRestaurant(Restaurant_ restaurant);

    @Delete
    void deleteFavoriteRestaurant(Restaurant_ restaurant);

    @Query("SELECT * FROM restaurant WHERE id =:id")
    LiveData<Restaurant_> loadSingleRestaurant(String id);

}
