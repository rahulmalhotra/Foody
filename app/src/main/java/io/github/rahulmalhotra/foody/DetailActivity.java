package io.github.rahulmalhotra.foody;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import butterknife.ButterKnife;
import io.github.rahulmalhotra.foody.Fragments.RestaurantDetailFragment;
import io.github.rahulmalhotra.foody.Objects.Location;
import io.github.rahulmalhotra.foody.Objects.Restaurant_;
import io.github.rahulmalhotra.foody.Objects.UserRating;

public class DetailActivity extends AppCompatActivity {

    private Restaurant_ restaurant;
    private UserRating userRating;
    private Location location;
    private boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        isTablet = this.getResources().getBoolean(R.bool.isTablet);
        Log.d("changed", String.valueOf(isTablet));
        if(isTablet) {
            finish();
        }
        setFragment();
    }

    private void setFragment() {
        RestaurantDetailFragment fragment = new RestaurantDetailFragment();
        if(getIntent()!=null)
            fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.restaurantDetailContainer, fragment).commit();
    }

}
