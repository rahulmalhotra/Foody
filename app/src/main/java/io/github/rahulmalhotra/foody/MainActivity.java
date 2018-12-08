package io.github.rahulmalhotra.foody;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.rahulmalhotra.foody.API.RestaurantAPIService;
import io.github.rahulmalhotra.foody.API.RetrofitClient;
import io.github.rahulmalhotra.foody.Adapters.TabAdapter;
import io.github.rahulmalhotra.foody.Fragments.MainActivityFragment;
import io.github.rahulmalhotra.foody.Objects.RestaurantSearch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LocationListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    LocationManager locationManager;
    MainActivityFragment mainActivityFragment1 = null;
    MainActivityFragment mainActivityFragment2 = null;

    final int PLACE_PICKER_REQUEST_CODE = 1;
    final int CUISINE_REQUEST_CODE = 2;
    final int SETTINGS_REQUEST_CODE = 3;

    Double latitude, longitude;
    String cuisineIds, sortBy, radius, sortOrder;
    Boolean useCurrentLocation;
    SharedPreferences sharedPreferences;

    private static String BASE_URL = "https://developers.zomato.com/api/v2.1/";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.changeLocation:
                showPlacePicker();
                break;
            case R.id.currentLocation:
                if(!useCurrentLocation) {
                    useCurrentLocation = true;
                    onRefresh();
                } else {
                    Toast.makeText(this, "Already using current location", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent, SETTINGS_REQUEST_CODE);
                break;
            case R.id.cuisinePreference:
                Intent intent1 = new Intent(this, CuisinePreference.class);
                intent1.putExtra("latitude", String.valueOf(latitude));
                intent1.putExtra("longitude", String.valueOf(longitude));
                startActivityForResult(intent1, CUISINE_REQUEST_CODE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Binding views by id
        ButterKnife.bind(this);
        useCurrentLocation = true;
        mainActivityFragment1 = new MainActivityFragment();
        mainActivityFragment2 = new MainActivityFragment();
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        tabAdapter.addFragment(mainActivityFragment1);
        tabAdapter.addFragment(mainActivityFragment2);
        // Setting up view pager and tab layout
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        // Setting up location manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        swipeRefreshLayout.setOnRefreshListener(this);
        sharedPreferences = getSharedPreferences("foody", Context.MODE_PRIVATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK) {
            if (requestCode == PLACE_PICKER_REQUEST_CODE) {
                locationManager.removeUpdates(this);
                useCurrentLocation = false;
                Place place = PlacePicker.getPlace(this, data);
                LatLng latLng = place.getLatLng();
                swipeRefreshLayout.setRefreshing(true);
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                getRestaurantList();
            } else if (requestCode == CUISINE_REQUEST_CODE) {
                swipeRefreshLayout.setRefreshing(true);
                getRestaurantList();
            } else if (requestCode == SETTINGS_REQUEST_CODE) {
                swipeRefreshLayout.setRefreshing(true);
                getRestaurantList();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private void getRestaurantList() {
        if(isNetworkAvailable()) {
            RestaurantAPIService restaurantAPIService = RetrofitClient.getClient(BASE_URL)
                                                        .create(RestaurantAPIService.class);
            restaurantAPIService.getRestaurants(String.valueOf(latitude), String.valueOf(longitude), radius, cuisineIds, sortBy, sortOrder)
                    .enqueue(new Callback<RestaurantSearch>() {
                        @Override
                        public void onResponse(Call<RestaurantSearch> call, Response<RestaurantSearch> response) {
                            swipeRefreshLayout.setRefreshing(false);
                            mainActivityFragment1.setRestaurantArrayList(new ArrayList<>(response.body().getRestaurants()));
                        }

                        @Override
                        public void onFailure(Call<RestaurantSearch> call, Throwable t) {
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(MainActivity.this, "Unable to get restaurants", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Mobile network not available", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
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

    private void getCurrentLocation() {
        swipeRefreshLayout.setRefreshing(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},0);
            return;
        }
        locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 50, this);
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 50, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
/*
        cuisineIds = sharedPreferences.getString("cuisineIds", "none");
        radius = sharedPreferences.getString("radius", "100");
        sortBy = sharedPreferences.getString("sortBy", getResources().getStringArray(R.array.sortByInputValues)[0]);
        sortOrder = sharedPreferences.getString("sortOrder", getResources().getStringArray(R.array.sortOrderInputValues)[0]);
        if(useCurrentLocation) {
            getCurrentLocation();
        }
*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(useCurrentLocation) {
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        getRestaurantList();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onRefresh() {
        if(useCurrentLocation) {
            swipeRefreshLayout.setRefreshing(true);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},0);
                return;
            }
            locationManager.requestSingleUpdate(locationManager.GPS_PROVIDER, this, null);
            locationManager.requestSingleUpdate(locationManager.NETWORK_PROVIDER, this, null);
        } else {
            getRestaurantList();
        }
    }
}
