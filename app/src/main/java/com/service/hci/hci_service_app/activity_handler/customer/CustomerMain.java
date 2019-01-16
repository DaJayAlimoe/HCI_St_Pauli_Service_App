package com.service.hci.hci_service_app.activity_handler.customer;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.service.hci.hci_service_app.R;
import com.service.hci.hci_service_app.activity_handler.customer.fragments.MenuFragment;
import com.service.hci.hci_service_app.activity_handler.customer.fragments.OrdersFragment;

import java.util.ArrayList;
import java.util.List;

public class CustomerMain extends AppCompatActivity {

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
        setContentView(R.layout.customer_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.customer_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ViewPager viewPager = (ViewPager) findViewById(R.id.customer_viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add Fragments to adapter one by one
        adapter.addFragment(new MenuFragment(), "Menu Karte");
        adapter.addFragment(new OrdersFragment(), "Meine Bestellungen");
        viewPager.setAdapter(adapter);

        //TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.customer_tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    // Adapter for the viewpager using FragmentPagerAdapter
    class ViewPagerAdapter extends FragmentPagerAdapter {
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