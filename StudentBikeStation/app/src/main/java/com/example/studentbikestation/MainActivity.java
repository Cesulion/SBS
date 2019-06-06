package com.example.studentbikestation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    /*Firebase variables*/
    private DatabaseReference ref;
    private FirebaseDatabase database;
    /*RV variables*/
    private ArrayList<Station> stationList;
    private RecyclerView stationRv;
    RecyclerView.Adapter stationAdapter;
    /*Bottom navigation variables*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Navigationbar
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(bottomNavigationListener);
        navigation.setSelectedItemId(R.id.action_home);

        //RV of stations
        stationRv = findViewById(R.id.rv_stations);
        stationRv.hasFixedSize();
        stationRv.setLayoutManager(new LinearLayoutManager(this));
        stationList = new ArrayList<>();
        //Database
        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("stations");
        getData();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch(menuItem.getItemId())
                    {
                        case R.id.action_map:
                            Intent mapsIntent = new Intent(MainActivity.this, MapsActivity.class);
                            startActivity(mapsIntent);
                            break;
                        case R.id.action_weather:
                            Intent weatherIntent = new Intent(MainActivity.this, WeatherActivity.class);
                            startActivity(weatherIntent);
                            break;


                    }
                    /*getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();*/
                    return true;
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.loginItem:
                break;


        }
        return super.onOptionsItemSelected(item);

    }

    public void getData() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                stationList.clear();
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot postSnapShot:dataSnapshot.getChildren())
                    {
                        Station s=postSnapShot.getValue(Station.class);
                        Log.d("Station "+s.getName(), s.getLock1()+" "+s.getLock2()+" "+s.getLock3());
                        stationList.add(s);

                    }
                }
                fillRV();


            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void fillRV()
    {
        final List<Station> allStation = stationList;
        if(allStation.size() > 0){
            stationRv.setVisibility(View.VISIBLE);
            stationAdapter = new StationAdapter(this, allStation);
            stationRv.setAdapter(stationAdapter);
        }else {
            stationRv.setVisibility(View.GONE);
            Toast.makeText(this, "Either theere are no stations yet or there is a connexion problem.", Toast.LENGTH_LONG).show();
        }
    }

}
