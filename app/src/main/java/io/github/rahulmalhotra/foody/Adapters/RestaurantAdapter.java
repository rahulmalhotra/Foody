package io.github.rahulmalhotra.foody.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.rahulmalhotra.foody.DetailActivity;
import io.github.rahulmalhotra.foody.Objects.Location;
import io.github.rahulmalhotra.foody.Objects.Restaurant;
import io.github.rahulmalhotra.foody.Objects.Restaurant_;
import io.github.rahulmalhotra.foody.R;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    Context context;
    List<Restaurant> restaurantList;

    public RestaurantAdapter(Context context)
    {
        this.context = context;
    }

    public void setRestaurantList(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RestaurantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.restaurant_card, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantAdapter.ViewHolder viewHolder, int i) {
        String address = "";
        Restaurant_ restaurant = restaurantList.get(viewHolder.getAdapterPosition()).getRestaurant();
        viewHolder.name.setText(restaurant.getName());
        if(restaurant.getUserRating()!=null) {
            viewHolder.rating.setText("Rating: " + restaurant.getUserRating().getAggregateRating());
            viewHolder.votes.setText(restaurant.getUserRating().getVotes() + " Votes");
        } else if(restaurant.getIsBookmarked()) {
            viewHolder.rating.setText(restaurant.getRestaurantRating());
            viewHolder.votes.setText(restaurant.getRestaurantVotes());
        }
        Location location = restaurant.getLocation();
        if(location!=null && !location.getAddress().isEmpty()) {
            address += restaurant.getLocation().getAddress();
        }
        if(location!=null && !location.getLocality().isEmpty()) {
            if(!address.isEmpty()) {
                address +=  ", ";
            }
            address += restaurant.getLocation().getLocality();
        }
        if(location!=null && !location.getCity().isEmpty()) {
            if(!address.isEmpty()) {
                address +=  ", ";
            }
            address += restaurant.getLocation().getCity();
        }
        if(location!=null && !location.getZipcode().isEmpty()) {
            if(!address.isEmpty()) {
                address +=  " - ";
            }
            address += restaurant.getLocation().getZipcode();
        }
        if(restaurant.getIsBookmarked()) {
            address = restaurant.getRestaurantAddress();
        }
        viewHolder.address.setText(address);
        if(!restaurant.getThumb().isEmpty()) {
            Picasso.get().load(restaurant.getThumb()).into(viewHolder.image);
        } else {
            viewHolder.image.setImageResource(R.drawable.noimage);
        }
    }

    @Override
    public int getItemCount() {

        if(restaurantList != null) {
            return restaurantList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.image)
        public ImageView image;

        @BindView(R.id.name)
        public TextView name;

        @BindView(R.id.rating)
        public TextView rating;

        @BindView(R.id.votes)
        public TextView votes;

        @BindView(R.id.address)
        public TextView address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {

            Restaurant_ restaurant = restaurantList.get(getAdapterPosition()).getRestaurant();
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("restaurant", restaurant);
            intent.putExtra("userRating", restaurant.getUserRating());
            intent.putExtra("location", restaurant.getLocation());
            context.startActivity(intent);
        }
    }
}