package io.github.rahulmalhotra.foody;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.rahulmalhotra.foody.API.RestaurantAPIService;
import io.github.rahulmalhotra.foody.API.RetrofitClient;
import io.github.rahulmalhotra.foody.Objects.Cuisine;
import io.github.rahulmalhotra.foody.Objects.CuisineSearch;
import io.github.rahulmalhotra.foody.Objects.Cuisine_;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CuisinePreference extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, Button.OnClickListener {

    @BindView(R.id.cuisineList)
    ListView cuisineListView;

    @BindView(R.id.cuisineRefreshLayout)
    SwipeRefreshLayout cuisineRefreshLayout;

    @BindView(R.id.filterByCuisines)
    Button applyCuisineFilter;

    private static String BASE_URL = "https://developers.zomato.com/api/v2.1/";
    private String latitude, longitude;
    private HashMap<Integer, Cuisine_> cuisineMap;
    String cuisineIds = "";

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuisine_preference);
        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("foody", Context.MODE_PRIVATE);
        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");
        getCuisines();
        cuisineRefreshLayout.setRefreshing(true);
        applyCuisineFilter.setOnClickListener(this);
    }

    private void setCuisineListView(List<Cuisine> cuisineList) {

        cuisineMap = new HashMap<>();

        String[] cuisineNames = new String[cuisineList.size()];

        String selectedCuisineIdString = sharedPreferences.getString("cuisineIds", "none");
        String[] selectedCuisineIdArray = selectedCuisineIdString.split(",");
        ArrayList<String> selectedCuisineIds = new ArrayList<>(Arrays.asList(selectedCuisineIdArray));

        for(int i=0; i<cuisineList.size(); i++) {
            Cuisine cuisineWrap = cuisineList.get(i);
            Cuisine_ cuisine = cuisineWrap.getCuisine();
            cuisineMap.put(i, cuisine);
            cuisineNames[i] = cuisine.getCuisineName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, cuisineNames);
        cuisineListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        cuisineListView.setAdapter(adapter);
        for(int i=0; i<cuisineListView.getCount(); i++) {
            if(selectedCuisineIds.contains(String.valueOf(cuisineMap.get(i).getCuisineId()))) {
                cuisineListView.setItemChecked(i, true);
            }
        }
    }

    private void getCuisines() {
        if(latitude.isEmpty() || longitude.isEmpty()) {
            Toast.makeText(this, "No preferences available", Toast.LENGTH_SHORT).show();
            return;
        }
        if(isNetworkAvailable()) {
            RestaurantAPIService restaurantAPIService = RetrofitClient.getClient(BASE_URL)
                    .create(RestaurantAPIService.class);
            restaurantAPIService.getCuisines(latitude, longitude)
                    .enqueue(new Callback<CuisineSearch>() {
                        @Override
                        public void onResponse(Call<CuisineSearch> call, Response<CuisineSearch> response) {
                            cuisineRefreshLayout.setRefreshing(false);
                            setCuisineListView(response.body().getCuisines());
                        }

                        @Override
                        public void onFailure(Call<CuisineSearch> call, Throwable t) {
                            cuisineRefreshLayout.setRefreshing(false);
                            Toast.makeText(CuisinePreference.this, "Unable to load cuisines", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Mobile network not available", Toast.LENGTH_SHORT).show();
            cuisineRefreshLayout.setRefreshing(false);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if(connectivityManager==null) {
            return false;
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo!=null && networkInfo.isConnected();
    }

    @Override
    public void onRefresh() {
        cuisineRefreshLayout.setRefreshing(true);
        getCuisines();
    }
    
    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {

        SharedPreferences.Editor editor = sharedPreferences.edit();

        SparseBooleanArray sparseBooleanArray = cuisineListView.getCheckedItemPositions();
        for(int i=0; i<cuisineListView.getCount(); i++) {
            if(sparseBooleanArray.get(i)) {
                Cuisine_ cuisine_ = cuisineMap.get(i);
                cuisineIds = cuisineIds.concat(String.valueOf(cuisine_.getCuisineId()).concat(","));
            }
        }

        editor.putString("cuisineIds", cuisineIds);
        editor.commit();

        Toast.makeText(this, "Cuisine Preference Applied", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }
}
