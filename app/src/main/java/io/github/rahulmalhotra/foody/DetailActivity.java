package io.github.rahulmalhotra.foody;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.rahulmalhotra.foody.API.DetailActivityImageAsyncTask;
import io.github.rahulmalhotra.foody.DB.RestaurantDatabase;
import io.github.rahulmalhotra.foody.Objects.Location;
import io.github.rahulmalhotra.foody.Objects.Restaurant_;
import io.github.rahulmalhotra.foody.Objects.UserRating;
import io.github.rahulmalhotra.foody.Utils.RestaurantExecutors;
import io.github.rahulmalhotra.foody.Utils.RestaurantViewModel;
import io.github.rahulmalhotra.foody.Utils.RestaurantViewModelFactory;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.collapsingToolbar)
    Toolbar toolbar;

    @BindView(R.id.restaurantImage)
    ImageView restaurantImage;

    @BindView(R.id.address)
    TextView address;

    @BindView(R.id.cuisines)
    TextView cuisines;

    @BindView(R.id.averageCost)
    TextView averageCost;

    @BindView(R.id.votes1)
    TextView votes;

    @BindView(R.id.rating1)
    TextView rating;

    @BindView(R.id.detailBtn)
    Button detailBtn;

    @BindView(R.id.directionBtn)
    Button directionBtn;

    String featuredImageURL;
    String dLatLng;
    private boolean restaurantBookmarked;
    private RestaurantDatabase restaurantDatabaseInstance;

    private Restaurant_ restaurant;

    private void insertRestaurant() {
        RestaurantExecutors.getRestaurantExecutorsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                restaurant.setIsBookmarked(true);
                restaurant.setRestaurantAddress(address.getText().toString());
                restaurant.setRestaurantRating(rating.getText().toString());
                restaurant.setRestaurantVotes(votes.getText().toString());
                restaurant.setLatLng(dLatLng);
                restaurantDatabaseInstance.restaurantDao().insertFavoriteRestaurant(restaurant);
            }
        });
    }
    private void deleteRestaurant() {
        RestaurantExecutors.getRestaurantExecutorsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                restaurantDatabaseInstance.restaurantDao().deleteFavoriteRestaurant(restaurant);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bookmark:
                if(!restaurantBookmarked) {
                    insertRestaurant();
                    item.setIcon(R.drawable.bookmark);
                } else {
                    deleteRestaurant();
                    item.setIcon(R.drawable.bookmarkempty);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail, menu);
        if(restaurant!= null && restaurant.getIsBookmarked()) {
            menu.findItem(R.id.bookmark).setIcon(R.drawable.bookmark);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);
        restaurantBookmarked = false;

        restaurant = getIntent().getParcelableExtra("restaurant");
        UserRating userRating = getIntent().getParcelableExtra("userRating");
        Location location = getIntent().getParcelableExtra("location");
        toolbar.setTitle(restaurant.getName());
        featuredImageURL = restaurant.getFeaturedImage();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(featuredImageURL.isEmpty()) {
            restaurantImage.setImageResource(R.drawable.noimage);
        } else {
            try {
                URL restaurantImageURL = new URL(featuredImageURL);
                new DetailActivityImageAsyncTask(restaurantImage).execute(restaurantImageURL);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        String addressString = "";
        if(location!=null) {
            if(!location.getAddress().isEmpty()) {
                addressString += location.getAddress();
            }
            if(!location.getLocality().isEmpty()) {
                if(!addressString.isEmpty()) {
                    addressString +=  ", ";
                }
                addressString += location.getLocality();
            }
            if(!location.getCity().isEmpty()) {
                if(!addressString.isEmpty()) {
                    addressString +=  ", ";
                }
                addressString += location.getCity();
            }
            if(!location.getZipcode().isEmpty()) {
                if(!addressString.isEmpty()) {
                    addressString +=  " - ";
                }
                addressString += location.getZipcode();
            }
            if(!location.getLatitude().isEmpty() && !location.getLongitude().isEmpty()) {
                dLatLng = location.getLatitude() + "," + location.getLongitude();
            }
        }
        address.setText(addressString);
        cuisines.setText("Cuisines: " + restaurant.getCuisines());
        averageCost.setText("Average Cost for two: " + restaurant.getAverageCostForTwo());
        if(userRating!=null) {
            rating.setText("User Rating: " + userRating.getAggregateRating());
            votes.setText("Votes: " + userRating.getVotes());
        }
        if(restaurant.getIsBookmarked()) {
            restaurantBookmarked = true;
            rating.setText(restaurant.getRestaurantRating());
            votes.setText(restaurant.getRestaurantVotes());
            dLatLng = restaurant.getLatLng();
        }
        restaurantDatabaseInstance = RestaurantDatabase.getInstance(getApplicationContext());
        initializeRestaurantViewModel();

        detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRestaurantDetails();
            }
        });

        directionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGoogleMaps();
            }
        });
    }

    private void openRestaurantDetails() {
        String url = restaurant.getUrl();
        if(url.isEmpty()) {
            Toast.makeText(this, "No more details found", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void openGoogleMaps() {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + dLatLng));
        startActivity(intent);
    }

    private void initializeRestaurantViewModel() {
        RestaurantViewModelFactory restaurantViewModelFactory = new RestaurantViewModelFactory(restaurantDatabaseInstance, restaurant.getId());
        RestaurantViewModel restaurantViewModel = ViewModelProviders.of(this, restaurantViewModelFactory).get(RestaurantViewModel.class);
        restaurantViewModel.getRestaurantLiveData().observe(this, new Observer<Restaurant_>() {
            @Override
            public void onChanged(@Nullable Restaurant_ bookmarkedRestaurant) {
                if(bookmarkedRestaurant!=null) {
                    restaurant = bookmarkedRestaurant;
                    restaurantBookmarked = true;
                } else {
                    restaurantBookmarked = false;
                }
            }
        });
    }
}
