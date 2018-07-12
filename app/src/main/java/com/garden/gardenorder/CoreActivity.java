package com.garden.gardenorder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.lang.reflect.Field;

public class CoreActivity extends AppCompatActivity {

    FragmentManager fm = getSupportFragmentManager();


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_cart:
                    startCartFragment();
                    return true;
                case R.id.nav_orders:
                    startOrdersFragment();
                    return true;
                case R.id.nav_log_out:
                    Intent logOut = new Intent(CoreActivity.this, MainActivity.class);
                    return true;

                case R.id.nav_menu:
                    startMenuFragment();
                    return true;
            }
            return false;
        }
    };

    void startMenuFragment() {
        Fragment homeFragment = new HomeFragment();
        fm.beginTransaction().replace(R.id.container_main, homeFragment).commit();
    }

    void startOrdersFragment() {
    }
    void startCartFragment() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.removeShiftMode(navigation);

        startMenuFragment();
    }

    void foodListFragment(String categoryId){
        Fragment foodList = FoodListFragment.newInstance(categoryId);
        fm.beginTransaction().replace(R.id.container_main, foodList).commit();
    }

}


