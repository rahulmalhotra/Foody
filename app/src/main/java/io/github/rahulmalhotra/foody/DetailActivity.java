package io.github.rahulmalhotra.foody;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.rahulmalhotra.foody.API.DetailActivityImageAsyncTask;
import io.github.rahulmalhotra.foody.Objects.Location;
import io.github.rahulmalhotra.foody.Objects.Restaurant_;
import io.github.rahulmalhotra.foody.Objects.UserRating;

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

    String featuredImageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        Restaurant_ restaurant = getIntent().getParcelableExtra("restaurant");
        UserRating userRating = getIntent().getParcelableExtra("userRating");
        Location location = getIntent().getParcelableExtra("location");
        toolbar.setTitle(restaurant.getName());
        featuredImageURL = restaurant.getFeaturedImage();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            if(featuredImageURL.isEmpty()) {
                featuredImageURL = "https://via.placeholder.com/200x200.png/D3D3D3/000000?text=No+Image";
            }
            URL restaurantImageURL = new URL(featuredImageURL);
            new DetailActivityImageAsyncTask(restaurantImage).execute(restaurantImageURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String addressString = "";
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

        address.setText(addressString);
        cuisines.setText("Cuisines: " + restaurant.getCuisines());
        averageCost.setText("Average Cost for two: " + restaurant.getAverageCostForTwo());
        rating.setText("User Rating: " + userRating.getAggregateRating());
        votes.setText("Votes: " + userRating.getVotes());
    }
}
