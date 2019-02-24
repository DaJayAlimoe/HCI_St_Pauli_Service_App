package com.service.hci.hci_service_app.activity_handler.service;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.service.hci.hci_service_app.R;
import com.service.hci.hci_service_app.activity_handler.service.adapters.ServiceOrdersFragmentAdapter;
import com.service.hci.hci_service_app.activity_handler.service.fragments.AllOrdersFragment;
import com.service.hci.hci_service_app.activity_handler.service.fragments.SelectedOrdersFragment;

import java.util.ArrayList;
import java.util.List;

public class ServiceMain extends AppCompatActivity {

    public  ViewPagerAdapter adapter;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_service);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = findViewById(R.id.viewPager_service_orders);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        // Add Fragments to adapter one by one
        adapter.addFragment(new AllOrdersFragment(), "Alle Bestellungen");
        adapter.addFragment(new SelectedOrdersFragment(), "Meine Aufträge");
        viewPager.setAdapter(adapter);

        //adapter = new ServiceOrdersFragmentAdapter(this, getSupportFragmentManager());
//        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout_service_orders);
        tabLayout.setupWithViewPager(viewPager);
    }

    // Adapter for the viewpager using FragmentPagerAdapter
    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
