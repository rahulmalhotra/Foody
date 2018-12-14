package io.github.rahulmalhotra.foody;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity implements Button.OnClickListener {

    @BindView(R.id.saveButton)
    Button saveButton;

    @BindView(R.id.radiusInput)
    EditText radiusInput;

    @BindView(R.id.sortByInput)
    Spinner sortByInput;

    @BindView(R.id.sortOrderInput)
    Spinner sortOrderInput;

    SharedPreferences sharedPreferences;
    String radius, sortBy, sortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.sharedPreferencesName), Context.MODE_PRIVATE);
        radius = sharedPreferences.getString(getResources().getString(R.string.spRadius), getResources().getString(R.string.spRadiusDefaultValue));
        sortBy = sharedPreferences.getString(getResources().getString(R.string.spSortBy), getResources().getStringArray(R.array.sortByInputValues)[0]);
        sortOrder = sharedPreferences.getString(getResources().getString(R.string.spSortOrder), getResources().getStringArray(R.array.sortOrderInputValues)[0]);
        radiusInput.setText(radius);
        for(int i=0; i<sortByInput.getCount(); i++) {
            if(sortByInput.getItemAtPosition(i).equals(sortBy)) {
                sortByInput.setSelection(i);
            }
        }
        for(int i=0; i<sortOrderInput.getCount(); i++) {
            if(sortOrderInput.getItemAtPosition(i).equals(sortOrder)) {
                sortOrderInput.setSelection(i);
            }
        }
        saveButton.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        radius = radiusInput.getText().toString();
        sortBy = sortByInput.getSelectedItem().toString();
        sortOrder = sortOrderInput.getSelectedItem().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getResources().getString(R.string.spRadius), radius);
        editor.putString(getResources().getString(R.string.spSortBy), sortBy);
        editor.putString(getResources().getString(R.string.spSortOrder), sortOrder);
        editor.apply();
        onBackPressed();
    }
}
