package com.yujotseng.expenselogger;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private SectionsPagerAdapter sectionPagerAdapter;

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sectionPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.container);

        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_date_range_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_assessment_black_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_settings_black_24dp);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter spAdapter = new SectionsPagerAdapter((getSupportFragmentManager()));

        spAdapter.addFragment(new CalendarFragment());
        spAdapter.addFragment(new AnalysisFragment());
        spAdapter.addFragment(new SettingsFragment());

        viewPager.setAdapter(spAdapter);
    }
}
