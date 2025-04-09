package com.canara.navigator;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LauncherActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    public static final String OFFICE = "office";
    public static final String DEANS_OFFICE = "deans_office";
    public static final String AUDITORIUM = "auditorium";
    public static final String CANTEEN = "canteen";
    public static final String FROM = "from";
    public static final String MODE = "mode";
    public String userMode = "user";

    private List<String> locations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laucher_activiy);
        ConstraintLayout layout = findViewById(R.id.main_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) layout.getBackground();
        animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        locations.add(OFFICE);
        locations.add(DEANS_OFFICE);
        locations.add(AUDITORIUM);
        
        locations.add(CANTEEN);

        ImageButton settingsBtn = findViewById(R.id.settings_btn);
        settingsBtn.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(getApplicationContext(), v);
            popup.getMenuInflater().inflate(R.menu.menu_main ,popup.getMenu());
            popup.setOnMenuItemClickListener(LauncherActivity.this::onMenuItemClick);
            popup.show();
        });

        ImageButton officeBtn = findViewById(R.id.office_btn);
        ImageButton deans_officeBtn = findViewById(R.id.deans_office_btn);
        ImageButton auditoriumBtn = findViewById(R.id.auditorium_btn);
        ImageButton canteenBtn = findViewById(R.id.canteen_btn);
        TextView searchBtn = findViewById(R.id.searchBtn);

        searchBtn.setOnClickListener(this);
        officeBtn.setOnClickListener(this);
        deans_officeBtn.setOnClickListener(this);
        auditoriumBtn.setOnClickListener(this);
        canteenBtn.setOnClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                userMode = "user";
                Toast.makeText(this, "You are in User mode", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item2:
                userMode = "admin";
                Toast.makeText(this, "You are in Admin mode", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item3:
                showAddLocationPopup();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.office_btn:
                goToCameraActivity(OFFICE);
                break;
            case R.id.deans_office_btn:
                goToCameraActivity(DEANS_OFFICE);
                break;
            case R.id.auditorium_btn:
                goToCameraActivity(AUDITORIUM);
                break;
            case R.id.canteen_btn:
                goToCameraActivity(CANTEEN);
                break;
            case R.id.searchBtn:
                EditText searchText = findViewById(R.id.search);
                String searchQuery = searchText.getText().toString().trim();
                performSearch(searchQuery);
                break;
        }
    }

    private void performSearch(String searchQuery) {
        boolean found = false;
        for (String location : locations) {
            if (location.equalsIgnoreCase(searchQuery)) {
                found = true;
                Toast.makeText(this, "Found Location: " + location, Toast.LENGTH_SHORT).show();
                goToCameraActivity(location);
                break;
            }
        }
        if (!found) {
            Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void goToCameraActivity(String Section) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra(FROM, Section);
        i.putExtra(MODE, userMode);
        startActivity(i);
    }

    private void showAddLocationPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.add_location_dialog, null);
        builder.setView(dialogView);

        EditText locationNameInput = dialogView.findViewById(R.id.location_name_input);
        Button addLocationButton = dialogView.findViewById(R.id.add_location_button);

        AlertDialog dialog = builder.create();
        dialog.show();

        addLocationButton.setOnClickListener(v -> {
            String locationName = locationNameInput.getText().toString().trim();
            if (!locationName.isEmpty()) {
                Toast.makeText(this, "Added Location: " + locationName, Toast.LENGTH_SHORT).show();
                locations.add(locationName);
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Please enter a location name", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
