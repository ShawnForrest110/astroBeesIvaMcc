package com.example.nasaproject;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
/*
The menu for the android application
Handles all navigation logic depending on what the user chooses


 */
public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Log.i("MenuActivity", "Item in the menu selected");

       /* if (id == R.id.details) {
            setTitle("Details");
            details details = new details();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_drawer_fragment, details, "IN DEPTH").commit();
        }*/ if (id == R.id.Stream) {
            // Opening Training Manual Page
            setTitle("Live Stream");
            LiveStream live = new LiveStream();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_drawer_fragment, live, "MANUALS").commit();
            for ( Fragment f : fragmentManager.getFragments() ) {
                fragmentManager.beginTransaction().remove(f).commit();
            }
            fragmentManager.beginTransaction().add(live, "MANUALS").commit();
      /*  } else if (id == R.id.rover) {
            // Open Messages Fragment
            setTitle("Rover Details");
            rover rover = new rover();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_drawer_fragment, rover, "MESSAGES").commit();
            */
        } else if (id == R.id.single) {
            setTitle("Astronauts");
            singleAstro single = new singleAstro();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_drawer_fragment, single, "SINGLE").commit();
        }  else if (id == R.id.field) {
            setTitle("Field Notes");
            field field = new field();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_drawer_fragment, field, "ALERTS").commit();
        } else if (id == R.id.Settings){
            setTitle("Settings");
            SettingsFrag settingsFrag = new SettingsFrag();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_drawer_fragment, settingsFrag, "SETTINGS").commit();
        } else if (id == R.id.EVA){
            setTitle("EVA Details");
            Eva eva = new Eva();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_drawer_fragment, eva, "EVA").commit();
        } else if (id == R.id.rover){
            setTitle("Rover");
            rover rover = new rover();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_drawer_fragment, rover, "rover").commit();
        }else if (id == R.id.Record){
            setTitle("Record");
            RecordFrag recordFrag = new RecordFrag();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_drawer_fragment, recordFrag, "Record").commit();

        }


        return true;

    }
}






