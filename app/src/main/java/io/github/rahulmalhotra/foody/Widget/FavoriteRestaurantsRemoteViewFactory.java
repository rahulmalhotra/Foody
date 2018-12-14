package io.github.rahulmalhotra.foody.Widget;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import io.github.rahulmalhotra.foody.DB.RestaurantDao;
import io.github.rahulmalhotra.foody.DB.RestaurantDatabase;
import io.github.rahulmalhotra.foody.Objects.Restaurant_;
import io.github.rahulmalhotra.foody.R;

public class FavoriteRestaurantsRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private static List<String> favoriteRestaurantList;
    private RestaurantDatabase restaurantDatabaseInstance;
    private RestaurantDao restaurantDao;

    public FavoriteRestaurantsRemoteViewFactory(Context context, Intent intent) {
        this.context = context;
        restaurantDatabaseInstance = RestaurantDatabase.getInstance(context);
        restaurantDao = restaurantDatabaseInstance.restaurantDao();
        favoriteRestaurantList = new ArrayList<>();
    }

    private static void getFavoriteRestaurants(List<Restaurant_> favoriteRestaurants) {
        favoriteRestaurantList.clear();
        if(favoriteRestaurants!=null) {
            for(int i=0; i<favoriteRestaurants.size(); i++) {
                favoriteRestaurantList.add(favoriteRestaurants.get(i).getName());
            }
        }
    }

    private void refreshRestaurants() {
        new QueryAsyncTask(restaurantDao).execute();
    }

    private static class QueryAsyncTask extends AsyncTask<Void, Void, List<Restaurant_>> {

        private RestaurantDao restaurantDao;

        @Override
        protected void onPostExecute(List<Restaurant_> favoriteRestaurants) {
            getFavoriteRestaurants(favoriteRestaurants);
        }

        QueryAsyncTask(RestaurantDao dao) {
            restaurantDao = dao;
        }

        @Override
        protected List<Restaurant_> doInBackground(Void... voids) {
            return restaurantDao.loadFavoriteRestaurantsForWidget();
        }
    }

    @Override
    public void onCreate() {
        refreshRestaurants();
    }

    @Override
    public void onDataSetChanged() {
        refreshRestaurants();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return favoriteRestaurantList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_list_row);
        remoteView.setTextViewText(R.id.favoriteRestaurantName, favoriteRestaurantList.get(position));
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
