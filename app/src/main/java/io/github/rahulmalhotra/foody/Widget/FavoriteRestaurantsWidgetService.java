package io.github.rahulmalhotra.foody.Widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

public class FavoriteRestaurantsWidgetService extends IntentService {

    public static final String ACTION_UPDATE_FAVORITE_RESTAURANTS = "com.example.rahulmalhotra.bakingapp.action.update_favorite_restaurants";
    private static String favoriteRestaurants;

    public FavoriteRestaurantsWidgetService() {
        super("FavoriteRestaurantWidgetService");
    }

    public FavoriteRestaurantsWidgetService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent!=null) {
            String action = intent.getAction();
            if(ACTION_UPDATE_FAVORITE_RESTAURANTS.equals(action)) {
                handleActionUpdateFavoriteRestaurants();
            }
        }
    }

    private void handleActionUpdateFavoriteRestaurants() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(this, FavoriteRestaurantsProvider.class));
        FavoriteRestaurantsProvider.updateAppWidget(this, appWidgetManager, appWidgetIds, favoriteRestaurants);
    }

    public static void startActionUpdateFavoriteRestaurants(Context context, String restaurantNames) {
        favoriteRestaurants = restaurantNames;
        Intent intent = new Intent(context, FavoriteRestaurantsWidgetService.class);
        intent.setAction(ACTION_UPDATE_FAVORITE_RESTAURANTS);
        context.startService(intent);
    }
}
