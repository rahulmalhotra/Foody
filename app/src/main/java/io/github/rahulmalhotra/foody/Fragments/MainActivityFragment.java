package io.github.rahulmalhotra.foody.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.rahulmalhotra.foody.Adapters.RestaurantAdapter;
import io.github.rahulmalhotra.foody.Objects.Restaurant;
import io.github.rahulmalhotra.foody.R;

public class MainActivityFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView restaurantsList;

    RestaurantAdapter restaurantAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        restaurantAdapter = new RestaurantAdapter(getActivity());
        restaurantsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        restaurantsList.setHasFixedSize(true);
        restaurantsList.setAdapter(restaurantAdapter);
        return view;
    }

    public void setRestaurantArrayList(ArrayList<Restaurant> restaurantArrayList) {
        restaurantAdapter.setRestaurantList(restaurantArrayList);
    }
}
