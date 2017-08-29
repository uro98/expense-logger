package com.yujotseng.expenselogger;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.constraint.solver.ArrayLinkedVariables;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnListItemSelectedListener {

    private static final String TAG = "MainActivity";

    private BottomNavigationView bottomNavigationView;
    private SectionsPagerAdapter sectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView);

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
        sectionsPagerAdapter.addFragment(new NewEntryFragment());
    }

    private void switchFragment(int pos) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, sectionsPagerAdapter.getItem(pos));
        transaction.commit();
    }

    public void onListItemSelected(long id, String name) {
        ExpenseInfoFragment expenseInfoFragment = (ExpenseInfoFragment) getSupportFragmentManager().findFragmentByTag("ExpenseInfoFragment");
        if (expenseInfoFragment == null) {
            expenseInfoFragment = new ExpenseInfoFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putLong(expenseInfoFragment.EXPENSE_ID, id);
        bundle.putString(expenseInfoFragment.EXPENSE_NAME, name);
        expenseInfoFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, expenseInfoFragment);
        transaction.commit();
    }
}
