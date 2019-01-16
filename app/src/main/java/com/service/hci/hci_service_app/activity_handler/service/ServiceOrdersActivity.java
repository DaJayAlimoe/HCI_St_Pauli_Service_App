package com.service.hci.hci_service_app.activity_handler.service;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.service.hci.hci_service_app.R;
import com.service.hci.hci_service_app.activity_handler.service.adapters.ServiceOrdersFragmentAdapter;

public class ServiceOrdersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_main);

        ViewPager viewPager = findViewById(R.id.service_orders_viewpager);

        ServiceOrdersFragmentAdapter adapter =
                new ServiceOrdersFragmentAdapter(this, getSupportFragmentManager());

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.service_orders_tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }
}
