package com.yujotseng.expenselogger;

import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements HomeFragment.PassDataListener {

    private static final String TAG = "MainActivity";

    private SectionsPagerAdapter sectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView);

        // Add bottomNavView item click listener
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
//                    case R.id.action_settings:
//                        switchFragment(3);
//                        return true;
                }
                return false;
            }
        });

        buildFragmentsList();

        // Default select HomeFragment at app launch
        switchFragment(0);

        // Disable bot nav view icon animation
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
    }

    private void buildFragmentsList() {
        sectionsPagerAdapter.addFragment(new HomeFragment());
        sectionsPagerAdapter.addFragment(new CalendarFragment());
        sectionsPagerAdapter.addFragment(new AnalysisFragment());
//        sectionsPagerAdapter.addFragment(new SettingsFragment());
        sectionsPagerAdapter.addFragment(new NewEntryFragment());
    }

    private void switchFragment(int pos) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, sectionsPagerAdapter.getItem(pos));
        transaction.commit();
    }

    public void passID(long id, boolean toView) {
        if (toView) {
            ExpenseInfoFragment expenseInfoFragment = (ExpenseInfoFragment) getSupportFragmentManager().findFragmentByTag("ExpenseInfoFragment");
            if (expenseInfoFragment == null) {
                expenseInfoFragment = new ExpenseInfoFragment();
            }

            Bundle bundle = new Bundle();
            bundle.putLong(ExpenseInfoFragment.EXPENSE_ID, id);
            expenseInfoFragment.setArguments(bundle);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, expenseInfoFragment);
            transaction.commit();
        } else {
            ModifyEntryFragment modifyEntryFragment = (ModifyEntryFragment) getSupportFragmentManager().findFragmentByTag("ModifyEntryFragment");
            if (modifyEntryFragment == null) {
                modifyEntryFragment = new ModifyEntryFragment();
            }

            Bundle bundle = new Bundle();
            bundle.putLong(ModifyEntryFragment.EXPENSE_ID, id);
            modifyEntryFragment.setArguments(bundle);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, modifyEntryFragment);
            transaction.commit();
        }
    }

    @Override
    public void PassDate(String date, int year, int month, int day, boolean toHome) {
        if (toHome) {
            HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("HomeFragment");
            if (homeFragment == null) {
                homeFragment = new HomeFragment();
            }

            Bundle bundle = new Bundle();
            bundle.putString(HomeFragment.DATE, date);
            bundle.putInt(HomeFragment.YEAR, year);
            bundle.putInt(HomeFragment.MONTH, month);
            bundle.putInt(HomeFragment.DAY, day);
            homeFragment.setArguments(bundle);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, homeFragment);
            transaction.commit();
        } else {
            NewEntryFragment newEntryFragment = (NewEntryFragment) getSupportFragmentManager().findFragmentByTag("NewEntryFragment");
            if (newEntryFragment == null) {
                newEntryFragment = new NewEntryFragment();
            }

            Bundle bundle = new Bundle();
            bundle.putString(NewEntryFragment.DATE, date);
            newEntryFragment.setArguments(bundle);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, newEntryFragment);
            transaction.commit();
        }
    }
}
