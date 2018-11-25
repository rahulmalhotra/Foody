package io.github.rahulmalhotra.foody;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
        sharedPreferences = getSharedPreferences("foody", Context.MODE_PRIVATE);
        radius = sharedPreferences.getString("radius", "100");
        sortBy = sharedPreferences.getString("sortBy", getResources().getStringArray(R.array.sortByInputValues)[0]);
        sortOrder = sharedPreferences.getString("sortOrder", getResources().getStringArray(R.array.sortOrderInputValues)[0]);
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
        editor.putString("radius", radius);
        editor.putString("sortBy", sortBy);
        editor.putString("sortOrder", sortOrder);
        editor.commit();
        onBackPressed();
    }
}
