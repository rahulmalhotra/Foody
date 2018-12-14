package io.github.rahulmalhotra.foody.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import io.github.rahulmalhotra.foody.MainActivity;
import io.github.rahulmalhotra.foody.R;

public class FavoriteRestaurantsProvider extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if(action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, FavoriteRestaurantsProvider.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(componentName), R.id.favoriteRestaurantsList);
        }
        super.onReceive(context, intent);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.favorite_restaurant_widget);
        Intent intent = new Intent(context, FavoriteRestaurantsWidgetService.class);
        remoteViews.setRemoteAdapter(R.id.favoriteRestaurantsList, intent);
        Intent intent1 = new Intent(context, MainActivity.class);
        intent1.putExtra(context.getResources().getString(R.string.activeTabIntent), 1);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.favoriteRestaurantsWidget, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int appWidgetId: appWidgetIds) {
            updateAppWidget(context, appWidgetManager, new int[]{appWidgetId});
        }
    }

    public static void sendRefreshBroadcast(Context context) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, FavoriteRestaurantsProvider.class));
        context.sendBroadcast(intent);
    }
}
