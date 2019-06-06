package com.example.studentbikestation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

public class WeatherActivity extends AppCompatActivity {
    static TextView tempView, cityView, descView, weatherView;
    static ImageView weatherImageView;
    JSONObject data = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Navigationbar
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(bottomNavigationListener);
        tempView = (TextView)findViewById(R.id.temperatureView);
        cityView = (TextView)findViewById(R.id.citynameView);
        descView = (TextView)findViewById(R.id.descView);
        weatherView =(TextView)findViewById(R.id.weatherView);
        weatherImageView =(ImageView)findViewById(R.id.weatherImageView);
        navigation.setSelectedItemId(R.id.action_weather);
        JsonFetcher dataFetcher = new JsonFetcher();
        dataFetcher.execute();


    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch(menuItem.getItemId())
                    {
                        case R.id.action_map:
                            Intent mapsIntent = new Intent(WeatherActivity.this, MapsActivity.class);
                            startActivity(mapsIntent);
                            break;
                        case R.id.action_home:
                            Intent homeIntent = new Intent(WeatherActivity.this, MainActivity.class);
                            startActivity(homeIntent);
                            break;


                    }
                    /*getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();*/
                    return true;
                }
            };

}

