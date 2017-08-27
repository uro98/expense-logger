package com.yujotseng.expenselogger;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private BottomNavigationView bottomNavigationView;
    private SectionsPagerAdapter sectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView);

        // BottomNavView item click listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_home:
                        switchFragment(0);
                        return true;
                    case R.id.action_calendar:
                        switchFragment(1);
                        return true;
                    case R.id.action_analysis:
                        switchFragment(2);
                        return true;
                    case R.id.action_settings:
                        switchFragment(3);
                        return true;
                }

                return false;
            }
        });

        buildFragmentsList();

        // Default select HomeFragment at app launch
        switchFragment(0);

        // Disable bot nav view icon animation
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
    }

    private void buildFragmentsList() {
        sectionsPagerAdapter.addFragment(new HomeFragment());
        sectionsPagerAdapter.addFragment(new CalendarFragment());
        sectionsPagerAdapter.addFragment(new AnalysisFragment());
        sectionsPagerAdapter.addFragment(new SettingsFragment());
    }

    private void switchFragment(int pos) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, sectionsPagerAdapter.getItem(pos));
        transaction.commit();
    }
}
