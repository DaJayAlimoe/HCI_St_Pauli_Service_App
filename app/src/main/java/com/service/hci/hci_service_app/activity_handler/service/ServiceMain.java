package com.service.hci.hci_service_app.activity_handler.service;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.service.hci.hci_service_app.R;
import com.service.hci.hci_service_app.activity_handler.service.adapters.ServiceOrdersFragmentAdapter;

public class ServiceMain extends AppCompatActivity {

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.service_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = findViewById(R.id.service_orders_viewpager);

        ServiceOrdersFragmentAdapter adapter =
                new ServiceOrdersFragmentAdapter(this, getSupportFragmentManager());

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.service_orders_tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }
}
