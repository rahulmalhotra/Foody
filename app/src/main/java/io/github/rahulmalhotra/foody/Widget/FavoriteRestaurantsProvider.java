package io.github.rahulmalhotra.foody.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import io.github.rahulmalhotra.foody.MainActivity;
import io.github.rahulmalhotra.foody.R;

public class FavoriteRestaurantsProvider extends AppWidgetProvider {

    private static String favoriteRestaurants;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, String restaurants) {
        favoriteRestaurants = restaurants;
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_restaurant_widget);
        views.setTextViewText(R.id.favoriteRestaurants, favoriteRestaurants);
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("activeTab", 1);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.favoriteRestaurantsWidget, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int appWidgetId: appWidgetIds) {
            updateAppWidget(context, appWidgetManager, new int[]{appWidgetId}, favoriteRestaurants);
        }
    }
}
