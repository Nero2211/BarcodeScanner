package com.example.nero.barcodescanner;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainControl extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_control);

        toolbar = (Toolbar)findViewById(R.id.MainControlToolbar);
        tabLayout = (TabLayout)findViewById(R.id.MainControlTab);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("WELCOME!");

        viewPager = (ViewPager)findViewById(R.id.MainControlFragmentContainer);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragments(new Scanner(), "Scanner");
        viewPagerAdapter.addFragments(new Recent(), "Recent");
        viewPagerAdapter.addFragments(new Favourite(), "Favourite");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);



    }
}
